package tech.eita.task;

import java.io.IOException;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleFactory;
import tech.eita.factory.helpers.ModuleFactoryHelpers;
import tech.eita.utils.Utils;

public class GenerateHelperTask extends CleanArchitectureDefaultTask {
  private String name;

  @Option(option = "name", description = "Set driven adapter name")
  public void setName(String name) {
    this.name = name;
  }

  @TaskAction
  public void generateHelperTask() throws IOException, CleanException {
    ModuleFactory moduleFactory = ModuleFactoryHelpers.getDrivenAdapterFactory();
    logger.lifecycle("Clean Architecture plugin version: {}", Utils.getVersionPlugin());
    logger.lifecycle("Helper name: {}", name);
    builder.addParam("task-param-name", name);
    builder.addParam("lombok", builder.isEnableLombok());
    moduleFactory.buildModule(builder);
    builder.persist();
  }
}
