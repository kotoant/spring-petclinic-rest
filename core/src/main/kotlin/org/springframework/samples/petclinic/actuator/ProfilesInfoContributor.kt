package org.springframework.samples.petclinic.actuator

import org.springframework.boot.actuate.info.Info
import org.springframework.boot.actuate.info.InfoContributor
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class ProfilesInfoContributor(private val environment: Environment) : InfoContributor {

    private val profiles = environment.activeProfiles.ifEmpty { environment.defaultProfiles }.toList()

    override fun contribute(builder: Info.Builder) {
        builder.withDetail("profiles", profiles)
    }
}
