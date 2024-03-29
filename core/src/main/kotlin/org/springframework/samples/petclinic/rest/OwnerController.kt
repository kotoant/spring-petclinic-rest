package org.springframework.samples.petclinic.rest

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.samples.petclinic.mapper.OwnerMapper
import org.springframework.samples.petclinic.mapper.PetMapper
import org.springframework.samples.petclinic.mapper.VisitMapper
import org.springframework.samples.petclinic.rest.api.OwnersApi
import org.springframework.samples.petclinic.rest.dto.OwnerDto
import org.springframework.samples.petclinic.rest.dto.OwnerFieldsDto
import org.springframework.samples.petclinic.rest.dto.PetDto
import org.springframework.samples.petclinic.rest.dto.PetFieldsDto
import org.springframework.samples.petclinic.rest.dto.VisitDto
import org.springframework.samples.petclinic.rest.dto.VisitFieldsDto
import org.springframework.samples.petclinic.service.JdbcClinicService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
@ConditionalOnWebApplication(type = SERVLET)
@Profile("jdbc")
class OwnerController(
    private val clinicService: JdbcClinicService,
    private val ownerMapper: OwnerMapper,
    private val petMapper: PetMapper,
    private val visitMapper: VisitMapper
) : OwnersApi {

    override fun addOwner(ownerFieldsDto: OwnerFieldsDto): ResponseEntity<OwnerDto> {
        val owner = clinicService.saveOwner(ownerMapper.toOwner(ownerFieldsDto))
        val headers = HttpHeaders()
        headers.location = UriComponentsBuilder.newInstance().path("/api/owners/{id}").buildAndExpand(owner.id).toUri()
        return ResponseEntity(ownerMapper.toOwnerDto(owner), headers, HttpStatus.CREATED)
    }

    override fun deleteOwner(ownerId: Int): ResponseEntity<Unit> {
        return if (clinicService.deleteOwner(ownerId)) {
            ResponseEntity(HttpStatus.NO_CONTENT)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    override fun getOwner(ownerId: Int): ResponseEntity<OwnerDto> {
        val owner = clinicService.findOwnerById(ownerId)
        return ResponseEntity(ownerMapper.toOwnerDto(owner), HttpStatus.OK)
    }

    override fun listOwners(lastName: String?, lastId: Int, pageSize: Int): ResponseEntity<List<OwnerDto>> {
        val owners = if (lastName != null) {
            clinicService.findOwnerByLastName(lastName, lastId, pageSize)
        } else {
            clinicService.findAllOwners(lastId, pageSize)
        }
        return if (owners.isEmpty()) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else ResponseEntity(ownerMapper.toOwnersDto(owners), HttpStatus.OK)
    }

    override fun updateOwner(ownerId: Int, ownerFieldsDto: OwnerFieldsDto): ResponseEntity<OwnerDto> {
        val owner = clinicService.saveOwner(ownerMapper.toOwner(ownerId, ownerFieldsDto))
        return ResponseEntity(ownerMapper.toOwnerDto(owner), HttpStatus.OK)
    }

    override fun addPetToOwner(ownerId: Int, petFieldsDto: PetFieldsDto): ResponseEntity<PetDto> {
        val pet = clinicService.savePet(petMapper.toPet(petFieldsDto).copy(ownerId = ownerId))
        val headers = HttpHeaders()
        headers.location = UriComponentsBuilder.newInstance().path("/api/pets/{id}").buildAndExpand(pet.id).toUri()
        return ResponseEntity<PetDto>(petMapper.toPetDto(pet), headers, HttpStatus.CREATED)
    }

    override fun addVisitToOwner(ownerId: Int, petId: Int, visitFieldsDto: VisitFieldsDto): ResponseEntity<VisitDto> {
        val visit = clinicService.saveVisit(visitMapper.toVisit(visitFieldsDto).copy(petId = petId))
        val headers = HttpHeaders()
        headers.location = UriComponentsBuilder.newInstance().path("/api/visits/{id}").buildAndExpand(visit.id).toUri()
        return ResponseEntity<VisitDto>(visitMapper.toVisitDto(visit), headers, HttpStatus.CREATED)
    }
}
