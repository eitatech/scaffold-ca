package tech.eita.factory.helpers;

import static org.junit.Assert.assertNotNull;

import tech.eita.exceptions.InvalidTaskOptionException;
import tech.eita.factory.ModuleFactory;
import org.junit.Test;

public class ModuleFactoryHelperTest {

  @Test
  public void shouldReturnModuleFactory() throws InvalidTaskOptionException {
    ModuleFactory factory = ModuleFactoryHelpers.getDrivenAdapterFactory();
    assertNotNull(factory);
  }
}
