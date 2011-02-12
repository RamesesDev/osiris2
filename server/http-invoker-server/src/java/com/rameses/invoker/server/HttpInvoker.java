package com.rameses.invoker.server;

import com.rameses.invoker.server.InvokerHelper.NameParser;
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
            NameParser np = InvokerHelper.createNameParser(req);
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
