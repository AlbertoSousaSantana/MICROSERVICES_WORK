package br.com.impacta.quarkus;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class CartaoService {

    @Transactional
    public Cartao addCartao(Cartao cartao){
        Cartao.persist(cartao);
        return cartao;
    }
    
 
    @Transactional
    public Cartao getCartaoById(Cartao cartao){
        cartao = Cartao.findById(cartao.id);
        return cartao;
    }

    @Transactional
    public Cartao aumentaLimiteEmUso(Long IdCatao, BigDecimal valor){
        Cartao cartao = Cartao.findById(IdCatao);
        cartao.setLimiteEmUso(cartao.getLimiteEmUso().add(valor));       
        Cartao.persist(cartao);
        return cartao;
    }

    @Transactional
    public Cartao diminuiLimiteEmUso(Long IdCatao, BigDecimal valor){
        Cartao cartao = Cartao.findById(IdCatao);
        cartao.setLimiteEmUso(cartao.getLimiteEmUso().subtract(valor));       
        Cartao.persist(cartao);
        return cartao;
    }


    
    @Transactional
    public List<Cartao> listCartao(){
        List<Cartao> cList = Cartao.listAll();
        return cList;
    }

    
   
}