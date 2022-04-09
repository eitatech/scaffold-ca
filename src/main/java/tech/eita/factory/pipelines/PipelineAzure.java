package tech.eita.factory.pipelines;

import java.io.IOException;
import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.factory.ModuleFactory;

public class PipelineAzure implements ModuleFactory {

  @Override
  public void buildModule(ModuleBuilder builder) throws IOException, CleanException {
    builder.addParam("sonar.java.binaries", "**/build/classes/java/main");
    builder.addParam("sonar.junit.reportsPaths", "**/build/test-results/test");
    builder.setupFromTemplate("pipeline/azure");
  }
}
