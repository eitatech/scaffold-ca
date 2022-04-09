package tech.eita.factory.entrypoints;

import tech.eita.exceptions.InvalidTaskOptionException;
import tech.eita.factory.ModuleFactory;

public class ModuleFactoryEntryPoint {

  public static ModuleFactory getEntryPointFactory(EntryPointType type)
      throws InvalidTaskOptionException {
    switch (type) {
      case RESTMVC:
        return new EntryPointRestMvc();
      case WEBFLUX:
        return new EntryPointRestWebflux();
      case GENERIC:
        return new EntryPointGeneric();
      case RSOCKET:
        return new EntryPointRSocketResponder();
      case GRAPHQL:
        return new EntryPointGraphql();
      case ASYNCEVENTHANDLER:
        return new EntryPointAsyncEventHandler();
      case MQ:
        return new EntryPointMQ();
      default:
        throw new InvalidTaskOptionException("Entry Point type invalid");
    }
  }

  public enum EntryPointType {
    RESTMVC,
    WEBFLUX,
    GENERIC,
    RSOCKET,
    GRAPHQL,
    ASYNCEVENTHANDLER,
    MQ
  }
}
