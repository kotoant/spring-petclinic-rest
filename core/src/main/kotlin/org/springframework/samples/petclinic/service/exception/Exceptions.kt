package org.springframework.samples.petclinic.service.exception

abstract class NotFoundException(override val message: String) : RuntimeException()
class OwnerNotFoundException(id: Int) : NotFoundException("Owner not found [id: $id]")
class PetNotFoundException(id: Int) : NotFoundException("Pet not found [id: $id]")
class PetTypeNotFoundException(id: Int) : NotFoundException("Pet type not found [id: $id]")
class VisitNotFoundException(id: Int) : NotFoundException("Visit not found [id: $id]")
