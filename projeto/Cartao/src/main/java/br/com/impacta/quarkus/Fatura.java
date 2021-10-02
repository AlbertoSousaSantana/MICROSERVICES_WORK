
package br.com.impacta.quarkus;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Fatura extends PanacheEntity  {
 

   private BigDecimal valor = new BigDecimal("0.01"); 
   private int mes = 0; 
   private Calendar dataEmissao = Calendar.getInstance();
   private Calendar dataVencimento = Calendar.getInstance();

   @ManyToOne
   @JoinColumn(name = "cartao_id")
   private Cartao cartao;

   public Fatura() {
   }

   public Fatura(Cartao cartao, BigDecimal valor, int mes, Calendar dataEmissao,Calendar dataVencimento) {
      this.valor = valor;
      this.dataEmissao = dataEmissao;
      this.mes = mes;
      this.dataVencimento = dataVencimento;
      this.cartao = cartao;
   }


  public static List<Fatura> findByCartao(Cartao cartao){
   List<Fatura> faturas = Fatura.list("cartao_id",cartao.id);
     return faturas;
  }


   public BigDecimal getValor() {
      return this.valor;
   }

   public void setValor(BigDecimal valor) {
      this.valor = valor;
   }

   public Calendar getDataEmissao() {
      return this.dataEmissao;
   }

   public void setDataEmissao(Calendar dataEmissao) {
      this.dataEmissao = dataEmissao;
   }

   public Calendar getVencimento() {
    return this.dataVencimento;
   }

   public void setVencimento(Calendar dataVencimento) {
    this.dataVencimento = dataVencimento;
   }

   public int getMes() {
      return this.mes;
   }

   public void setMes(int mes) {
      this.mes = mes;
   }

   public Cartao getCartao() {
      return this.cartao;
   }

   public void setCartao(Cartao cartao) {
      this.cartao = cartao;
   }

}
