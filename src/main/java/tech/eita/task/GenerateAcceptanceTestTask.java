package tech.eita.task;

import tech.eita.exceptions.CleanException;
import tech.eita.factory.tests.ModuleFactoryTests;
import tech.eita.utils.Utils;
import java.io.IOException;
import org.gradle.api.tasks.TaskAction;

public class GenerateAcceptanceTestTask extends CleanArchitectureDefaultTask {

  @TaskAction
  public void generateAcceptanceTestTask() throws IOException, CleanException {

    logger.lifecycle("Clean Architecture plugin version: {}", Utils.getVersionPlugin());
    ModuleFactoryTests.getTestsFactory().buildModule(builder);
    builder.persist();
  }
}
