package beans;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import modelo.cliente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named ("cajeroBean")
@SessionScoped
public class CajeroBean implements Serializable {
    private List<cliente> clientes = new ArrayList<>();
    private String cuentaIngresada;
    private String pinIngresado;
    private double monto;
    private cliente clienteActual;

    public CajeroBean() {
        clientes.add(new cliente("1001", 500.00, "1234"));
        clientes.add(new cliente("1002", 1000.00, "2345"));
        clientes.add(new cliente("1003", 750.00, "3456"));
        clientes.add(new cliente("1004", 1200.00, "4567"));
        clientes.add(new cliente("1005", 300.00, "5678"));
    }

    public String validarCliente() {
        for (cliente c : clientes) {
            if (c.getNumeroCuenta().equals(cuentaIngresada) && c.getPin().equals(pinIngresado)) {
                clienteActual = c;
                return "transaccion";
            }
        }
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "PIN o cuenta inválida", ""));
        return null;
    }

    public void retirar() {
        if (!validarPin()) return;

        if (monto <= 0) {
            mostrarMensaje("Monto inválido", FacesMessage.SEVERITY_ERROR);
            return;
        }

        if (clienteActual.getSaldo() >= monto) {
            clienteActual.setSaldo(clienteActual.getSaldo() - monto);
            mostrarMensaje("Retiro exitoso. Nuevo saldo: $" + clienteActual.getSaldo(), FacesMessage.SEVERITY_INFO);
        } else {
            mostrarMensaje("Saldo insuficiente", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void depositar() {
        if (!validarPin()) return;

        if (monto <= 0) {
            mostrarMensaje("Monto inválido", FacesMessage.SEVERITY_ERROR);
            return;
        }

        clienteActual.setSaldo(clienteActual.getSaldo() + monto);
        mostrarMensaje("Depósito exitoso. Nuevo saldo: $" + clienteActual.getSaldo(), FacesMessage.SEVERITY_INFO);
    }

    private boolean validarPin() {
        for (cliente c : clientes) {
            if (c.getNumeroCuenta().equals(cuentaIngresada) && c.getPin().equals(pinIngresado)) {
                clienteActual = c;
                return true;
            }
        }
        mostrarMensaje("PIN o cuenta inválida", FacesMessage.SEVERITY_ERROR);
        return false;
    }

    private void mostrarMensaje(String mensaje, FacesMessage.Severity tipo) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(tipo, mensaje, ""));
    }

    // Getters y Setters
    public String getCuentaIngresada() { return cuentaIngresada; }
    public void setCuentaIngresada(String cuentaIngresada) { this.cuentaIngresada = cuentaIngresada; }
    public String getPinIngresado() { return pinIngresado; }
    public void setPinIngresado(String pinIngresado) { this.pinIngresado = pinIngresado; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public cliente getClienteActual() { return clienteActual; }
}
