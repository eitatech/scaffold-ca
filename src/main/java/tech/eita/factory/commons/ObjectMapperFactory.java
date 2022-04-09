package tech.eita.factory.commons;

import static tech.eita.Constants.APP_SERVICE;
import static tech.eita.utils.Utils.buildImplementation;

import tech.eita.Constants;
import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.factory.ModuleFactory;

import java.io.IOException;

public class ObjectMapperFactory implements ModuleFactory {

  @Override
  public void buildModule(ModuleBuilder builder) throws IOException, CleanException {
    builder.setupFromTemplate("commons/object-mapper");
    String dependency =
        buildImplementation(
            builder.isKotlin(),
            "org.reactivecommons.utils:object-mapper:" + Constants.RCOMMONS_OBJECT_MAPPER_VERSION);
    builder.appendDependencyToModule(APP_SERVICE, dependency);
  }
}
