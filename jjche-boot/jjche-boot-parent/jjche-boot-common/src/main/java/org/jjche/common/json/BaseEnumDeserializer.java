package org.jjche.common.json;


import cn.hutool.core.util.ClassUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.jjche.common.constant.EnumConstant;
import org.jjche.common.enums.IBaseEnum;
import org.springframework.beans.BeanUtils;

import java.io.IOException;

public class BaseEnumDeserializer extends JsonDeserializer<IBaseEnum> {


    @Override
    @SuppressWarnings("unchecked")
    public IBaseEnum deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String currentName = jp.currentName();
        Object currentValue = jp.getCurrentValue();
        @SuppressWarnings("rawtypes")
        Class findPropertyType = BeanUtils.findPropertyType(currentName, currentValue.getClass());
        JsonFormat annotation = (JsonFormat) findPropertyType.getAnnotation(JsonFormat.class);
        IBaseEnum valueOf = null;

        String enumClassName = findPropertyType.getName();
        if (annotation == null || annotation.shape() != JsonFormat.Shape.OBJECT) {
            JSONObject enumJsonObject = JSONUtil.parseObj(node.toString());
            String value = enumJsonObject.getStr(EnumConstant.VALUE);
            String[] values = {value};
            valueOf = ClassUtil.invoke(enumClassName, "resolve", values);
        }
        return valueOf;
    }
}
