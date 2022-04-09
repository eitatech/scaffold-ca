package tech.eita.factory.adapters;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import tech.eita.exceptions.InvalidTaskOptionException;
import tech.eita.factory.ModuleFactory;

public class ModuleFactoryDrivenAdapterTest {

  @Test
  public void shouldReturnModuleFactory() throws InvalidTaskOptionException {
    for (ModuleFactoryDrivenAdapter.DrivenAdapterType type :
        ModuleFactoryDrivenAdapter.DrivenAdapterType.values()) {
      ModuleFactory factory = ModuleFactoryDrivenAdapter.getDrivenAdapterFactory(type);
      assertNotNull(factory);
    }
  }

  @Test(expected = NullPointerException.class)
  public void shouldHandleError() throws InvalidTaskOptionException {
    ModuleFactoryDrivenAdapter.getDrivenAdapterFactory(null);
  }
}
