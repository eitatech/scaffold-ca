package tech.eita.factory.entrypoints;

import static tech.eita.Constants.APP_SERVICE;
import static tech.eita.utils.Utils.buildImplementationFromProject;

import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.factory.ModuleFactory;

import java.io.IOException;

public class EntryPointRestMvc implements ModuleFactory {

  @Override
  public void buildModule(ModuleBuilder builder) throws IOException, CleanException {
    builder.setupFromTemplate("entry-point/rest-mvc");
    builder.appendToSettings("api-rest", "infrastructure/entry-points");
    String dependency = buildImplementationFromProject(builder.isKotlin(), ":api-rest");
    builder.appendDependencyToModule(APP_SERVICE, dependency);
    if (Boolean.TRUE.equals(builder.getBooleanParam("include-swagger"))) {
      builder.addParam("module", "api-rest");
      builder.setupFromTemplate("entry-point/swagger");
      if (builder.isKotlin()) {
        builder
            .appendToProperties("spring.mvc.pathmatch")
            .put("matching-strategy", "ant_path_matcher");
      }
    }
    new EntryPointRestMvcServer().buildModule(builder);
  }
}
