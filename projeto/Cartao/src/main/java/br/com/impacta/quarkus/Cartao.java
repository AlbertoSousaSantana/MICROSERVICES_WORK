package br.com.impacta.quarkus;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class Cartao extends PanacheEntity  {

    public Cartao(){

    }

    public Cartao(Long numero, String nome,String descricao, String titular, String bandeira, BigDecimal limite,BigDecimal limiteEmUso, Long idBanco) {
      this.numero = numero;  
      this.nome = nome;
      this.descricao = descricao;
      this.bandeira = bandeira;
      this.limite = limite;
      this.limiteEmUso = limiteEmUso;      
      this.idBanco = idBanco;
     }
  private Long numero;
  private String nome;
  private String descricao;
  private String titular;
  private String bandeira;
  private BigDecimal limite;
  private BigDecimal limiteEmUso;
  private Long idBanco;


  public Long getNumero() {
   return this.numero;
  }

   public void setNumero(Long numero) {
   this.numero = numero;
  }
  public Long getIdBanco() {
   return this.idBanco;
  }

   public void setIdBanco(Long idBanco) {
   this.idBanco = idBanco;
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

 public String getTitular() {
   return this.titular;
}

public void setTitular(String titular) {
   this.titular = titular;
}

 public String getBandeira() {
   return this.bandeira;
}

public void setBandeira(String bandeira) {
   this.bandeira = bandeira;
}


 public BigDecimal getLimite() {
    return this.limite;
 }

 public void setLimite(BigDecimal limite) {
    this.limite = limite;
 }

 
 public BigDecimal getLimiteEmUso() {
   return this.limiteEmUso;
}

public void setLimiteEmUso(BigDecimal limiteEmUso) {
   this.limiteEmUso = limiteEmUso;
}

}