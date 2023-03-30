package org.springframework.samples.petclinic.rest.coroutine

import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.samples.petclinic.mapper.VisitMapper
import org.springframework.samples.petclinic.rest.api.coroutine.VisitsCoroutineApi
import org.springframework.samples.petclinic.rest.dto.VisitDto
import org.springframework.samples.petclinic.rest.dto.VisitFieldsDto
import org.springframework.samples.petclinic.service.coroutine.CoroutineClinicService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
@Profile("coroutine")
class CoroutineVisitController(
    private val clinicService: CoroutineClinicService, private val visitMapper: VisitMapper
) : VisitsCoroutineApi {

    override suspend fun addVisit(visitDto: VisitDto): ResponseEntity<VisitDto> {
        val visit = clinicService.saveVisit(visitMapper.toVisit(visitDto))
        val headers = HttpHeaders()
        headers.location = UriComponentsBuilder.newInstance().path("/api/visits/{id}").buildAndExpand(visit.id).toUri()
        return ResponseEntity(visitMapper.toVisitDto(visit), headers, HttpStatus.CREATED)
    }

    override suspend fun deleteVisit(visitId: Int): ResponseEntity<Unit> {
        return if (clinicService.deleteVisit(visitId)) {
            ResponseEntity(HttpStatus.NO_CONTENT)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    override suspend fun getVisit(visitId: Int): ResponseEntity<VisitDto> {
        val visit = clinicService.findVisitById(visitId) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity(visitMapper.toVisitDto(visit), HttpStatus.OK)
    }

    override suspend fun listVisits(lastId: Int?, pageSize: Int?): ResponseEntity<List<VisitDto>> {
        val visits = clinicService.findAllVisits(lastId, pageSize)
        return if (visits.isEmpty()) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else ResponseEntity(visitMapper.toVisitsDto(visits), HttpStatus.OK)
    }

    override suspend fun updateVisit(visitId: Int, visitFieldsDto: VisitFieldsDto): ResponseEntity<VisitDto> {
        val currentVisit = clinicService.findVisitById(visitId) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val visit = clinicService.saveVisit(currentVisit.copy(date = visitFieldsDto.date, description = visitFieldsDto.description))
        return ResponseEntity(visitMapper.toVisitDto(visit), HttpStatus.OK)
    }
}
