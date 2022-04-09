package tech.eita.factory.entrypoints;

import static tech.eita.Constants.APP_SERVICE;
import static tech.eita.utils.Utils.buildImplementation;
import static tech.eita.utils.Utils.tomcatExclusion;

import tech.eita.Constants;
import tech.eita.exceptions.InvalidTaskOptionException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.factory.ModuleFactory;

import java.io.IOException;

public class EntryPointRestMvcServer implements ModuleFactory {

  @Override
  public void buildModule(ModuleBuilder builder) throws IOException, InvalidTaskOptionException {
    Server server = (Server) builder.getParam("task-param-server");

    switch (server) {
      case UNDERTOW:
        String undertowDependency =
            buildImplementation(
                builder.isKotlin(),
                "org.springframework.boot:spring-boot-starter-undertow:"
                    + Constants.UNDERTOW_VERSION);
        builder.appendDependencyToModule(APP_SERVICE, undertowDependency);
        builder.appendConfigurationToModule(APP_SERVICE, tomcatExclusion(builder.isKotlin()));
        return;
      case JETTY:
        String jettyDependency =
            buildImplementation(
                builder.isKotlin(),
                "org.springframework.boot:spring-boot-starter-jetty:" + Constants.JETTY_VERSION);
        builder.appendDependencyToModule(APP_SERVICE, jettyDependency);
        builder.appendConfigurationToModule(APP_SERVICE, tomcatExclusion(builder.isKotlin()));
        return;
      case TOMCAT:
        return;
      default:
        throw new InvalidTaskOptionException("Server option invalid");
    }
  }

  public enum Server {
    UNDERTOW,
    TOMCAT,
    JETTY
  }
}
