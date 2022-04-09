package tech.eita.factory.adapters;

import static tech.eita.utils.Utils.buildImplementationFromProject;

import java.io.IOException;
import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.factory.ModuleFactory;
import tech.eita.factory.commons.ObjectMapperFactory;

public class DrivenAdapterBinStash implements ModuleFactory {

  @Override
  public void buildModule(ModuleBuilder builder) throws IOException, CleanException {

    CacheMode cacheMode = (CacheMode) builder.getParam("task-param-cache-mode");
    builder.setupFromTemplate("driven-adapter/bin-stash");

    builder.addParam("include-local", cacheMode.equals(CacheMode.LOCAL));
    builder.addParam("include-hybrid", cacheMode.equals(CacheMode.HYBRID));
    builder.addParam("include-centralized", cacheMode.equals(CacheMode.CENTRALIZED));
    builder.appendToSettings("bin-stash", "infrastructure/driven-adapters");
    String dependency = buildImplementationFromProject(builder.isKotlin(), ":bin-stash");
    builder.appendDependencyToModule("app-service", dependency);

    builder.appendToProperties("stash.memory").put("maxSize", "10000");
    builder
        .appendToProperties("stash.redis")
        .put("host", "myredis.host")
        .put("port", "6379")
        .put("database", "0")
        .put("password", "mypwd");

    new ObjectMapperFactory().buildModule(builder);
  }

  public enum CacheMode {
    LOCAL,
    HYBRID,
    CENTRALIZED
  }
}
