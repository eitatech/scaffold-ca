package tech.eita.factory.commons;

import static tech.eita.Constants.APP_SERVICE;
import static tech.eita.utils.Utils.buildImplementationFromProject;

import java.io.IOException;
import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.utils.Utils;

public class GenericModule {
  private GenericModule() {}

  public static void generateGenericModule(
      ModuleBuilder builder, String exceptionMessage, String baseDir, String template)
      throws IOException, CleanException {
    String name = builder.getStringParam("task-param-name");

    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException(exceptionMessage);
    }
    String dashName = Utils.toDashName(name);
    builder.addParam("name-dash", dashName);
    builder.addParam("name-package", name.toLowerCase());
    builder.appendToSettings(dashName, baseDir);
    String dependency = buildImplementationFromProject(builder.isKotlin(), ":" + dashName);
    builder.appendDependencyToModule(APP_SERVICE, dependency);
    builder.setupFromTemplate(template);
  }
}
