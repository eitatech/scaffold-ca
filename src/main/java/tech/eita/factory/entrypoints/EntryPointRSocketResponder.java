package tech.eita.factory.entrypoints;

import static tech.eita.Constants.APP_SERVICE;
import static tech.eita.utils.Utils.buildImplementationFromProject;

import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.factory.ModuleFactory;
import tech.eita.factory.validations.ReactiveTypeValidation;

import java.io.IOException;

public class EntryPointRSocketResponder implements ModuleFactory {

  @Override
  public void buildModule(ModuleBuilder builder) throws IOException, CleanException {
    builder.runValidations(ReactiveTypeValidation.class);
    builder.appendToSettings("rsocket-responder", "infrastructure/entry-points");
    builder.appendToProperties("spring.rsocket.server").put("port", 7000);
    String dependency = buildImplementationFromProject(builder.isKotlin(), ":rsocket-responder");
    builder.appendDependencyToModule(APP_SERVICE, dependency);
    builder.setupFromTemplate("entry-point/rsocket-responder");
  }
}
