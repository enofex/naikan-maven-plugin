package com.enofex.naikan.maven;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

final class ProviderFactory {

  private ProviderFactory() {
  }

  static <T> List<Provider<T>> providers(Class<T> clazz) {
    List<Provider<T>> providers = new ArrayList<>();

    for (Provider<T> provider : ServiceLoader.load(Provider.class)) {
      if (provider.support(clazz)) {
        providers.add(provider);
      }
    }

    providers.sort(Comparator.comparingInt(Provider::order));

    return providers;
  }
}
