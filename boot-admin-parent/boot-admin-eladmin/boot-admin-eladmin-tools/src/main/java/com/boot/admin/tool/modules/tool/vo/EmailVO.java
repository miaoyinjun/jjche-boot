package com.boot.admin.tool.modules.tool.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 发送邮件时，接收参数的类
 *
 * @author 郑杰
 * @since 2018/09/28 12:02:14
 * @version 1.0.8-SNAPSHOT
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailVO {

    /** 收件人，支持多个收件人 */
    @NotEmpty
    private List<String> tos;

    @NotBlank
    private String subject;

    @NotBlank
    private String content;
}
