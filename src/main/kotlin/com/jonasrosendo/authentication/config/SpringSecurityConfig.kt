package com.jonasrosendo.authentication.config

import com.jonasrosendo.authentication.jwt.JwtAuthenticationEntrypoint
import com.jonasrosendo.authentication.jwt.JwtAuthorizationFilter
import com.jonasrosendo.authentication.jwt.JwtUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@Configuration
@EnableMethodSecurity
@EnableWebMvc
class SpringSecurityConfig {

    @Bean
    fun filterChain(httpSecurity: HttpSecurity, userDetailsService: JwtUserDetailsService): SecurityFilterChain {
        return httpSecurity
            .csrf { it.disable() }
            .cors { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { requestMatcherRegistry ->
                requestMatcherRegistry.run {
                    requestMatchers(HttpMethod.POST, "api/v1/auth").permitAll()
                    anyRequest().authenticated()
                }
            }
            .addFilterBefore(
                jwtAuthorizationFilter(userDetailsService),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .exceptionHandling {
                it.authenticationEntryPoint(JwtAuthenticationEntrypoint())
            }
            .build()
    }

    @Bean
    fun jwtAuthorizationFilter(userDetailsService: JwtUserDetailsService): JwtAuthorizationFilter {
        return JwtAuthorizationFilter(userDetailsService)
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}