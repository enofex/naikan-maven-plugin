package com.enofex.naikan.maven.provider.pom;

import com.enofex.naikan.maven.AbstractProvider;

public abstract class PomProvider<T> extends AbstractProvider<T> {

  public static final int POM_ORDER = -1073741824;

  @Override
  public int order() {
    return POM_ORDER;
  }
}
