import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import controller.TestFilterController;
import org.jjche.common.constant.FilterEncryptionConstant;
import org.jjche.common.enums.FilterEncryptionEnum;
import org.jjche.common.wrapper.enums.ResultWrapperCodeEnum;
import org.jjche.filter.property.FilterEncryptionProperties;
import org.jjche.filter.util.EncryptionUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {TestFilterController.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("md5")
public class TestEncryptionMD5Filter {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FilterEncryptionProperties encryptionProperties;

    @Test
    public void Test1() {
        String appId = "12";
        String baseUrl = "http://localhost:" + port + "/filter/test?";
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("pageIndex", "1");
        queryMap.put("pageSize", "10");
        queryMap.put("name", "中as1");
        String queryOrderedString = MapUtil.sortJoin(queryMap, "&", "=", false);

        String timestamp = String.valueOf(System.currentTimeMillis() - FilterEncryptionConstant.EXPIRE_TIME);
        String nonce = RandomUtil.randomString(FilterEncryptionConstant.NONCE_LENGTH);
        /** appId非空验证*/
        ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(baseUrl, String.class);
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode().value(), HttpStatus.HTTP_BAD_REQUEST);
        Object body = responseEntity.getBody();
        Map<String, String> map = JSONUtil.toBean(body.toString(), Map.class);
        assertEquals(map.get("message"), FilterEncryptionEnum.APP_ID.getErrMsg());

        /** 时间戳非空验证*/
        HttpHeaders headers = new HttpHeaders();
        headers.add(FilterEncryptionEnum.APP_ID.getKey(), appId);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        responseEntity = restTemplate.exchange(baseUrl + queryOrderedString, HttpMethod.GET, requestEntity, String.class);
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode().value(), HttpStatus.HTTP_BAD_REQUEST);
        body = responseEntity.getBody();
        map = JSONUtil.toBean(body.toString(), Map.class);
        assertEquals(map.get("message"), FilterEncryptionEnum.TIMESTAMP.getErrMsg());

        /** 随机数非空验证*/
        headers.add(FilterEncryptionEnum.TIMESTAMP.getKey(), timestamp);
        requestEntity = new HttpEntity<>(null, headers);
        responseEntity = restTemplate.exchange(baseUrl + queryOrderedString, HttpMethod.GET, requestEntity, String.class);
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode().value(), HttpStatus.HTTP_BAD_REQUEST);
        body = responseEntity.getBody();
        map = JSONUtil.toBean(body.toString(), Map.class);
        assertEquals(map.get("message"), FilterEncryptionEnum.NONCE.getErrMsg());

        /** 签名非空验证*/
        headers.add(FilterEncryptionEnum.NONCE.getKey(), nonce);
        requestEntity = new HttpEntity<>(null, headers);
        responseEntity = restTemplate.exchange(baseUrl + queryOrderedString, HttpMethod.GET, requestEntity, String.class);
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode().value(), HttpStatus.HTTP_BAD_REQUEST);
        body = responseEntity.getBody();
        map = JSONUtil.toBean(body.toString(), Map.class);
        assertEquals(map.get("message"), FilterEncryptionEnum.SIGN.getErrMsg());

        /** 请求超时验证*/
        headers.add(FilterEncryptionEnum.SIGN.getKey(), nonce);
        requestEntity = new HttpEntity<>(null, headers);
        responseEntity = restTemplate.exchange(baseUrl + queryOrderedString, HttpMethod.GET, requestEntity, String.class);
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode().value(), HttpStatus.HTTP_BAD_REQUEST);
        body = responseEntity.getBody();
        map = JSONUtil.toBean(body.toString(), Map.class);
        assertEquals(map.get("message"), ResultWrapperCodeEnum.REQUEST_TIMEOUT.getMsg());

        /** appId错误验证*/
        timestamp = String.valueOf(System.currentTimeMillis());
        headers.remove(FilterEncryptionEnum.TIMESTAMP.getKey());
        headers.add(FilterEncryptionEnum.TIMESTAMP.getKey(), timestamp);
        requestEntity = new HttpEntity<>(null, headers);
        responseEntity = restTemplate.exchange(baseUrl + queryOrderedString, HttpMethod.GET, requestEntity, String.class);
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode().value(), HttpStatus.HTTP_BAD_REQUEST);
        body = responseEntity.getBody();
        map = JSONUtil.toBean(body.toString(), Map.class);
        assertEquals(map.get("message"), FilterEncryptionEnum.APP_ID.getErrMsg());

        /** 签名错误验证*/
        timestamp = String.valueOf(System.currentTimeMillis());
        appId = "1";
        headers.remove(FilterEncryptionEnum.TIMESTAMP.getKey());
        headers.remove(FilterEncryptionEnum.APP_ID.getKey());
        headers.add(FilterEncryptionEnum.TIMESTAMP.getKey(), timestamp);
        headers.add(FilterEncryptionEnum.APP_ID.getKey(), appId);
        requestEntity = new HttpEntity<>(null, headers);
        responseEntity = restTemplate.exchange(baseUrl + queryOrderedString, HttpMethod.GET, requestEntity, String.class);
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode().value(), HttpStatus.HTTP_BAD_REQUEST);
        body = responseEntity.getBody();
        map = JSONUtil.toBean(body.toString(), Map.class);
        assertEquals(map.get("message"), ResultWrapperCodeEnum.SIGN_ERROR.getMsg());

        String appKey = "11";
        /** 根据参数排序后拼接为字符串 */
        queryOrderedString = EncryptionUtil.queryOrderedString(queryOrderedString);
        /** md5生成16进制小写字符串 */
        String mySign = EncryptionUtil.md5Sign(queryOrderedString, appKey, timestamp, nonce);
        headers.remove(FilterEncryptionEnum.SIGN.getKey());
        headers.add(FilterEncryptionEnum.SIGN.getKey(), mySign);
        requestEntity = new HttpEntity<>(null, headers);
        responseEntity = restTemplate.exchange(baseUrl + queryOrderedString, HttpMethod.GET, requestEntity, String.class);
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode().value(), HttpStatus.HTTP_OK);
        body = responseEntity.getBody();
        assertEquals(body, "test");
    }

}