package com.example.lombok;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LombokDemo {

    public static void main(String[] args) throws ClassNotFoundException {

        Map map = new ConcurrentHashMap<>();
        Class c = String.class;
        String cn = c.getName();

        map.put(cn, c);

        long start = System.nanoTime();

        for (int i = 0; i < 10000; i++) {
            map.get(cn);
        }

        System.out.println(System.nanoTime() - start);

        start = System.nanoTime();

        for (int i = 0; i < 10000; i++) {
            Class.forName(cn);
        }

        System.out.println(System.nanoTime() - start);

        start = System.nanoTime();

        for (int i = 0; i < 10000; i++) {
            try {
                Class.forName(cn);
            }catch (ClassNotFoundException e){

            }
        }

        System.out.println(System.nanoTime() - start);
    }
}

@Data
@Accessors(prefix = "f")
class User {
    private Integer fId;
    private String fName;
}