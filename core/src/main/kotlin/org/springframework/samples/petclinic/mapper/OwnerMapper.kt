package org.springframework.samples.petclinic.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.springframework.samples.petclinic.model.Owner
import org.springframework.samples.petclinic.rest.dto.OwnerDto
import org.springframework.samples.petclinic.rest.dto.OwnerFieldsDto

/**
 * Maps Owner & OwnerDto using Mapstruct
 */
@Mapper(config = CentralConfig::class)
interface OwnerMapper {
    fun toOwnerDto(owner: Owner): OwnerDto
    fun toOwner(ownerDto: OwnerDto): Owner

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pets", ignore = true)
    fun toOwner(ownerFieldsDto: OwnerFieldsDto): Owner
    fun toOwnersDto(owners: List<Owner>): List<OwnerDto>
    fun toOwners(ownersDto: List<OwnerDto>): List<Owner>
}
