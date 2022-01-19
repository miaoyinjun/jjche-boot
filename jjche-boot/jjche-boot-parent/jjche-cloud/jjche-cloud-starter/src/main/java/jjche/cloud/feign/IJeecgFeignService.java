package jjche.cloud.feign;

public interface IJeecgFeignService {

    <T> T newInstance(Class<T> apiType, String name);
}
