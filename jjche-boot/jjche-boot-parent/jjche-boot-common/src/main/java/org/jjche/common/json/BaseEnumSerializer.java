package org.jjche.common.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.jjche.common.enums.IBaseEnum;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BaseEnumSerializer extends JsonSerializer<IBaseEnum> {

    @Override
    public void serialize(IBaseEnum IBaseEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Map<String, String> map = new HashMap<>(2);
        map.put("value", IBaseEnum.getValue());
        map.put("desc", IBaseEnum.getDesc());
        jsonGenerator.writeObject(map);
    }
}
