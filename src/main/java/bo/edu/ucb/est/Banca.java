package bo.edu.ucb.est;

import java.util.*;

public class Banca {

    private String nombre;
    private List<Cliente> clientes;

    public Banca(String nombre) {
        this.nombre = nombre;
        this.clientes = new ArrayList();
    }

    public String getNombre(){
        return nombre;
    }

    public void agregarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public Cliente buscarClientePorPin (long userId, String pin) {
        Cliente cliente = null;
        for (int i = 0; i < clientes.size(); i++) {
            cliente = clientes.get(i); // Sacando elemento por elemento
            if (cliente.getUserId() == (userId)){
                if (cliente.getPinSeguridad().equals(pin) == false) {
                    cliente = null;
                }
            break;
            }
        }
        return cliente;
    }

    public Cliente buscarCliente (long userId) {
        Cliente cliente = null;
        for (int i = 0; i < clientes.size(); i++) {
            cliente = clientes.get(i); // Sacando elemento por elemento
            if (cliente.getUserId() == (userId)) {
                break;
            }
        }
        return cliente;
    }
}