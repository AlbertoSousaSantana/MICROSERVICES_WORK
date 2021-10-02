package br.com.impacta.quarkus;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.eclipse.microprofile.rest.client.inject.RestClient;
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

@Path("/pagamento")
public class PagamentoResource {
  

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
  PagamentoService pagamentoService;
  
  @Inject
  CartaoService cartaoService;

  @Inject
  @RestClient
  SaqueRestClient saqueRestClient;

  @Inject UserTransaction transaction;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
  @Timeout(timeout)
  @Fallback(fallbackMethod = "fallbackPagamento")
  @CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)
  public List<Pagamento> listPagamentos(){
      if (isTestingFault){
          executeFault();
      }
      return pagamentoService.getPagamentos();
  }


  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
  @Timeout(timeout)
  //@Fallback(fallbackMethod = "fallbackPagamento")
  @CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)
  @Path("/{id}")
  public List<Pagamento> getPagamentoByFaturaId(@PathParam("id") Long idFatura){
 
    List<Pagamento> cList =  pagamentoService.getPagamentosByFaturaId(idFatura);
    return cList;

  }

/*
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
  @Timeout(timeout)
  //@Fallback(fallbackMethod = "fallbackContaCorrente")
  @CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)
   public Pagamento addPagamento(Pagamento pagamento){  
    Pagamento pagamentoEntity = pagamentoService.addPagamento(pagamento);
      return pagamentoEntity;
  }
*/
@POST
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Retry(maxRetries = quantidadeRetry, delay = delayValue, delayUnit = ChronoUnit.SECONDS)
@Timeout(timeout)
//@Fallback(fallbackMethod = "fallbackContaCorrente")
@CircuitBreaker(requestVolumeThreshold = requestVolumeThresholdValue)
  public Response addPagamento(Pagamento pagamento) throws IllegalStateException, SecurityException, SystemException{   

    
   
    Transacao transacao = new Transacao();  
    ContaCorrente conta = new ContaCorrente();
    try
    {
      transaction.begin();
        conta = new ContaCorrente();
        conta.setId(pagamento.getFatura().getCartao().getIdBanco());
        transacao.setConta(conta);   
        transacao.setTipo(TipoTransacao.DEBITO);
        transacao.setValor(pagamento.getValor());      
    
        saqueRestClient.sacar(transacao);      

        cartaoService.diminuiLimiteEmUso(pagamento.getFatura().getCartao().id, pagamento.getValor());
        Pagamento pagamentoEntity = pagamentoService.addPagamento(pagamento);

      transaction.commit();
      return Response.ok(pagamentoEntity).build();

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

  private List<Pagamento> fallbackPagamento(){
      return new ArrayList<Pagamento>(0);
     // throw new RuntimeException("FallBack ");
  }



  @Gauge(name = "QUARKUS_QUANTIDADE_PAGAMENTOS", unit = MetricUnits.NONE, description = "QUARKUS_QUANTIDADE_PAGAMENTOS")
  public long checkContaCorrenteAmmout(){
      return pagamentoService.getPagamentos().size();
  }

}