package bo.edu.ucb.est;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.*;

public class Main extends TelegramLongPollingBot{

    private Banca banco = new Banca("Banco de la Fortuna");
    private Map estadoCliente = new HashMap();
    private Map estadoOpciones = new HashMap();
    private Map estadoProceso = new HashMap();
    private Map numeroCuenta = new HashMap();
    private String[] menu = {"Bienvenid@!!!","Elige una opción:\n1. Ver Saldo \uD83E\uDDFE \n2. Retirar dinero. \uD83D\uDECD \n3. Depositar dinero. \uD83D\uDCB0 \n4. Crear cuenta. \uD83D\uDC64 \n5. Salir. \uD83D\uDEAA"};
    private int numCuenta = 100000;

    @Override
    public String getBotUsername() {
        return "proyecto_banca_bot";
    }

    @Override
    public String getBotToken() {
        return "1962465858:AAFNJGKGS1YUkzU6Z7y4U6qCTEZC1VkR0QY";
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Llego mensaje: " + update.toString());
        long userId = update.getMessage().getChatId(); // Obtenemos el user ID.
        Integer estado = (Integer) estadoCliente.get(userId); // Obtenemos el estado del user ID.
        if (estado == null){ // Verificamos si es la primera vez del usuario.
            estado = 0;
            estadoCliente.put(userId, estado); // Ingresamos al usuario al Map.
        }
        if(update.hasMessage()) { // Verificamos que tenga mensaje
            SendMessage message = new SendMessage(); // Creamos el objeto para enviar un mensaje.
            message.setChatId(update.getMessage().getChatId().toString()); // Definimos a quien le vamos a enviar el mensaje.
            try {
                switch (estado) { // Preparar respuesta dependiendo del estado del usuario.
                    case 0: {
                        message.setText("Bienvenid@ al "+banco.getNombre()+". \uD83D\uDE80"); execute(message); // Llenar mensaje y enviar respuesta.
                        message.setText("He notado que aún no eres cliente, procedamos a registrarte."); execute(message); // Llenar mensaje y enviar respuesta.
                        message.setText("\uD83E\uDD11"); execute(message); // Llenar mensaje y enviar respuesta.
                        message.setText("¿Cual es tu nombre completo?"); execute(message); // Llenar mensaje y enviar respuesta.
                        estadoCliente.put(userId, 1); // Actualizar estado.
                        break; // Terminar.
                    }
                    case 1: {
                        Cliente usuario = new Cliente(userId, update.getMessage().getText()); // Creamos un objeto de tipo Cliente.
                        banco.agregarCliente(usuario); // Agregamos el objeto a la lista de clientes del Banco.
                        message.setText("Por favor elige un PIN de seguridad, este te será requerido cada que ingreses al sistema."); execute(message); // Llenar mensaje y enviar respuesta.
                        message.setText("\uD83D\uDD10"); execute(message); // Llenar mensaje y enviar respuesta.
                        estadoCliente.put(userId, 2); // Actualizar estado.
                        break; // Terminar.
                    }
                    case 2: {
                        Cliente cliente = banco.buscarCliente(userId); //
                        cliente.setPinSeguridad(update.getMessage().getText());
                        message.setText("Te hemos registrado correctamente."); execute(message); // Llenar mensaje y enviar respuesta.
                        message.setText("\uD83E\uDD73"); execute(message); // Llenar mensaje y enviar respuesta.
                        message.setText("Hola de nuevo "+banco.buscarCliente(userId).getNombreCliente()); execute(message); // Llenar mensaje y enviar respuesta.
                        message.setText("Solo por seguridad ¿cuál es tu PIN?"); execute(message); // Llenar mensaje y enviar respuesta.
                        message.setText("\uD83D\uDD10"); execute(message); // Llenar mensaje y enviar respuesta.
                        estadoCliente.put(userId, 3); // Actualizar estado.
                        break; // Terminar.
                    }
                    case 3: {
                        Cliente usuario = banco.buscarClientePorPin(userId, update.getMessage().getText());
                        if (usuario == null) {
                            message.setText("Lo siento, el código es incorrecto."); execute(message); // Llenar mensaje y enviar respuesta.
                            message.setText("\uD83E\uDD25"); execute(message); // Llenar mensaje y enviar respuesta.
                            message.setText("Hola de nuevo "+banco.buscarCliente(userId).getNombreCliente()); execute(message); // Llenar mensaje y enviar respuesta.
                            message.setText("Solo por seguridad ¿cuál es tu PIN?"); execute(message); // Llenar mensaje y enviar respuesta.
                            message.setText("\uD83D\uDD10"); execute(message); // Llenar mensaje y enviar respuesta.
                        } else {
                            message.setText(menu[0]); execute(message); // Llenar mensaje y enviar respuesta.
                            message.setText(menu[1]); execute(message); // Llenar mensaje y enviar respuesta.
                            estadoCliente.put(userId, 4); // Actualizar estado.
                        }
                        break; // Terminar.
                    }
                    case 4: {
                        if (comprobarNumero(update.getMessage().getText())){
                            Integer option = (Integer) estadoOpciones.get(userId); // Obtenemos el estado del user ID.
                            if (option == null) {
                                option = Integer.parseInt(update.getMessage().getText()); // Verificamos si es la primera vez del usuario.
                                estadoOpciones.put(userId, option); // Ingresamos al usuario al Map.
                            }
                            switch (option) {
                                case 1: {
                                    Integer process = (Integer) estadoProceso.get(userId); // Obtenemos el estado del user ID.
                                    if (process == null) { // Verificamos si es la primera vez del usuario.
                                        process = 0;
                                        estadoProceso.put(userId, process); // Ingresamos al usuario al Map.
                                    }
                                    switch (process) {
                                        case 0: {
                                            String cuentas = banco.buscarCliente(userId).mostrarCuentas();
                                            if (cuentas == "") {
                                                message.setText("Usted aún no tiene ninguna cuenta activa, cree una primero."); execute(message); // Llenar mensaje y enviar.
                                                message.setText("\uD83E\uDD7A"); execute(message); // Llenar mensaje y enviar respuesta.
                                                message.setText(menu[0]); execute(message); // Llenar mensaje y enviar respuesta.
                                                message.setText(menu[1]); execute(message); // Llenar mensaje y enviar respuesta.
                                                estadoProceso.put(userId, null);
                                                estadoOpciones.put(userId, null);
                                            } else {
                                                message.setText("Sus cuentas\n"+cuentas+"Elija una de sus cuentas:"); execute(message); // Llenar mensaje y enviar respuesta.
                                                estadoProceso.put(userId, 1);
                                            }
                                            break;
                                        }
                                        case 1: {
                                            int n = banco.buscarCliente(userId).getCuentas().size();
                                            int nroCuenta = Integer.parseInt(update.getMessage().getText());
                                            if (nroCuenta > n || nroCuenta <= 0){
                                                message.setText("Cuenta inexistente."); execute(message); // Llenar mensaje y enviar.
                                                message.setText("\uD83D\uDE14"); execute(message); // Llenar mensaje y enviar.
                                                message.setText(menu[0]); execute(message); // Llenar mensaje y enviar respuesta.
                                                message.setText(menu[1]); execute(message); // Llenar mensaje y enviar respuesta.
                                                estadoProceso.put(userId, null);
                                                estadoOpciones.put(userId, null);
                                            } else {
                                                String cuentas = banco.buscarCliente(userId).mostrarCuenta(nroCuenta);
                                                message.setText(cuentas); execute(message); // Llenar mensaje y enviar.
                                                message.setText("\uD83D\uDE0E"); execute(message); // Llenar mensaje y enviar.
                                                message.setText(menu[0]); execute(message); // Llenar mensaje y enviar respuesta.
                                                message.setText(menu[1]); execute(message); // Llenar mensaje y enviar respuesta.
                                                estadoProceso.put(userId, null);
                                                estadoOpciones.put(userId, null);
                                            }
                                            break;
                                        }
                                    }
                                    break;
                                }
                                case 2: {
                                    Integer process = (Integer) estadoProceso.get(userId); // Obtenemos el estado del user ID.
                                    if (process == null) { // Verificamos si es la primera vez del usuario.
                                        process = 0;
                                        estadoProceso.put(userId, process); // Ingresamos al usuario al Map.
                                    }
                                    switch (process) {
                                        case 0: {
                                            String cuentas = banco.buscarCliente(userId).mostrarCuentas();
                                            if (cuentas == "") {
                                                message.setText("Usted aún no tiene ninguna cuenta activa, cree una primero."); execute(message); // Llenar mensaje y enviar.
                                                message.setText("\uD83E\uDD7A"); execute(message); // Llenar mensaje y enviar respuesta.
                                                message.setText(menu[0]); execute(message); // Llenar mensaje y enviar respuesta.
                                                message.setText(menu[1]); execute(message); // Llenar mensaje y enviar respuesta.
                                                estadoProceso.put(userId, null);
                                                estadoOpciones.put(userId, null);
                                            } else {
                                                message.setText("Sus cuentas\n"+cuentas+"Elija una de sus cuentas:"); execute(message); // Llenar mensaje y enviar respuesta.
                                                estadoProceso.put(userId, 1);
                                            }
                                            break;
                                        }
                                        case 1: {
                                            int n = banco.buscarCliente(userId).getCuentas().size();
                                            int nroCuenta = Integer.parseInt(update.getMessage().getText());
                                            if (nroCuenta > n || nroCuenta <= 0){
                                                message.setText("Cuenta inexistente."); execute(message); // Llenar mensaje y enviar.
                                                message.setText("\uD83D\uDE14"); execute(message); // Llenar mensaje y enviar.
                                                message.setText(menu[0]); execute(message); // Llenar mensaje y enviar respuesta.
                                                message.setText(menu[1]); execute(message); // Llenar mensaje y enviar respuesta.
                                                estadoProceso.put(userId, null);
                                                estadoOpciones.put(userId, null);
                                            } else {
                                                numeroCuenta.put(userId, nroCuenta);
                                                message.setText("\uD83D\uDCB8"); execute(message); // Llenar mensaje y enviar respuesta.
                                                message.setText("Ingrese el monto a retirar:"); execute(message); // Llenar mensaje y enviar respuesta.
                                                estadoProceso.put(userId, 2);
                                            }
                                            break;
                                        }
                                        case 2: {
                                            int monto = Integer.parseInt(update.getMessage().getText());
                                            Integer numC = (Integer) numeroCuenta.get(userId);
                                            Cuenta count = banco.buscarCliente(userId).buscarCuentaPorPos(numC);
                                            if (count.retirar(monto)){
                                                message.setText("Se ha realizado el retiro correctamente."); execute(message); // Llenar mensaje y enviar respuesta.
                                                message.setText("\uD83D\uDE09"); execute(message); // Llenar mensaje y enviar respuesta.
                                            } else {
                                                message.setText("Se ha producido un error al realizar el retiro, inténtelo nuevamente."); execute(message); // Llenar mensaje y enviar respuesta.
                                                if (monto == 0){
                                                    message.setText("El monto a retirar no puede ser cero."); execute(message); // Llenar mensaje y enviar respuesta.
                                                    message.setText("\uD83D\uDCA5"); execute(message); // Llenar mensaje y enviar respuesta.
                                                } else {
                                                    if (monto < 0){
                                                        message.setText("El monto a retirar no puede ser negativo."); execute(message); // Llenar mensaje y enviar respuesta.
                                                        message.setText("\uD83D\uDCA5"); execute(message); // Llenar mensaje y enviar respuesta.
                                                    } else {
                                                        message.setText("El monto a retirar no puede ser mayor al saldo de la cuenta."); execute(message); // Llenar mensaje y enviar respuesta.
                                                        message.setText("\uD83D\uDCA5"); execute(message); // Llenar mensaje y enviar respuesta.
                                                    }
                                                }
                                            }
                                            numeroCuenta.put(userId, null);
                                            estadoProceso.put(userId, null);
                                            estadoOpciones.put(userId, null);
                                            message.setText(menu[0]); execute(message); // Llenar mensaje y enviar respuesta.
                                            message.setText(menu[1]); execute(message); // Llenar mensaje y enviar respuesta.
                                            break; // Terminar.
                                        }
                                    }
                                    break;
                                }
                                case 3: {
                                    Integer process = (Integer) estadoProceso.get(userId); // Obtenemos el estado del user ID.
                                    if (process == null) { // Verificamos si es la primera vez del usuario.
                                        process = 0;
                                        estadoProceso.put(userId, process); // Ingresamos al usuario al Map.
                                    }
                                    switch (process) {
                                        case 0: {
                                            String cuentas = banco.buscarCliente(userId).mostrarCuentas();
                                            if (cuentas == "") {
                                                message.setText("Usted aún no tiene ninguna cuenta activa, cree una primero."); execute(message); // Llenar mensaje y enviar.
                                                message.setText("\uD83E\uDD7A"); execute(message); // Llenar mensaje y enviar respuesta.
                                                message.setText(menu[0]); execute(message); // Llenar mensaje y enviar respuesta.
                                                message.setText(menu[1]); execute(message); // Llenar mensaje y enviar respuesta.
                                                estadoProceso.put(userId, null);
                                                estadoOpciones.put(userId, null);
                                            } else {
                                                message.setText("Sus cuentas\n"+cuentas+"Elija una de sus cuentas:"); execute(message); // Llenar mensaje y enviar.
                                                estadoProceso.put(userId, 1);
                                            }
                                            break;
                                        }
                                        case 1: {
                                            int n = banco.buscarCliente(userId).getCuentas().size();
                                            int nroCuenta = Integer.parseInt(update.getMessage().getText());
                                            if (nroCuenta > n || nroCuenta <= 0){
                                                message.setText("Cuenta inexistente."); execute(message); // Llenar mensaje y enviar.
                                                message.setText("\uD83D\uDE14"); execute(message); // Llenar mensaje y enviar.
                                                message.setText(menu[0]); execute(message); // Llenar mensaje y enviar respuesta.
                                                message.setText(menu[1]); execute(message); // Llenar mensaje y enviar respuesta.
                                                estadoProceso.put(userId, null);
                                                estadoOpciones.put(userId, null);
                                            } else {
                                                numeroCuenta.put(userId, nroCuenta);
                                                message.setText("\uD83D\uDCB8"); execute(message); // Llenar mensaje y enviar respuesta.
                                                message.setText("Ingrese el monto a depositar:"); execute(message); // Llenar mensaje y enviar respuesta.
                                                estadoProceso.put(userId, 2);
                                            }
                                            break;
                                        }
                                        case 2: {
                                            int monto = Integer.parseInt(update.getMessage().getText());
                                            Integer numC = (Integer) numeroCuenta.get(userId);
                                            Cuenta count = banco.buscarCliente(userId).buscarCuentaPorPos(numC);
                                            if (count.depositar(monto)){
                                                message.setText("Se ha realizado el deposito correctamente."); execute(message); // Llenar mensaje y enviar respuesta.
                                                message.setText("\uD83D\uDE09"); execute(message); // Llenar mensaje y enviar respuesta.
                                            } else {
                                                message.setText("Se ha producido un error al realizar el deposito, inténtelo nuevamente."); execute(message); // Llenar mensaje y enviar respuesta.
                                                if (monto == 0){
                                                    message.setText("El monto a depositar no puede ser cero."); execute(message); // Llenar mensaje y enviar respuesta.
                                                    message.setText("\uD83D\uDCA5"); execute(message); // Llenar mensaje y enviar respuesta.
                                                } else {
                                                    message.setText("El monto a depositar no puede ser negativo."); execute(message); // Llenar mensaje y enviar respuesta.
                                                    message.setText("\uD83D\uDCA5"); execute(message); // Llenar mensaje y enviar respuesta.
                                                }
                                            }
                                            numeroCuenta.put(userId, null);
                                            estadoProceso.put(userId, null);
                                            estadoOpciones.put(userId, null);
                                            message.setText(menu[0]); execute(message); // Llenar mensaje y enviar respuesta.
                                            message.setText(menu[1]); execute(message); // Llenar mensaje y enviar respuesta.
                                            break; // Terminar.
                                        }
                                    }
                                    break;
                                }
                                case 4: {
                                    Integer process = (Integer) estadoProceso.get(userId); // Obtenemos el estado del user ID.
                                    if (process == null) { // Verificamos si es la primera vez del usuario.
                                        process = 0;
                                        estadoProceso.put(userId, process); // Ingresamos al usuario al Map.
                                    }
                                    switch (process) {
                                        case 0: {
                                            message.setText("\uD83D\uDC47"); execute(message); // Llenar mensaje y enviar.
                                            message.setText("Seleccione la moneda:\n1. Dólares\n2. Bolivianos"); execute(message); // Llenar mensaje y enviar.
                                            estadoProceso.put(userId, 1);
                                            break;
                                        }
                                        case 1: {
                                            numCuenta++;
                                            Integer numC = (Integer) numeroCuenta.get(userId); // Obtenemos el estado del user ID.
                                            if (numC == null) { // Verificamos si es la primera vez del usuario.
                                                numC = numCuenta;
                                                numeroCuenta.put(userId, numC); // Ingresamos al usuario al Map.
                                            }
                                            int money = Integer.parseInt(update.getMessage().getText());
                                            switch (money){
                                                case 1: {
                                                    Cuenta cuenta = new Cuenta("Dólares", 0, String.valueOf(numC));
                                                    banco.buscarCliente(userId).agregarCuenta(cuenta);
                                                    message.setText("Seleccione el tipo de cuenta:\n1. Cuenta de Ahorros\n2. Cuenta Corriente"); execute(message); // Llenar mensaje y enviar.
                                                    message.setText("\uD83D\uDC46"); execute(message); // Llenar mensaje y enviar.
                                                    estadoProceso.put(userId, 2);
                                                    break;
                                                }
                                                case 2: {
                                                    Cuenta cuenta = new Cuenta("Bolivianos", 0, String.valueOf(numCuenta));
                                                    banco.buscarCliente(userId).agregarCuenta(cuenta);
                                                    message.setText("Seleccione el tipo de cuenta:\n1. Cuenta de Ahorros\n2. Cuenta Corriente"); execute(message); // Llenar mensaje y enviar.
                                                    message.setText("\uD83D\uDC46"); execute(message); // Llenar mensaje y enviar.
                                                    estadoProceso.put(userId, 2);
                                                    break;
                                                }
                                                default: {
                                                    message.setText("Opción de moneda no válida."); execute(message); // Llenar mensaje y enviar.
                                                    message.setText("\uD83D\uDE14"); execute(message); // Llenar mensaje y enviar.
                                                    message.setText(menu[0]); execute(message); // Llenar mensaje y enviar respuesta.
                                                    message.setText(menu[1]); execute(message); // Llenar mensaje y enviar respuesta.
                                                    numeroCuenta.put(userId, null);
                                                    estadoProceso.put(userId, null);
                                                    estadoOpciones.put(userId, null);
                                                    break;
                                                }
                                            }
                                            break; // Terminar.
                                        }
                                        case 2: {
                                            Integer num = (Integer) numeroCuenta.get(userId);
                                            Cuenta count = banco.buscarCliente(userId).buscarCuentaPorNro(String.valueOf(num));
                                            int caja = Integer.parseInt(update.getMessage().getText());
                                            switch (caja){
                                                case 1: {
                                                    count.setTipo("Cuenta de Ahorros");
                                                    message.setText("Se le ha creado una cuenta en "+count.getMoneda()+" con saldo cero, cuyo número es "+count.getNroCuenta()+", tipo "+count.getTipo());
                                                    execute(message); // Llenar mensaje y enviar.
                                                    message.setText("\uD83E\uDD20"); execute(message); // Llenar mensaje y enviar.
                                                    message.setText(menu[0]); execute(message); // Llenar mensaje y enviar respuesta.
                                                    message.setText(menu[1]); execute(message); // Llenar mensaje y enviar respuesta.
                                                    numeroCuenta.put(userId, null);
                                                    estadoProceso.put(userId, null);
                                                    estadoOpciones.put(userId, null);
                                                    break;
                                                }
                                                case 2: {
                                                    count.setTipo("Cuenta Corriente");
                                                    message.setText("Se le ha creado una cuenta en "+count.getMoneda()+" con saldo cero, cuyo número es "+count.getNroCuenta()+", tipo "+count.getTipo());
                                                    execute(message); // Llenar mensaje y enviar.
                                                    message.setText("\uD83E\uDD20"); execute(message); // Llenar mensaje y enviar.
                                                    message.setText(menu[0]); execute(message); // Llenar mensaje y enviar respuesta.
                                                    message.setText(menu[1]); execute(message); // Llenar mensaje y enviar respuesta.
                                                    numeroCuenta.put(userId, null);
                                                    estadoProceso.put(userId, null);
                                                    estadoOpciones.put(userId, null);
                                                    break;
                                                }
                                                default: {
                                                    message.setText("Opción de cuenta no válida."); execute(message); // Llenar mensaje y enviar.
                                                    message.setText("\uD83D\uDE14"); execute(message); // Llenar mensaje y enviar.
                                                    message.setText(menu[0]); execute(message); // Llenar mensaje y enviar respuesta.
                                                    message.setText(menu[1]); execute(message); // Llenar mensaje y enviar respuesta.
                                                    banco.buscarCliente(userId).eliminarCuentas();
                                                    numeroCuenta.put(userId, null);
                                                    estadoProceso.put(userId, null);
                                                    estadoOpciones.put(userId, null);
                                                    break;
                                                }
                                            }
                                            break; // Terminar.
                                        }
                                    }
                                    break; // Terminar.
                                }
                                case 5: {
                                    message.setText("Hasta pronto "+banco.buscarCliente(userId).getNombreCliente()+"."); execute(message); // Llenar mensaje y enviar.
                                    message.setText("\uD83D\uDC4B"); execute(message); // Llenar mensaje y enviar.
                                    message.setText("Hola de nuevo "+banco.buscarCliente(userId).getNombreCliente()); execute(message); // Llenar mensaje y enviar.
                                    message.setText("Solo por seguridad ¿cuál es tu PIN?"); execute(message); // Llenar mensaje y enviar.
                                    message.setText("\uD83D\uDD10"); execute(message); // Llenar mensaje y enviar respuesta.
                                    estadoCliente.put(userId, 3); // Actualizar mensaje.
                                    estadoOpciones.put(userId, null);
                                    break;
                                }
                                default: {
                                    message.setText("La opción seleccionada no existe."); execute(message); // Llenar mensaje y enviar.
                                    message.setText("\uD83D\uDE16"); execute(message); // Llenar mensaje y enviar.
                                    message.setText(menu[0]); execute(message); // Llenar mensaje y enviar respuesta.
                                    message.setText(menu[1]); execute(message); // Llenar mensaje y enviar respuesta.
                                    estadoOpciones.put(userId, null);
                                    break;
                                }
                            }
                        } else {
                            message.setText("La opción seleccionada no existe."); execute(message); // Llenar mensaje y enviar.
                            message.setText("\uD83D\uDE16"); execute(message); // Llenar mensaje y enviar.
                            message.setText(menu[0]); execute(message); // Llenar mensaje y enviar respuesta.
                            message.setText(menu[1]); execute(message); // Llenar mensaje y enviar respuesta.
                            banco.buscarCliente(userId).eliminarCuentas();
                            numeroCuenta.put(userId, null);
                            estadoOpciones.put(userId, null);
                            estadoProceso.put(userId, null);
                        }
                        break;
                    }
                }
            } catch(TelegramApiException e){
                e.printStackTrace();
            }
        }
    }

    public boolean comprobarNumero (String n){
        boolean flag = true;
        try{
            int aux = Integer.parseInt(n);
        }catch(NumberFormatException ex){
            System.out.println("Error");
            flag = false;
        }
        return flag;
    }
}
