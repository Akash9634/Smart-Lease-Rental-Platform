package com.smartlease.smartlease_backend.config;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class JwtService {


    private static final String SECRET_KEY = "Y3VwcHJvY2Vzc3Vwb253b3Jlc3RpZmZlYXJ0ZXJyaWJsZXByb3BlcmNvdXJzZWNpdGk=";

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) //24 hours valid
                .signWith(getSignInKey(), SignatureAlgorithm.ES256)
                .compact();
    }


}
