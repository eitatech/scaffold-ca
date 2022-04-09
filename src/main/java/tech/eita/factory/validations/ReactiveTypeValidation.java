package tech.eita.factory.validations;

import tech.eita.exceptions.ValidationException;
import tech.eita.factory.ModuleBuilder;

public class ReactiveTypeValidation implements Validation {

  @Override
  public void validate(ModuleBuilder moduleBuilder) throws ValidationException {
    if (Boolean.FALSE.equals(moduleBuilder.isReactive())) {
      throw new ValidationException("This module is only available for reactive projects");
    }
  }
}
