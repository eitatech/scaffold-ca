package tech.eita.factory.adapters;

import static tech.eita.utils.Utils.buildImplementationFromProject;

import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.factory.ModuleFactory;
import tech.eita.factory.validations.LanguageValidation;

import java.io.IOException;

public class DrivenAdapterKtorClient implements ModuleFactory {

  @Override
  public void buildModule(ModuleBuilder builder) throws IOException, CleanException {
    builder.runValidations(LanguageValidation.class);
    builder.appendToSettings("ktor-client", "infrastructure/driven-adapters");
    String dependency = buildImplementationFromProject(builder.isKotlin(), ":ktor-client");
    builder.appendDependencyToModule("app-service", dependency);
    builder.setupFromTemplate("driven-adapter/ktor-client");
  }
}
