package com.rameses.osiris2.netbeans;

import com.rameses.client.updates.UpdateCenter;
import com.rameses.platform.interfaces.AppLoader;
import com.rameses.platform.interfaces.MainWindow;
import com.rameses.platform.interfaces.MainWindowListener;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.openide.modules.ModuleInstall;
import org.openide.windows.WindowManager;

public class NBInstaller extends ModuleInstall {
    
    private Container origPanel;
    private JFrame mainWindow;
    private NBMainWindow nbMainWindow;
    private NBPlatform nbPlatform;
    
    private AppLoader appLoader;
    private ClassLoader classLoader;
    private Map env;
    
    public void restored() {
        try {
            WindowManager.getDefault().addPropertyChangeListener(new WindowManagerPropertyListener());
            loadHttpsBypass();
            loadApplication();
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    private void loadApplication() throws Exception{
        String filename = System.getProperty("user.dir") + "/client.conf";
        File f = new File(filename);
        if (!f.exists())
            throw new Exception("client.conf does not exist");
        
        Properties props = new Properties();
        props.load(new FileInputStream(f));
        
        System.getProperties().putAll(props);
        String appsys = (String) props.get("app.url");
        if (appsys == null || appsys.trim().length() == 0)
            throw new NullPointerException("app.url must be provided");
        
        UpdateCenter uc = new UpdateCenter(appsys);
        uc.start();
        
        env = uc.getEnv();
        Iterator keys = env.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next().toString();
            if (key.startsWith("app.")) {
                Object val = env.get(key);
                if (val != null) System.getProperties().put(key, val);
            }
        }
        
        classLoader = uc.getClassLoader(Thread.currentThread().getContextClassLoader());
        String apploader = (String) env.get("app.loader");
        if (apploader == null || apploader.trim().length() == 0)
            throw new NullPointerException("app.loader must be provided in the ENV");
        
        appLoader = (AppLoader) classLoader.loadClass(apploader).newInstance();
        
    }
    
    private void loadHttpsBypass() throws Exception {
        TrustManager[] tm = new TrustManager[]
        {
            new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] x509Certificate, String string) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] x509Certificate, String string) throws CertificateException {}
                public X509Certificate[] getAcceptedIssuers() { return null; }
            }
        };
        
        SSLContext ssl = SSLContext.getInstance("SSL");
        ssl.init(null, tm, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(ssl.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String string, SSLSession sSLSession) {
                return true;
            }
        });
    }
    
    private void onactiveMode() throws Exception {
        File f = new File("netbeans.conf");
        if( f.exists()) {
            FileInputStream fis = null;
            
            try {
                fis = new FileInputStream(f);
                Properties props = new Properties();
                props.load( fis );
                for( Object o : props.entrySet()) {
                    Map.Entry me = (Map.Entry)o;
                    System.setProperty( me.getKey()+"", me.getValue()+"" );
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            } finally {
                try { fis.close(); } catch(Exception ign){;}
            }
        }
        
        mainWindow = (JFrame) WindowManager.getDefault().getMainWindow();
        mainWindow.setTitle("Rameses Client Platform");
        mainWindow.addWindowListener(new WindowOpenAdapter());
        
        NBManager.getInstance().setMainWindow(mainWindow);
        MainWindowCustomizer.customize(mainWindow);
        
        System.getProperties().put("StartupModuleDispatched", "false");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    startBootProcess2();
                    
                } catch(Exception ex) {
                    alert(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
        
    }
    
    
    
    private void startBootProcess2() throws Exception {
        Thread.currentThread().setContextClassLoader(classLoader);
        //set the plaf
        try {
            String plaf = (String)env.get("plaf");
            if(plaf==null || plaf.trim().length()==0) {
                plaf = "com.jgoodies.plaf.plastic.PlasticXPLookAndFeel";
            }
            UIManager.setLookAndFeel(plaf);
        } catch(Exception ign){;}
        
        nbMainWindow = new NBMainWindow(mainWindow);
        nbPlatform = new NBPlatform(nbMainWindow);
        mainWindow.getRootPane().putClientProperty(MainWindow.class, nbMainWindow);
        appLoader.load(classLoader, env, nbPlatform);
        //setting application icon
        try {
            File fileIcon = new File(System.getProperty("user.dir") + "/icon.gif");
            System.getProperties().put("app.icon.url", fileIcon.toURL());
            ImageIcon iicon = new ImageIcon(fileIcon.toURL());
            mainWindow.setIconImage(iicon.getImage());
        } catch(Exception ign) {;}
        
    }
    
    private void alert( final String msg ) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(mainWindow, msg);
            }
        });
    }
    
    public boolean closing() {
        if (nbMainWindow != null && nbMainWindow.getListeners() != null) {
            //if (nbMainWindow.getListener().onClose()) return true;
            for(MainWindowListener mwl : nbMainWindow.getListeners() ) {
                try {
                    if ( !mwl.onClose() ) return false;
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        
        int opt = JOptionPane.showConfirmDialog(mainWindow, "Are you sure you want to exit from the system?", "Confirmation", JOptionPane.YES_NO_OPTION);
        return (opt == JOptionPane.YES_OPTION);
    }
    
    
    //<editor-fold defaultstate="collapsed" desc=" WindowManagerPropertyListener (Class) ">
    private class WindowManagerPropertyListener implements PropertyChangeListener {
        private boolean activated;
        
        public void propertyChange(PropertyChangeEvent e) {
            if (activated) return;
            
            if (SwingUtilities.isEventDispatchThread()) {
                activated = true;
                
                try {
                    onactiveMode();
                } catch(Exception ex) {
                    alert(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" WindowOpenAdapter (Class) ">
    private class WindowOpenAdapter extends WindowAdapter {
        public void windowOpened(WindowEvent e) {
            mainWindow.removeWindowListener(this);
            
            try {
                System.getProperties().put("StartupModuleDispatched","false");
                //startupModule.run();
            } catch(Exception ex) {
                alert(ex.getMessage());
                SwingUtilities.invokeLater(new DisposeWindowInvoker());
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" DisposeWindowInvoker (Class) ">
    private class DisposeWindowInvoker implements Runnable {
        public void run() { mainWindow.dispose(); }
    }
    //</editor-fold>
    
}
