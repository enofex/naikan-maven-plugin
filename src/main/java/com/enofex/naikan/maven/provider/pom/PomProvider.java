package com.enofex.naikan.maven.provider.pom;

import com.enofex.naikan.maven.Provider;

public abstract class PomProvider<T> implements Provider<T> {

  int POM_ORDER = -1073741824;

  @Override
  public int order() {
    return POM_ORDER;
  }
}
