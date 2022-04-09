package tech.eita.factory.tests;

import tech.eita.factory.ModuleFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModuleFactoryTests {
  public static ModuleFactory getTestsFactory() {
    return new AcceptanceTest();
  }
}
