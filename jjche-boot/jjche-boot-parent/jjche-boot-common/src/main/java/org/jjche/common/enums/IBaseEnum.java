package org.jjche.common.enums;

import java.io.Serializable;

public interface IBaseEnum extends Serializable {
    String getDesc();
    String getValue();
}