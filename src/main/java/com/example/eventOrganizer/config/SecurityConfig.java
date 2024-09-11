package com.example.eventOrganizer.config;


import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration //anotasi untuk nandain kalo class ini adalah konfigurasi spring
@EnableWebSecurity //anotasi untuk mengaktifkan keaman spring(spring security)
public class SecurityConfig {

    // panggil RsaKeyProperties untuk mengakses public dan priavte key
    private final RsaKeyProperties rsaKeys;

    // constructor yang dibuat untuk mengambil public key dan private key
    public SecurityConfig(RsaKeyProperties rsaKeys) {
        this.rsaKeys = rsaKeys;
    }


    // InMemoryUserDetailsManager : kelas dari spring security yang dibuat untuk menyimpan informasi user secara sementara
    @Bean
    public InMemoryUserDetailsManager user() {
        return new InMemoryUserDetailsManager(
                User.withUsername("danu") // buat objek User dengan username "danu
                        .password("{noop}123") // set password, {noop} untuk membuat password agar tidak di enkripsi
                        .authorities("read") // mengatur otoritas(izin) yang artinya user ini hanya pnya akses untuk membaca
                        .build() // build(buat) objek yang udah dikonfigurasi diatas
        );
    }

    /* SecurityFilterChain : komponen utama spring security untuk konfigurasi filter keamanan */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) //Menonaktifkan Cross-Site Request Forgery (CSRF) protection.
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().authenticated()) // Mengatur semua permintaan HTTP harus di-autentikasi
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/api/events/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/events/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/events/**").authenticated() // requestsMatchers untuk mengatur otorisasi pada endpoint tertentu
                        .anyRequest().permitAll()) // selain endpoint yang dibatasi diatas, bisa digunakan tanpa otorisasi
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder()))) // konfigurasi server sumber daya OAuth2 untuk menggunakan JWT sebagai akses token
                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS)) // set manajemen session menjadi STATELESS yang artinya ga ada sesi yang disimpen di server
                .httpBasic(withDefaults()) // aktifin otentikasi basic(basic auth)
                .build(); // build SecurityFilterChain
    }

    // untuk mendekode/mendekripsi JWT
    @Bean
    JwtDecoder jwtDecoder() {
        // atur public key untuk verifikasi JWT
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        //objek JWK (JSON Web Key) yang berisi public key & private key dari RSA.
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
        //  ImmutableJWKSet untuk menyimpan JWK.
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
}
