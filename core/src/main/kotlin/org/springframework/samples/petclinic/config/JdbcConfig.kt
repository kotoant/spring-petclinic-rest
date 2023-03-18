package org.springframework.samples.petclinic.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.support.JdbcTransactionManager
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

@Configuration
@Import(
    value = [
        SqlInitializationAutoConfiguration::class
    ]
)
@Profile("jdbc")
class JdbcConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    fun springDataSource(properties: DataSourceProperties): HikariDataSource {
        return newHikariDataSource(properties)
    }

    private fun newHikariDataSource(properties: DataSourceProperties): HikariDataSource {
        val dataSource = HikariDataSource()
        dataSource.driverClassName = properties.driverClassName
        dataSource.jdbcUrl = properties.url
        dataSource.username = properties.username
        dataSource.password = properties.password
        return dataSource
    }

    @Bean
    fun transactionManager(dataSource: HikariDataSource): DataSourceTransactionManager {
        return JdbcTransactionManager(dataSource)
    }

    @Bean
    @Profile("jdbc & reactive & !loom")
    fun reactiveJdbcServiceScheduler(dataSource: HikariDataSource): Scheduler {
        val threadCount = dataSource.maximumPoolSize
        return Schedulers.newBoundedElastic(threadCount, Int.MAX_VALUE, "reactiveJdbcServiceScheduler")
    }
}
