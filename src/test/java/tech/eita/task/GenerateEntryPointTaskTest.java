package tech.eita.task;

import static org.junit.Assert.*;
import static tech.eita.Constants.APP_SERVICE;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.file.SimplePathVisitor;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Test;
import tech.eita.Constants;
import tech.eita.exceptions.CleanException;
import tech.eita.factory.entrypoints.EntryPointRestMvcServer;
import tech.eita.factory.entrypoints.ModuleFactoryEntryPoint;

public class GenerateEntryPointTaskTest {
  private GenerateEntryPointTask task;

  @Before
  public void init() throws IOException, CleanException {
    setup(GenerateStructureTask.ProjectType.IMPERATIVE);
  }

  public void setup(GenerateStructureTask.ProjectType type) throws IOException, CleanException {
    Project project = ProjectBuilder.builder().withProjectDir(new File("build/unitTest")).build();
    deleteStructure(project.getProjectDir().toPath());
    project.getTasks().create("ca", GenerateStructureTask.class);
    GenerateStructureTask caTask = (GenerateStructureTask) project.getTasks().getByName("ca");
    caTask.setType(type);
    caTask.generateStructureTask();

    ProjectBuilder.builder()
        .withProjectDir(new File("build/unitTest/applications/app-service"))
        .withName(APP_SERVICE)
        .withParent(project)
        .build();

    project.getTasks().create("test", GenerateEntryPointTask.class);
    task = (GenerateEntryPointTask) project.getTasks().getByName("test");
  }

  private void deleteStructure(Path sourcePath) {
    try {
      Files.walkFileTree(
          sourcePath,
          new SimplePathVisitor() {
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
              Files.delete(dir);
              return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
                throws IOException {
              Files.delete(file);
              return FileVisitResult.CONTINUE;
            }
          });
    } catch (IOException e) {
      System.out.println("error delete Structure " + e.getMessage());
    }
  }

  // Assert
  @Test(expected = IllegalArgumentException.class)
  public void shouldHandleErrorWhenNoType() throws IOException, CleanException {
    // Arrange
    task.setType(null);
    // Act
    task.generateEntryPointTask();
  }

  // Assert
  @Test(expected = IllegalArgumentException.class)
  public void shouldHandleErrorWhenNoName() throws IOException, CleanException {
    // Arrange
    task.setType(ModuleFactoryEntryPoint.EntryPointType.GENERIC);
    // Act
    task.generateEntryPointTask();
  }

  // Assert
  @Test(expected = IllegalArgumentException.class)
  public void shouldHandleErrorWhenEmptyName() throws IOException, CleanException {
    // Arrange
    task.setType(ModuleFactoryEntryPoint.EntryPointType.GENERIC);
    task.setName("");
    // Act
    task.generateEntryPointTask();
  }

  @Test
  public void generateEntryPointGeneric() throws IOException, CleanException {
    // Arrange
    task.setType(ModuleFactoryEntryPoint.EntryPointType.GENERIC);
    task.setName("MyEntryPoint");
    // Act
    task.generateEntryPointTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/entry-points/my-entry-point/build.gradle")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/my-entry-point/src/main/java/tech/eita/myentrypoint")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/my-entry-point/src/test/java/tech/eita/myentrypoint")
            .exists());
  }

  @Test
  public void generateEntryPointRsocketResponder() throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.REACTIVE);
    task.setType(ModuleFactoryEntryPoint.EntryPointType.RSOCKET);
    // Act
    task.generateEntryPointTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/entry-points/rsocket-responder/build.gradle")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/rsocket-responder/src/main/java/tech/eita/controller/RsocketController.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/rsocket-responder/src/test/java/tech/eita/controller")
            .exists());
  }

  @Test
  public void generateEntryPointApiGraphql() throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.REACTIVE);
    task.setType(ModuleFactoryEntryPoint.EntryPointType.GRAPHQL);
    // Act
    task.generateEntryPointTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/entry-points/graphql-api/build.gradle").exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/graphql-api/src/main/java/tech/eita/graphqlapi/ApiQueries.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/graphql-api/src/main/java/tech/eita/graphqlapi/ApiMutations.java")
            .exists());
  }

  @Test
  public void generateEntryPointApiRestWithDefaultServer() throws IOException, CleanException {
    // Arrange
    task.setType(ModuleFactoryEntryPoint.EntryPointType.RESTMVC);
    // Act
    task.generateEntryPointTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/entry-points/api-rest/build.gradle").exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/api-rest/src/main/java/tech/eita/api/ApiRest.java")
            .exists());
    assertTrue(
        new File("build/unitTest/infrastructure/entry-points/api-rest/src/test/java/tech/eita/api")
            .exists());
  }

  @Test
  public void generateEntryPointApiRestWithDefaultServerAndSwagger()
      throws IOException, CleanException {
    // Arrange
    task.setType(ModuleFactoryEntryPoint.EntryPointType.RESTMVC);
    task.setSwagger(Constants.BooleanOption.TRUE);
    // Act
    task.generateEntryPointTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/entry-points/api-rest/build.gradle").exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/api-rest/src/main/java/tech/eita/api/ApiRest.java")
            .exists());
    assertTrue(
        new File("build/unitTest/infrastructure/entry-points/api-rest/src/test/java/tech/eita/api")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/api-rest/src/main/java/tech/eita/config/SpringFoxConfig.java")
            .exists());
  }

  @Test
  public void generateEntryPointApiRestWithUndertowServer() throws IOException, CleanException {
    // Arrange
    task.setType(ModuleFactoryEntryPoint.EntryPointType.RESTMVC);
    task.setServer(EntryPointRestMvcServer.Server.UNDERTOW);
    // Act
    task.generateEntryPointTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/entry-points/api-rest/build.gradle").exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/api-rest/src/main/java/tech/eita/api/ApiRest.java")
            .exists());
    assertTrue(
        new File("build/unitTest/infrastructure/entry-points/api-rest/src/test/java/tech/eita/api")
            .exists());

    assertTrue(
        FileUtils.readFileToString(
                new File("build/unitTest/applications/app-service/build.gradle"),
                StandardCharsets.UTF_8)
            .contains("spring-boot-starter-undertow"));
    assertTrue(
        FileUtils.readFileToString(
                new File("build/unitTest/applications/app-service/build.gradle"),
                StandardCharsets.UTF_8)
            .contains(
                "compile.exclude group: \"org.springframework.boot\", module:\"spring-boot-starter-tomcat\""));
  }

  @Test
  public void generateEntryPointApiRestWithJettyServer() throws IOException, CleanException {
    // Arrange
    task.setType(ModuleFactoryEntryPoint.EntryPointType.RESTMVC);
    task.setServer(EntryPointRestMvcServer.Server.JETTY);
    // Act
    task.generateEntryPointTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/entry-points/api-rest/build.gradle").exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/api-rest/src/main/java/tech/eita/api/ApiRest.java")
            .exists());
    assertTrue(
        new File("build/unitTest/infrastructure/entry-points/api-rest/src/test/java/tech/eita/api")
            .exists());

    assertTrue(
        FileUtils.readFileToString(
                new File("build/unitTest/applications/app-service/build.gradle"),
                StandardCharsets.UTF_8)
            .contains("spring-boot-starter-jetty"));
    assertTrue(
        FileUtils.readFileToString(
                new File("build/unitTest/applications/app-service/build.gradle"),
                StandardCharsets.UTF_8)
            .contains(
                "compile.exclude group: \"org.springframework.boot\", module:\"spring-boot-starter-tomcat\""));
  }

  @Test
  public void generateEntryPointApiRestWithTomcatServer() throws IOException, CleanException {
    // Arrange
    task.setType(ModuleFactoryEntryPoint.EntryPointType.RESTMVC);
    task.setServer(EntryPointRestMvcServer.Server.TOMCAT);
    // Act
    task.generateEntryPointTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/entry-points/api-rest/build.gradle").exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/api-rest/src/main/java/tech/eita/api/ApiRest.java")
            .exists());
    assertTrue(
        new File("build/unitTest/infrastructure/entry-points/api-rest/src/test/java/tech/eita/api")
            .exists());

    assertFalse(
        FileUtils.readFileToString(
                new File("build/unitTest/applications/app-service/build.gradle"),
                StandardCharsets.UTF_8)
            .contains(
                "compile.exclude group: \"org.springframework.boot\", module:\"spring-boot-starter-tomcat\""));
  }

  @Test
  public void generateEntryPointReactiveWebWithoutRouterFunctions()
      throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.REACTIVE);
    task.setType(ModuleFactoryEntryPoint.EntryPointType.WEBFLUX);
    task.setRouter(Constants.BooleanOption.FALSE);
    // Act
    task.generateEntryPointTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/entry-points/reactive-web/build.gradle").exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/reactive-web/src/main/java/tech/eita/api/ApiRest.java")
            .exists());
    assertFalse(
        new File(
                "build/unitTest/infrastructure/entry-points/reactive-web/src/main/java/tech/eita/api/RouterRest.java")
            .exists());
    assertFalse(
        new File(
                "build/unitTest/infrastructure/entry-points/reactive-web/src/main/java/tech/eita/api/Handler.java")
            .exists());
  }

  @Test
  public void generateEntryPointReactiveWebWithoutRouterFunctionsAndSwagger()
      throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.REACTIVE);
    task.setType(ModuleFactoryEntryPoint.EntryPointType.WEBFLUX);
    task.setRouter(Constants.BooleanOption.FALSE);
    task.setSwagger(Constants.BooleanOption.TRUE);
    // Act
    task.generateEntryPointTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/entry-points/reactive-web/build.gradle").exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/reactive-web/src/main/java/tech/eita/api/ApiRest.java")
            .exists());
    assertFalse(
        new File(
                "build/unitTest/infrastructure/entry-points/reactive-web/src/main/java/tech/eita/api/RouterRest.java")
            .exists());
    assertFalse(
        new File(
                "build/unitTest/infrastructure/entry-points/reactive-web/src/main/java/tech/eita/api/Handler.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/reactive-web/src/main/java/tech/eita/config/SpringFoxConfig.java")
            .exists());
  }

  @Test
  public void generateEntryPointReactiveWebWithRouterFunctions()
      throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.REACTIVE);
    task.setType(ModuleFactoryEntryPoint.EntryPointType.WEBFLUX);
    task.setRouter(Constants.BooleanOption.TRUE);

    // Act
    task.generateEntryPointTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/entry-points/reactive-web/build.gradle").exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/reactive-web/src/main/java/tech/eita/api/RouterRest.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/reactive-web/src/main/java/tech/eita/api/Handler.java")
            .exists());
  }

  @Test
  public void generateEntryPointReactiveWebWithDefaultOptionFunctions()
      throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.REACTIVE);
    task.setType(ModuleFactoryEntryPoint.EntryPointType.WEBFLUX);

    // Act
    task.generateEntryPointTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/entry-points/reactive-web/build.gradle").exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/reactive-web/src/main/java/tech/eita/api/RouterRest.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/reactive-web/src/main/java/tech/eita/api/Handler.java")
            .exists());
  }

  @Test
  public void generateEntryPointAsyncEventHandler() throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.REACTIVE);
    task.setType(ModuleFactoryEntryPoint.EntryPointType.ASYNCEVENTHANDLER);
    // Act
    task.generateEntryPointTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/entry-points/async-event-handler/build.gradle")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/async-event-handler/src/main/java/tech/eita/events/HandlerRegistryConfiguration.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/async-event-handler/src/main/java/tech/eita/events/handlers/EventsHandler.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/async-event-handler/src/main/java/tech/eita/events/handlers/CommandsHandler.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/async-event-handler/src/main/java/tech/eita/events/handlers/QueriesHandler.java")
            .exists());
  }

  @Test
  public void generateEntryPointMQListener() throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.REACTIVE);
    task.setType(ModuleFactoryEntryPoint.EntryPointType.MQ);

    // Act
    task.generateEntryPointTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/entry-points/mq-listener/build.gradle").exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/entry-points/mq-listener/src/main/java/tech/eita/mq/listener/SampleMQMessageListener.java")
            .exists());
  }

  @Test
  public void shouldGetServerOptions() {
    // Arrange
    // Act
    List<EntryPointRestMvcServer.Server> options = task.getServerOptions();
    // Assert
    assertEquals(3, options.size());
  }

  @Test
  public void shouldGetRouterOptions() {
    // Arrange
    // Act
    List<Constants.BooleanOption> options = task.getRoutersOptions();
    // Assert
    assertEquals(2, options.size());
  }
}
