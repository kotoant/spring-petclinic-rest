package org.springframework.samples.petclinic.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.rest.dto.OwnerDto
import org.springframework.samples.petclinic.rest.dto.OwnerFieldsDto

/**
 * Maps Owner & OwnerDto using Mapstruct
 */
@Mapper(config = CentralConfig::class, uses = [PetMapper::class])
interface OwnerMapper {
    fun toOwnerDto(owner: Owner): OwnerDto
    fun toOwnerFieldsDto(owner: Owner): OwnerFieldsDto
    fun toOwner(ownerDto: OwnerDto): Owner

    @Mapping(target = "pets", expression = "java(List.of())")
    fun toOwner(id: Int, ownerFieldsDto: OwnerFieldsDto): Owner

    @Mapping(target = "id", expression = "java(0)")
    @Mapping(target = "pets", expression = "java(List.of())")
    fun toOwner(ownerFieldsDto: OwnerFieldsDto): Owner
    fun toOwnersDto(owners: List<Owner>): List<OwnerDto>
    fun toOwners(ownersDto: List<OwnerDto>): List<Owner>
}
