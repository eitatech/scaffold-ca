package tech.eita.factory.entrypoints;

import static tech.eita.Constants.APP_SERVICE;
import static tech.eita.utils.Utils.buildImplementationFromProject;

import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.factory.ModuleFactory;
import tech.eita.factory.validations.ReactiveTypeValidation;

import java.io.IOException;

public class EntryPointRestWebflux implements ModuleFactory {
  @Override
  public void buildModule(ModuleBuilder builder) throws IOException, CleanException {
    builder.runValidations(ReactiveTypeValidation.class);
    if (Boolean.TRUE.equals(builder.getBooleanParam("task-param-router"))) {
      builder.setupFromTemplate("entry-point/rest-webflux/router-functions");
    } else {
      builder.setupFromTemplate("entry-point/rest-webflux");
      if (Boolean.TRUE.equals(builder.getBooleanParam("include-swagger"))) {
        builder.addParam("module", "reactive-web");
        builder.setupFromTemplate("entry-point/swagger");
        if (builder.isKotlin()) {
          builder
              .appendToProperties("spring.mvc.pathmatch")
              .put("matching-strategy", "ant_path_matcher");
        }
      }
    }
    builder.appendToSettings("reactive-web", "infrastructure/entry-points");
    String dependency = buildImplementationFromProject(builder.isKotlin(), ":reactive-web");
    builder.appendDependencyToModule(APP_SERVICE, dependency);
  }
}
