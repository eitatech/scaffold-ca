package tech.eita.factory.adapters;

import static tech.eita.Constants.APP_SERVICE;
import static tech.eita.utils.Utils.buildImplementation;

import tech.eita.Constants;
import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.factory.ModuleFactory;
import java.io.IOException;
import org.gradle.api.logging.Logger;

public class DrivenAdapterSecrets implements ModuleFactory {
  @Override
  public void buildModule(ModuleBuilder builder) throws IOException, CleanException {
    Logger logger = builder.getProject().getLogger();
    String secretLibrary = "";
    if (Boolean.TRUE.equals(builder.isReactive())) {
      secretLibrary = "aws-secrets-manager-async";
      builder.setupFromTemplate("driven-adapter/secrets-reactive");
    } else {
      secretLibrary = "aws-secrets-manager-sync";
      builder.setupFromTemplate("driven-adapter/secrets");
    }
    logger.lifecycle("Generating  mode");
    String dependency =
        buildImplementation(
            builder.isKotlin(),
            "tech.eita:" + secretLibrary + ":" + Constants.SECRETS_VERSION);
    builder.appendDependencyToModule(APP_SERVICE, dependency);
    builder.appendToProperties("aws").put("region", "us-east-1").put("secretName", "my-secret");
  }
}
