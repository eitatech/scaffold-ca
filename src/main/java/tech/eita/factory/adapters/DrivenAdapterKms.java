package tech.eita.factory.adapters;

import static tech.eita.Constants.APP_SERVICE;
import static tech.eita.utils.Utils.buildImplementationFromProject;

import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.factory.ModuleFactory;
import java.io.IOException;
import org.gradle.api.logging.Logger;

public class DrivenAdapterKms implements ModuleFactory {

  @Override
  public void buildModule(ModuleBuilder builder) throws IOException, CleanException {
    Logger logger = builder.getProject().getLogger();
    String typePath = getPathType(builder.isReactive());
    logger.lifecycle("Generating {}", typePath);
    builder.setupFromTemplate("driven-adapter/" + typePath);
    builder.appendToSettings("kms-repository", "infrastructure/driven-adapters");
    builder
        .appendToProperties("adapter.aws.kms")
        .put("region", "us-east-1")
        .put("host", "localhost")
        .put("protocol", "http")
        .put("port", "4566")
        .put("keyId", "add-your-key-here"); // implementation project('kms-repository')
    String dependency = buildImplementationFromProject(builder.isKotlin(), ":kms-repository");
    builder.appendDependencyToModule(APP_SERVICE, dependency);
    new DrivenAdapterSecrets().buildModule(builder);
  }

  protected String getPathType(boolean isReactive) {
    return isReactive ? "kms-reactive" : "kms";
  }
}
