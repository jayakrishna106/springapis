package com.mytest.webapi.constraint;

import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class PasswordConstraint implements ConstraintValidator<ValidPassword, String> {
    @Override
    public void initialize(ValidPassword constraintAnnotation){}

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context){
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(10, 160),
                new CharacterRule(EnglishCharacterData.UpperCase,1),
                new CharacterRule(EnglishCharacterData.LowerCase,1),
                new CharacterRule(EnglishCharacterData.Digit,1),
                new CharacterRule(EnglishCharacterData.Special,1),
                new WhitespaceRule()
        ));
        if (password==null)
            return false;

        final RuleResult result = validator.validate(new PasswordData(password));

        if(result.isValid())
            return true;

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(String.join("\n",validator.getMessages(result)))
                .addConstraintViolation();
        return false;
    }
}
