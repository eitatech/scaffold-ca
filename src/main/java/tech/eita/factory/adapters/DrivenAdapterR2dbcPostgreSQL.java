package tech.eita.factory.adapters;

import static tech.eita.Constants.APP_SERVICE;
import static tech.eita.utils.Utils.buildImplementationFromProject;

import java.io.IOException;
import org.gradle.api.logging.Logger;
import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.factory.ModuleFactory;
import tech.eita.factory.validations.ReactiveTypeValidation;

public class DrivenAdapterR2dbcPostgreSQL implements ModuleFactory {

  @Override
  public void buildModule(ModuleBuilder builder) throws IOException, CleanException {
    Logger logger = builder.getProject().getLogger();
    builder.runValidations(ReactiveTypeValidation.class);
    logger.lifecycle("Generating for reactive project");
    builder.setupFromTemplate("driven-adapter/r2dbc-postgresql");
    String dependency = buildImplementationFromProject(builder.isKotlin(), ":r2dbc-postgresql");
    builder.appendDependencyToModule(APP_SERVICE, dependency);
    builder.appendToSettings("r2dbc-postgresql", "infrastructure/driven-adapters");
  }
}
