package org.springframework.samples.petclinic.model

import java.time.LocalDate

data class Visit(val id: Int, val petId: Int, val date: LocalDate, val description: String)

data class PetType(val id: Int, val name: String)

data class Pet(
    val id: Int,
    val name: String,
    val birthDate: LocalDate,
    val type: PetType,
    val ownerId: Int,
    val visits: List<Visit>
)

data class Owner(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val address: String,
    val city: String,
    val telephone: String,
    val pets: List<Pet>
)
