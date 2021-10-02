package br.com.impacta.quarkus;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import javax.transaction.SystemException;
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
import javax.transaction.UserTransaction;

@Path("/investimento")
public class InvestimentoResource {

  private static final Logger LOGGER = Logger.getLogger(InvestimentoResource.class.toString());

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
  InvestimentoService investimentoService;

  @Inject
  @RestClient
  SaqueRestClient saqueRestClient;

  @Inject UserTransaction transaction;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
  @Timeout(timeout)
  //@Fallback(fallbackMethod = "fallbackinvestimento")
  @CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)
  public List<Investimento> listInvestimento(){
      if (isTestingFault){
          executeFault();
      }
      return investimentoService.listInvestimento();
  }

  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
  @Timeout(timeout)
  //@Fallback(fallbackMethod = "fallbackinvestimento")
  @CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)  
  public Response addInvestimento(Investimento investimento)throws IllegalStateException, SecurityException, SystemException{      
  
      Investimento investimentoEntity = investimentoService.addInvestimento(investimento);      
     
      Transacao transacao = new Transacao();  

      try
      {
        transaction.begin();
          ContaCorrente conta = new ContaCorrente();
          conta.setId(investimento.getConta());
          transacao.setConta(conta);   
          transacao.setTipo(TipoTransacao.DEBITO);
          transacao.setValor(investimento.getValor());      
      
           saqueRestClient.sacar(transacao);
         transaction.commit();
        return Response.ok(investimentoEntity).build();
      
      }
      catch(Throwable  e)
      {
       transaction.rollback();   
       String message = "Falha na operação ."+e.getMessage();
       return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
       .entity(message)
       .type(MediaType.APPLICATION_JSON)
       .build();
      }     
      
     
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

  private List<Investimento> fallbackinvestimento(){
      return new ArrayList<Investimento>(0);
     // throw new RuntimeException("FallBack ");
  }



  @Gauge(name = "QUARKUS_QUANTIDADE_INVESTIMENTOS", unit = MetricUnits.NONE, description = "QUARKUS_QUANTIDADE_INVESTIMENTOS")
  public long checkContaCorrenteAmmout(){
      return investimentoService.listInvestimento().size();
  }

}