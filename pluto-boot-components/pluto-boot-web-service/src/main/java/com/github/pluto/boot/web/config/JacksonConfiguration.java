package com.github.pluto.boot.web.config;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.github.pluto.boot.web.annatation.LongToString;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class JacksonConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            SimpleModule module = new SimpleModule();
            module.setSerializerModifier(new BeanSerializerModifier() {
                @Override
                public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                                 BeanDescription beanDesc,
                                                                 List<BeanPropertyWriter> beanProperties) {
                    for (BeanPropertyWriter writer : beanProperties) {
                        if (writer.getMember().hasAnnotation(LongToString.class)
                                && (writer.getType().getRawClass() == Long.class || writer.getType().getRawClass() == long.class)) {
                            writer.assignSerializer(new LongToStringSerializer());
                        }
                    }
                    return beanProperties;
                }
            });
            builder.modules(module);
        };
    }
}