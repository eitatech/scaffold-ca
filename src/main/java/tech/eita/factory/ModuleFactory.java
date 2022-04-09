package tech.eita.factory;

import tech.eita.exceptions.CleanException;
import java.io.IOException;

public interface ModuleFactory {
  void buildModule(ModuleBuilder builder) throws IOException, CleanException;
}
