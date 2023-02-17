package zfx.util.yamlencryptor.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author zhangfx
 * @date 2022/11/2
 */
@Component
public class SpringBeanUtills implements ApplicationContextAware {
    private static ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtills.context = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return (T) context.getBean(clazz);
    }

    public static Object  getBean(String name) throws BeansException {
        return context.getBean(name);
    }
    public static <T> T  getBean(String name, Class<T> clazz) throws BeansException {
        return context.getBean(name,clazz);
    }
}
