package tech.eita.factory.adapters;

import static tech.eita.Constants.APP_SERVICE;
import static tech.eita.utils.Utils.buildImplementationFromProject;

import java.io.IOException;
import org.gradle.api.logging.Logger;
import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.factory.ModuleFactory;

public class DrivenAdapterS3 implements ModuleFactory {

  @Override
  public void buildModule(ModuleBuilder builder) throws IOException, CleanException {
    Logger logger = builder.getProject().getLogger();
    String typePath = getPathType(builder.isReactive());
    logger.lifecycle("Generating {}", typePath);
    builder.setupFromTemplate("driven-adapter/" + typePath);
    builder.appendToSettings("s3-repository", "infrastructure/driven-adapters");
    builder
        .appendToProperties("adapter.aws.s3")
        .put("bucketName", "test")
        .put("region", "us-east-1")
        .put("endpoint", "http://localhost:49153");
    String dependency = buildImplementationFromProject(builder.isKotlin(), ":s3-repository");
    builder.appendDependencyToModule(APP_SERVICE, dependency);
  }

  protected String getPathType(boolean isReactive) {
    return isReactive ? "s3-reactive" : "s3";
  }
}
