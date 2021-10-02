
package br.com.impacta.quarkus;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import io.quarkus.hibernate.orm.panache.PanacheEntity;


@Entity
public class Pagamento extends PanacheEntity  {
 

   private BigDecimal valor = new BigDecimal("0.01"); 
   private Calendar data = Calendar.getInstance();
   @ManyToOne
   @JoinColumn(name = "fatura_id")
   private Fatura fatura;

   public Pagamento() {
   }

   public Pagamento(Fatura fatura, BigDecimal valor, Calendar data) {
      this.valor = valor;
      this.data = data;  
      this.fatura = fatura;
   }


  public static List<Pagamento> findByFatura(Fatura fatura){
   List<Pagamento> pagamentos = Pagamento.list("fatura_id",fatura.id);
     return pagamentos;
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
   
   public Fatura getFatura() {
      return this.fatura;
   }

   public void setFatura(Fatura fatura) {
      this.fatura = fatura;
   }

}
