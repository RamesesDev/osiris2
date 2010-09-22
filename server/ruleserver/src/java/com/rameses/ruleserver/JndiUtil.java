/*
 * JndiUtil.java
 *
 * Created on July 23, 2009, 5:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.ruleserver;

import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.LinkRef;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

/**
 *
 * @author elmo
 */
public class JndiUtil {
    
   
    private static final Logger log = Logger.getLogger(JndiUtil.class.getName());

   /** Create a subcontext including any intermediate contexts.
    @param ctx, the parent JNDI Context under which value will be bound
    @param name, the name relative to ctx of the subcontext.
    @return The new or existing JNDI subcontext
    @throws javax.naming.NamingException on any JNDI failure
    */
   public static Context createSubcontext(Context ctx, String name) throws NamingException
   {
      Name n = ctx.getNameParser("").parse(name);
      return createSubcontext(ctx, n);
   }

   /** Create a subcontext including any intermediate contexts.
    @param ctx, the parent JNDI Context under which value will be bound
    @param name, the name relative to ctx of the subcontext.
    @return The new or existing JNDI subcontext
    @throws NamingException on any JNDI failure
    */
   public static Context createSubcontext(Context ctx, Name name) throws NamingException
   {
      Context subctx = ctx;
      for (int pos = 0; pos < name.size(); pos++)
      {
         String ctxName = name.get(pos);
         try
         {
            subctx = (Context) ctx.lookup(ctxName);
         }
         catch (NameNotFoundException e)
         {
            subctx = ctx.createSubcontext(ctxName);
         }
         // The current subctx will be the ctx for the next name component
         ctx = subctx;
      }
      return subctx;
   }

   /** Bind val to name in ctx, and make sure that all intermediate contexts exist
    @param ctx, the parent JNDI Context under which value will be bound
    @param name, the name relative to ctx where value will be bound
    @param value, the value to bind.
    */
   public static void bind(Context ctx, String name, Object value) throws NamingException
   {
      Name n = ctx.getNameParser("").parse(name);
      bind(ctx, n, value);
   }

   /** Bind val to name in ctx, and make sure that all intermediate contexts exist
    @param ctx, the parent JNDI Context under which value will be bound
    @param name, the name relative to ctx where value will be bound
    @param value, the value to bind.
    */
   public static void bind(Context ctx, Name name, Object value) throws NamingException
   {
      int size = name.size();
      String atom = name.get(size - 1);
      Context parentCtx = createSubcontext(ctx, name.getPrefix(size - 1));
      parentCtx.bind(atom, value);
   }

   /** Rebind val to name in ctx, and make sure that all intermediate contexts exist
    @param ctx, the parent JNDI Context under which value will be bound
    @param name, the name relative to ctx where value will be bound
    @param value, the value to bind.
    */
   public static void rebind(Context ctx, String name, Object value) throws NamingException
   {
      Name n = ctx.getNameParser("").parse(name);
      rebind(ctx, n, value);
   }

   /** Rebind val to name in ctx, and make sure that all intermediate contexts exist
    @param ctx, the parent JNDI Context under which value will be bound
    @param name, the name relative to ctx where value will be bound
    @param value, the value to bind.
    */
   public static void rebind(Context ctx, Name name, Object value) throws NamingException
   {
      int size = name.size();
      String atom = name.get(size - 1);
      Context parentCtx = createSubcontext(ctx, name.getPrefix(size - 1));
      parentCtx.rebind(atom, value);
   }

   /** Unbinds a name from ctx, and removes parents if they are empty
    @param ctx, the parent JNDI Context under which the name will be unbound
    @param name, The name to unbind
    */
   public static void unbind(Context ctx, String name) throws NamingException
   {
      unbind(ctx, ctx.getNameParser("").parse(name));
   }

   /** Unbinds a name from ctx, and removes parents if they are empty
    @param ctx, the parent JNDI Context under which the name will be unbound
    @param name, The name to unbind
    */
   public static void unbind(Context ctx, Name name) throws NamingException
   {
      ctx.unbind(name); //unbind the end node in the name
      int sz = name.size();
      // walk the tree backwards, stopping at the domain
      while (--sz > 0)
      {
         Name pname = name.getPrefix(sz);
         try
         {
            ctx.destroySubcontext(pname);
         }
         catch (NamingException e)
         {
            log.info("Unable to remove context " + pname + e.getMessage());
            break;
         }
      }
   }
   
   /**
    * Lookup an object in the default initial context
    * 
    * @param name the name to lookup
    * @param clazz the expected type
    * @return the object
    * @throws Exception for any error
    */
   public static Object lookup(String name, Class clazz) throws Exception
   {
      InitialContext ctx = new InitialContext();
      try
      {
         return lookup(ctx, name, clazz);
      }
      finally
      {
         ctx.close();
      }
   }
   
   /**
    * Lookup an object in the default initial context
    * 
    * @param name the name to lookup
    * @param clazz the expected type
    * @return the object
    * @throws Exception for any error
    */
   public static Object lookup(Name name, Class clazz) throws Exception
   {
      InitialContext ctx = new InitialContext();
      try
      {
         return lookup(ctx, name, clazz);
      }
      finally
      {
         ctx.close();
      }
   }
   
   /**
    * Lookup an object in the given context
    * 
    * @param context the context
    * @param name the name to lookup
    * @param clazz the expected type
    * @return the object
    * @throws Exception for any error
    */
   public static Object lookup(Context context, String name, Class clazz) throws Exception
   {
      Object result = context.lookup(name);
      checkObject(context, name, result, clazz);
      return result;
   }
   
   /**
    * Lookup an object in the given context
    * 
    * @param context the context
    * @param name the name to lookup
    * @param clazz the expected type
    * @return the object
    * @throws Exception for any error
    */
   public static Object lookup(Context context, Name name, Class clazz) throws Exception
   {
      Object result = context.lookup(name);
      checkObject(context, name.toString(), result, clazz);
      return result;
   }

   /**
    * Create a link
    * 
    * @param fromName the from name
    * @param toName the to name
    * @throws NamingException for any error
    */
   public static void createLinkRef(String fromName, String toName) throws NamingException
   {
      InitialContext ctx = new InitialContext();
      createLinkRef(ctx, fromName, toName);
   }
   
   /**
    * Create a link
    * 
    * @param ctx the context
    * @param fromName the from name
    * @param toName the to name
    * @throws NamingException for any error
    */
   public static void createLinkRef(Context ctx, String fromName, String toName) throws NamingException
   {
      LinkRef link = new LinkRef(toName);
      Context fromCtx = ctx;
      Name name = ctx.getNameParser("").parse(fromName);
      String atom = name.get(name.size()-1);
      for(int n = 0; n < name.size()-1; n ++)
      {
         String comp = name.get(n);
         try
         {
            fromCtx = (Context) fromCtx.lookup(comp);
         }
         catch(NameNotFoundException e)
         {
            fromCtx = fromCtx.createSubcontext(comp);
         }
      }

      log.info("atom: " + atom);
      log.info("link: " + link);
      
      fromCtx.rebind(atom, link);

      log.info("Bound link " + fromName + " to " + toName);
   }
   
   /**
    * Remove the link ref
    *
    * @param name the name of the link binding
    * @throws NamingException for any error
    */
   public static void removeLinkRef(String name) throws NamingException
   {
      InitialContext ctx = new InitialContext();
      removeLinkRef(ctx, name);
   }
   
   /**
    * Remove the link ref
    *
    * @param ctx the context
    * @param name the name of the link binding
    * @throws NamingException for any error
    */
   public static void removeLinkRef(Context ctx, String name) throws NamingException
   {
      log.info("Unbinding link " + name);
      ctx.unbind(name);
   }
   
   /**
    * Checks an object implements the given class
    * 
    * @param context the context
    * @param name the name to lookup
    * @param object the object
    * @param clazz the expected type
    */
   protected static void checkObject(Context context, String name, Object object, Class clazz) throws Exception
   {
      Class objectClass = object.getClass();
      if (clazz.isAssignableFrom(objectClass) == false)
      {
         StringBuffer buffer = new StringBuffer(100);
         buffer.append("Object at '").append(name);
         buffer.append("' in context ").append(context.getEnvironment());
         buffer.append(" is not an instance of ");
         appendClassInfo(buffer, clazz);
         buffer.append(" object class is ");
         appendClassInfo(buffer, object.getClass());
         throw new ClassCastException(buffer.toString());
      }
   }
   
   /**
    * Append Class Info
    *
    * @param buffer the buffer to append to
    * @param clazz the class to describe
    */
   protected static void appendClassInfo(StringBuffer buffer, Class clazz)
   {
      buffer.append("[class=").append(clazz.getName());
      buffer.append(" classloader=").append(clazz.getClassLoader());
      buffer.append(" interfaces={");
      Class[] interfaces = clazz.getInterfaces();
      for (int i=0; i<interfaces.length; ++i)
      {
         if (i > 0)
            buffer.append(", ");
         buffer.append("interface=").append(interfaces[i].getName());
         buffer.append(" classloader=").append(interfaces[i].getClassLoader());
      }
      buffer.append("}]");
   }

    
}
