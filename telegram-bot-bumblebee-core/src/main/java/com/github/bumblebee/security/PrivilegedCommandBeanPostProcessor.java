package com.github.bumblebee.security;

import com.github.bumblebee.security.exception.UnprivilegedExecutionException;
import com.github.bumblebee.security.roles.service.UserRolesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import telegram.domain.Update;
import telegram.polling.UpdateHandler;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Component
public class PrivilegedCommandBeanPostProcessor implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(PrivilegedCommandBeanPostProcessor.class);

    private final Map<String, Class> beans = new HashMap<>();
    private final Method updateHandlerMethod;

    private final UserRolesService rolesService;

    @Autowired
    public PrivilegedCommandBeanPostProcessor(UserRolesService rolesService) {
        this.rolesService = rolesService;
        updateHandlerMethod = UpdateHandler.class.getMethods()[0];
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        if (clazz.isAnnotationPresent(PrivilegedCommand.class)) {
            if (!UpdateHandler.class.isAssignableFrom(clazz)) {
                throw new IllegalStateException(clazz.getName() + "is not an UpdateHandler");
            }
            beans.put(beanName, clazz);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class clazz = beans.get(beanName);
        if (clazz == null) {
            return bean;
        }

        log.info("Instantiating proxy for {}", clazz);

        ProxyFactory proxyFactory = new ProxyFactory(bean);
        proxyFactory.addAdvice((MethodBeforeAdvice) (method, args, target) -> {

            if (updateHandlerMethod.equals(method)) {
                Update update = (Update) args[0];
                UserRole role = ((PrivilegedCommand) clazz.getAnnotation(PrivilegedCommand.class)).role();

                ensurePrivilege(target, update, role);
            }
        });
        return proxyFactory.getProxy();
    }

    private void ensurePrivilege(Object bean, Update update, UserRole role) {
        Long userId = update.getMessage().getFrom().getId();
        log.info("Executing privileged from: {}", userId);

        if (!rolesService.hasPrivilege(userId, role)) {

            if (bean instanceof UnauthorizedRequestAware) {
                ((UnauthorizedRequestAware) bean).onUnauthorizedRequest(update);
            }

            throw new UnprivilegedExecutionException(
                    MessageFormat.format("Role '{0}' required. User id: {1}", role, userId));
        }
    }
}
