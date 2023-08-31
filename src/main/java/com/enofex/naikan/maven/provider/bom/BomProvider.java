package com.enofex.naikan.maven.provider.bom;

import com.enofex.naikan.maven.Provider;

public abstract class BomProvider<T> implements Provider<T> {

  int BOM_ORDER = 1073741824;

  @Override
  public int order() {
    return BOM_ORDER;
  }
}
