package tech.eita.factory.entrypoints;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import tech.eita.exceptions.InvalidTaskOptionException;
import tech.eita.factory.ModuleFactory;

public class ModuleFactoryEntryPointTest {

  @Test
  public void shouldReturnModuleFactory() throws InvalidTaskOptionException {
    for (ModuleFactoryEntryPoint.EntryPointType type :
        ModuleFactoryEntryPoint.EntryPointType.values()) {
      ModuleFactory factory = ModuleFactoryEntryPoint.getEntryPointFactory(type);
      assertNotNull(factory);
    }
  }

  @Test(expected = NullPointerException.class)
  public void shouldHandleError() throws InvalidTaskOptionException {
    ModuleFactoryEntryPoint.getEntryPointFactory(null);
  }
}
