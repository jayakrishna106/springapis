package com.mytest.webapi.constraint;

import javax.validation.Payload;
import java.lang.annotation.*;

public @interface FieldMatch {
    String message() default "The fields must match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String first();
    String second();

    @Target({ElementType.TYPE,ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List{
        FieldMatch[] value();
    }
}
