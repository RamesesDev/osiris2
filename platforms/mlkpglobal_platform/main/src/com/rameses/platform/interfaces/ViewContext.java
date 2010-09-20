package com.rameses.platform.interfaces;

public abstract interface ViewContext
{
  public static final long serialVersionUID = 1L;

  public abstract boolean close();

  public abstract void display();
}