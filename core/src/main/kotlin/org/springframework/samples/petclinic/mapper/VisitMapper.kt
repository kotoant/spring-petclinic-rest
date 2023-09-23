package org.springframework.samples.petclinic.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.springframework.samples.petclinic.model.Visit
import org.springframework.samples.petclinic.rest.dto.VisitDto
import org.springframework.samples.petclinic.rest.dto.VisitFieldsDto

/**
 * Map Visit & VisitDto using mapstruct
 */
@Mapper(config = CentralConfig::class)
interface VisitMapper {
    fun toVisit(visitDto: VisitDto): Visit

    @Mapping(target = "petId", expression = "java(0)")
    fun toVisit(id: Int, visitFieldsDto: VisitFieldsDto): Visit

    @Mapping(target = "id", expression = "java(0)")
    @Mapping(target = "petId", expression = "java(0)")
    fun toVisit(visitFieldsDto: VisitFieldsDto): Visit
    fun toVisitDto(visit: Visit): VisitDto
    fun toVisitFieldsDto(visit: Visit): VisitFieldsDto
    fun toVisitsDto(visits: List<Visit>): List<VisitDto>
}
