package tech.eita.task;

import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleFactory;
import tech.eita.factory.adapters.DrivenAdapterBinStash;
import tech.eita.factory.adapters.DrivenAdapterRedis;
import tech.eita.factory.adapters.ModuleFactoryDrivenAdapter;
import tech.eita.utils.Utils;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.gradle.api.tasks.options.OptionValues;
import tech.eita.Constants;

public class GenerateDrivenAdapterTask extends CleanArchitectureDefaultTask {
  private ModuleFactoryDrivenAdapter.DrivenAdapterType type;
  private String name;
  private String url = "http://localhost:8080";
  private DrivenAdapterRedis.Mode mode = DrivenAdapterRedis.Mode.TEMPLATE;
  private DrivenAdapterBinStash.CacheMode cacheMode = DrivenAdapterBinStash.CacheMode.LOCAL;

  private Constants.BooleanOption secret = Constants.BooleanOption.FALSE;

  @Option(option = "type", description = "Set type of driven adapter to be generated")
  public void setType(ModuleFactoryDrivenAdapter.DrivenAdapterType type) {
    this.type = type;
  }

  @Option(option = "name", description = "Set driven adapter name when GENERIC type")
  public void setName(String name) {
    this.name = name;
  }

  @Option(option = "url", description = "Set driven adapter url when RESTCONSUMER type")
  public void setUrl(String url) {
    this.url = url;
  }

  @Option(option = "mode", description = "Set template or repository mode when REDIS type")
  public void setMode(DrivenAdapterRedis.Mode mode) {
    this.mode = mode;
  }

  @Option(option = "secret", description = "Enable secrets for this driven adapter")
  public void setSecret(Constants.BooleanOption secret) {
    this.secret = secret;
  }

  @OptionValues("type")
  public List<ModuleFactoryDrivenAdapter.DrivenAdapterType> getTypes() {
    return Arrays.asList(ModuleFactoryDrivenAdapter.DrivenAdapterType.values());
  }

  @OptionValues("secret")
  public List<Constants.BooleanOption> getSecretOptions() {
    return Arrays.asList(Constants.BooleanOption.values());
  }

  @Option(option = "cache-mode", description = "Set value for cache type")
  public void setcacheMode(DrivenAdapterBinStash.CacheMode cacheMode) {
    this.cacheMode = cacheMode;
  }

  @TaskAction
  public void generateDrivenAdapterTask() throws IOException, CleanException {
    if (type == null) {
      printHelp();
      throw new IllegalArgumentException(
          "No Driven Adapter type is set, usage: gradle generateDrivenAdapter "
              + "--type "
              + Utils.formatTaskOptions(getTypes()));
    }
    ModuleFactory moduleFactory = ModuleFactoryDrivenAdapter.getDrivenAdapterFactory(type);
    logger.lifecycle("Clean Architecture plugin version: {}", Utils.getVersionPlugin());
    logger.lifecycle("Driven Adapter type: {}", type);
    builder.addParam("task-param-name", name);
    builder.addParam("task-param-cache-mode", cacheMode);
    builder.addParam("include-secret", secret == Constants.BooleanOption.TRUE);
    builder.addParam(DrivenAdapterRedis.PARAM_MODE, mode);
    builder.addParam("lombok", builder.isEnableLombok());
    builder.addParam("task-param-url", url);
    moduleFactory.buildModule(builder);
    builder.persist();
  }
}
