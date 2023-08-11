package com.enofex.naikan.test.architecture;


import java.util.Collection;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class ArchitectureTest {

  @TestFactory
  Collection<DynamicTest> shouldFulfilArchitectureConstrains() {
    return ArchUnitTestsConfig.defaultConfig().getDynamicTests();
  }
}
