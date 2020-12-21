package pojos;

import java.sql.Date;

public class Medico {
    private String nombre, apellidos, genero, domicilio, num_personal, num_cedula, contrasena, status;
    private int idMedico;
    private byte [] fotografia;
    private Date fecha_nacimiento;

    public Medico() {
    }

    public Medico(String nombre, String apellidos, String genero, String domicilio, String num_personal, String num_cedula, String contrasena, String status, int idMedico, byte[] fotografia, Date fecha_nacimiento) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.genero = genero;
        this.domicilio = domicilio;
        this.num_personal = num_personal;
        this.num_cedula = num_cedula;
        this.contrasena = contrasena;
        this.status = status;
        this.idMedico = idMedico;
        this.fotografia = fotografia;
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public Medico(String nombre, String apellidos, String genero, String domicilio, String num_personal, String num_cedula, String contrasena, int idMedico, Date fecha_nacimiento) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.genero = genero;
        this.domicilio = domicilio;
        this.num_personal = num_personal;
        this.num_cedula = num_cedula;
        this.contrasena = contrasena;
        this.idMedico = idMedico;
        this.fecha_nacimiento = fecha_nacimiento;
    }
    
    public Medico(String apellidos, String contrasena, String domicilio, Date fecha_nacimiento, String genero, int idMedico, String nombre, String num_cedula, String num_personal, String status) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.genero = genero;
        this.domicilio = domicilio;
        this.num_personal = num_personal;
        this.num_cedula = num_cedula;
        this.contrasena = contrasena;
        this.idMedico = idMedico;
        this.fecha_nacimiento = fecha_nacimiento;
        this.status = status;
    }

    public Medico(String nombre, String apellidos, Date fecha_nacimiento, String genero, String domicilio, String num_personal, String num_cedula, String contrasena, String status) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fecha_nacimiento = fecha_nacimiento;
        this.genero = genero;
        this.domicilio = domicilio;
        this.num_personal = num_personal;
        this.num_cedula = num_cedula;
        this.contrasena = contrasena;
        this.status = status;
    }
    
    public Medico(String nombre, String apellidos, String genero, String domicilio, String num_personal, String num_cedula, String contrasena, String status) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.genero = genero;
        this.domicilio = domicilio;
        this.num_personal = num_personal;
        this.num_cedula = num_cedula;
        this.contrasena = contrasena;
        this.status = status;
    }
    
    
    
    public Medico(int idMedico, String status) {
        this.status = status;
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

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getNum_personal() {
        return num_personal;
    }

    public void setNum_personal(String num_personal) {
        this.num_personal = num_personal;
    }

    public String getNum_cedula() {
        return num_cedula;
    }

    public void setNum_cedula(String num_cedula) {
        this.num_cedula = num_cedula;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public byte[] getFotografia() {
        return fotografia;
    }

    public void setFotografia(byte[] fotografia) {
        this.fotografia = fotografia;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    @Override
    public String toString() {
        return this.nombre + " " + this.apellidos;
    }
    
    
}
