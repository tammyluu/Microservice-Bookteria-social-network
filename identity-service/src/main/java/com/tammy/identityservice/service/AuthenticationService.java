package com.tammy.identityservice.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tammy.identityservice.dto.request.AuthenticationRequest;
import com.tammy.identityservice.dto.request.IntrospectRequest;
import com.tammy.identityservice.dto.response.AuthenticationResponse;
import com.tammy.identityservice.dto.response.IntrospectResponse;
import com.tammy.identityservice.exception.AppException;
import com.tammy.identityservice.exception.ErrorCode;
import com.tammy.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    UserRepository userRepository;

    @NonFinal // lombok annotation : not inject in constructor
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY ;

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(),
                user.getPassword());

        //verify is token valid for comparing system which is issued before?
        if (!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        var token = generateToken(request.getUsername());
        //return token for user
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)//optional
                .build();
    }
    // create a token _ step by step
    private String generateToken(String username) {
        // first step : create a header contains algo that we use -> HS512
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        // Need a body and content we want to send inside token
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username) // subject is a username
                .issuer("tammy.com") //indentify token is issued by whom(domain)
                .issueTime(new Date()) //current date
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli() //expired date
                ))
                .claim("userId", "Custom")//optional
                .build();
        // 2nd step :  create payload which has a constructor with 2 params : jwtClamSet & JsonObject
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        //By header & payload we can create signature
        JWSObject jwsObject = new JWSObject(header, payload);
        // il need a secret that is a string 32 bytes (256 bits)  generate key by site : https://generate-random.org/encryption-key-generator
        // key sign and key
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize(); // serialize by String type
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }
    public IntrospectResponse introspect (IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);// verify content of token unchanged -> true


        return  IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date())) // time expiry must be after current date
                .build();

    }
}
