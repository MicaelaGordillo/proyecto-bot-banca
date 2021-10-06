package bo.edu.ucb.est;

import java.util.*;

public class Cliente {

    private long userId;
    private String nombreCliente;
    private String pinSeguridad;
    private List<Cuenta> cuentas;

    public Cliente(long userId, String nombreCliente) {
        this.userId = userId;
        this.nombreCliente = nombreCliente;
        this.pinSeguridad = null;
        this.cuentas = new ArrayList();
    }

    public long getUserId (){
        return userId;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getPinSeguridad() {
        return pinSeguridad;
    }

    public void setPinSeguridad(String pin) {
        this.pinSeguridad = pin;
    }

    public void agregarCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public Cuenta buscarCuentaPorNro (String nro) {
        Cuenta cuenta = null;
        for (int i = 0; i < cuentas.size(); i++) {
            cuenta = cuentas.get(i); // Sacando elemento por elemento
            if (cuenta.getNroCuenta() == (nro)) {
                break;
            }
        }
        return cuenta;
    }

    public void eliminarCuentas() {
        Cuenta cuenta = null;
        for (int i = 0; i < cuentas.size(); i++) {
            cuenta = cuentas.get(i); // Sacando elemento por elemento
            if (cuenta.getTipo() == null) {
                cuentas.remove(cuenta);
            }
        }
    }

    public Cuenta buscarCuentaPorPos (int p) {
        Cuenta cuenta = null;
        for (int i = 0; i < cuentas.size(); i++) {
            cuenta = cuentas.get(i); // Sacando elemento por elemento
            if (i == p-1) {
                break;
            }
        }
        return cuenta;
    }

    public String mostrarCuentas (){
        String listadoCuentas = "";
        for (int i = 0 ; i < getCuentas().size() ; i ++) {
            Cuenta cuenta = getCuentas().get(i);
            listadoCuentas = listadoCuentas+(i + 1)+". "+cuenta.getNroCuenta()+" "+cuenta.getTipo()+"\n";
        }
        return listadoCuentas;
    }

    public String mostrarCuenta (int n){
        String count = null;
        for (int i = 0 ; i < getCuentas().size() ; i ++) {
            Cuenta cuenta = getCuentas().get(i);
            if (i == n-1){
                count = "Cuenta "+(i + 1)+": "+cuenta.getNroCuenta()+"\nMoneda: "+cuenta.getMoneda()+
                        "\nTipo: "+cuenta.getTipo()+"\nSaldo: "+cuenta.getSaldo();
                break;
            }
        }
        return count;
    }
}
