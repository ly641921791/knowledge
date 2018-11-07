package com.example.mybatis.config;

import com.example.mybatis.enums.SexEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ly
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    class StringToSexEnumConverter implements Converter<String, SexEnum> {

        @Override
        public SexEnum convert(String source) {
            if (source == null) {
                return null;
            }
            try {
                Integer code = Integer.parseInt(source);
                return SexEnum.resolve(code);
            } catch (NumberFormatException e) {
                throw e;
            }
        }
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToSexEnumConverter());
    }
}
