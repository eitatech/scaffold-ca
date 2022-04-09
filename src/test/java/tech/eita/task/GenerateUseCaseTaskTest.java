package tech.eita.task;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Test;
import tech.eita.exceptions.CleanException;
import tech.eita.exceptions.ParamNotFoundException;

public class GenerateUseCaseTaskTest {
  private GenerateUseCaseTask task;

  @Before
  public void setup() throws IOException, CleanException {
    Project project = ProjectBuilder.builder().withProjectDir(new File("build/unitTest")).build();
    project.getTasks().create("test", GenerateUseCaseTask.class);
    project.getTasks().create("ca", GenerateStructureTask.class);
    GenerateStructureTask caTask = (GenerateStructureTask) project.getTasks().getByName("ca");
    caTask.generateStructureTask();
    task = (GenerateUseCaseTask) project.getTasks().getByName("test");
  }

  // Assert
  @Test(expected = IllegalArgumentException.class)
  public void generateUseCaseException() throws IOException, ParamNotFoundException {
    // Arrange
    // Act
    task.generateUseCaseTask();
  }

  @Test
  public void generateUseCase() throws IOException, ParamNotFoundException {
    // Arrange
    task.setName("business");
    // Act
    task.generateUseCaseTask();
    // Assert
    assertTrue(
        new File(
                "build/unitTest/domain/usecase/src/main/java/tech/eita/usecase/business/BusinessUseCase.java")
            .exists());
    assertTrue(new File("build/unitTest/domain/usecase/build.gradle").exists());
  }

  @Test
  public void generateUseCaseWithCorrectName() throws IOException, ParamNotFoundException {
    // Arrange
    task.setName("MyUseCase");
    // Act
    task.generateUseCaseTask();
    // Assert
    assertTrue(
        new File("build/unitTest/domain/usecase/src/main/java/tech/eita/usecase/my/MyUseCase.java")
            .exists());
    assertTrue(new File("build/unitTest/domain/usecase/build.gradle").exists());
  }
}
