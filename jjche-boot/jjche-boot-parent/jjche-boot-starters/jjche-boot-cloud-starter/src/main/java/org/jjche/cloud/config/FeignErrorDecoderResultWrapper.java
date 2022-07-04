package org.jjche.cloud.config;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.jjche.core.exception.FeignResultWrapperException;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * <p>
 * ResultWrapper捕捉feign异常
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-06-10
 */
@Configuration
public class FeignErrorDecoderResultWrapper implements ErrorDecoder {

    /**
     * {@inheritDoc}
     */
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