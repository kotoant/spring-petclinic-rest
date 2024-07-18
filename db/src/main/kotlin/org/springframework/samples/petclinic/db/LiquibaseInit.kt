package org.springframework.samples.petclinic.db

import liquibase.Contexts
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import java.sql.Connection

object LiquibaseInit {
    @JvmStatic
    fun initFunction(connection: Connection) {
        val database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(JdbcConnection(connection))
        database.defaultSchemaName = "public"
        Liquibase("db/changelog.xml", ClassLoaderResourceAccessor(), database).update(Contexts("schema"))
    }
}
