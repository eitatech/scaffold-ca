package tech.eita.adapters;

import java.util.Optional;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import tech.eita.models.DependencyRelease;
import tech.eita.models.Release;
import tech.eita.utils.RestConsumer;

public class RestService {
  public static final String PLUGIN_RELEASES =
      "http://api.github.com/repos/eitatech/scaffold-ca/releases";
  public static final String DEPENDENCY_RELEASES =
      "https://search.maven.org/solrsearch/select?q=g:%22%s%22+AND+a:%22%s%22&core=gav&rows=1&wt=json";
  private final Logger logger = Logging.getLogger(RestService.class);

  public Release getLatestPluginVersion() {
    try {
      return RestConsumer.callRequest(PLUGIN_RELEASES, Release[].class)[0];
    } catch (Exception e) {
      logger.lifecycle(
          "\tx Can't check the latest version of the plugin, reason: {}", e.getMessage());
      return null;
    }
  }

  public Optional<DependencyRelease> getTheLastDependencyRelease(String dependency) {
    try {
      return Optional.of(
          RestConsumer.callRequest(getDependencyEndpoint(dependency), DependencyRelease.class));
    } catch (Exception e) {
      logger.lifecycle(
          "\tx Can't update this dependency {}, reason: {}", dependency, e.getMessage());
      return Optional.empty();
    }
  }

  private String getDependencyEndpoint(String dependency) {
    String[] id = dependency.split(":");
    if (id.length >= 2) {
      return DEPENDENCY_RELEASES.replaceFirst("%s", id[0]).replace("%s", id[1]);
    }
    throw new IllegalArgumentException(
        dependency
            + " is not a valid dependency usage: gradle u "
            + "--dependency "
            + "dependency.group:artifact");
  }
}
