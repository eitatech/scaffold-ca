package tech.eita.factory.helpers;

import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.factory.ModuleFactory;
import tech.eita.factory.commons.GenericModule;

import java.io.IOException;

public class HelperGeneric implements ModuleFactory {
  @Override
  public void buildModule(ModuleBuilder builder) throws IOException, CleanException {
    GenericModule.generateGenericModule(
        builder,
        "No Helper name is set, usage: gradle generateHelper " + "--name",
        "infrastructure/helpers",
        "helper/generic");
  }
}
