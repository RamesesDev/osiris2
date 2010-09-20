package com.rameses.platform.interfaces;

public abstract interface MainWindowListener
{
  public abstract Object onEvent(String paramString, Object paramObject);

  public abstract boolean onClose();
}