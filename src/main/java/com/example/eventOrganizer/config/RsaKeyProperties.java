package com.example.eventOrganizer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/* @ConfigurationProperties(prefix = "rsa") : untuk membaca properti yang berisi keyword "rsa"
 dari file application.properties dan dimapping ke RsaKeyProperties*/
@ConfigurationProperties(prefix = "rsa")

// record RsaKeyProperties : untuk menyimpan public & private key yang akan digunakan untuk enkripsi dan dekripsi JWT.
public record RsaKeyProperties(
        RSAPublicKey publicKey, // objek yang nyimpen public key dari rsa.public-key
        RSAPrivateKey privateKey) { // objek yang nyimpen public key dari rsa.private-key
}
