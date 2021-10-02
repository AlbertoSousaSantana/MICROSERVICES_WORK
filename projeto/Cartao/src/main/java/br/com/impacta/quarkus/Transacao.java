package br.com.impacta.quarkus;
import java.math.BigDecimal;
import java.util.Calendar;


public class Transacao   {

  

   private BigDecimal valor = new BigDecimal("0.01"); 
   private Calendar data = Calendar.getInstance();
   private TipoTransacao tipo;  
   private ContaCorrente conta;
    public Transacao() {
   }


   public Transacao(ContaCorrente conta, BigDecimal valor, Calendar data) {
      this.valor = valor;
      this.data = data;        
   }
   
   public ContaCorrente getConta() {
      return this.conta;
   }

   public void setConta(ContaCorrente conta) {
      this.conta = conta;
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

}
