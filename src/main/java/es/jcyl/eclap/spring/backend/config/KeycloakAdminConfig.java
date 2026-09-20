package es.jcyl.eclap.spring.backend.config;


import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminConfig {

    @Value("${keycloak.admin.server-url}")
    private String serverUrl;

    @Value("${keycloak.admin.realm-master}")
    private String realmMaster;

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    @Value("${keycloak.admin.usuario}")
    private String usuario;

    @Value("${keycloak.admin.clave}")
    private String clave;

    @Bean
    public Keycloak keycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realmMaster)
                .clientId(clientId)
                .username(usuario)
                .password(clave)
                .build();
    }
}