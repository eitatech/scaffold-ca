package tech.eita.task;

import static tech.eita.Constants.APP_SERVICE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import tech.eita.Constants;
import tech.eita.exceptions.CleanException;
import tech.eita.exceptions.ValidationException;
import tech.eita.factory.adapters.DrivenAdapterRedis;
import tech.eita.factory.adapters.ModuleFactoryDrivenAdapter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Test;

public class GenerateDrivenAdapterTaskTest {
  private GenerateDrivenAdapterTask task;

  @Before
  public void init() throws IOException, CleanException {
    setup(GenerateStructureTask.ProjectType.IMPERATIVE);
  }

  private void setup(GenerateStructureTask.ProjectType type) throws IOException, CleanException {
    Project project = ProjectBuilder.builder().withProjectDir(new File("build/unitTest")).build();

    ProjectBuilder.builder()
        .withName(APP_SERVICE)
        .withProjectDir(new File("build/unitTest/applications/app-service"))
        .withParent(project)
        .build();

    project.getTasks().create("ca", GenerateStructureTask.class);
    GenerateStructureTask taskStructure =
        (GenerateStructureTask) project.getTasks().getByName("ca");
    taskStructure.setType(type);
    taskStructure.generateStructureTask();

    project.getTasks().create("test", GenerateDrivenAdapterTask.class);
    task = (GenerateDrivenAdapterTask) project.getTasks().getByName("test");
  }

  // Assert
  @Test(expected = IllegalArgumentException.class)
  public void shouldHandleErrorWhenNoType() throws IOException, CleanException {
    // Arrange
    task.setType(null);
    // Act
    task.generateDrivenAdapterTask();
  }

  // Assert
  @Test(expected = IllegalArgumentException.class)
  public void shouldHandleErrorWhenNoName() throws IOException, CleanException {
    // Arrange
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.GENERIC);
    // Act
    task.generateDrivenAdapterTask();
  }

  // Assert
  @Test(expected = IllegalArgumentException.class)
  public void shouldHandleErrorWhenEmptyName() throws IOException, CleanException {
    // Arrange
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.GENERIC);
    task.setName("");
    // Act
    task.generateDrivenAdapterTask();
  }

  @Test
  public void generateDrivenAdapterGeneric() throws IOException, CleanException {
    // Arrange
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.GENERIC);
    task.setName("MyDrivenAdapter");
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/my-driven-adapter/build.gradle")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/my-driven-adapter/src/main/java/tech/eita/mydrivenadapter")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/my-driven-adapter/src/test/java/tech/eita/mydrivenadapter")
            .exists());
  }

  @Test
  public void generateRestConsumer() throws IOException, CleanException {
    // Arrange
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.RESTCONSUMER);
    task.setUrl("http://localhost:8080");
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/rest-consumer/build.gradle")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/rest-consumer/src/main/java/tech/eita/consumer/RestConsumer.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/rest-consumer/src/main/java/tech/eita/consumer/ObjectResponse.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/rest-consumer/src/main/java/tech/eita/consumer/ObjectRequest.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/rest-consumer/src/main/java/tech/eita/consumer/config/RestConsumerConfig.java")
            .exists());
  }

  @Test
  public void generateRsocketRequester() throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.REACTIVE);
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.RSOCKET);
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/rsocket-requester/build.gradle")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/rsocket-requester/src/main/java/tech/eita/rsocket/config/RequesterConfig.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/rsocket-requester/src/main/java/tech/eita/rsocket/service/RsocketAdapter.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/rsocket-requester/src/test/java/tech/eita/rsocket/service")
            .exists());
  }

  @Test
  public void generateDrivenAdapterJPARepository() throws IOException, CleanException {
    // Arrange
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.JPA);
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/jpa-repository/build.gradle")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/jpa-repository/src/main/java/tech/eita/jpa/JPARepository.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/jpa-repository/src/main/java/tech/eita/jpa/JPARepositoryAdapter.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/jpa-repository/src/main/java/tech/eita/jpa/helper/AdapterOperations.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/jpa-repository/src/main/java/tech/eita/jpa/config/DBSecret.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/jpa-repository/src/main/java/tech/eita/jpa/config/JpaConfig.java")
            .exists());
  }

  @Test
  public void generateDrivenAdapterMongoRepository() throws IOException, CleanException {
    // Arrange
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.MONGODB);
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/mongo-repository/build.gradle")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/mongo-repository/src/main/java/tech/eita/mongo/MongoDBRepository.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/mongo-repository/src/main/java/tech/eita/mongo/MongoRepositoryAdapter.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/mongo-repository/src/main/java/tech/eita/mongo/helper/AdapterOperations.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/mongo-repository/src/main/java/tech/eita/mongo/config/MongoDBSecret.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/mongo-repository/src/main/java/tech/eita/mongo/config/MongoConfig.java")
            .exists());
  }

  @Test
  public void generateDrivenAdapterMongoRepositoryWithSecrets() throws IOException, CleanException {
    // Arrange
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.MONGODB);
    task.setSecret(Constants.BooleanOption.TRUE);
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/mongo-repository/build.gradle")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/mongo-repository/src/main/java/tech/eita/mongo/MongoDBRepository.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/mongo-repository/src/main/java/tech/eita/mongo/MongoRepositoryAdapter.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/mongo-repository/src/main/java/tech/eita/mongo/helper/AdapterOperations.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/mongo-repository/src/main/java/tech/eita/mongo/config/MongoDBSecret.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/mongo-repository/src/main/java/tech/eita/mongo/config/MongoConfig.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/applications/app-service/src/main/java/tech/eita/config/SecretsConfig.java")
            .exists());
  }

  @Test
  public void generateDrivenAdapterMongoRepositoryForNoProjectType()
      throws IOException, CleanException {
    // Arrange
    writeString(
        new File("build/unitTest/gradle.properties"),
        "package=tech.eita\nsystemProp.version=" + Constants.PLUGIN_VERSION + "\n");
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.MONGODB);
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/mongo-repository/build.gradle")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/mongo-repository/src/main/java/tech/eita/mongo/MongoDBRepository.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/mongo-repository/src/main/java/tech/eita/mongo/MongoRepositoryAdapter.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/mongo-repository/src/main/java/tech/eita/mongo/helper/AdapterOperations.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/mongo-repository/src/main/java/tech/eita/mongo/config/MongoDBSecret.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/mongo-repository/src/main/java/tech/eita/mongo/config/MongoConfig.java")
            .exists());
  }

  @Test
  public void generateDrivenAdapterMongoRepositoryForReactive() throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.REACTIVE);
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.MONGODB);
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/mongo-repository/build.gradle")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/mongo-repository/src/main/java/tech/eita/mongo/MongoDBRepository.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/mongo-repository/src/main/java/tech/eita/mongo/MongoRepositoryAdapter.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/mongo-repository/src/main/java/tech/eita/mongo/helper/AdapterOperations.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/mongo-repository/src/main/java/tech/eita/mongo/config/MongoDBSecret.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/mongo-repository/src/main/java/tech/eita/mongo/config/MongoConfig.java")
            .exists());
  }

  @Test
  public void generateReactiveRestConsumer() throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.REACTIVE);
    task.setUrl("http://localhost:8080");
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.RESTCONSUMER);
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/rest-consumer/build.gradle")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/rest-consumer/src/main/java/tech/eita/consumer/RestConsumer.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/rest-consumer/src/main/java/tech/eita/consumer/ObjectResponse.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/rest-consumer/src/main/java/tech/eita/consumer/ObjectRequest.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/rest-consumer/src/main/java/tech/eita/consumer/config/RestConsumerConfig.java")
            .exists());
  }

  @Test
  public void generateDrivenAdapterEventBus() throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.REACTIVE);
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.ASYNCEVENTBUS);
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/async-event-bus/build.gradle")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/async-event-bus/src/main/java/tech/eita/events/ReactiveEventsGateway.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/async-event-bus/src/main/java/tech/eita/events/ReactiveDirectAsyncGateway.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/domain/model/src/main/java/tech/eita/model/events/gateways/EventsGateway.java")
            .exists());
  }

  @Test
  public void shouldGetSecretOptions() {
    // Arrange
    // Act
    List<Constants.BooleanOption> options = task.getSecretOptions();
    // Assert
    assertEquals(2, options.size());
  }

  @Test
  public void generateDrivenAdapterRedisRepositoryForImperative()
      throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.IMPERATIVE);
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.REDIS);
    task.setMode(DrivenAdapterRedis.Mode.REPOSITORY);
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/redis/build.gradle").exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/redis/src/main/java/tech/eita/redis/repository/helper/RepositoryAdapterOperations.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/redis/src/main/java/tech/eita/redis/repository/RedisRepository.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/redis/src/main/java/tech/eita/redis/repository/RedisRepositoryAdapter.java")
            .exists());
  }

  @Test
  public void generateDrivenAdapterRedisRepositoryForReactiveWithSecret()
      throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.REACTIVE);
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.REDIS);
    task.setMode(DrivenAdapterRedis.Mode.REPOSITORY);
    task.setSecret(Constants.BooleanOption.TRUE);
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/redis/build.gradle").exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/redis/src/main/java/tech/eita/redis/repository/helper/ReactiveRepositoryAdapterOperations.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/redis/src/main/java/tech/eita/redis/repository/ReactiveRedisRepository.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/redis/src/main/java/tech/eita/redis/repository/ReactiveRedisRepositoryAdapter.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/redis/src/main/java/tech/eita/redis/config/RedisConfig.java")
            .exists());
  }

  @Test
  public void generateDrivenAdapterRedisTemplateForImperative() throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.IMPERATIVE);
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.REDIS);
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/redis/build.gradle").exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/redis/src/main/java/tech/eita/redis/template/helper/TemplateAdapterOperations.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/redis/src/main/java/tech/eita/redis/template/RedisTemplateAdapter.java")
            .exists());
  }

  @Test
  public void generateDrivenAdapterBinStashTemplateForImperative()
      throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.IMPERATIVE);
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.BINSTASH);
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/bin-stash/build.gradle").exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/bin-stash/src/main/java/tech/eita/binstash/config/BinStashCacheConfig.java")
            .exists());
  }

  @Test
  public void generateDrivenAdapterR2dbcReactive() throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.REACTIVE);
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.R2DBC);
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/r2dbc-postgresql/build.gradle")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/r2dbc-postgresql/src/main/java/tech/eita/config/PostgreSQLConnectionPool.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/r2dbc-postgresql/src/main/java/tech/eita/config/PostgresqlConnectionProperties.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/r2dbc-postgresql/src/main/java/tech/eita/MyReactiveRepository.java")
            .exists());
  }

  @Test
  public void generateDrivenAdapterRedisTemplateForReactiveWithSecret()
      throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.REACTIVE);
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.REDIS);
    task.setSecret(Constants.BooleanOption.TRUE);
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/redis/build.gradle").exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/redis/src/main/java/tech/eita/redis/template/helper/ReactiveTemplateAdapterOperations.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/redis/src/main/java/tech/eita/redis/template/ReactiveRedisTemplateAdapter.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/redis/src/main/java/tech/eita/redis/config/RedisConfig.java")
            .exists());
  }

  @Test
  public void generateDrivenAdapterKMSForReactive() throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.REACTIVE);
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.KMS);
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/kms-repository/build.gradle")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/kms-repository/src/main/java/tech/eita/kms/KmsAdapter.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/redis/src/main/java/tech/eita/redis/template/ReactiveRedisTemplateAdapter.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/redis/src/main/java/tech/eita/redis/config/RedisConfig.java")
            .exists());
  }

  @Test
  public void generateDrivenAdapterKMSForImperative() throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.IMPERATIVE);
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.KMS);
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/kms-repository/build.gradle")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/kms-repository/src/main/java/tech/eita/kms/KmsAdapter.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/kms-repository/src/main/java/tech/eita/kms/config/KmsConfig.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/kms-repository/src/main/java/tech/eita/kms/config/model/KmsConnectionProperties.java")
            .exists());
  }

  @Test
  public void generateDrivenAdapterS3ForReactive() throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.REACTIVE);
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.S3);
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/s3-repository/build.gradle")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/s3-repository/src/main/java/tech/eita/s3/adapter/S3Adapter.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/s3-repository/src/main/java/tech/eita/s3/operations/S3Operations.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/s3-repository/src/main/java/tech/eita/s3/config/S3Config.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/s3-repository/src/main/java/tech/eita/s3/config/model/S3ConnectionProperties.java")
            .exists());
  }

  @Test
  public void generateDrivenAdapterS3ForImperative() throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.IMPERATIVE);
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.S3);
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/s3-repository/build.gradle")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/s3-repository/src/main/java/tech/eita/s3/adapter/S3Adapter.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/s3-repository/src/main/java/tech/eita/s3/operations/S3Operations.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/s3-repository/src/main/java/tech/eita/s3/config/S3Config.java")
            .exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/s3-repository/src/main/java/tech/eita/s3/config/model/S3ConnectionProperties.java")
            .exists());
  }

  @Test
  public void generateMQSender() throws IOException, CleanException {
    // Arrange
    setup(GenerateStructureTask.ProjectType.REACTIVE);
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.MQ);
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/mq-sender/build.gradle").exists());
    assertTrue(
        new File(
                "build/unitTest/infrastructure/driven-adapters/mq-sender/src/main/java/tech/eita/mq/sender/SampleMQMessageSender.java")
            .exists());
  }

  @Test
  public void generateDrivenAdapterDynamoDB() throws IOException, CleanException {
    // Arrange
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.DYNAMODB);
    // Act
    task.generateDrivenAdapterTask();
    // Assert
    assertTrue(
        new File("build/unitTest/infrastructure/driven-adapters/dynamo-db/build.gradle").exists());
  }

  @Test(expected = ValidationException.class)
  public void generateDrivenAdapterKtorShouldThrowValidationException()
      throws IOException, CleanException {
    // Arrange
    task.setType(ModuleFactoryDrivenAdapter.DrivenAdapterType.KTOR);
    // Act
    task.generateDrivenAdapterTask();
  }

  private void writeString(File file, String string) throws IOException {
    try (Writer writer = new FileWriter(file)) {
      writer.write(string);
    }
  }
}
