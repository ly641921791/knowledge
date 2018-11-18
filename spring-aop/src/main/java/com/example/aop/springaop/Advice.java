package com.example.aop.springaop;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Spring AOP 通知类
 *
 * @author ly
 */
public class Advice implements MethodBeforeAdvice, AfterReturningAdvice, ThrowsAdvice {

    /**
     * 定义于 MethodBeforeAdvice 接口，方法前置通知
     *
     * @param method 目标方法
     * @param args   目标方法参数
     * @param target 目标类
     * @throws Throwable 异常
     */
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("方法前置通知输出 start");
        System.out.println("目标对象：" + target);
        System.out.println("目标方法：" + method.getName());
        System.out.println("参数：" + Arrays.asList(args));
        System.out.println("方法前置通知输出 end");
    }

    /**
     * 定义于 AfterReturningAdvice 接口，方法后置通知
     *
     * @param returnValue 返回值
     * @param method      目标方法
     * @param args        参数
     * @param target      目标对象
     * @throws Throwable 异常
     */
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("方法后置通知输出 start ");
        System.out.println("目标对象：" + target);
        System.out.println("目标方法：" + method.getName());
        System.out.println("参数：" + Arrays.asList(args));
        System.out.println("返回值：" + returnValue);
        System.out.println("方法后置通知输出 end ");
    }

    public void afterThrowing(RuntimeException rx) {

    }

    /**
     * 对未知异常的处理.
     */
    public void afterThrowing(Method method, Object[] args, Object target, Exception ex) throws Throwable {
        System.out.println("*************************************");
        System.out.println("Error happened in class: " + target.getClass().getName());
        System.out.println("Error happened in method: " + method.getName());

        for (int i = 0; i < args.length; i++) {
            System.out.println("arg[" + i + "]: " + args[i]);
        }

        System.out.println("Exception class: " + ex.getClass().getName());
        System.out.println("*************************************");
    }

    /**
     * 对NumberFormatException异常的处理
     */
    public void afterThrowing(Method method, Object[] args, Object target, NumberFormatException ex) throws Throwable {
        System.out.println("*************************************");
        System.out.println("Error happened in class: " + target.getClass().getName());
        System.out.println("Error happened in method: " + method.getName());

        for (int i = 0; i < args.length; i++) {
            System.out.println("args[" + i + "]: " + args[i]);
        }

        System.out.println("Exception class: " + ex.getClass().getName());
        System.out.println("*************************************");
    }

}
