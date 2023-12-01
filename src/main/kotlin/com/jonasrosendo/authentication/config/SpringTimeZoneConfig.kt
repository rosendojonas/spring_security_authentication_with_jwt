package com.jonasrosendo.authentication.config

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import java.util.TimeZone

@Configuration
class SpringTimeZoneConfig {

    @PostConstruct
    fun timezoneConfig() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"))
    }
}