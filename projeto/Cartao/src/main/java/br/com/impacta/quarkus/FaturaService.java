
package br.com.impacta.quarkus;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class FaturaService {

 
    @Transactional
    public List<Fatura> getFaturas(){
        List<Fatura> cList = Fatura.listAll();
        return cList;
    }  

    @Transactional
    public List<Fatura> getFaturasByCartaoId(Long idCartao){
        Cartao cartao = new Cartao();
        cartao =  Cartao.findById(idCartao);      
        
        List<Fatura> cList = Fatura.findByCartao(cartao);
        return cList;
    } 

    @Transactional
    public Fatura addFatura(Fatura fatura){
        Fatura.persist(fatura);
        return fatura;
    }

}