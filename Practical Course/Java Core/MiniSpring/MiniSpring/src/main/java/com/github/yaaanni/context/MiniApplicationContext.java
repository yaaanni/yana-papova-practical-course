package com.github.yaaanni.context;

import com.github.yaaanni.annotations.Component;
import com.github.yaaanni.annotations.Scope;
import com.github.yaaanni.factory.BeanFactory;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MiniApplicationContext {
    private final Reflections scanner;
    private final Map<Class<?>, Object> beans;
    private final BeanFactory beanFactory;

    public MiniApplicationContext(String path) {
        this.scanner = new Reflections(path);
        beans = new ConcurrentHashMap<>();
        this.beanFactory = new BeanFactory(this);
        registerBean();
    }

    public <T> T getBean(Class<T> clazz) {
        if(isPrototype(clazz)){
            return beanFactory.getBean(clazz);
        }
        return (T) beans.get(clazz);
    }

    public void registerBean() {
        Set<Class<?>> componentClasses = scanner.getTypesAnnotatedWith(Component.class);
        for (Class<?> clazz : componentClasses) {
            if(!isPrototype(clazz)) beans.put(clazz, beanFactory.getBean(clazz));
        }
    }

    private <T> boolean isPrototype(Class<T> clazz){
        Scope scope = clazz.getAnnotation(Scope.class);
        return scope != null && "prototype".equals(scope.value());
    }
}
