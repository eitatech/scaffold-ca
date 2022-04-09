package tech.eita.task;

import static tech.eita.utils.Utils.buildImplementationFromProject;

import tech.eita.utils.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.gradle.api.tasks.options.OptionValues;
import tech.eita.Constants;

public class DeleteModuleTask extends CleanArchitectureDefaultTask {
  private String module;

  @Option(option = "module", description = "Set module name to delete")
  public void setModule(String module) {
    this.module = module;
  }

  @OptionValues("module")
  public List<String> getModules() {
    return new ArrayList<>(getProject().getChildProjects().keySet());
  }

  @TaskAction
  public void deleteModule() throws IOException {
    if (module == null || !getProject().getChildProjects().containsKey(module)) {
      printHelp();
      throw new IllegalArgumentException(
          "No valid module name is set, usage: gradle deleteModule --module "
              + Utils.formatTaskOptions(getModules()));
    }
    String dependency = buildImplementationFromProject(builder.isKotlin(), ":" + module);
    builder.deleteModule(module);
    builder.removeFromSettings(module);
    builder.removeDependencyFromModule(Constants.APP_SERVICE, dependency);
    builder.persist();
  }
}
