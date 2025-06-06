package net.hamza.banque.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
@Configuration
@EnableWebSecurity
public class StarterSecurity extends AbstractSecurityWebApplicationInitializer {
}
