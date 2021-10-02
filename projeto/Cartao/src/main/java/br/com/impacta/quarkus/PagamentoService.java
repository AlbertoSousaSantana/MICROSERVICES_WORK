
package br.com.impacta.quarkus;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class PagamentoService {

 
    @Transactional
    public List<Pagamento> getPagamentos(){
        List<Pagamento> cList = Pagamento.listAll();
        return cList;
    }  

    @Transactional
    public List<Pagamento> getPagamentosByFaturaId(Long idFatura){
        Fatura fatura = new Fatura();
        fatura =  Fatura.findById(idFatura);      
        
        List<Pagamento> cList = Pagamento.findByFatura(fatura);
        return cList;
    } 

    @Transactional
    public Pagamento addPagamento(Pagamento pagamento){
        Pagamento.persist(pagamento);
        return pagamento;
    }

}