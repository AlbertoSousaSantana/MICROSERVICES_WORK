package br.com.impacta.quarkus;
import java.math.BigDecimal;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ContaCorrenteService {

    @Transactional
    public ContaCorrente addConta(ContaCorrente conta){
        ContaCorrente.persist(conta);
        return conta;
    }

    @Transactional
    public ContaCorrente sacar(Transacao transacao) {
        ContaCorrente conta = new ContaCorrente();
        conta =  ContaCorrente.findById(transacao.getConta().id);

        BigDecimal diff = conta.getSaldo().subtract( transacao.getValor());

        if (diff.signum() < 0)
        {
            throw new RuntimeException("Saldo insuficiente");
        }

        conta.sacar(transacao);       
        ContaCorrente.persist(conta);  
        transacao.setTipo(TipoTransacao.DEBITO);
        transacao.setConta(conta);             
        Transacao.persist(transacao);  
     return conta;
     }
     @Transactional
     public ContaCorrente depositar(Transacao transacao) {
        ContaCorrente conta = new ContaCorrente();
        conta =  ContaCorrente.findById(transacao.getConta().id);
        conta.depositar(transacao);     
        ContaCorrente.persist(conta);    
        transacao.setTipo(TipoTransacao.CREDITO);
        transacao.setConta(conta);     
        Transacao.persist(transacao);  
      return conta;
     }
 
    @Transactional
    public ContaCorrente getContaById(ContaCorrente conta){
        conta = ContaCorrente.findById(conta.id);
        return conta;
    }

    @Transactional
    public List<ContaCorrente> getByTitular(ContaCorrente conta){
        List<ContaCorrente> cList = ContaCorrente.findByTitular(conta);
        return cList;
    }

  
    @Transactional
    public ContaCorrente getContaCorrenteByRg(ContaCorrente conta){
        conta = ContaCorrente.findByRg(conta);
        return conta;
    }

    @Transactional
    public List<ContaCorrente> listContaCorrente(){
        List<ContaCorrente> cList = ContaCorrente.listAll();
        return cList;
    }

    @Transactional
    public ContaCorrente deleteContaCorrente(ContaCorrente conta){
        conta = ContaCorrente.findById(conta.id);
        ContaCorrente.deleteById(conta.id);
        return conta;
    }

    @Transactional
    public ContaCorrente updateContaCorrente(ContaCorrente conta){
        ContaCorrente contaEntity = ContaCorrente.findById(conta.id);
        if (contaEntity != null){
            contaEntity.setTitular(conta.getTitular());         
            contaEntity.setRg(conta.getRg());
          
        }
        return contaEntity;
    }
}