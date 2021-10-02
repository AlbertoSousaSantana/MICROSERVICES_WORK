package br.com.impacta.quarkus;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Gauge;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;

@Path("/transacao")
public class TransacaoResource {
  

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
  TransacaoService transacaoService;


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
  @Timeout(timeout)
  @Fallback(fallbackMethod = "fallbackTransacao")
  @CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)
  public List<Transacao> listTransacoes(){
      if (isTestingFault){
          executeFault();
      }
      return transacaoService.getTransacoes();
  }


  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
  @Timeout(timeout)
  //@Fallback(fallbackMethod = "fallbackTransacao")
  @CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)
  @Path("/{id}")
  public List<Transacao> getTransacoesByContaId(@PathParam("id") Long idConta){
 
    List<Transacao> cList =  transacaoService.getTransacoesByContaId(idConta);
    return cList;

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

  private List<Transacao> fallbackTransacao(){
      return new ArrayList<Transacao>(0);
     // throw new RuntimeException("FallBack ");
  }



  @Gauge(name = "QUARKUS_QUANTIDADE_TRANSACOES", unit = MetricUnits.NONE, description = "QUARKUS_QUANTIDADE_TRANSACOES")
  public long checkContaCorrenteAmmout(){
      return transacaoService.getTransacoes().size();
  }

}