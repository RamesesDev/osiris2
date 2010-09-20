package com.rameses.platform.interfaces;

import java.util.Map;
import javax.swing.JComponent;

public abstract interface Platform
{
  public static final long serialVersionUID = 1L;

  public abstract void showWindow(JComponent paramJComponent1, JComponent paramJComponent2, Map paramMap);

  public abstract boolean isWindowExists(String paramString);

  public abstract void activateWindow(String paramString);

  public abstract void closeWindow(String paramString);

  public abstract void showPopup(JComponent paramJComponent1, JComponent paramJComponent2, Map paramMap);

  public abstract void showError(JComponent paramJComponent, Exception paramException);

  public abstract boolean showConfirm(JComponent paramJComponent, Object paramObject);

  public abstract void showInfo(JComponent paramJComponent, Object paramObject);

  public abstract void showAlert(JComponent paramJComponent, Object paramObject);

  public abstract Object showInput(JComponent paramJComponent, Object paramObject);

  public abstract MainWindow getMainWindow();

  public abstract void exit();
}