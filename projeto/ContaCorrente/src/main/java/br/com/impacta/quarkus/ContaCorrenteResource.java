package br.com.impacta.quarkus;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Path("/contacorrente")
public class ContaCorrenteResource {

  private static final Logger LOGGER = Logger.getLogger(ContaCorrenteResource.class.toString());

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
  ContaCorrenteService contaCorrenteService;


  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
  @Timeout(timeout)
  @Fallback(fallbackMethod = "fallbackContaCorrente")
  @CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)
  public List<ContaCorrente> listContaCorrente(){
      if (isTestingFault){
          executeFault();
      }
      return contaCorrenteService.listContaCorrente();
  }

  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
  @Timeout(timeout)
  //@Fallback(fallbackMethod = "fallbackContaCorrente")
  @CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)
  @Path("/{id}")
  public ContaCorrente getContaById(@PathParam("id") Long id){
      ContaCorrente ContaCorrenteEntity = new ContaCorrente();
      ContaCorrenteEntity.id = id;
      ContaCorrenteEntity = contaCorrenteService.getContaById(ContaCorrenteEntity);
      return ContaCorrenteEntity;
  }

  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
  @Timeout(timeout)
  //@Fallback(fallbackMethod = "fallbackContaCorrente")
  @CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)
  @Path("/rg/{rg}")
  public ContaCorrente getContaCorrenteByRG(@PathParam("rg") Integer rg){
      ContaCorrente contaCorrenteEntity = new ContaCorrente();
      contaCorrenteEntity.setRg(rg);
      contaCorrenteEntity = contaCorrenteService.getContaCorrenteByRg(contaCorrenteEntity);
      return contaCorrenteEntity;
  }

  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
  @Timeout(timeout)
 // @Fallback(fallbackMethod = "fallbackContaCorrente")
  @CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)
  @Path("/titular/{titular}")
  public List<ContaCorrente> getContaCorrenteByName(@PathParam("titular") String titular){
      ContaCorrente contaCorrenteEntity = new ContaCorrente();
      contaCorrenteEntity.setTitular(titular);
      List<ContaCorrente> contas = contaCorrenteService.getByTitular(contaCorrenteEntity);
      return contas;
  }


  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
  @Timeout(timeout)
  //@Fallback(fallbackMethod = "fallbackContaCorrente")
  @CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)
  public ContaCorrente addConta(ContaCorrente conta){    
      ContaCorrente contaCorrenteEntity = contaCorrenteService.addConta(conta);
      return contaCorrenteEntity;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON) 
  @Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
  @Timeout(timeout)
  //@Fallback(fallbackMethod = "fallbackContaCorrente")
  @CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)
  @Path("/sacar")
  public Response sacar(Transacao transacao){  
    ContaCorrente conta = new ContaCorrente();
    try
    {
        conta = contaCorrenteService.sacar(transacao);
    } catch (Throwable e){
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(e.getMessage())
        .type(MediaType.APPLICATION_JSON)
        .build();
    }    
   
     return Response.ok(conta).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON) 
  @Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
  @Timeout(timeout)
 // @Fallback(fallbackMethod = "fallbackContaCorrente")
  @CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)
  @Path("/depositar")
  public BigDecimal depositar(Transacao transacao){      
      ContaCorrente conta = contaCorrenteService.depositar(transacao);     
      return conta.getSaldo();
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
  @Timeout(timeout)
 // @Fallback(fallbackMethod = "fallbackContaCorrente")
  @CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)
  public ContaCorrente updateContaCorrente(ContaCorrente conta){
      ContaCorrente contaCorrenteEntity = contaCorrenteService.updateContaCorrente(conta);
      return contaCorrenteEntity;
  }

  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
  @Timeout(timeout)
  //@Fallback(fallbackMethod = "fallbackContaCorrente")
  @CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)
  @Path("/rg/{rg}")
  public String deleteContaCorrenteByRg(@PathParam("rg") Integer rg){
      ContaCorrente contaCorrenteEntity = new ContaCorrente();
      contaCorrenteEntity.setRg(rg);
      contaCorrenteEntity = contaCorrenteService.getContaCorrenteByRg(contaCorrenteEntity);
      contaCorrenteEntity = contaCorrenteService.deleteContaCorrente(contaCorrenteEntity);
      return "Operação Realizada com Sucesso";
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

  private List<ContaCorrente> fallbackContaCorrente(){
      return new ArrayList<ContaCorrente>(0);
     // throw new RuntimeException("FallBack ");
  }



  @Gauge(name = "QUARKUS_QUANTIDADE_CONTAS", unit = MetricUnits.NONE, description = "QUARKUS_QUANTIDADE_CONTAS")
  public long checkContaCorrenteAmmout(){
      return contaCorrenteService.listContaCorrente().size();
  }

}