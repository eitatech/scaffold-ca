package tech.eita.factory.pipelines;

import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.factory.ModuleFactory;

import java.io.IOException;

public class GitHubAction implements ModuleFactory {

  @Override
  public void buildModule(ModuleBuilder builder) throws IOException, CleanException {
    builder.setupFromTemplate("pipeline/github");
  }
}
