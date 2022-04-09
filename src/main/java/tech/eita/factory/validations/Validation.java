package tech.eita.factory.validations;

import tech.eita.exceptions.ValidationException;
import tech.eita.factory.ModuleBuilder;

public interface Validation {
  void validate(ModuleBuilder moduleBuilder) throws ValidationException;
}
