package com.rameses.platform.interfaces;

import java.util.Map;

public abstract interface AppLoader
{
  public static final long serialVersionUID = 1L;

  public abstract void load(ClassLoader paramClassLoader, Map paramMap, Platform paramPlatform);
}