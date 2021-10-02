package br.com.impacta.quarkus;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class InvestimentoService {

    @Transactional
    public Investimento addInvestimento(Investimento investimento){
        Investimento.persist(investimento);
        return investimento;
    }
    
 
    @Transactional
    public Investimento getContaById(Investimento investimento){
        investimento = Investimento.findById(investimento.id);
        return investimento;
    }

    
    @Transactional
    public List<Investimento> listInvestimento(){
        List<Investimento> cList = Investimento.listAll();
        return cList;
    }

    
   
}