package com.boot.admin.core.feign;

import com.boot.admin.core.exception.FeignResultWrapperException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * <p>
 * ResultWrapper捕捉feign异常
 * </p>
 *
 * @author miaoyj
 * @since 2021-06-10
 * @version 1.0.0-SNAPSHOT
 */
@Configuration
public class FeignErrorDecoderResultWrapper implements ErrorDecoder {

    /** {@inheritDoc} */
    @Override
    public Exception decode(String methodKey, Response response) {
        try {
           String body = Util.toString(response.body().asReader(Charset.defaultCharset()));
           return new FeignResultWrapperException(body);
        } catch (IOException e) {
        }
        return decode(methodKey, response);
    }
}
