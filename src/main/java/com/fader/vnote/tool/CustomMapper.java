package com.fader.vnote.tool;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.metadata.Type;

import java.util.Date;

/**
 * @author FaderW
 * 2019/10/17
 */

public class CustomMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        ConverterFactory converterFactory = factory.getConverterFactory();
        converterFactory.registerConverter(new DateToLongConverter());

    }

    static class DateToLongConverter extends CustomConverter<Date, Long> {

        @Override
        public Long convert(Date source, Type<? extends Long> destinationType, MappingContext mappingContext) {
            return null;
        }
    }
}
