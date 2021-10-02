package br.com.impacta.quarkus;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Timeout;

@Path("/cartao")
public class CartaoResource {

  private static final Logger LOGGER = Logger.getLogger(CartaoResource.class.toString());

  @ConfigProperty(name = "isTestingFault")
  boolean isTestingFault;
  //Constantes
  final int quantidadeRetry = 5;
  final int timeout = 250;
  final int requestVolumeThresholdValue = 20;
  final int delayValue = 1; 

  @ConfigProperty(name = "isRetry")
  boolean isRetry;
  int exceptionCount = quantidadeRetry - 1;
  int count = 0;

  @ConfigProperty(name = "isFallBack")
  boolean isFallBack;

  @Inject
  CartaoService cartaoService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
  @Timeout(timeout)
  //@Fallback(fallbackMethod = "fallbackcartao")
  @CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)
  public List<Cartao> listCartao(){
      if (isTestingFault){
          executeFault();
      }
      return cartaoService.listCartao();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
  @Timeout(timeout)
  //@Fallback(fallbackMethod = "fallbackContaCorrente")
  @CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)
  public Cartao addCartao(Cartao cartao){    
    Cartao cartaoEntity = cartaoService.addCartao(cartao);
      return cartaoEntity;
  }

  private void executeFault(){
     if (isRetry){
         while ( count < exceptionCount){
             count++;
             String message = "Simulando Erro: " + count;
         //    LOGGER.log(Level.ERROR, message);
             throw new RuntimeException(message);
         }
         count = 0;
     }
     if (isFallBack){
         throw new RuntimeException("Simulando Erro: ");
     }
  }

  private List<Cartao> fallbackcartao(){
      return new ArrayList<Cartao>(0);
     // throw new RuntimeException("FallBack ");
  }



  @Gauge(name = "QUARKUS_QUANTIDADE_CARTOES", unit = MetricUnits.NONE, description = "QUARKUS_QUANTIDADE_CARTOES")
  public long checkContaCorrenteAmmout(){
      return cartaoService.listCartao().size();
  }

}