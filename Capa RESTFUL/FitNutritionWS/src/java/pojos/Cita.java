/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author dany
 */
public class Cita {
    private int idCita, idPaciente;
    private String observaciones;
    private Date fecha;
    private Time hora;

    public Cita(){
    }
    
    public Cita(int idCita, int idPaciente, String observaciones, Date fecha, Time hora) {
        this.idCita = idCita;
        this.idPaciente = idPaciente;
        this.observaciones = observaciones;
        this.fecha = fecha;
        this.hora = hora;
    }

    public Cita(int idPaciente, String observaciones, Date fecha, Time hora) {
        this.idPaciente = idPaciente;
        this.observaciones = observaciones;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }
    
    
    
}
