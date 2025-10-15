package com.github.yaaanni.factory;

import com.github.yaaanni.annotations.Autowired;
import com.github.yaaanni.annotations.Scope;
import com.github.yaaanni.context.MiniApplicationContext;
import com.github.yaaanni.lifecycle.InitializingBean;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {
    MiniApplicationContext miniApplicationContext;
    Map<Class<?>, Object> beans;
    Reflections scanner;

    public BeanFactory(MiniApplicationContext miniApplicationContext, Reflections scanner) {
        this.miniApplicationContext = miniApplicationContext;
        this.beans = new ConcurrentHashMap<>();
        this.scanner = scanner;
    }

    private <T> T instantiateImplementation(T bean, Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Set<Class<? extends T>> implementationClasses = scanner.getSubTypesOf(clazz);
        if (implementationClasses.size() != 1) {
            throw new NoSuchMethodException("There are no implementations or more than one");
        }
        bean = implementationClasses.stream().findFirst().get().getDeclaredConstructor().newInstance();
        return bean;
    }

    private <T> void injectDependencies(T bean) throws InvocationTargetException, NoSuchMethodException, InstantiationException {
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                Class<?> type = field.getType();
                try {
                    field.setAccessible(true);
                    field.set(bean, getBean(type));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private <T> void initializeBean(T bean) {
        if (bean instanceof InitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }
    }

    public <T> T getBean(Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T bean = null;
        Class<?> requestedType = clazz;
        if (clazz.isInterface()) {
            bean = instantiateImplementation(bean, clazz);
        } else {
            if (beans.containsKey(clazz)) {
                return (T) beans.get(clazz);
            }
            bean = clazz.getDeclaredConstructor().newInstance();
        }
        injectDependencies(bean);
        initializeBean(bean);
        if (isPrototype(bean.getClass())) {
            return bean;
        }
        beans.put(requestedType, bean);
        return bean;
    }

    private <T> boolean isPrototype(Class<T> clazz) {
        Scope scope = clazz.getAnnotation(Scope.class);
        return scope != null && "prototype".equals(scope.value());
    }


}
