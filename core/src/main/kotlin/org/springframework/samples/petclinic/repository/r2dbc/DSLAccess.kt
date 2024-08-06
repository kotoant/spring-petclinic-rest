package org.springframework.samples.petclinic.repository.r2dbc

import io.r2dbc.spi.Connection
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.conf.Settings
import org.jooq.impl.DSL
import org.reactivestreams.Publisher
import org.springframework.r2dbc.core.DatabaseClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

private val settings = Settings()

private fun Connection.dsl() = DSL.using(this, SQLDialect.POSTGRES, settings)

fun <T : Any> DatabaseClient.inContext(block: (DSLContext) -> Mono<T>): Mono<T> =
    inConnection { connection -> block(connection.dsl()) }

suspend fun <T : Any> DatabaseClient.inContextCoroutine(block: suspend (DSLContext) -> T): T =
    inContext { ctx -> mono { block(ctx) } }.awaitSingle()

suspend fun <T : Any> DatabaseClient.inContextCoroutineNullable(block: suspend (DSLContext) -> T?): T? =
    inContext { ctx -> mono { block(ctx) } }.awaitSingleOrNull()

fun <T : Any> Publisher<T>.toMono() = Mono.from(this)

fun <T : Any> Publisher<T>.toFlux() = Flux.from(this)

fun <T : Any, R : Any> Publisher<T>.map(mapper: java.util.function.Function<in T, out R>) = toMono().map(mapper)

fun <T : Any, V : Any> Mono<T>.then(other: Publisher<V>) = then(other.toMono())

fun <T : Any, V : Any> Publisher<T>.then(other: Publisher<V>) = toMono().then(other)

fun <T : Any> Publisher<T>.then() = toMono().then()

fun <T : Any, V : Any> Publisher<T>.thenReturn(value: V) = toMono().thenReturn(value)

fun <V : Any> Publisher<Int>.ifZeroThrowElseReturn(value: V, error: () -> Throwable) = toMono().handle { res, sink ->
    if (res == 0) {
        sink.error(error.invoke())
    } else {
        sink.next(value)
        sink.complete()
    }
}

