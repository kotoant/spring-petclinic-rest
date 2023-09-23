package org.springframework.samples.petclinic.rest

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.samples.petclinic.mapper.VisitMapper
import org.springframework.samples.petclinic.rest.api.VisitsApi
import org.springframework.samples.petclinic.rest.dto.VisitDto
import org.springframework.samples.petclinic.rest.dto.VisitFieldsDto
import org.springframework.samples.petclinic.service.JdbcClinicService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
@ConditionalOnWebApplication(type = SERVLET)
@Profile("jdbc")
class VisitController(private val clinicService: JdbcClinicService, private val visitMapper: VisitMapper) : VisitsApi {

    override fun addVisit(visitDto: VisitDto): ResponseEntity<VisitDto> {
        val visit = clinicService.saveVisit(visitMapper.toVisit(visitDto))
        val headers = HttpHeaders()
        headers.location = UriComponentsBuilder.newInstance().path("/api/visits/{id}").buildAndExpand(visit.id).toUri()
        return ResponseEntity(visitMapper.toVisitDto(visit), headers, HttpStatus.CREATED)
    }

    override fun deleteVisit(visitId: Int): ResponseEntity<Unit> {
        return if (clinicService.deleteVisit(visitId)) {
            ResponseEntity(HttpStatus.NO_CONTENT)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    override fun getVisit(visitId: Int): ResponseEntity<VisitDto> {
        val visit = clinicService.findVisitById(visitId)
        return ResponseEntity(visitMapper.toVisitDto(visit), HttpStatus.OK)
    }

    override fun listVisits(lastId: Int, pageSize: Int): ResponseEntity<List<VisitDto>> {
        val visits = clinicService.findAllVisits(lastId, pageSize)
        return if (visits.isEmpty()) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else ResponseEntity(visitMapper.toVisitsDto(visits), HttpStatus.OK)
    }

    override fun updateVisit(visitId: Int, visitFieldsDto: VisitFieldsDto): ResponseEntity<VisitDto> {
        val visit = clinicService.saveVisit(visitMapper.toVisit(visitId, visitFieldsDto))
        return ResponseEntity(visitMapper.toVisitDto(visit), HttpStatus.OK)
    }
}
