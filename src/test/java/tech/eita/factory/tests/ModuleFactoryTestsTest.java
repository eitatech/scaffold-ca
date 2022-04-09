package tech.eita.factory.tests;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import tech.eita.exceptions.InvalidTaskOptionException;
import tech.eita.factory.ModuleFactory;

public class ModuleFactoryTestsTest {

  @Test
  public void shouldReturnModuleFactory() throws InvalidTaskOptionException {
    ModuleFactory factory = ModuleFactoryTests.getTestsFactory();
    assertNotNull(factory);
  }
}
