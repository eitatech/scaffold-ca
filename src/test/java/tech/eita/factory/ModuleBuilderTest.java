package tech.eita.factory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Test;
import tech.eita.exceptions.CleanException;
import tech.eita.task.GenerateStructureTask;

public class ModuleBuilderTest {
  private ModuleBuilder builder;

  @Before
  public void init() throws IOException, CleanException {
    File projectDir = new File("build/unitTest");
    Files.createDirectories(projectDir.toPath());
    writeString(
        new File(projectDir, "build.gradle"),
        "plugins {" + "  id('tech.eita.cleanArchitecture')" + "}");

    Project project =
        ProjectBuilder.builder()
            .withName("cleanArchitecture")
            .withProjectDir(new File("build/unitTest"))
            .build();

    project.getTasks().create("testStructure", GenerateStructureTask.class);
    GenerateStructureTask taskStructure =
        (GenerateStructureTask) project.getTasks().getByName("testStructure");
    taskStructure.generateStructureTask();

    builder = new ModuleBuilder(project);
  }

  @Test
  public void shouldAppendProperties() throws IOException {
    builder.appendToProperties("spring.datasource").put("url", "mydburl");
    builder.appendToProperties("").put("test", "myUnitTes");
    builder.appendToProperties("server").put("port", 8000);
    builder.removeDir(null);
    builder.persist();
  }

  private void writeString(File file, String string) throws IOException {
    try (Writer writer = new FileWriter(file)) {
      writer.write(string);
    }
  }
}
