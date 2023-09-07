package com.enofex.naikan.maven.provider.bom;

import com.enofex.naikan.maven.AbstractProvider;

public abstract class BomProvider<T> extends AbstractProvider<T> {

  public static final int BOM_ORDER = 1073741824;


  @Override
  public int order() {
    return BOM_ORDER;
  }
}
