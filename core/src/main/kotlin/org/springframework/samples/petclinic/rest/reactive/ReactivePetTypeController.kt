package org.springframework.samples.petclinic.rest.reactive

import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.samples.petclinic.mapper.PetTypeMapper
import org.springframework.samples.petclinic.rest.api.reactive.PettypesReactiveApi
import org.springframework.samples.petclinic.rest.dto.PetTypeDto
import org.springframework.samples.petclinic.service.reactive.ReactiveClinicService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@RestController
@Profile("reactive")
class ReactivePetTypeController(
    private val clinicService: ReactiveClinicService, private val petTypeMapper: PetTypeMapper
) : PettypesReactiveApi {

    override fun addPetType(petTypeDto: PetTypeDto): Mono<ResponseEntity<PetTypeDto>> {
        return clinicService.savePetType(petTypeMapper.toPetType(petTypeDto)).map { petType ->
            val headers = HttpHeaders()
            headers.location =
                UriComponentsBuilder.newInstance().path("/api/petTypes/{id}").buildAndExpand(petType.id).toUri()
            ResponseEntity(petTypeMapper.toPetTypeDto(petType), headers, HttpStatus.CREATED)
        }
    }

    override fun deletePetType(petTypeId: Int): Mono<ResponseEntity<PetTypeDto>> {
        return clinicService.deletePetType(petTypeId).map { deleted ->
            if (deleted) {
                ResponseEntity(HttpStatus.NO_CONTENT)
            } else {
                ResponseEntity(HttpStatus.NOT_FOUND)
            }
        }
    }

    override fun getPetType(petTypeId: Int): Mono<ResponseEntity<PetTypeDto>> {
        return clinicService.findPetTypeById(petTypeId).map { petType ->
            ResponseEntity(petTypeMapper.toPetTypeDto(petType), HttpStatus.OK)
        }.switchIfEmpty { Mono.just(ResponseEntity(HttpStatus.NOT_FOUND)) }
    }

    override fun listPetTypes(lastId: Int?, pageSize: Int?): Mono<ResponseEntity<List<PetTypeDto>>> {
        return clinicService.findAllPetTypes(lastId, pageSize).map { petTypes ->
            if (petTypes.isEmpty()) {
                ResponseEntity(HttpStatus.NOT_FOUND)
            } else {
                ResponseEntity(petTypeMapper.toPetTypesDto(petTypes), HttpStatus.OK)
            }
        }
    }

    override fun updatePetType(petTypeId: Int, petTypeDto: PetTypeDto): Mono<ResponseEntity<PetTypeDto>> {
        return clinicService.findPetTypeById(petTypeId).flatMap { currentPetType ->
            clinicService.savePetType(currentPetType.copy(name = petTypeDto.name))
        }.map { petType -> ResponseEntity(petTypeMapper.toPetTypeDto(petType), HttpStatus.OK) }
            .switchIfEmpty { Mono.just(ResponseEntity(HttpStatus.NOT_FOUND)) }
    }
}
