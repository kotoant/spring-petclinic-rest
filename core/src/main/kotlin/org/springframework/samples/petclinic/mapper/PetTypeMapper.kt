package org.springframework.samples.petclinic.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.springframework.samples.petclinic.model.PetType
import org.springframework.samples.petclinic.rest.dto.PetTypeDto
import org.springframework.samples.petclinic.rest.dto.PetTypeFieldsDto

/**
 * Map PetType & PetTypeDto using mapstruct
 */
@Mapper(config = CentralConfig::class)
interface PetTypeMapper {
    fun toPetType(petTypeDto: PetTypeDto): PetType

    fun toPetType(id: Int, petTypeFieldsDto: PetTypeFieldsDto): PetType

    @Mapping(target = "id", expression = "java(0)")
    fun toPetType(petTypeFieldsDto: PetTypeFieldsDto): PetType
    fun toPetTypeDto(petType: PetType): PetTypeDto
    fun toPetTypesDto(petTypes: List<PetType>): List<PetTypeDto>
}
