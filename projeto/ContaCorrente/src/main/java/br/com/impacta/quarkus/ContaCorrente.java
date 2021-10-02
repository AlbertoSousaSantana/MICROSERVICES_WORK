package br.com.impacta.quarkus;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.Entity;
import java.util.List;
import java.math.BigDecimal;

@Entity
public class ContaCorrente extends PanacheEntity  {

    public ContaCorrente(){

    }

    public ContaCorrente(String titular, Integer agencia, Integer numeroConta, BigDecimal saldo, boolean status) {
        this.titular = titular;
        this.agencia = agencia;
        this.numeroConta = numeroConta;
        this.saldo = saldo;
        this.status = status;     
     }

  private Integer rg = 0;
  private int numeroCep =0; 
  private String titular;
  private Integer agencia;
  private Integer numeroConta;
  private BigDecimal saldo;
  private boolean status = true;

 public void sacar(Transacao transacao) {
    transacao.setConta(this);
    this.setSaldo(this.getSaldo().add(transacao.getValor().negate()));
 }

 public void depositar(Transacao transacao) {
    transacao.setConta(this);
    transacao.setTipo(TipoTransacao.CREDITO);
    this.setSaldo(this.getSaldo().add(transacao.getValor()));
 }

  public static List<ContaCorrente> findByTitular(ContaCorrente conta){
      List<ContaCorrente> contaList = list("titular",  conta.getTitular());
      return contaList;
  }

  public static ContaCorrente findByRg(ContaCorrente conta){
    conta = ContaCorrente.find("rg", conta.getRg()).firstResult();
      return conta;
  }

  public Integer getNumeroCep() {
    return numeroCep;
  }

  public void setNumeroCep(Integer numeroCep) {
    this.numeroCep = numeroCep;
  }

  public Integer getRg() {
      return rg;
  }

  public void setRg(Integer rg) {
      this.rg = rg;
  }

 public String getTitular() {
    return this.titular;
 }

 public void setTitular(String titular) {
    this.titular = titular;
 }

 public Integer getAgencia() {
    return this.agencia;
 }

 public void setAgencia(Integer agencia) {
    this.agencia = agencia;
 }

 public Integer getNumeroConta() {
    return this.numeroConta;
 }

 public void setNumeroConta(Integer numeroConta) {
    this.numeroConta = numeroConta;
 }

 public BigDecimal getSaldo() {
    return this.saldo;
 }

 public void setSaldo(BigDecimal saldo) {
    this.saldo = saldo;
 }

 public boolean getStatus() {
    return this.status;
 }

 public void setStatus(boolean status) {
    this.status = status;
 }

 public boolean isStatus() {
    return this.status;
 }

}