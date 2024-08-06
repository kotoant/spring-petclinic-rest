package org.springframework.samples.petclinic.repository

import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper
import org.jooq.Records
import org.jooq.impl.DSL.multiset
import org.jooq.impl.DSL.noCondition
import org.jooq.impl.DSL.row
import org.reactivestreams.Publisher
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.samples.petclinic.jooq.pg_catalog.Routines
import org.springframework.samples.petclinic.jooq.public_.Tables.OWNERS
import org.springframework.samples.petclinic.jooq.public_.Tables.PETS
import org.springframework.samples.petclinic.jooq.public_.Tables.TYPES
import org.springframework.samples.petclinic.jooq.public_.Tables.VISITS
import org.springframework.samples.petclinic.jooq.public_.tables.SleepAndFetch.SLEEP_AND_FETCH
import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.repository.r2dbc.ifZeroThrowElseReturn
import org.springframework.samples.petclinic.repository.r2dbc.toFlux
import org.springframework.samples.petclinic.repository.r2dbc.toMono
import org.springframework.samples.petclinic.service.exception.OwnerNotFoundException
import org.springframework.samples.petclinic.service.exception.PetNotFoundException
import org.springframework.samples.petclinic.service.exception.PetTypeNotFoundException
import org.springframework.samples.petclinic.service.exception.VisitNotFoundException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.sql.ResultSet

fun <R : Record, E : Any> Publisher<R>.fetchOneReactive(mapper: RecordMapper<in R, E>) = toMono().map(mapper)

fun <R : Record, E : Any> Publisher<R>.fetchReactive(mapper: RecordMapper<in R, E>) = toFlux().map(mapper).collectList()

fun DSLContext.fetchOneVisitByIdReactive(id: Int) = selectOneVisitById(id).fetchOneReactive(visitMapper())

fun DSLContext.fetchOneVisitById(id: Int) = selectOneVisitById(id).fetchOne(visitMapper())

private fun visitMapper() = Records.mapping(::Visit)

private fun DSLContext.selectOneVisitById(id: Int) = selectVisits().where(VISITS.ID.equal(id))

private fun DSLContext.selectVisits() =
    select(VISITS.ID, VISITS.PET_ID, VISITS.VISIT_DATE, VISITS.DESCRIPTION)
        .from(VISITS)

fun DSLContext.fetchOnePetByIdReactive(id: Int) = selectOnePetById(id).fetchOneReactive(petMapper())

fun DSLContext.fetchOnePetById(id: Int) = selectOnePetById(id).fetchOne(petMapper())

private fun petMapper() = Records.mapping(::Pet)

private fun DSLContext.selectOnePetById(id: Int) = selectPets().where(PETS.ID.equal(id))

private fun DSLContext.selectPets() =
    select(
        PETS.ID, PETS.NAME, PETS.BIRTH_DATE,
        row(PETS.types_().ID, PETS.types_().NAME).mapping(::PetType),
        PETS.OWNER_ID,
        multiset(
            selectVisits()
                .where(VISITS.PET_ID.eq(PETS.ID))
                .orderBy(VISITS.ID)
        ).`as`("visits").convertFrom { r -> r.map(visitMapper()) }
    ).from(PETS)

fun DSLContext.fetchOneOwnerByIdReactive(id: Int) = selectOneOwnerById(id).fetchOneReactive(ownerMapper())

fun DSLContext.fetchOneOwnerById(id: Int) = selectOneOwnerById(id).fetchOne(ownerMapper())

private fun ownerMapper() = Records.mapping(::Owner)

private fun DSLContext.selectOneOwnerById(id: Int) = selectOwners().where(OWNERS.ID.equal(id))

private fun DSLContext.selectOwners() =
    select(
        OWNERS.ID,
        OWNERS.FIRST_NAME,
        OWNERS.LAST_NAME,
        OWNERS.ADDRESS,
        OWNERS.CITY,
        OWNERS.TELEPHONE,
        multiset(
            selectPets()
                .where(PETS.OWNER_ID.equal(OWNERS.ID))
                .orderBy(PETS.ID)
        ).`as`("pets")
            .convertFrom { r -> r.map(petMapper()) }
    ).from(OWNERS)

fun DSLContext.fetchOnePetTypeByIdReactive(id: Int) = selectOnePetTypeById(id).fetchOneReactive(petTypeMapper())

fun DSLContext.fetchOnePetTypeById(id: Int) = selectOnePetTypeById(id).fetchOne(petTypeMapper())

private fun petTypeMapper() = Records.mapping(::PetType)

private fun DSLContext.selectOnePetTypeById(id: Int) = selectPetTypes().where(TYPES.ID.equal(id))

private fun DSLContext.selectPetTypes() = select(TYPES.ID, TYPES.NAME).from(TYPES)


fun DSLContext.insertVisitReactive(visit: Visit) = insert(visit).fetchOneReactive { r -> visit.copy(id = r.value1()) }

fun DSLContext.insertVisit(visit: Visit) = insert(visit).fetchOne { r -> visit.copy(id = r.value1()!!) }!!

private fun DSLContext.insert(visit: Visit) =
    insertInto(VISITS, VISITS.PET_ID, VISITS.VISIT_DATE, VISITS.DESCRIPTION)
        .values(visit.petId, visit.date, visit.description)
        .returningResult(VISITS.ID)

fun DSLContext.insertPetReactive(pet: Pet) = insert(pet).fetchOneReactive { r -> pet.copy(id = r.value1()) }

fun DSLContext.insertPet(pet: Pet) = insert(pet).fetchOne { r -> pet.copy(id = r.value1()) }!!

private fun DSLContext.insert(pet: Pet) =
    insertInto(PETS, PETS.NAME, PETS.BIRTH_DATE, PETS.TYPE_ID, PETS.OWNER_ID)
        .values(pet.name, pet.birthDate, pet.type.id, pet.ownerId)
        .returningResult(PETS.ID)

fun DSLContext.insertOwnerReactive(owner: Owner) = insert(owner).fetchOneReactive { r -> owner.copy(id = r.value1()) }

fun DSLContext.insertOwner(owner: Owner) = insert(owner).fetchOne { r -> owner.copy(id = r.value1()) }!!

private fun DSLContext.insert(owner: Owner) =
    insertInto(OWNERS, OWNERS.FIRST_NAME, OWNERS.LAST_NAME, OWNERS.ADDRESS, OWNERS.CITY, OWNERS.TELEPHONE)
        .values(owner.firstName, owner.lastName, owner.address, owner.city, owner.telephone)
        .returningResult(OWNERS.ID)

fun DSLContext.insertPetTypeReactive(petType: PetType) =
    insert(petType).fetchOneReactive { r -> petType.copy(id = r.value1()) }

fun DSLContext.insertPetType(petType: PetType) = insert(petType).fetchOne { r -> petType.copy(id = r.value1()!!) }!!

private fun DSLContext.insert(petType: PetType) =
    insertInto(TYPES, TYPES.NAME)
        .values(petType.name)
        .returningResult(TYPES.ID)


fun DSLContext.updateVisitReactive(visit: Visit) =
    update(visit).fetchOneReactive { r -> visit.copy(petId = r.value1()) }
        .switchIfEmpty(Mono.error(VisitNotFoundException(visit.id)))

fun DSLContext.updateVisit(visit: Visit): Visit {
    return update(visit).fetchOne { r -> visit.copy(petId = r.value1()) } ?: throw VisitNotFoundException(visit.id)
}

private fun DSLContext.update(visit: Visit) =
    update(VISITS)
        .set(VISITS.VISIT_DATE, visit.date)
        .set(VISITS.DESCRIPTION, visit.description)
        .where(VISITS.ID.eq(visit.id))
        .returningResult(VISITS.PET_ID)

fun DSLContext.updatePetReactive(pet: Pet) = update(pet).fetchOneReactive { r -> pet.copy(ownerId = r.value1()) }
    .switchIfEmpty(Mono.error(PetNotFoundException(pet.id)))

fun DSLContext.updatePet(pet: Pet): Pet {
    return update(pet).fetchOne { r -> pet.copy(ownerId = r.value1()) } ?: throw PetNotFoundException(pet.id)
}

private fun DSLContext.update(pet: Pet) =
    update(PETS)
        .set(PETS.NAME, pet.name)
        .set(PETS.BIRTH_DATE, pet.birthDate)
        .set(PETS.TYPE_ID, pet.type.id)
        .where(PETS.ID.eq(pet.id))
        .returningResult(PETS.OWNER_ID)

fun DSLContext.updateOwnerReactive(owner: Owner) =
    update(owner).ifZeroThrowElseReturn(owner) { OwnerNotFoundException(owner.id) }

fun DSLContext.updateOwner(owner: Owner): Owner {
    if (update(owner).execute() == 0) {
        throw OwnerNotFoundException(owner.id)
    }
    return owner
}

private fun DSLContext.update(owner: Owner) =
    update(OWNERS)
        .set(OWNERS.FIRST_NAME, owner.firstName)
        .set(OWNERS.LAST_NAME, owner.lastName)
        .set(OWNERS.ADDRESS, owner.address)
        .set(OWNERS.CITY, owner.city)
        .set(OWNERS.TELEPHONE, owner.telephone)
        .where(OWNERS.ID.eq(owner.id))

fun DSLContext.updatePetTypeReactive(petType: PetType) =
    update(petType).ifZeroThrowElseReturn(petType) { PetTypeNotFoundException(petType.id) }

fun DSLContext.updatePetType(petType: PetType): PetType {
    if (update(petType).execute() == 0) {
        throw PetTypeNotFoundException(petType.id)
    }
    return petType
}

private fun DSLContext.update(petType: PetType) =
    update(TYPES)
        .set(TYPES.NAME, petType.name)
        .where(TYPES.ID.eq(petType.id))


const val DEFAULT_LAST_ID = 0
const val DEFAULT_PAGE_SIZE = 10

fun DSLContext.fetchAllVisitsReactive(
    lastId: Int = DEFAULT_LAST_ID,
    pageSize: Int = DEFAULT_PAGE_SIZE
): Mono<List<Visit>> = selectAllVisits(lastId, pageSize).fetchReactive(visitMapper())

fun DSLContext.fetchAllVisits(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): List<Visit> =
    selectAllVisits(lastId, pageSize).fetch(visitMapper())

private fun DSLContext.selectAllVisits(lastId: Int, pageSize: Int) =
    selectVisits()
        .orderBy(VISITS.ID)
        .seek(lastId)
        .limit(pageSize)

fun DSLContext.fetchAllPetsReactive(
    lastId: Int = DEFAULT_LAST_ID,
    pageSize: Int = DEFAULT_PAGE_SIZE
): Mono<List<Pet>> = selectAllPets(lastId, pageSize).fetchReactive(petMapper())

fun DSLContext.fetchAllPets(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): List<Pet> =
    selectAllPets(lastId, pageSize).fetch(petMapper())

private fun DSLContext.selectAllPets(lastId: Int, pageSize: Int) =
    selectPets()
        .orderBy(PETS.ID)
        .seek(lastId)
        .limit(pageSize)

fun DSLContext.fetchAllOwnersReactive(
    lastId: Int = DEFAULT_LAST_ID,
    pageSize: Int = DEFAULT_PAGE_SIZE
): Mono<List<Owner>> = selectAllOwners(lastId, pageSize).fetchReactive(ownerMapper())

fun DSLContext.fetchAllOwners(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): List<Owner> =
    selectAllOwners(lastId, pageSize).fetch(ownerMapper())

private fun DSLContext.selectAllOwners(lastId: Int, pageSize: Int) =
    selectOwnersByCondition(noCondition(), lastId, pageSize)

private fun DSLContext.selectOwnersByCondition(condition: Condition, lastId: Int, pageSize: Int) =
    selectOwners()
        .where(condition)
        .orderBy(OWNERS.ID)
        .seek(lastId)
        .limit(pageSize)

fun DSLContext.fetchOwnersByLastNameReactive(
    lastName: String,
    lastId: Int = DEFAULT_LAST_ID,
    pageSize: Int = DEFAULT_PAGE_SIZE
): Mono<List<Owner>> = selectOwnersByLastName(lastName, lastId, pageSize).fetchReactive(ownerMapper())

fun DSLContext.fetchOwnersByLastName(
    lastName: String,
    lastId: Int = DEFAULT_LAST_ID,
    pageSize: Int = DEFAULT_PAGE_SIZE
): List<Owner> = selectOwnersByLastName(lastName, lastId, pageSize).fetch(ownerMapper())

private fun DSLContext.selectOwnersByLastName(lastName: String, lastId: Int, pageSize: Int) =
    selectOwnersByCondition(OWNERS.LAST_NAME.like("$lastName%"), lastId, pageSize)

fun DSLContext.fetchAllPetTypesReactive(
    lastId: Int = DEFAULT_LAST_ID,
    pageSize: Int = DEFAULT_PAGE_SIZE
): Mono<List<PetType>> = selectAllPetTypes(lastId, pageSize).fetchReactive(petTypeMapper())

fun DSLContext.fetchAllPetTypes(lastId: Int = DEFAULT_LAST_ID, pageSize: Int = DEFAULT_PAGE_SIZE): List<PetType> =
    selectAllPetTypes(lastId, pageSize).fetch(petTypeMapper())

private fun DSLContext.selectAllPetTypes(lastId: Int, pageSize: Int) =
    selectPetTypes()
        .orderBy(TYPES.ID)
        .seek(lastId)
        .limit(pageSize)


fun DSLContext.deleteVisitById(id: Int) = delete(VISITS).where(VISITS.ID.eq(id))

fun DSLContext.deleteVisitsByPetId(petId: Int) = delete(VISITS).where(VISITS.PET_ID.eq(petId))

fun DSLContext.deletePetById(id: Int) = delete(PETS).where(PETS.ID.eq(id))

fun DSLContext.deleteVisitsByOwnerId(ownerId: Int) = delete(VISITS).where(
    VISITS.PET_ID.`in`(
        select(PETS.ID).from(PETS).where(PETS.OWNER_ID.eq(ownerId))
    )
)

fun DSLContext.deletePetsByOwnerId(ownerId: Int) = delete(PETS).where(PETS.OWNER_ID.eq(ownerId))

fun DSLContext.deleteOwnerById(id: Int) = delete(OWNERS).where(OWNERS.ID.eq(id))

fun DSLContext.deleteVisitsByPetTypeId(petTypeId: Int) = delete(VISITS).where(
    VISITS.PET_ID.`in`(
        select(PETS.ID).from(PETS).where(PETS.TYPE_ID.eq(petTypeId))
    )
)

fun DSLContext.deletePetsByPetTypeId(petTypeId: Int) = delete(PETS).where(PETS.TYPE_ID.eq(petTypeId))

fun DSLContext.deletePetTypeById(id: Int) = delete(TYPES).where(TYPES.ID.eq(id))

fun DSLContext.sleep(millis: Int) = Routines.pgSleep(configuration(), millis / 1000.0)

fun DSLContext.selectSleepAndFetch(sleep: Boolean, millis: Int, strings: Int, length: Int) =
    select(SLEEP_AND_FETCH.STRING)
        .from(
            org.springframework.samples.petclinic.jooq.public_.Routines.sleepAndFetch(
                sleep,
                millis / 1000.0,
                strings,
                length
            )
        )

fun DSLContext.sleepAndFetch(sleep: Boolean, millis: Int, strings: Int, length: Int): List<String> =
    selectSleepAndFetch(sleep, millis, strings, length).fetch { it.value1() }

fun DSLContext.sleepAndFetchReactive(sleep: Boolean, millis: Int, strings: Int, length: Int): Mono<List<String>> =
    selectSleepAndFetch(sleep, millis, strings, length).fetchReactive { it.value1() }

fun DatabaseClient.sleep(millis: Int) =
    sql("select pg_sleep(:seconds)").bind("seconds", millis / 1000.0).then().then(Unit.toMono())

fun NamedParameterJdbcTemplate.sleepAndFetch(sleep: Boolean, millis: Int, strings: Int, length: Int): List<String> {
    val params = MapSqlParameterSource()
        .addValue("sleep", sleep)
        .addValue("seconds", millis / 1000.0)
        .addValue("strings", strings)
        .addValue("length", length)
    return query("select * from sleep_and_fetch(:sleep, :seconds, :strings, :length)", params, StringRowMapper)
}

object StringRowMapper : RowMapper<String> {
    override fun mapRow(rs: ResultSet, rowNum: Int): String?  = rs.getString(1)
}

fun DatabaseClient.sleepAndFetch(sleep: Boolean, millis: Int, strings: Int, length: Int) =
    sql("select * from sleep_and_fetch(:sleep, :seconds, :strings, :length)")
        .bind("sleep", sleep)
        .bind("seconds", millis / 1000.0)
        .bind("strings", strings)
        .bind("length", length)
        .map { row -> row.get(0, String::class.java)!! }
        .all()
        .collectList()
