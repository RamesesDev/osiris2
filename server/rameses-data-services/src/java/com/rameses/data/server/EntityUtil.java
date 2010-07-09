package com.rameses.data.server;

import java.lang.reflect.Field;
import javax.persistence.Id;

public final class EntityUtil {
    
    public static Object getId( Object o ) {
        Class c = o.getClass();
        Object id = null;
        while( c != null ) {
            id = extractId( o, c );
            if( id != null )
                break;
            else {
                c = c.getSuperclass();
                if(c.getClass().getName().equals("java.lang.Object"))
                    break;
            }
        }
        if( id == null )
            throw new IllegalStateException( "Error in EntityUtil.getId. Please check if there is a @Id declared in the object" );
        return id;
    }
    
    private static Object extractId( Object o, Class cls ) {
        Object id = null;
        for( Field f : cls.getDeclaredFields() ) {
            if( f.isAnnotationPresent( Id.class )) {
                try {
                    boolean a = f.isAccessible();
                    f.setAccessible(true);
                    id = f.get( o );
                    f.setAccessible(a);
                    break;
                } catch (Exception ignore) {
                    System.out.println( ignore.getMessage() );
                }
            }
        }
        return id;
    }
    
    public static String getIdName( Class clz ) {
        String idname = null;
        while( clz != null ) {
            idname = extractIdName( clz );
            if( idname != null )
                break;
            else {
                clz = clz.getSuperclass();
                if(clz.getClass().getName().equals("java.lang.Object"))
                    break;
            }
        }
        if( idname == null )
            throw new IllegalStateException( "Error in EntityUtil.getId. Please check if there is a @Id declared in the object" );
        return idname;
    }
    
    private static String extractIdName( Class cls ) {
        String idname = null;
        for( Field f : cls.getDeclaredFields() ) {
            if( f.isAnnotationPresent( Id.class )) {
                try {
                    idname = f.getName();
                    break;
                } catch (Exception ignore) {
                    System.out.println( ignore.getMessage() );
                }
            }
        }
        return idname;
    }
    
}
