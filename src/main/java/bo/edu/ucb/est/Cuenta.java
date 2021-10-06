package bo.edu.ucb.est;

public class Cuenta {

    private String moneda;
    private String nroCuenta;
    private String tipo;
    private double saldo;

    public Cuenta(String moneda, double saldoInicial, String nroCuenta) {
        this.moneda = moneda;
        this.saldo = saldoInicial;
        this.tipo = null;
        this.nroCuenta = nroCuenta;
    }

    public boolean retirar(double monto) {
        boolean resultado = false;
        if (monto > 0 && monto <= saldo) { // verifica que no sea negativo, cero o exceda su saldo
            saldo = saldo - monto;
            resultado = true; // si he podido retirar
        }
        return resultado;
    }

    public boolean depositar(double monto) {
        boolean resultado = false;
        if (monto > 0) { // verifica que no sea negativo, cero o exceda su saldo
            saldo = saldo + monto;
            resultado = true; // si he podido retirar
        }
        return resultado;
    }

    public String getMoneda() {
        return moneda;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo1) {
        this.tipo = tipo1;
    }

    public double getSaldo() {
        return saldo;
    }

    public String getNroCuenta() {
        return nroCuenta;
    }

}
