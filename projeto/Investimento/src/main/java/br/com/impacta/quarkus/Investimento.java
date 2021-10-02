package br.com.impacta.quarkus;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class Investimento extends PanacheEntity  {

    public Investimento(){

    }

    public Investimento(String nome,String descricao, String tipo, BigDecimal valor, Long conta) {
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
        this.valor = valor;   
        this.conta = conta;    
     }

  private  String nome;
  private String descricao;
  private String tipo;
  private BigDecimal valor; 
  private long conta;
 

  public Long getConta() {
   return this.conta;
}

public void setConta(Long conta) {
   this.conta = conta;
}
   
 public String getNome() {
    return this.nome;
 }

 public void setNome(String nome) {
    this.nome = nome;
 }

 public String getDescricao() {
    return this.descricao;
 }

 public void setDescricao(String descricao) {
    this.descricao = descricao;
 }

 public String getTipo() {
   return this.tipo;
}

public void setTipo(String tipo) {
   this.tipo = tipo;
}


 public BigDecimal getValor() {
    return this.valor;
 }

 public void setValor(BigDecimal valor) {
    this.valor = valor;
 }


}