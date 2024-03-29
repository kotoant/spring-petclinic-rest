package org.springframework.samples.petclinic.autoconfigure

import mu.KotlinLogging
import org.apache.coyote.ProtocolHandler
import org.eclipse.jetty.util.thread.ThreadPool
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration
import org.springframework.boot.web.embedded.jetty.ConfigurableJettyWebServerFactory
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.task.AsyncTaskExecutor
import org.springframework.core.task.support.TaskExecutorAdapter
import org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

private val log = KotlinLogging.logger {}

@AutoConfiguration
@AutoConfigureBefore(TaskExecutionAutoConfiguration::class)
@Profile("loom")
class LoomAutoConfiguration {

    @Bean(
        name = [
            TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME,
            AsyncAnnotationBeanPostProcessor.DEFAULT_TASK_EXECUTOR_BEAN_NAME
        ]
    )
    @Profile("loom")
    fun asyncTaskExecutor(): AsyncTaskExecutor = TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor())

    @Bean
    @Profile("jdbc & reactive & loom")
    fun reactiveJdbcServiceScheduler(): Scheduler {
        log.info("Loom JDBC Reactive enabled")
        return Schedulers.fromExecutorService(Executors.newVirtualThreadPerTaskExecutor())
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(ProtocolHandler::class)
    @Profile("loom & !reactive")
    class TomcatConfiguration {
        @Bean
        @Profile("loom & !reactive")
        fun loomTomcatProtocolHandlerVirtualThreadExecutorCustomizer(): TomcatProtocolHandlerCustomizer<ProtocolHandler> =
            TomcatProtocolHandlerCustomizer { protocolHandler ->
                protocolHandler.executor = Executors.newVirtualThreadPerTaskExecutor()
                log.info("Loom Tomcat enabled")
            }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(ThreadPool::class)
    @Profile("loom & !reactive")
    class JettyConfiguration {
        @Bean
        @Profile("loom & !reactive")
        fun loomJettyWebServerFactoryCustomizer(): WebServerFactoryCustomizer<ConfigurableJettyWebServerFactory> =
            WebServerFactoryCustomizer { factory ->
                factory.setThreadPool(object : ThreadPool {
                    private val executor = Executors.newVirtualThreadPerTaskExecutor()
                    override fun execute(command: Runnable) {
                        executor.execute(command)
                    }

                    override fun join() {
                        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS)
                    }

                    override fun getThreads(): Int {
                        return 1
                    }

                    override fun getIdleThreads(): Int {
                        return 1
                    }

                    override fun isLowOnThreads(): Boolean {
                        return false
                    }
                })
                log.info("Loom Jetty enabled")
            }
    }
}
