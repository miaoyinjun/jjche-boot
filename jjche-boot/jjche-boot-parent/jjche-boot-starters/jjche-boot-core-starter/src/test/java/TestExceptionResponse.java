import cn.hutool.http.HttpStatus;
import controller.CoreTestController;
import dto.LoginDTO;
import org.jjche.common.response.response.ResultWrapper;
import org.jjche.common.wrapper.enums.ResultWrapperCodeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {CoreTestController.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestExceptionResponse {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void TestAssert() {
        ResponseEntity<ResultWrapper> wrapperResponseEntity = this.restTemplate.getForEntity("http://localhost:" + port + "/core/assert", ResultWrapper.class);
        assertEquals(wrapperResponseEntity.getStatusCode().value(), HttpStatus.HTTP_BAD_REQUEST);
        ResultWrapper wrapper = wrapperResponseEntity.getBody();
        assertNotNull(wrapper);
        assertEquals(wrapper.getCode(), ResultWrapperCodeEnum.VALID_ERROR.getCode());
        assertEquals(wrapper.getMessage(), CoreTestController.ASSERT_MSG);
    }

    @Test
    public void TestGetParamValid() {
        ResponseEntity<ResultWrapper> wrapperResponseEntity = this.restTemplate.getForEntity("http://localhost:" + port + "/core/getParamValid", ResultWrapper.class);
        assertEquals(wrapperResponseEntity.getStatusCode().value(), HttpStatus.HTTP_BAD_REQUEST);
        ResultWrapper wrapper = wrapperResponseEntity.getBody();
        assertNotNull(wrapper);
        assertEquals(wrapper.getCode(), ResultWrapperCodeEnum.PARAMETER_ERROR.getCode());
        assertEquals(wrapper.getMessage(), "name: 不能为空");
    }

    @Test
    public void TestGetParamCustomMsgValid() {
        ResponseEntity<ResultWrapper> wrapperResponseEntity = this.restTemplate.getForEntity("http://localhost:" + port + "/core/getParamCustomMsgValid?name=&sex=", ResultWrapper.class);
        assertEquals(wrapperResponseEntity.getStatusCode().value(), HttpStatus.HTTP_BAD_REQUEST);
        ResultWrapper wrapper = wrapperResponseEntity.getBody();
        assertNotNull(wrapper);
        assertEquals(wrapper.getCode(), ResultWrapperCodeEnum.PARAMETER_ERROR.getCode());
        assertTrue(CoreTestController.GET_PARAM_CUSTOM_MSG_VALID_USER_NAME_MSG.equals(wrapper.getMessage()) ||
                CoreTestController.GET_PARAM_CUSTOM_MSG_VALID_SEX_MSG.equals(wrapper.getMessage()));
    }

    @Test
    public void TestPOSTParamsValid() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> strEntity = new HttpEntity<String>("{}", headers);
        ResponseEntity<ResultWrapper> wrapperResponseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/core/POSTParamsValid", strEntity, ResultWrapper.class);
        assertEquals(wrapperResponseEntity.getStatusCode().value(), HttpStatus.HTTP_BAD_REQUEST);
        ResultWrapper wrapper = wrapperResponseEntity.getBody();
        assertNotNull(wrapper);
        assertEquals(wrapper.getCode(), ResultWrapperCodeEnum.PARAMETER_ERROR.getCode());
        assertTrue(LoginDTO.NICKNAME_MSG.equals(wrapper.getMessage()) || LoginDTO.PHONE_MSG.equals(wrapper.getMessage()));
    }

    @Test
    public void TestException() {
        ResponseEntity<ResultWrapper> wrapperResponseEntity = this.restTemplate.getForEntity("http://localhost:" + port + "/core/exception", ResultWrapper.class);
        assertEquals(wrapperResponseEntity.getStatusCode().value(), HttpStatus.HTTP_INTERNAL_ERROR);
        ResultWrapper wrapper = wrapperResponseEntity.getBody();
        assertNotNull(wrapper);
        assertEquals(wrapper.getCode(), ResultWrapperCodeEnum.UNKNOWN_ERROR.getCode());
        assertEquals(wrapper.getMessage(), ResultWrapperCodeEnum.UNKNOWN_ERROR.getMsg());
    }
}