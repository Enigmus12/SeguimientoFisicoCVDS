package Escuelaing.edu.co.Seguimiento.Fisico.y.Reservas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // Crea una factory para configurar timeouts (opcional)
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 5 segundos de timeout de conexión
        factory.setReadTimeout(5000);    // 5 segundos de timeout de lectura

        return new RestTemplate(factory);
    }
}
