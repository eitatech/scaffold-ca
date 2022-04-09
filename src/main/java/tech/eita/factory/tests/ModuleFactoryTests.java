package tech.eita.factory.tests;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import tech.eita.factory.ModuleFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModuleFactoryTests {
  public static ModuleFactory getTestsFactory() {
    return new AcceptanceTest();
  }
}
