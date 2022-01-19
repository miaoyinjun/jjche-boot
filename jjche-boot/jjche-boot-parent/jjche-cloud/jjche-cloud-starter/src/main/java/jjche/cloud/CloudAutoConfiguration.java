package jjche.cloud;

import jjche.cloud.config.FeignConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({FeignConfig.class})
public class CloudAutoConfiguration {
}
