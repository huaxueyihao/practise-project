package com.boot.study.util;

import com.boot.study.exeception.BusinessException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 参数校验器
 */
public class ValidatorUtil {

    public static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static void validator(Object obj) {
        Set<ConstraintViolation<Object>> set = validator.validate(obj);
        set.forEach(constraint -> {
            throw new BusinessException(100, constraint.getMessage());
        });
    }


    public static void validatorProperties(Object obj, String... property) {
        if (Objects.nonNull(property) && property.length > 0) {
            Set<ConstraintViolation<Object>> set = Stream.of(property).flatMap(
                    item -> validator.validateProperty(obj, item, Default.class
                    ).stream()).collect(Collectors.toSet());

            if (Objects.nonNull(set) && set.size() > 0) {
                set.forEach(constraint -> {
                    throw new BusinessException(100, constraint.getMessage());
                });
            }
        }
    }


}
