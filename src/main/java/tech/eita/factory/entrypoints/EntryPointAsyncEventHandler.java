package tech.eita.factory.entrypoints;

import static tech.eita.Constants.APP_SERVICE;
import static tech.eita.utils.Utils.buildImplementationFromProject;

import java.io.IOException;
import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.factory.ModuleFactory;
import tech.eita.factory.validations.ReactiveTypeValidation;

public class EntryPointAsyncEventHandler implements ModuleFactory {
  @Override
  public void buildModule(ModuleBuilder builder) throws IOException, CleanException {
    builder.runValidations(ReactiveTypeValidation.class);
    builder.setupFromTemplate("entry-point/async-event-handler");
    builder.appendToSettings("async-event-handler", "infrastructure/entry-points");
    String dependency = buildImplementationFromProject(builder.isKotlin(), ":async-event-handler");
    builder.appendDependencyToModule(APP_SERVICE, dependency);
  }
}
