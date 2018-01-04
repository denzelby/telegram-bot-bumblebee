package com.github.bumblebee.security

import com.github.bumblebee.security.exception.UnprivilegedExecutionException
import com.github.bumblebee.security.roles.service.UserRolesService
import com.github.bumblebee.util.loggerFor
import com.github.telegram.domain.Update
import org.springframework.aop.MethodBeforeAdvice
import org.springframework.aop.framework.ProxyFactory
import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
import com.github.bumblebee.bot.consumer.UpdateHandler
import java.lang.reflect.Method
import java.text.MessageFormat
import java.util.*

@Component
class PrivilegedCommandBeanPostProcessor(private val rolesService: UserRolesService) : BeanPostProcessor {

    private val beans = HashMap<String, Class<*>>()
    private val updateHandlerMethod: Method = UpdateHandler::class.java.methods[0]

    @Throws(BeansException::class)
    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        val clazz = bean.javaClass
        if (clazz.isAnnotationPresent(PrivilegedCommand::class.java)) {
            if (!UpdateHandler::class.java.isAssignableFrom(clazz)) {
                throw IllegalStateException(clazz.name + "is not an UpdateHandler")
            }
            beans.put(beanName, clazz)
        }
        return bean
    }

    @Throws(BeansException::class)
    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        val clazz = beans[beanName] ?: return bean

        log.info("Instantiating proxy for {}", clazz)

        val proxyFactory = ProxyFactory(bean)
        proxyFactory.addAdvice(MethodBeforeAdvice { method, args, target ->
            if (updateHandlerMethod == method) {
                val update = args[0] as Update
                val role = (clazz.getAnnotation(PrivilegedCommand::class.java) as PrivilegedCommand).role

                ensurePrivilege(target, update, role)
            }
        })
        return proxyFactory.proxy
    }

    private fun ensurePrivilege(bean: Any, update: Update, role: UserRole) {
        val userId = update.message?.from?.id
        log.info("Executing privileged from: {}", userId)

        if (userId == null || !rolesService.hasPrivilege(userId, role)) {

            (bean as? UnauthorizedRequestAware)?.onUnauthorizedRequest(update)

            throw UnprivilegedExecutionException(
                    MessageFormat.format("Role '{0}' required. User id: {1}", role, userId))
        }
    }

    companion object {
        private val log = loggerFor<PrivilegedCommandBeanPostProcessor>()
    }
}
