package com.eos.ss.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import com.eos.ss.exception.CustomAuthenticationFailureHandler;
import com.eos.ss.exception.CustomBearerTokenAccessDeniedHandler;
import com.eos.ss.exception.CustomBearerTokenAuthenticationEntryPoint;
import com.eos.ss.exception.DelegatedAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class JwtSecurityConfiguration {
	
	@Autowired
    DelegatedAuthenticationEntryPoint authEntryPoint;
	
	@Autowired
    //@Qualifier("customBearerTokenAuthenticationEntryPoint")
    CustomBearerTokenAuthenticationEntryPoint bearerTokenEntryPoint;
	
	@Autowired
    CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler;
	
	@Autowired
    CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
	
	
	
	
	 
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests(
						auth -> {
							auth
							//.requestMatchers(HttpMethod.GET,"api/**").hasAuthority("SCOPE_ROLE_ADMIN")
							.anyRequest().authenticated();
						});
		
		http.sessionManagement(
						session -> 
							session.sessionCreationPolicy(
									SessionCreationPolicy.STATELESS)
						);
		
		//http.httpBasic(withDefaults()); 
		http.httpBasic(httpBasic->httpBasic.authenticationEntryPoint(this.authEntryPoint));
		
		http.csrf(csrf -> csrf.disable());
		
		http.headers(headers -> headers.frameOptions(frameOptionsConfig-> frameOptionsConfig.disable()));
		
		//http.oauth2ResourceServer((oauth2) -> oauth2.jwt(withDefaults()));
		http.oauth2ResourceServer((oauth2) -> oauth2.jwt().and().authenticationEntryPoint(this.bearerTokenEntryPoint)
				.accessDeniedHandler(customBearerTokenAccessDeniedHandler));
		
		//http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		 
		
		return http.build();
	}
	
//	@Bean
//	public DataSource dataSource() {
//		return new EmbeddedDatabaseBuilder()
//				.setType(EmbeddedDatabaseType.H2)
//				.addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
//				.build();
//	}
//	
//	@Bean
//	public UserDetailsService userDetailService(DataSource dataSource) {
//		
//		var user = User.withUsername("in28minutes")
//			//.password("{noop}dummy")
//			.password("dummy")
//			.passwordEncoder(str -> passwordEncoder().encode(str))
//			.roles("USER")
//			.build();
//		
//		var admin = User.withUsername("admin")
//				//.password("{noop}dummy")
//				.password("dummy")
//				.passwordEncoder(str -> passwordEncoder().encode(str))
//				.roles("ADMIN", "USER")
//				.build();
//		
//		var jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//		jdbcUserDetailsManager.createUser(user);
//		jdbcUserDetailsManager.createUser(admin);
//
//		return jdbcUserDetailsManager;
//	}
	
//	@Bean
//	public BCryptPasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
	
	@Bean
	public KeyPair keyPair() {
		try {
			var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			return keyPairGenerator.generateKeyPair();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	@Bean
	public RSAKey rsaKey(KeyPair keyPair) {
		
		return new RSAKey
				.Builder((RSAPublicKey)keyPair.getPublic())
				.privateKey(keyPair.getPrivate())
				.keyID(UUID.randomUUID().toString())
				.build();
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
		var jwkSet = new JWKSet(rsaKey);
		
		return (jwkSelector, context) ->  jwkSelector.select(jwkSet);
		
	}
	
	@Bean
	public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
		return NimbusJwtDecoder
				.withPublicKey(rsaKey.toRSAPublicKey())
				.build();
		
	}
	
	@Bean
	public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
		return new NimbusJwtEncoder(jwkSource);
	}

}
