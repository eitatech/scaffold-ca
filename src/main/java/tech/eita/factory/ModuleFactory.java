package tech.eita.factory;

import java.io.IOException;
import tech.eita.exceptions.CleanException;

public interface ModuleFactory {
  void buildModule(ModuleBuilder builder) throws IOException, CleanException;
}
