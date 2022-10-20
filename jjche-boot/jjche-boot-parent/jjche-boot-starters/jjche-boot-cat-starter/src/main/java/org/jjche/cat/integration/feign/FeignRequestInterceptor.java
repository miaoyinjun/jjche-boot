package org.jjche.cat.integration.feign;

import com.dianping.cat.Cat;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.jjche.cat.constant.CatConstantsExt;
import org.jjche.cat.constant.CatContext;
import org.jjche.common.constant.SecurityConstant;
import org.springframework.beans.factory.annotation.Value;

/**
 * <p>
 * Feign拦截器，Cat消息树生成
 * </p>
 *
 * @author miaoyj
 * @since 2022-10-20
 */
public class FeignRequestInterceptor implements RequestInterceptor {

    @Value("${spring.application.name:unknown}")
    private String applicationName;

    @Override
    public void apply(RequestTemplate template) {
        CatContext catContext = new CatContext();
        Cat.logRemoteCallClient(catContext, Cat.getManager().getDomain());
        template.header(CatConstantsExt.CAT_HTTP_HEADER_ROOT_MESSAGE_ID, catContext.getProperty(Cat.Context.ROOT));
        template.header(CatConstantsExt.CAT_HTTP_HEADER_PARENT_MESSAGE_ID, catContext.getProperty(Cat.Context.PARENT));
        template.header(CatConstantsExt.CAT_HTTP_HEADER_CHILD_MESSAGE_ID, catContext.getProperty(Cat.Context.CHILD));
        template.header(SecurityConstant.FEIGN_SERVICE_NAME, applicationName);
    }

}