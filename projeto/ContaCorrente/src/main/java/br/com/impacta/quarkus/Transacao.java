package br.com.impacta.quarkus;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity

public class Transacao extends PanacheEntity  {
  

   private BigDecimal valor = new BigDecimal("0.01"); 
   private Calendar data = Calendar.getInstance();
   private TipoTransacao tipo;
   @ManyToOne
   @JoinColumn(name = "conta_id")
   private ContaCorrente conta;

   public Transacao() {
   }

   public Transacao(ContaCorrente conta, BigDecimal valor, Calendar data) {
      this.valor = valor;
      this.data = data;
      this.conta = conta;
   }


  public static List<Transacao> findByConta(ContaCorrente conta){
   List<Transacao> transacoes = Transacao.list("conta_id",conta.id);
     return transacoes;
  }


   public BigDecimal getValor() {
      return this.valor;
   }

   public void setValor(BigDecimal valor) {
      this.valor = valor;
   }

   public Calendar getData() {
      return this.data;
   }

   public void setData(Calendar data) {
      this.data = data;
   }

   public TipoTransacao getTipo() {
      return this.tipo;
   }

   public void setTipo(TipoTransacao tipo) {
      this.tipo = tipo;
   }

   public ContaCorrente getConta() {
      return this.conta;
   }

   public void setConta(ContaCorrente conta) {
      this.conta = conta;
   }

}
