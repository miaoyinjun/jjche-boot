import cn.hutool.http.HttpStatus;
import controller.SecurityController;
import dto.LoginDTO;
import org.jjche.core.wrapper.enums.ResultWrapperCodeEnum;
import org.jjche.core.wrapper.response.ResultWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import service.JwtUserDetailsServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {JwtUserDetailsServiceImpl.class, SecurityController.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestSecurity {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * <p>
     * 未授权
     * </p>
     *
     * @author miaoyj
     * @since 2020-09-10
     */
    @Test
    public void TestTokenError() {
        /**未提供token*/
        ResponseEntity<ResultWrapper> wrapperResponseEntity = this.restTemplate.getForEntity("http://localhost:" + port + "/security/test", ResultWrapper.class);
        assertEquals(wrapperResponseEntity.getStatusCode().value(), HttpStatus.HTTP_UNAUTHORIZED);
        ResultWrapper wrapper = wrapperResponseEntity.getBody();
        assertNotNull(wrapper);
        assertEquals(wrapper.getCode(), ResultWrapperCodeEnum.TOKEN_ERROR.getCode());
        assertEquals(wrapper.getMessage(), ResultWrapperCodeEnum.TOKEN_ERROR.getMsg());

        /**提供的token无法解析*/
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer dfsdfsdf");
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        wrapperResponseEntity = this.restTemplate.exchange("http://localhost:" + port + "/security/test", HttpMethod.GET, requestEntity, ResultWrapper.class);
        assertEquals(wrapperResponseEntity.getStatusCode().value(), HttpStatus.HTTP_UNAUTHORIZED);
        wrapper = wrapperResponseEntity.getBody();
        assertNotNull(wrapper);
        assertEquals(wrapper.getCode(), ResultWrapperCodeEnum.TOKEN_ERROR.getCode());
        assertEquals(wrapper.getMessage(), ResultWrapperCodeEnum.TOKEN_ERROR.getMsg());
    }

    /**
     * <p>
     * 账户错误
     * </p>
     *
     * @author miaoyj
     * @since 2020-09-10
     */
    @Test
    public void TestUserError() {
        /**用户名或密码错误*/
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(JwtUserDetailsServiceImpl.username);
        loginDTO.setPassword("11");
        ResponseEntity<ResultWrapper> wrapperResponseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/security/login", loginDTO, ResultWrapper.class);
        assertEquals(wrapperResponseEntity.getStatusCode().value(), HttpStatus.HTTP_BAD_REQUEST);
        ResultWrapper wrapper = wrapperResponseEntity.getBody();
        assertNotNull(wrapper);
        assertEquals(wrapper.getCode(), ResultWrapperCodeEnum.USERNAME_NOT_FOUND_OR_BAD_CREDENTIALS.getCode());
        assertEquals(wrapper.getMessage(), ResultWrapperCodeEnum.USERNAME_NOT_FOUND_OR_BAD_CREDENTIALS.getMsg());

        /**账户已被禁用*/
        loginDTO = new LoginDTO();
        loginDTO.setUsername(JwtUserDetailsServiceImpl.usernameEnabled);
        loginDTO.setPassword(JwtUserDetailsServiceImpl.username);
        wrapperResponseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/security/login", loginDTO, ResultWrapper.class);
        assertEquals(wrapperResponseEntity.getStatusCode().value(), HttpStatus.HTTP_BAD_REQUEST);
        wrapper = wrapperResponseEntity.getBody();
        assertNotNull(wrapper);
        assertEquals(wrapper.getCode(), ResultWrapperCodeEnum.USER_DISABLED.getCode());
        assertEquals(wrapper.getMessage(), ResultWrapperCodeEnum.USER_DISABLED.getMsg());

        /**账户被锁定*/
        loginDTO = new LoginDTO();
        loginDTO.setUsername(JwtUserDetailsServiceImpl.usernameAccountNonLocked);
        loginDTO.setPassword(JwtUserDetailsServiceImpl.username);
        wrapperResponseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/security/login", loginDTO, ResultWrapper.class);
        assertEquals(wrapperResponseEntity.getStatusCode().value(), HttpStatus.HTTP_BAD_REQUEST);
        wrapper = wrapperResponseEntity.getBody();
        assertNotNull(wrapper);
        assertEquals(wrapper.getCode(), ResultWrapperCodeEnum.USER_LOCKED.getCode());
        assertEquals(wrapper.getMessage(), ResultWrapperCodeEnum.USER_LOCKED.getMsg());


        /**账户过期*/
        loginDTO = new LoginDTO();
        loginDTO.setUsername(JwtUserDetailsServiceImpl.usernameAccountNonExpired);
        loginDTO.setPassword(JwtUserDetailsServiceImpl.username);
        wrapperResponseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/security/login", loginDTO, ResultWrapper.class);
        assertEquals(wrapperResponseEntity.getStatusCode().value(), HttpStatus.HTTP_BAD_REQUEST);
        wrapper = wrapperResponseEntity.getBody();
        assertNotNull(wrapper);
        assertEquals(wrapper.getCode(), ResultWrapperCodeEnum.USERNAME_EXPIRED.getCode());
        assertEquals(wrapper.getMessage(), ResultWrapperCodeEnum.USERNAME_EXPIRED.getMsg());

        /**密码过期*/
        loginDTO = new LoginDTO();
        loginDTO.setUsername(JwtUserDetailsServiceImpl.usernameCredentialsNonExpired);
        loginDTO.setPassword(JwtUserDetailsServiceImpl.username);
        wrapperResponseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/security/login", loginDTO, ResultWrapper.class);
        assertEquals(wrapperResponseEntity.getStatusCode().value(), HttpStatus.HTTP_BAD_REQUEST);
        wrapper = wrapperResponseEntity.getBody();
        assertNotNull(wrapper);
        assertEquals(wrapper.getCode(), ResultWrapperCodeEnum.USER_CREDENTIALS_EXPIRED.getCode());
        assertEquals(wrapper.getMessage(), ResultWrapperCodeEnum.USER_CREDENTIALS_EXPIRED.getMsg());
    }

    /**
     * <p>
     * 认证错误
     * </p>
     *
     * @author miaoyj
     * @since 2020-09-10
     */
    @Test
    public void TestTokenAccessError() throws InterruptedException {
        /**用户名密码正确*/
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(JwtUserDetailsServiceImpl.username);
        loginDTO.setPassword(JwtUserDetailsServiceImpl.password);
        ResponseEntity<ResultWrapper> wrapperResponseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/security/login", loginDTO, ResultWrapper.class);
        assertEquals(wrapperResponseEntity.getStatusCode().value(), HttpStatus.HTTP_OK);
        ResultWrapper wrapper = wrapperResponseEntity.getBody();
        assertNotNull(wrapper);
        assertNotNull(wrapper.getData());
        assertEquals(wrapper.getCode(), ResultWrapperCodeEnum.SUCCESS.getCode());
        assertEquals(wrapper.getMessage(), ResultWrapperCodeEnum.SUCCESS.getMsg());

        String token = wrapper.getData().toString();

        /**不允许访问*/
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        wrapperResponseEntity = this.restTemplate.exchange("http://localhost:" + port + "/security/not_allow", HttpMethod.GET, requestEntity, ResultWrapper.class);
        assertEquals(wrapperResponseEntity.getStatusCode().value(), HttpStatus.HTTP_FORBIDDEN);
        wrapper = wrapperResponseEntity.getBody();
        assertNotNull(wrapper);
        assertEquals(wrapper.getCode(), ResultWrapperCodeEnum.USER_ACCESS_DENIED.getCode());
        assertEquals(wrapper.getMessage(), ResultWrapperCodeEnum.USER_ACCESS_DENIED.getMsg());

        /**访问正常*/
        wrapperResponseEntity = this.restTemplate.exchange("http://localhost:" + port + "/security/allow", HttpMethod.GET, requestEntity, ResultWrapper.class);
        assertEquals(wrapperResponseEntity.getStatusCode().value(), HttpStatus.HTTP_OK);
        wrapper = wrapperResponseEntity.getBody();
        assertNotNull(wrapper);
        assertEquals(wrapper.getCode(), ResultWrapperCodeEnum.SUCCESS.getCode());
        assertEquals(wrapper.getMessage(), ResultWrapperCodeEnum.SUCCESS.getMsg());

//        Thread.sleep(4 * 1000);
        /**授权过期 redis被删除无法判定过期*/
//        wrapperResponseEntity = this.restTemplate.exchange("http://localhost:" + port + "/security/allow", HttpMethod.GET, requestEntity, ResultWrapper.class);
//        assertEquals(wrapperResponseEntity.getStatusCode().value(), HttpStatus.HTTP_FORBIDDEN);
//        wrapper = wrapperResponseEntity.getBody();
//        assertNotNull(wrapper);
//        assertEquals(wrapper.getCode(), ResultWrapperCodeEnum.TOKEN_EXPIRED.getCode());
//        assertEquals(wrapper.getMessage(), ResultWrapperCodeEnum.TOKEN_EXPIRED.getMsg());
    }

}