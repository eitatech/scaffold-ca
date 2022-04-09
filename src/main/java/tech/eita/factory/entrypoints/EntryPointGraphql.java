package tech.eita.factory.entrypoints;

import static tech.eita.Constants.APP_SERVICE;
import static tech.eita.utils.Utils.buildImplementationFromProject;

import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.factory.ModuleFactory;
import tech.eita.factory.validations.ReactiveTypeValidation;

import java.io.IOException;

public class EntryPointGraphql implements ModuleFactory {

  @Override
  public void buildModule(ModuleBuilder builder) throws IOException, CleanException {
    builder.runValidations(ReactiveTypeValidation.class);
    String name = builder.getStringParam("task-param-pathgql");
    if (!name.startsWith("/")) {
      throw new IllegalArgumentException("The path must start with /");
    }
    builder.appendToSettings("graphql-api", "infrastructure/entry-points");
    builder.appendToProperties("graphql.servlet").put("enabled", true).put("mapping", name);
    builder
        .appendToProperties("graphql.playground")
        .put("mapping", "/playground")
        .put("endpoint", name)
        .put("enabled", true);
    String dependency = buildImplementationFromProject(builder.isKotlin(), ":graphql-api");
    builder.appendDependencyToModule(APP_SERVICE, dependency);
    builder.setupFromTemplate("entry-point/graphql-api");
  }
}
