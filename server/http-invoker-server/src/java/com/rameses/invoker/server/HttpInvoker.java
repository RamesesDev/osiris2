package com.rameses.invoker.server;


import com.rameses.server.common.AppContext;
import com.rameses.web.common.RequestNameParser;
import com.rameses.util.ExceptionManager;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class HttpInvoker extends HttpServlet {
    
    protected Object[] filterInput( Object obj ) throws Exception {
        return  (Object[])obj;
    }
    
    protected Object filterOutput( Object obj ) throws Exception {
        return obj;
    }

    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {
            //NameParser np = InvokerHelper.createNameParser(req);
            
            RequestNameParser np = new RequestNameParser(req);
            np.setContext(AppContext.getName());
            //NameParser np = new InvokerHelper.NameParser(req);
            in = new ObjectInputStream(req.getInputStream());
            Object[] values = filterInput( in.readObject() );

            Object response = InvokerHelper.invoke(np, values );
            if (response == null) {
                response = "#NULL";
            }
            
            out = new ObjectOutputStream(res.getOutputStream());
            out.writeObject(filterOutput(response));
        } 
        catch (Exception ex) {
            ex.printStackTrace();
            try {
                
                Exception ne = ExceptionManager.getOriginal(ex);
                out = new ObjectOutputStream(res.getOutputStream());
                out.writeObject(filterOutput(ne));
            }
            catch(Exception ign){
                System.out.println("error in filtering output");
            }
        } 
        finally {
            try { out.close(); } catch (Exception ex) {;}
        }
    }
    

    
    
}
