package com.enofex.naikan.maven.provider.pom;

import com.enofex.naikan.maven.Provider;

public abstract class PomProvider<T> implements Provider<T> {

  @Override
  public int order() {
    return POM_ORDER;
  }
}
