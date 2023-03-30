package org.springframework.samples.petclinic.rest.reactive

import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.samples.petclinic.mapper.VisitMapper
import org.springframework.samples.petclinic.rest.api.reactive.VisitsReactiveApi
import org.springframework.samples.petclinic.rest.dto.VisitDto
import org.springframework.samples.petclinic.rest.dto.VisitFieldsDto
import org.springframework.samples.petclinic.service.reactive.ReactiveClinicService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@RestController
@Profile("reactive")
class ReactiveVisitController(private val clinicService: ReactiveClinicService, private val visitMapper: VisitMapper) :
    VisitsReactiveApi {

    override fun addVisit(visitDto: VisitDto): Mono<ResponseEntity<VisitDto>> {
        return clinicService.saveVisit(visitMapper.toVisit(visitDto)).map { visit ->
            val headers = HttpHeaders()
            headers.location =
                UriComponentsBuilder.newInstance().path("/api/visits/{id}").buildAndExpand(visit.id).toUri()
            ResponseEntity(visitMapper.toVisitDto(visit), headers, HttpStatus.CREATED)
        }
    }

    override fun deleteVisit(visitId: Int): Mono<ResponseEntity<Unit>> {
        return clinicService.deleteVisit(visitId).map { deleted ->
            if (deleted) {
                ResponseEntity(HttpStatus.NO_CONTENT)
            } else {
                ResponseEntity(HttpStatus.NOT_FOUND)
            }
        }
    }

    override fun getVisit(visitId: Int): Mono<ResponseEntity<VisitDto>> {
        return clinicService.findVisitById(visitId).map { visit ->
            ResponseEntity(visitMapper.toVisitDto(visit), HttpStatus.OK)
        }.switchIfEmpty { Mono.just(ResponseEntity(HttpStatus.NOT_FOUND)) }
    }

    override fun listVisits(lastId: Int?, pageSize: Int?): Mono<ResponseEntity<List<VisitDto>>> {
        return clinicService.findAllVisits(lastId, pageSize).map { visits ->
            if (visits.isEmpty()) {
                ResponseEntity(HttpStatus.NOT_FOUND)
            } else {
                ResponseEntity(visitMapper.toVisitsDto(visits), HttpStatus.OK)
            }
        }
    }

    override fun updateVisit(visitId: Int, visitFieldsDto: VisitFieldsDto): Mono<ResponseEntity<VisitDto>> {
        return clinicService.findVisitById(visitId).flatMap { currentVisit ->
            clinicService.saveVisit(currentVisit.copy(date = visitFieldsDto.date, description = visitFieldsDto.description))
        }.map { visit -> ResponseEntity(visitMapper.toVisitDto(visit), HttpStatus.OK) }
            .switchIfEmpty { Mono.just(ResponseEntity(HttpStatus.NOT_FOUND)) }
    }
}
