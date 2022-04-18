package org.jjche.common.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import org.jjche.common.json.BaseEnumDeserializer;
import org.jjche.common.json.BaseEnumSerializer;

import java.io.Serializable;

@JsonDeserialize(using = BaseEnumDeserializer.class)
@JsonSerialize(using = BaseEnumSerializer.class)
public interface IBaseEnum extends Serializable {
    @ApiModelProperty("描述")
    String getDesc();

    @ApiModelProperty("值")
    String getValue();
}