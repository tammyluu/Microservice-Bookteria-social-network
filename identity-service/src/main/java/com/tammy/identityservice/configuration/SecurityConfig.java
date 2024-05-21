package com.tammy.identityservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final String[] PUBLIC_ENDPOINTS  = {"/users","/auth/token", "/auth/introspect"};

    @Value("${jwt.signerKey}")
    private String signerKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // requestMatchers has 2 params (method, endpoint)
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.POST,PUBLIC_ENDPOINTS ).permitAll()
                        .anyRequest().authenticated());
        // need to register one provider manager , auth provider to support jwt token co nghia là khi chung ta thuc hien 1 req
        // chung ta cung cap & token vào header Auth thi jwt auth provider no se incharge và no se bat dau thuc hien viec authentication
        // khi chung ta thuc hien validate cai jwt này thi chung ta can jwtDecoder.
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
        ); //ResourceServer has many config:  jwkSetUri for oauth 3rd part(idb outside), decoder for jwt which we generated
        // Decoder is an interface, we need an implement of this interface ->jwtDecoder


        // csrf protect endpoint to avoid attack cross high
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    JwtDecoder jwtDecoder(){
        //sd decoder này de thuc hien decoder cai token de biet token này hop le hay kg hop le
        //SecretKeySpec has 2 params( signerKey which configured at application.yaml , algo)
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

}
