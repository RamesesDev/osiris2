package com.rameses.platform.interfaces;

import javax.swing.JComponent;

public abstract interface MainWindow
{
  public static final long serialVersionUID = 1L;
  public static final String TOOLBAR = "toolbar";
  public static final String MENUBAR = "menubar";
  public static final String CONTENT = "content";
  public static final String STATUSBAR = "statusbar";

  public abstract void setListener(MainWindowListener paramMainWindowListener);

  public abstract void setComponent(JComponent paramJComponent, String paramString);

  public abstract void setTitle(String paramString);

  public abstract void close();

  public abstract void show();
}