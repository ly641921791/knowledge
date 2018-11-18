package com.example.aop.bean;

import lombok.Data;

/**
 * @author ly
 */
@Data
public class Target {

    public void target(RuntimeException e) {
        if (e != null) {
            throw e;
        }
        System.out.println("target 正常执行");
    }

}
