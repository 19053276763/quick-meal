package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//标识哪个操作需要公共字段
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutotoFill {
    OperationType value();


}
