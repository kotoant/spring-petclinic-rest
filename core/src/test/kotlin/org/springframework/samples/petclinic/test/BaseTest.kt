package org.springframework.samples.petclinic.test

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.samples.petclinic.mapper.OwnerMapper
import org.springframework.samples.petclinic.mapper.PetMapper
import org.springframework.samples.petclinic.mapper.PetTypeMapper
import org.springframework.samples.petclinic.mapper.VisitMapper
import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.repository.OwnerRepository
import org.springframework.samples.petclinic.repository.PetRepository
import org.springframework.samples.petclinic.repository.PetTypeRepository
import org.springframework.samples.petclinic.repository.VisitRepository
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Duration
import java.time.LocalDate

@TestInstance(Lifecycle.PER_CLASS)
class BaseTest : BaseTestcontainersTest() {
    @Autowired
    protected lateinit var ownerRepository: OwnerRepository

    @Autowired
    protected lateinit var petRepository: PetRepository

    @Autowired
    protected lateinit var visitRepository: VisitRepository

    @Autowired
    protected lateinit var petTypeRepository: PetTypeRepository

    protected lateinit var testOwner: Owner
    protected lateinit var testPet: Pet
    protected lateinit var testVisit: Visit
    protected lateinit var testPetType: PetType

    @Autowired
    protected lateinit var visitMapper: VisitMapper
    @Autowired
    protected lateinit var petTypeMapper: PetTypeMapper
    @Autowired
    protected lateinit var petMapper: PetMapper
    @Autowired
    protected lateinit var ownerMapper: OwnerMapper

    @Autowired
    protected lateinit var webClient: WebTestClient

    @BeforeAll
    internal fun beforeAll() {
        val owner = ownerRepository.insert(owner())
        val pet = petRepository.insert(pet(owner.id))
        val visit = visitRepository.insert(visit(pet.id))
        val petType = petTypeRepository.insert(petType())
        testOwner = ownerRepository.fetchOneById(owner.id)!!
        testPet = petRepository.fetchOneById(pet.id)!!
        testVisit = visitRepository.fetchOneById(visit.id)!!
        testPetType = petTypeRepository.fetchOneById(petType.id)!!
        webClient = webClient.mutate().responseTimeout(Duration.ofMinutes(5)).build()
    }

    fun owner() = Owner(
        0,
        "Firstname",
        "Lastname",
        "address",
        "city",
        "1234567",
        listOf()
    )
    fun assertOwner(owner: Owner, expected: Owner, id: Int = owner.id) {
        Assertions.assertThat(owner.id).isEqualTo(id)
        Assertions.assertThat(owner.firstName).isEqualTo(expected.firstName)
        Assertions.assertThat(owner.lastName).isEqualTo(expected.lastName)
        Assertions.assertThat(owner.address).isEqualTo(expected.address)
        Assertions.assertThat(owner.city).isEqualTo(expected.city)
        Assertions.assertThat(owner.telephone).isEqualTo(expected.telephone)
        Assertions.assertThat(owner.pets).isEmpty()
    }

    fun pet(ownerId: Int) = Pet(
        0,
        "Name",
        LocalDate.of(2023, 3, 14),
        PetType(1, "cat"),
        ownerId,
        listOf()
    )
    fun assertPet(pet: Pet, expected: Pet, id: Int = pet.id) {
        Assertions.assertThat(pet.id).isEqualTo(id)
        Assertions.assertThat(pet.name).isEqualTo(expected.name)
        Assertions.assertThat(pet.birthDate).isEqualTo(expected.birthDate)
        Assertions.assertThat(pet.type.id).isEqualTo(expected.type.id)
        Assertions.assertThat(pet.type.name).isEqualTo(expected.type.name)
        Assertions.assertThat(pet.ownerId).isEqualTo(expected.ownerId)
        Assertions.assertThat(pet.visits).isEmpty()
    }

    fun visit(petId: Int) =
        Visit(0, petId, LocalDate.of(2022, 3, 14), "description")
    fun assertVisit(visit: Visit, expected: Visit, id: Int = visit.id) {
        Assertions.assertThat(visit.id).isEqualTo(id)
        Assertions.assertThat(visit.petId).isEqualTo(expected.petId)
        Assertions.assertThat(visit.date).isEqualTo(expected.date)
        Assertions.assertThat(visit.description).isEqualTo(expected.description)
    }

    fun petType() = PetType(0, "parrot")
}


