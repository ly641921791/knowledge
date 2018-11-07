package com.example.mybatis.enums;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ly
 */
@AllArgsConstructor
@JsonSerialize(using = SexEnumSerializer.class)
public enum SexEnum {

    MALE(1, "男"),
    FEMALE(2, "女");

    @Getter
    private Integer code;
    @Getter
    private String desc;

    private static final Map<Integer, SexEnum> MAPPINGS = new HashMap<>(8);

    static {
        for (SexEnum sexEnum : values()) {
            MAPPINGS.put(sexEnum.code, sexEnum);
        }
    }

    public static SexEnum resolve(Object code) {
        return (code != null ? MAPPINGS.get(code) : null);
    }

}

class SexEnumSerializer extends JsonSerializer<SexEnum> {

    @Override
    public void serialize(SexEnum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeObject(value.getDesc());
        }
    }

}
