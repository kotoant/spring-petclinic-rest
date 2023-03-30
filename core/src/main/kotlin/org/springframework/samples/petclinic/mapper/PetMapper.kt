package org.springframework.samples.petclinic.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.springframework.samples.petclinic.model.Pet
import org.springframework.samples.petclinic.rest.dto.PetDto
import org.springframework.samples.petclinic.rest.dto.PetFieldsDto

/**
 * Map Pet & PetDto using mapstruct
 */
@Mapper(config = CentralConfig::class, uses = [PetTypeMapper::class, VisitMapper::class])
interface PetMapper {
    fun toPetDto(pet: Pet): PetDto
    fun toPetFieldsDto(pet: Pet): PetFieldsDto
    fun toPetsDto(pets: List<Pet>): List<PetDto>
    fun toPets(pets: List<PetDto>): List<Pet>
    fun toPet(petDto: PetDto): Pet

    @Mapping(target = "id", expression = "java(0)")
    @Mapping(target = "visits", expression = "java(List.of())")
    @Mapping(target = "ownerId", expression = "java(0)")
    fun toPet(petFieldsDto: PetFieldsDto): Pet
}
