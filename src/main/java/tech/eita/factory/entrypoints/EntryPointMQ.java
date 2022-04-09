package tech.eita.factory.entrypoints;

import static tech.eita.Constants.APP_SERVICE;
import static tech.eita.utils.Utils.buildImplementationFromProject;

import tech.eita.exceptions.CleanException;
import tech.eita.factory.ModuleBuilder;
import tech.eita.factory.ModuleFactory;

import java.io.IOException;

public class EntryPointMQ implements ModuleFactory {

  @Override
  public void buildModule(ModuleBuilder builder) throws IOException, CleanException {
    builder.setupFromTemplate(getTemplate(builder.isReactive()));
    builder.appendToSettings("mq-listener", "infrastructure/entry-points");
    String dependency = buildImplementationFromProject(builder.isKotlin(), ":mq-listener");
    builder.appendDependencyToModule(APP_SERVICE, dependency);

    builder
        .appendToProperties("commons.jms")
        .put("input-concurrency", 10)
        .put("input-queue", "DEV.QUEUE.2")
        .put("input-queue-alias", "")
        .put("reactive", builder.isReactive());
    builder.appendToProperties("ibm.mq").put("channel", "DEV.APP.SVRCONN").put("user", "app");
  }

  private String getTemplate(boolean reactive) {
    return reactive ? "entry-point/mq-listener" : "entry-point/mq-listener-sync";
  }
}
