package org.springframework.samples.petclinic.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.rest.dto.PetDto
import org.springframework.samples.petclinic.rest.dto.PetFieldsDto
import org.springframework.samples.petclinic.rest.dto.PetTypeDto

/**
 * Map Pet & PetDto using mapstruct
 */
@Mapper(config = CentralConfig::class)
interface PetMapper {
    fun toPetDto(pet: Pet): PetDto
    fun toPetsDto(pets: List<Pet>): List<PetDto>
    fun toPets(pets: List<PetDto>): List<Pet>
    fun toPet(petDto: PetDto): Pet
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "visits", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    fun toPet(petFieldsDto: PetFieldsDto): Pet
    fun toPetTypeDto(petType: PetType): PetTypeDto
    fun toPetType(petTypeDto: PetTypeDto): PetType
    fun toPetTypesDto(petTypes: List<PetType>): List<PetTypeDto>
}
