package tech.eita.factory.pipelines;

import tech.eita.exceptions.InvalidTaskOptionException;
import tech.eita.factory.ModuleFactory;

public class ModuleFactoryPipeline {

  @SuppressWarnings("SwitchStatementWithTooFewBranches")
  public static ModuleFactory getPipelineFactory(PipelineType type)
      throws InvalidTaskOptionException {
    switch (type) {
      case AZURE:
        return new PipelineAzure();
      case GITHUB:
        return new GitHubAction();
      default:
        throw new InvalidTaskOptionException("Pipeline value invalid");
    }
  }

  public enum PipelineType {
    AZURE,
    GITHUB
  }
}
