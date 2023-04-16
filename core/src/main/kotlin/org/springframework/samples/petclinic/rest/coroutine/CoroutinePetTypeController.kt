package org.springframework.samples.petclinic.rest.coroutine

import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.samples.petclinic.mapper.PetTypeMapper
import org.springframework.samples.petclinic.rest.api.coroutine.PettypesCoroutineApi
import org.springframework.samples.petclinic.rest.dto.PetTypeDto
import org.springframework.samples.petclinic.rest.dto.PetTypeFieldsDto
import org.springframework.samples.petclinic.service.coroutine.CoroutineClinicService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
@Profile("coroutine")
class CoroutinePetTypeController(
    private val clinicService: CoroutineClinicService, private val petTypeMapper: PetTypeMapper
) : PettypesCoroutineApi {

    override suspend fun addPetType(petTypeFieldsDto: PetTypeFieldsDto): ResponseEntity<PetTypeDto> {
        val petType = clinicService.savePetType(petTypeMapper.toPetType(petTypeFieldsDto))
        val headers = HttpHeaders()
        headers.location =
            UriComponentsBuilder.newInstance().path("/api/petTypes/{id}").buildAndExpand(petType.id).toUri()
        return ResponseEntity(petTypeMapper.toPetTypeDto(petType), headers, HttpStatus.CREATED)
    }

    override suspend fun deletePetType(petTypeId: Int): ResponseEntity<Unit> {
        return if (clinicService.deletePetType(petTypeId)) {
            ResponseEntity(HttpStatus.NO_CONTENT)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    override suspend fun getPetType(petTypeId: Int): ResponseEntity<PetTypeDto> {
        val petType = clinicService.findPetTypeById(petTypeId)
        return ResponseEntity(petTypeMapper.toPetTypeDto(petType), HttpStatus.OK)
    }

    override suspend fun listPetTypes(lastId: Int, pageSize: Int): ResponseEntity<List<PetTypeDto>> {
        val petTypes = clinicService.findAllPetTypes(lastId, pageSize)
        return if (petTypes.isEmpty()) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else ResponseEntity(petTypeMapper.toPetTypesDto(petTypes), HttpStatus.OK)
    }

    override suspend fun updatePetType(petTypeId: Int, petTypeFieldsDto: PetTypeFieldsDto): ResponseEntity<PetTypeDto> {
        val currentPetType = clinicService.findPetTypeById(petTypeId)
        val petType = clinicService.savePetType(currentPetType.copy(name = petTypeFieldsDto.name))
        return ResponseEntity(petTypeMapper.toPetTypeDto(petType), HttpStatus.OK)
    }
}
