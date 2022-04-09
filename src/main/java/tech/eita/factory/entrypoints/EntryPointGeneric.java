package tech.eita.factory.entrypoints;

import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.factory.ModuleFactory;
import tech.eita.factory.commons.GenericModule;

import java.io.IOException;

public class EntryPointGeneric implements ModuleFactory {
  @Override
  public void buildModule(ModuleBuilder builder) throws IOException, CleanException {
    GenericModule.generateGenericModule(
        builder,
        "No name is set for GENERIC type, usage: gradle generateEntryPoint "
            + "--type GENERIC --name [name]",
        "infrastructure/entry-points",
        "entry-point/generic");
  }
}
