package tech.eita.factory.validations;

import tech.eita.exceptions.ValidationException;
import tech.eita.factory.ModuleBuilder;

public class LanguageValidation implements Validation {

  @Override
  public void validate(ModuleBuilder moduleBuilder) throws ValidationException {
    if (!moduleBuilder.isKotlin()) {
      throw new ValidationException("This module is only available for kotlin projects");
    }
  }
}
