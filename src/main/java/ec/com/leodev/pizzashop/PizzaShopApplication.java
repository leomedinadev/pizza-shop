package ec.com.leodev.pizzashop;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@OpenAPIDefinition(
        info = @Info(
                title = "PIZZA SHOP API Documentation",
                version = "1.1.0-SNAPSHOT",
                description = "Documentación de la API con OpenAPI 3 - Pizza Shop de Spring Boot",
                contact = @Contact(name = "Leo Medina", email = "tioleodeveloper@gmail.com", url = "https://github.com/leomedinadev"),
                license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")
        )
)
@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
public class PizzaShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzaShopApplication.class, args);
	}

}
