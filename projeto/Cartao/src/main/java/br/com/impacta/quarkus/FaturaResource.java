package br.com.impacta.quarkus;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Gauge;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;

import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

@Path("/fatura")
public class FaturaResource {
  

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
  FaturaService faturaService;

  @Inject
  CartaoService cartaoService;

  @Inject UserTransaction transaction;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
  @Timeout(timeout)
  @Fallback(fallbackMethod = "fallbackFatura")
  @CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)
  public List<Fatura> listFaturas(){
      if (isTestingFault){
          executeFault();
      }
      return faturaService.getFaturas();
  }


  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
  @Timeout(timeout)
  //@Fallback(fallbackMethod = "fallbackFatura")
  @CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)
  @Path("/{id}")
  public List<Fatura> getTransacoesByContaId(@PathParam("id") Long idConta){
 
    List<Fatura> cList =  faturaService.getFaturasByCartaoId(idConta);
    return cList;

  }


  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
  @Timeout(timeout)
  //@Fallback(fallbackMethod = "fallbackContaCorrente")
  @CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)
  //@Path("/{idCartao}")
  public Response addFatura(Fatura fatura) throws IllegalStateException, SecurityException, SystemException{  

    Fatura faturaEntity = new Fatura();
     try {
            transaction.begin();
            faturaEntity = faturaService.addFatura(fatura);
            cartaoService.aumentaLimiteEmUso(fatura.getCartao().id, fatura.getValor());
            transaction.commit();
        }
        catch(Throwable e) {
            // do something on Tx failure
            transaction.rollback();
            String message = "Falha na operação ."+e.getMessage();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(message)
            .type(MediaType.APPLICATION_JSON)
            .build();
        }
    
        return Response.ok(faturaEntity).build();

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

  private List<Fatura> fallbackFatura(){
      return new ArrayList<Fatura>(0);
     // throw new RuntimeException("FallBack ");
  }



  @Gauge(name = "QUARKUS_QUANTIDADE_FATURAS", unit = MetricUnits.NONE, description = "QUARKUS_QUANTIDADE_FATURAS")
  public long checkContaCorrenteAmmout(){
      return faturaService.getFaturas().size();
  }

}