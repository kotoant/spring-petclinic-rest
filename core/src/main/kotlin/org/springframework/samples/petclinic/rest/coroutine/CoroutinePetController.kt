package org.springframework.samples.petclinic.rest.coroutine

import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.samples.petclinic.mapper.PetMapper
import org.springframework.samples.petclinic.rest.api.coroutine.PetsCoroutineApi
import org.springframework.samples.petclinic.rest.dto.PetDto
import org.springframework.samples.petclinic.service.coroutine.CoroutineClinicService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
@Profile("coroutine")
class CoroutinePetController(private val clinicService: CoroutineClinicService, private val petMapper: PetMapper) :
    PetsCoroutineApi {

    override suspend fun addPet(petDto: PetDto): ResponseEntity<PetDto> {
        val pet = clinicService.savePet(petMapper.toPet(petDto))
        val headers = HttpHeaders()
        headers.location = UriComponentsBuilder.newInstance().path("/api/pets/{id}").buildAndExpand(pet.id).toUri()
        return ResponseEntity(petMapper.toPetDto(pet), headers, HttpStatus.CREATED)
    }

    override suspend fun deletePet(petId: Int): ResponseEntity<PetDto> {
        return if (clinicService.deletePet(petId)) {
            ResponseEntity(HttpStatus.NO_CONTENT)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    override suspend fun getPet(petId: Int): ResponseEntity<PetDto> {
        val pet = clinicService.findPetById(petId) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity(petMapper.toPetDto(pet), HttpStatus.OK)
    }

    override suspend fun listPets(lastId: Int?, pageSize: Int?): ResponseEntity<List<PetDto>> {
        val pets = clinicService.findAllPets(lastId, pageSize)
        return if (pets.isEmpty()) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else ResponseEntity(petMapper.toPetsDto(pets), HttpStatus.OK)
    }

    override suspend fun updatePet(petId: Int, petDto: PetDto): ResponseEntity<PetDto> {
        val currentPet = clinicService.findPetById(petId) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val pet = clinicService.savePet(
            currentPet.copy(
                name = petDto.name, birthDate = petDto.birthDate, type = petMapper.toPetType(petDto.type)
            )
        )
        return ResponseEntity(petMapper.toPetDto(pet), HttpStatus.OK)
    }
}
