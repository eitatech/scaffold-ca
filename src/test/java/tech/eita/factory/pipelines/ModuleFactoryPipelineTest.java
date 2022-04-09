package tech.eita.factory.pipelines;

import static org.junit.Assert.assertNotNull;

import tech.eita.exceptions.InvalidTaskOptionException;
import tech.eita.factory.ModuleFactory;
import org.junit.Test;

public class ModuleFactoryPipelineTest {

  @Test
  public void shouldReturnModuleFactory() throws InvalidTaskOptionException {
    for (ModuleFactoryPipeline.PipelineType type : ModuleFactoryPipeline.PipelineType.values()) {
      ModuleFactory factory = ModuleFactoryPipeline.getPipelineFactory(type);
      assertNotNull(factory);
    }
  }

  @Test(expected = NullPointerException.class)
  public void shouldHandleError() throws InvalidTaskOptionException {
    ModuleFactoryPipeline.getPipelineFactory(null);
  }
}
