package tech.eita.factory.helpers;

import tech.eita.factory.ModuleFactory;

public class ModuleFactoryHelpers {

  private ModuleFactoryHelpers() {}

  public static ModuleFactory getDrivenAdapterFactory() {
    return new HelperGeneric();
  }
}
