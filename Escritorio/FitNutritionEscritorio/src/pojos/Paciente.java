/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

import java.sql.Date;

/**
 *
 * @author dany
 */
public class Paciente {
    private int idPaciente, peso, estatura,idMedico;
    private String nombre, apellidos, talla, genero, email, telefono, domicilio, usuario, contrasena, status;
    private Date fecha_nacimiento;
    private byte[] fotografia;

    public Paciente() {
    }

    public Paciente(int peso, int estatura, int idMedico, String nombre, String apellidos, String talla, String genero, String email, String telefono, String domicilio, String usuario, String contrasena, String status, Date fecha_nacimiento) {
        this.peso = peso;
        this.estatura = estatura;
        this.idMedico = idMedico;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.talla = talla;
        this.genero = genero;
        this.email = email;
        this.telefono = telefono;
        this.domicilio = domicilio;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.status = status;
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public Paciente(int idPaciente, int peso, int estatura, int idMedico, String nombre, String apellidos, String talla, String genero, String email, String telefono, String domicilio, String usuario, String contrasena, String status, Date fecha_nacimiento, byte[] fotografia) {
        this.idPaciente = idPaciente;
        this.peso = peso;
        this.estatura = estatura;
        this.idMedico = idMedico;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.talla = talla;
        this.genero = genero;
        this.email = email;
        this.telefono = telefono;
        this.domicilio = domicilio;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.status = status;
        this.fecha_nacimiento = fecha_nacimiento;
        this.fotografia = fotografia;
    }

    public Paciente(int idPaciente, int peso, int estatura, int idMedico, String nombre, String apellidos, String talla, String genero, String email, String telefono, String domicilio, String usuario, String contrasena, Date fecha_nacimiento) {
        this.idPaciente = idPaciente;
        this.peso = peso;
        this.estatura = estatura;
        this.idMedico = idMedico;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.talla = talla;
        this.genero = genero;
        this.email = email;
        this.telefono = telefono;
        this.domicilio = domicilio;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.fecha_nacimiento = fecha_nacimiento;
        this.fotografia = fotografia;
    }
    
    

    public Paciente(int idPaciente, String status) {
        this.idPaciente = idPaciente;
        this.status = status;
    }
    
    

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getEstatura() {
        return estatura;
    }

    public void setEstatura(int estatura) {
        this.estatura = estatura;
    }

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public byte[] getFotografia() {
        return fotografia;
    }

    public void setFotografia(byte[] fotografia) {
        this.fotografia = fotografia;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
    
}
