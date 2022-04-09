package tech.eita.factory.adapters;

import static tech.eita.Constants.APP_SERVICE;
import static tech.eita.utils.Utils.buildImplementationFromProject;

import java.io.IOException;
import org.gradle.api.logging.Logger;
import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.factory.ModuleFactory;
import tech.eita.factory.commons.ObjectMapperFactory;

public class DrivenAdapterMongoDB implements ModuleFactory {
  @Override
  public void buildModule(ModuleBuilder builder) throws IOException, CleanException {
    Logger logger = builder.getProject().getLogger();
    if (Boolean.TRUE.equals(builder.isReactive())) {
      logger.lifecycle("Generating for reactive project");
      builder.setupFromTemplate("driven-adapter/mongo-reactive");
    } else {
      logger.lifecycle("Generating for imperative project");
      builder.setupFromTemplate("driven-adapter/mongo-repository");
    }
    builder.appendToSettings("mongo-repository", "infrastructure/driven-adapters");
    builder.appendToProperties("spring.data.mongodb").put("uri", "mongodb://localhost:27017/test");
    String dependency = buildImplementationFromProject(builder.isKotlin(), ":mongo-repository");
    builder.appendDependencyToModule(APP_SERVICE, dependency);
    if (Boolean.TRUE.equals(builder.getBooleanParam("include-secret"))) {
      new DrivenAdapterSecrets().buildModule(builder);
    }
    new ObjectMapperFactory().buildModule(builder);
  }
}
