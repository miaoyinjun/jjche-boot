package org.jjche.demo.modules.student.rest;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.response.response.ResultWrapper;
import org.jjche.core.annotation.controller.ApiRestController;
import org.jjche.core.base.BaseController;
import org.jjche.demo.modules.student.api.dto.StudentDTO;
import org.jjche.demo.modules.student.api.enums.CourseEnum;
import org.jjche.demo.modules.student.api.vo.StudentVO;
import org.jjche.demo.modules.student.feign.JjcheSysApi;
import org.jjche.security.annotation.rest.AnonymousGetMapping;
import org.jjche.security.annotation.rest.AnonymousPostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 学生 控制器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
 */
@Api(tags = "cloud")
@ApiSupport(order = 1, author = "miaoyj")
@ApiRestController("cloud")
@RequiredArgsConstructor
public class CloudStudentController extends BaseController {

    private JjcheSysApi jjcheSysApi;

    @ApiOperation(value = "服务端")
    @AnonymousPostMapping(value = "server")
    public ResultWrapper<StudentVO> server(@RequestBody StudentDTO studentDTO) {
        StudentVO studentVO = new StudentVO();
        studentVO.setCourse(studentDTO.getCourse());
        return ResultWrapper.ok(studentVO);
    }

    @ApiOperation(value = "调用端")
    @AnonymousGetMapping(value = "client")
    public ResultWrapper<StudentVO> client() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setCourse(CourseEnum.AUDIO);
        return jjcheSysApi.server(studentDTO);
    }
}