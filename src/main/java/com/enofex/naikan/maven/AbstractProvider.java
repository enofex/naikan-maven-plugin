package com.enofex.naikan.maven;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

public abstract class AbstractProvider<T> implements Provider<T> {

  private final Log log;

  protected AbstractProvider() {
    this.log = new SystemStreamLog();
  }

  public Log getLog() {
    return this.log;
  }
}
