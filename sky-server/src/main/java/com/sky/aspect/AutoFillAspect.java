package com.sky.aspect;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.sky.annotation.AutotoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;


@Aspect
@Slf4j
@Component
@EnableKnife4j
public class AutoFillAspect {
    //切入点  只拦截 mapper 中标记了 @AutoFill 的方法
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutotoFill)")
    public void autoFillPointcut(){

    }
    @Before("autoFillPointcut()")
    public void autoFILL(JoinPoint joinpoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
       MethodSignature signature=(MethodSignature) joinpoint.getSignature();//方法签名对象
        AutotoFill autoFill=signature.getMethod().getAnnotation(AutotoFill.class);
        OperationType operationType=autoFill.value();//获得数据库操作类型

        Object[] args= joinpoint.getArgs();
        if(args==null||args.length==0)
        {
            return;
        }
        Object entity=args[0];

        LocalDateTime now=LocalDateTime.now();
        Long currentId= BaseContext.getCurrentId();

        if(operationType == OperationType.INSERT){
            //新增：四个字段都填充
            Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

            setCreateTime.invoke(entity, now);
            setCreateUser.invoke(entity, currentId);
            setUpdateTime.invoke(entity, now);
            setUpdateUser.invoke(entity, currentId);
        }else if(operationType == OperationType.UPDATE){
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

            setUpdateTime.invoke(entity, now);
            setUpdateUser.invoke(entity, currentId);
        }
    }

}
