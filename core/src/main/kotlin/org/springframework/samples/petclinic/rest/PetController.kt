package org.springframework.samples.petclinic.rest

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.samples.petclinic.mapper.PetMapper
import org.springframework.samples.petclinic.mapper.PetTypeMapper
import org.springframework.samples.petclinic.rest.api.PetsApi
import org.springframework.samples.petclinic.rest.dto.PetDto
import org.springframework.samples.petclinic.rest.dto.PetFieldsDto
import org.springframework.samples.petclinic.service.JdbcClinicService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
@ConditionalOnWebApplication(type = SERVLET)
@Profile("jdbc")
class PetController(
    private val clinicService: JdbcClinicService,
    private val petMapper: PetMapper,
    private val petTypeMapper: PetTypeMapper
) : PetsApi {

    override fun addPet(petDto: PetDto): ResponseEntity<PetDto> {
        val pet = clinicService.savePet(petMapper.toPet(petDto))
        val headers = HttpHeaders()
        headers.location = UriComponentsBuilder.newInstance().path("/api/pets/{id}").buildAndExpand(pet.id).toUri()
        return ResponseEntity(petMapper.toPetDto(pet), headers, HttpStatus.CREATED)
    }

    override fun deletePet(petId: Int): ResponseEntity<Unit> {
        return if (clinicService.deletePet(petId)) {
            ResponseEntity(HttpStatus.NO_CONTENT)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    override fun getPet(petId: Int): ResponseEntity<PetDto> {
        val pet = clinicService.findPetById(petId) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity(petMapper.toPetDto(pet), HttpStatus.OK)
    }

    override fun listPets(lastId: Int?, pageSize: Int?): ResponseEntity<List<PetDto>> {
        val pets = clinicService.findAllPets(lastId, pageSize)
        return if (pets.isEmpty()) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else ResponseEntity(petMapper.toPetsDto(pets), HttpStatus.OK)
    }

    override fun updatePet(petId: Int, petFieldsDto: PetFieldsDto): ResponseEntity<PetDto> {
        val currentPet = clinicService.findPetById(petId) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val pet = clinicService.savePet(
            currentPet.copy(
                name = petFieldsDto.name,
                birthDate = petFieldsDto.birthDate,
                type = petTypeMapper.toPetType(petFieldsDto.type)
            )
        )
        return ResponseEntity(petMapper.toPetDto(pet), HttpStatus.OK)
    }
}
