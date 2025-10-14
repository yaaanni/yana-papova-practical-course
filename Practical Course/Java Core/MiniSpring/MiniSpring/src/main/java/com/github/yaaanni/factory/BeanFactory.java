package com.github.yaaanni.factory;

import com.github.yaaanni.annotations.Autowired;
import com.github.yaaanni.annotations.Scope;
import com.github.yaaanni.context.MiniApplicationContext;
import com.github.yaaanni.lifecycle.InitializingBean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {
    MiniApplicationContext miniApplicationContext;
    Map<Class<?>, Object> beans;

    public BeanFactory(MiniApplicationContext miniApplicationContext) {
        this.miniApplicationContext = miniApplicationContext;
        this.beans = new ConcurrentHashMap<>();
    }

    public <T> T getBean(Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (beans.containsKey(clazz)) {
            return (T) beans.get(clazz);
        }
        T bean = clazz.getDeclaredConstructor().newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                Class<?> type = field.getType();
                try {
                    field.set(bean, getBean(type));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (bean instanceof InitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }
        if (isPrototype(clazz)) {
            return bean;
        }
        beans.put(clazz, bean);
        return bean;

    }

    private <T> boolean isPrototype(Class<T> clazz) {
        Scope scope = clazz.getAnnotation(Scope.class);
        return scope != null && "prototype".equals(scope.value());
    }


}
