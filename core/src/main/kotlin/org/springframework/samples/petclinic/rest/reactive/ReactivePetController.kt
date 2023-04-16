package org.springframework.samples.petclinic.rest.reactive

import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.samples.petclinic.mapper.PetMapper
import org.springframework.samples.petclinic.mapper.PetTypeMapper
import org.springframework.samples.petclinic.rest.api.reactive.PetsReactiveApi
import org.springframework.samples.petclinic.rest.dto.PetDto
import org.springframework.samples.petclinic.rest.dto.PetFieldsDto
import org.springframework.samples.petclinic.service.reactive.ReactiveClinicService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@RestController
@Profile("reactive")
class ReactivePetController(
    private val clinicService: ReactiveClinicService,
    private val petMapper: PetMapper,
    private val petTypeMapper: PetTypeMapper
) : PetsReactiveApi {

    override fun addPet(petDto: PetDto): Mono<ResponseEntity<PetDto>> {
        return clinicService.savePet(petMapper.toPet(petDto)).map { pet ->
            val headers = HttpHeaders()
            headers.location = UriComponentsBuilder.newInstance().path("/api/pets/{id}").buildAndExpand(pet.id).toUri()
            ResponseEntity(petMapper.toPetDto(pet), headers, HttpStatus.CREATED)
        }
    }

    override fun deletePet(petId: Int): Mono<ResponseEntity<Unit>> {
        return clinicService.deletePet(petId).map { deleted ->
            if (deleted) {
                ResponseEntity(HttpStatus.NO_CONTENT)
            } else {
                ResponseEntity(HttpStatus.NOT_FOUND)
            }
        }
    }

    override fun getPet(petId: Int): Mono<ResponseEntity<PetDto>> {
        return clinicService.findPetById(petId).map { pet ->
            ResponseEntity(petMapper.toPetDto(pet), HttpStatus.OK)
        }
    }

    override fun listPets(lastId: Int, pageSize: Int): Mono<ResponseEntity<List<PetDto>>> {
        return clinicService.findAllPets(lastId, pageSize).map { pets ->
            if (pets.isEmpty()) {
                ResponseEntity(HttpStatus.NOT_FOUND)
            } else {
                ResponseEntity(petMapper.toPetsDto(pets), HttpStatus.OK)
            }
        }
    }

    override fun updatePet(petId: Int, petFieldsDto: PetFieldsDto): Mono<ResponseEntity<PetDto>> {
        return clinicService.findPetById(petId).flatMap { currentPet ->
            clinicService.savePet(
                currentPet.copy(
                    name = petFieldsDto.name,
                    birthDate = petFieldsDto.birthDate,
                    type = petTypeMapper.toPetType(petFieldsDto.type)
                )
            )
        }.map { pet -> ResponseEntity(petMapper.toPetDto(pet), HttpStatus.OK) }
    }
}
