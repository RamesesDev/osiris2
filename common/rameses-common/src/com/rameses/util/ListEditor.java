/*
 * ListEditor.java
 *
 * Created on August 23, 2010, 12:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class ListEditor implements List {
    
    private String refname;
    private Map parent;
    private List list;
    
    public ListEditor(Map parent, String refname, List collection) {
        this.parent = parent;
        this.list = collection;
        this.refname = refname;
    }

    //important methods to override.
    public Object get(int index) {
        Object data = list.get(index);
        if(data instanceof Map) {
            return new MapEditor( (Map)data );
        }
        return data;
    }

    private void _setNewState(Object element) {
        if( element instanceof Map ) {
            ((Map)element).put("_new", true );
        }
    }
    
    private void _addDeleted( Object o ) {
        if(parent!=null) {
            parent.put("_deleted_" + refname, o);
        }
    }
    

    public void add(int index, Object element) {
        _setNewState(element);
        list.add(index,element);
    }

    
    public boolean add(Object element) {
        _setNewState(element);
        return list.add(element);
    }
   
    public boolean addAll(Collection c) {
        for(Object o: c) {
            _setNewState(c);
        }
        return list.addAll( c );
    }

    public boolean addAll(int index, Collection c) {
        for(Object o: c) {
            _setNewState(c);
        }
        return list.addAll( index, c );
    }

    public boolean remove(Object o) {
        _addDeleted(o);
        return list.remove(o);
    }

    public boolean removeAll(Collection c) {
        for( Object o: c ) {
            _addDeleted(o);
        }
        return list.removeAll( c );
    }

    public void clear() {
        removeAll( list );
    }

    public Object set(int index, Object element) {
        return list.set(index, element);
    }

    public Object remove(int index) {
        Object o = list.remove( index ); 
        _addDeleted(o);
        return o;
    }
    
    
    //other methods...
    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean contains(Object o) {
        return list.contains(o);
    }

    public Iterator<Object> iterator() {
        return list.iterator();
    }

    public Object[] toArray() {
        return list.toArray();
    }

    public Object[] toArray(Object[] a) {
        return list.toArray(a);
    }


    public boolean containsAll(Collection c) {
        return list.containsAll( c );
    }


    public boolean retainAll(Collection c) {
        return list.retainAll( c );
    }


    public int indexOf(Object o) {
        return list.indexOf( o );
    }

    public int lastIndexOf(Object o) {
        return list.lastIndexOf( o );
    }

    public ListIterator<Object> listIterator() {
        return list.listIterator();
    }

    public ListIterator<Object> listIterator(int index) {
        return list.listIterator( index );
    }

    public List<Object> subList(int fromIndex, int toIndex) {
        throw new RuntimeException("ListEditor.sublist is not supported"); 
    }
    
    
}



