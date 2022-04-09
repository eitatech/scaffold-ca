package tech.eita.factory.adapters;

import static tech.eita.Constants.APP_SERVICE;
import static tech.eita.utils.Utils.buildImplementationFromProject;

import java.io.IOException;
import org.gradle.api.logging.Logger;
import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.factory.ModuleFactory;
import tech.eita.factory.commons.ObjectMapperFactory;

public class DrivenAdapterRedis implements ModuleFactory {
  public static final String PARAM_MODE = "task-param-mode";

  @Override
  public void buildModule(ModuleBuilder builder) throws IOException, CleanException {
    Logger logger = builder.getProject().getLogger();
    String typePath = getPathType(builder.isReactive());
    String modePath = getPathMode((Mode) builder.getParam(PARAM_MODE));
    logger.lifecycle("Generating {} in {} mode", typePath, modePath);
    builder.setupFromTemplate("driven-adapter/" + typePath + "/" + modePath);
    builder.appendToSettings("redis", "infrastructure/driven-adapters");
    if (Boolean.TRUE.equals(builder.getBooleanParam("include-secret"))) {
      builder.setupFromTemplate("driven-adapter/" + typePath + "/secret");
    } else {
      builder.appendToProperties("spring.redis").put("host", "localhost").put("port", 6379);
    }
    String dependency = buildImplementationFromProject(builder.isKotlin(), ":redis");
    builder.appendDependencyToModule(APP_SERVICE, dependency);
    if (Boolean.TRUE.equals(builder.getBooleanParam("include-secret"))) {
      new DrivenAdapterSecrets().buildModule(builder);
    }
    new ObjectMapperFactory().buildModule(builder);
  }

  protected String getPathMode(Mode mode) {
    return mode == Mode.REPOSITORY ? "redis-repository" : "redis-template";
  }

  protected String getPathType(boolean isReactive) {
    return isReactive ? "redis-reactive" : "redis";
  }

  public enum Mode {
    REPOSITORY,
    TEMPLATE
  }
}
