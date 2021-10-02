package br.com.impacta.quarkus;
import java.math.BigDecimal;
public class ContaCorrente {

    private BigDecimal saldo;

    private Long id;

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}