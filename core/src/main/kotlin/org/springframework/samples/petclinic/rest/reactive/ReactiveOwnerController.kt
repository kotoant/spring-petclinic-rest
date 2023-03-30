package org.springframework.samples.petclinic.rest.reactive

import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.samples.petclinic.mapper.OwnerMapper
import org.springframework.samples.petclinic.mapper.PetMapper
import org.springframework.samples.petclinic.mapper.VisitMapper
import org.springframework.samples.petclinic.rest.api.reactive.OwnersReactiveApi
import org.springframework.samples.petclinic.rest.dto.OwnerDto
import org.springframework.samples.petclinic.rest.dto.OwnerFieldsDto
import org.springframework.samples.petclinic.rest.dto.PetDto
import org.springframework.samples.petclinic.rest.dto.PetFieldsDto
import org.springframework.samples.petclinic.rest.dto.VisitDto
import org.springframework.samples.petclinic.rest.dto.VisitFieldsDto
import org.springframework.samples.petclinic.service.reactive.ReactiveClinicService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@RestController
@Profile("reactive")
class ReactiveOwnerController(
    private val clinicService: ReactiveClinicService,
    private val ownerMapper: OwnerMapper,
    private val petMapper: PetMapper,
    private val visitMapper: VisitMapper
) : OwnersReactiveApi {

    override fun addOwner(ownerFieldsDto: OwnerFieldsDto): Mono<ResponseEntity<OwnerDto>> {
        return clinicService.saveOwner(ownerMapper.toOwner(ownerFieldsDto)).map { owner ->
            val headers = HttpHeaders()
            headers.location =
                UriComponentsBuilder.newInstance().path("/api/owners/{id}").buildAndExpand(owner.id).toUri()
            ResponseEntity(ownerMapper.toOwnerDto(owner), headers, HttpStatus.CREATED)
        }
    }

    override fun addPetToOwner(ownerId: Int, petFieldsDto: PetFieldsDto): Mono<ResponseEntity<PetDto>> {
        return clinicService.savePet(petMapper.toPet(petFieldsDto).copy(ownerId = ownerId)).map { pet ->
            val headers = HttpHeaders()
            headers.location = UriComponentsBuilder.newInstance().path("/api/pets/{id}").buildAndExpand(pet.id).toUri()
            ResponseEntity<PetDto>(petMapper.toPetDto(pet), headers, HttpStatus.CREATED)
        }
    }

    override fun addVisitToOwner(
        ownerId: Int, petId: Int, visitFieldsDto: VisitFieldsDto
    ): Mono<ResponseEntity<VisitDto>> {
        return clinicService.saveVisit(visitMapper.toVisit(visitFieldsDto).copy(petId = petId)).map { visit ->
            val headers = HttpHeaders()
            headers.location =
                UriComponentsBuilder.newInstance().path("/api/visits/{id}").buildAndExpand(visit.id).toUri()
            ResponseEntity<VisitDto>(visitMapper.toVisitDto(visit), headers, HttpStatus.CREATED)
        }
    }

    override fun deleteOwner(ownerId: Int): Mono<ResponseEntity<Unit>> {
        return clinicService.deleteOwner(ownerId).map { deleted ->
            if (deleted) {
                ResponseEntity(HttpStatus.NO_CONTENT)
            } else {
                ResponseEntity(HttpStatus.NOT_FOUND)
            }
        }
    }

    override fun getOwner(ownerId: Int): Mono<ResponseEntity<OwnerDto>> {
        return clinicService.findOwnerById(ownerId).map { owner ->
            ResponseEntity(ownerMapper.toOwnerDto(owner), HttpStatus.OK)
        }.switchIfEmpty { Mono.just(ResponseEntity(HttpStatus.NOT_FOUND)) }
    }

    override fun listOwners(lastName: String?, lastId: Int?, pageSize: Int?): Mono<ResponseEntity<List<OwnerDto>>> {
        return if (lastName != null) {
            clinicService.findOwnerByLastName(lastName, lastId, pageSize)
        } else {
            clinicService.findAllOwners(lastId, pageSize)
        }.map { owners ->
            if (owners.isEmpty()) {
                ResponseEntity(HttpStatus.NOT_FOUND)
            } else {
                ResponseEntity(ownerMapper.toOwnersDto(owners), HttpStatus.OK)
            }
        }
    }

    override fun updateOwner(ownerId: Int, ownerFieldsDto: OwnerFieldsDto): Mono<ResponseEntity<OwnerDto>> {
        return clinicService.findOwnerById(ownerId).flatMap { currentOwner ->
            clinicService.saveOwner(
                currentOwner.copy(
                    firstName = ownerFieldsDto.firstName,
                    lastName = ownerFieldsDto.lastName,
                    address = ownerFieldsDto.address,
                    city = ownerFieldsDto.city,
                    telephone = ownerFieldsDto.telephone
                )
            )
        }.map { owner -> ResponseEntity(ownerMapper.toOwnerDto(owner), HttpStatus.OK) }
            .switchIfEmpty { Mono.just(ResponseEntity(HttpStatus.NOT_FOUND)) }
    }
}
