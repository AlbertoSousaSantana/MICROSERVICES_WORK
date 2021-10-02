package br.com.impacta.quarkus;


import javax.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
        tags = {
                @Tag(name="contacorrente", description="API de Gerenciamento de Conta Corrente"),
        },
        info = @Info(
        title="Conta Corrente API",
        version = "1.0.0",
        contact = @Contact(
                name = "Conta Corrente API Support",
                url = "http://contacorrenteapi.com/contact",
                email = "contacorrenteapi@contacorrenteapi.com"),
        license = @License(
                name = "Apache 2.0",
                url = "http://www.apache.org/licenses/LICENSE-2.0.html"))
)
public class ContaCorrenteAPIApplication extends Application {}