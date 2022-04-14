package org.jjche.common.system.api;

import org.jjche.common.enums.IBaseEnum;
import org.springframework.core.convert.converter.Converter;

public class UniversalReversedEnumConverter implements Converter<IBaseEnum, String> {

    @Override
    public String convert(IBaseEnum source) {
        return source.getValue();
    }
}
