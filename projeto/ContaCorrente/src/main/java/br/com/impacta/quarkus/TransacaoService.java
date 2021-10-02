
package br.com.impacta.quarkus;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class TransacaoService {

 
    @Transactional
    public List<Transacao> getTransacoes(){
        List<Transacao> cList = Transacao.listAll();
        return cList;
    }  

    @Transactional
    public List<Transacao> getTransacoesByContaId(Long idConta){
        ContaCorrente conta = new ContaCorrente();
        conta =  ContaCorrente.findById(idConta);      
        
        List<Transacao> cList = Transacao.findByConta(conta);
        return cList;
    } 

}