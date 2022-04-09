package tech.eita.factory.adapters;

import static tech.eita.Constants.APP_SERVICE;
import static tech.eita.utils.Utils.buildImplementation;
import static tech.eita.utils.Utils.buildImplementationFromProject;

import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.factory.ModuleFactory;
import java.io.IOException;
import org.gradle.api.logging.Logger;

public class DrivenAdapterRestConsumer implements ModuleFactory {

  @Override
  public void buildModule(ModuleBuilder builder) throws IOException, CleanException {
    Logger logger = builder.getProject().getLogger();
    if (Boolean.TRUE.equals(builder.isReactive())) {
      logger.lifecycle("Generating rest-consumer for reactive project");
      builder.setupFromTemplate("driven-adapter/consumer-rest/reactive-rest-consumer");
      String implementation =
          buildImplementation(
              builder.isKotlin(), "org.springframework.boot:spring-boot-starter-webflux");
      builder.appendDependencyToModule(APP_SERVICE, implementation);
      builder.appendToProperties("adapter.restconsumer").put("timeout", 5000);
    } else {
      logger.lifecycle("Generating rest-consumer for imperative project");
      builder.setupFromTemplate("driven-adapter/consumer-rest/rest-consumer");
      String implementation =
          buildImplementation(builder.isKotlin(), "com.fasterxml.jackson.core:jackson-databind");
      builder.appendDependencyToModule(APP_SERVICE, implementation);
    }
    builder
        .appendToProperties("adapter.restconsumer")
        .put("url", builder.getStringParam("task-param-url"));
    builder.appendToSettings("rest-consumer", "infrastructure/driven-adapters");
    String dependency = buildImplementationFromProject(builder.isKotlin(), ":rest-consumer");
    builder.appendDependencyToModule(APP_SERVICE, dependency);
  }
}
