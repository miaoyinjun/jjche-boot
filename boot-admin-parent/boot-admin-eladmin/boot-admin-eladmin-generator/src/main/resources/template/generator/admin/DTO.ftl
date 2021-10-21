package ${packageApi}.dto;

import lombok.Data;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.sql.Timestamp;
import com.boot.admin.common.annotation.QueryCriteria;
import com.boot.admin.common.dto.BaseDTO;
import lombok.EqualsAndHashCode;
/**
* <p>
* ${apiAlias}
* </p>
*
* @author ${author}
* @since ${date}
*/
@Data
@EqualsAndHashCode(callSuper = true)
public class ${className}DTO extends BaseDTO {
<#if columns??>
    <#list columns as column>
   <#if column.formShow>
   <#if column.istNotNull && column.columnKey != 'PRI'>
   <#if column.columnType = 'String'>
   @NotBlank(message = "${column.remark}不能为空")
   <#else>
   @NotNull(message = "${column.remark}不能为空")
   </#if>
   </#if>
   <#if column.columnKey == 'PRI'>
   @NotNull(message = "id不能为空", groups = Update.class)
   </#if>
   @ApiModelProperty(value = "${column.remark}"<#if column.columnType = 'Timestamp'>, dataType = "java.lang.String"</#if>)
   private ${column.columnType} ${column.changeColumnName};
   </#if>
    </#list>
</#if>
}