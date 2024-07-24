package org.educa.airline.securityConfig;

import lombok.Getter;
import org.educa.airline.services.inservice.SecurityService;
import org.educa.airline.services.inservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Getter
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    /**
     * Clases a inyectar en el constructor.
     */
    private final UserService userService;
    private final SecurityService securityService;

    /**
     * @param userService     UserService uso de UserDetails.
     * @param securityService SecurityService uso de PasswordEncode.
     */
    @Autowired
    public SpringSecurityConfig(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    /**
     * Cuando un usuario intenta autenticarse, se invoca este método para en primer lugar, buscar al
     * usuario en el repositorio por useName y luego comparar contraseñas, la que se ha insertado con la
     * que hay en la base de datos.
     *
     * @param auth construye la autenticación.
     * @throws Exception sañta en caso de fallar la autenticación.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    /**
     * Seguriza los endpoint de la aplicación mediante restricciones de autenticación y de roles.
     * Los vuelos se rigen por el rol admin y las consultas por autenticación
     * Los pasajeros se rigen por el rol personal en todas las consultas con previa autenticación.
     *
     * @param http asegurar las solitudes http.
     * @return la configuración de seguridad.
     * @throws Exception sí ocurre un error en la segurización.
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable)
                .httpBasic(withDefaults()).authorizeHttpRequests(
                        request -> request
                                /// VUELOS
                                .requestMatchers(HttpMethod.POST, "/flights").hasRole("admin")
                                .requestMatchers(HttpMethod.DELETE, "/flights/*").hasRole("admin")
                                .requestMatchers(HttpMethod.PUT, "/flights/*").hasRole("admin")
                                // Búsqueda por id y fecha
                                .requestMatchers(HttpMethod.GET, "/flights/*").authenticated()
                                // Búsqueda por origen y destino
                                .requestMatchers(HttpMethod.GET, "/flights").authenticated()

                                // PASAJEROS
                                .requestMatchers(HttpMethod.POST, "/flights/*/passengers").hasRole("personal")
                                .requestMatchers(HttpMethod.PUT, "/flights/*/passengers/*").hasRole("personal")
                                .requestMatchers(HttpMethod.DELETE, "/flights/*/passengers/*").hasRole("personal")
                                // Búsqueda por NIF
                                .requestMatchers(HttpMethod.GET, "/flights/*/passengers/*").hasRole("personal")
                                // Búsqueda de todos los pasajeros de un vuelo
                                .requestMatchers(HttpMethod.GET, "/flights/*/passengers").hasRole("personal")

                                // USUARIOS
                                .requestMatchers(HttpMethod.POST, "/user").anonymous()
                                .anyRequest().authenticated()
                );

        return http.build();
    }

    /**
     * Invoca a passwordEncoder para que realice la comparación de contraseñas.
     * La que el usuario introduce y las que hay en el servidor.
     *
     * @return la clase securityService que lo implementa.
     */
    public SecurityService passwordEncoder() {
        return securityService;
    }


}
