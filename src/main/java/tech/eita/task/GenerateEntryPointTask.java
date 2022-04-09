package tech.eita.task;

import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleFactory;
import tech.eita.factory.entrypoints.ModuleFactoryEntryPoint;
import tech.eita.utils.Utils;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.gradle.api.tasks.options.OptionValues;
import tech.eita.Constants;
import tech.eita.factory.entrypoints.EntryPointRestMvcServer;

public class GenerateEntryPointTask extends CleanArchitectureDefaultTask {
  private ModuleFactoryEntryPoint.EntryPointType type;
  private String name;
  private String pathGraphql = Constants.PATH_GRAPHQL;
  private EntryPointRestMvcServer.Server server = EntryPointRestMvcServer.Server.UNDERTOW;
  private Constants.BooleanOption router = Constants.BooleanOption.TRUE;
  private Constants.BooleanOption swagger = Constants.BooleanOption.FALSE;

  @Option(option = "type", description = "Set type of entry point to be generated")
  public void setType(ModuleFactoryEntryPoint.EntryPointType type) {
    this.type = type;
  }

  @Option(option = "name", description = "Set entry point name when GENERIC type")
  public void setName(String name) {
    this.name = name;
  }

  @Option(
      option = "server",
      description = "Set server on which the application will run when RESTMVC type")
  public void setServer(EntryPointRestMvcServer.Server server) {
    this.server = server;
  }

  @Option(option = "router", description = "Set router function for webflux ")
  public void setRouter(Constants.BooleanOption router) {
    this.router = router;
  }

  @Option(option = "swagger", description = "Set swagger configuration to rest entry point ")
  public void setSwagger(Constants.BooleanOption swagger) {
    this.swagger = swagger;
  }

  @Option(option = "pathgql", description = "set API GraphQL path")
  public void setPathGraphql(String pathgql) {
    this.pathGraphql = pathgql;
  }

  @OptionValues("server")
  public List<EntryPointRestMvcServer.Server> getServerOptions() {
    return Arrays.asList(EntryPointRestMvcServer.Server.values());
  }

  @OptionValues("type")
  public List<ModuleFactoryEntryPoint.EntryPointType> getTypes() {
    return Arrays.asList(ModuleFactoryEntryPoint.EntryPointType.values());
  }

  @OptionValues("router")
  public List<Constants.BooleanOption> getRoutersOptions() {
    return Arrays.asList(Constants.BooleanOption.values());
  }

  @OptionValues("swagger")
  public List<Constants.BooleanOption> getSwaggerOptions() {
    return Arrays.asList(Constants.BooleanOption.values());
  }

  @TaskAction
  public void generateEntryPointTask() throws IOException, CleanException {
    if (type == null) {
      printHelp();
      throw new IllegalArgumentException(
          "No Entry Point is set, usage: gradle generateEntryPoint --type "
              + Utils.formatTaskOptions(getTypes()));
    }
    ModuleFactory moduleFactory = ModuleFactoryEntryPoint.getEntryPointFactory(type);
    logger.lifecycle("Clean Architecture plugin version: {}", Utils.getVersionPlugin());
    logger.lifecycle("Entry Point type: {}", type);
    builder.addParam("task-param-name", name);
    builder.addParam("task-param-server", server);
    builder.addParam("task-param-pathgql", pathGraphql);
    builder.addParam("task-param-router", router == Constants.BooleanOption.TRUE);
    builder.addParam("include-swagger", swagger == Constants.BooleanOption.TRUE);
    builder.addParam("lombok", builder.isEnableLombok());
    moduleFactory.buildModule(builder);
    builder.persist();
  }
}
