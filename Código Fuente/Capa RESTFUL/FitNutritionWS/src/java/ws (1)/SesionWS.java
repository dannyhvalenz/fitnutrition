/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import java.util.HashMap;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

import pojos.Alimento;
import pojos.Medico;
import pojos.Mensaje;
import pojos.Paciente;

/**
 * REST Web Service
 * http://localhost:8080/FitNutritionWS/ws/sesion/
 */
@Path("sesion")
public class SesionWS {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CatalogoWS
     */
    public SesionWS() {
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(){
        throw new UnsupportedOperationException();
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content){
    }
    @Path("login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje login(@FormParam("usuario") String usuario, @FormParam("contrasena") String contrasena){
        Mensaje respuesta = new Mensaje();
        SqlSession conn = MyBatisUtil.getSession();
        Paciente paciente =  new Paciente();
        HashMap<String,Object> paramPaciente = new HashMap<>();
        paramPaciente.put("usuario", usuario);
        paramPaciente.put("contrasena", contrasena);
        if(conn != null){
            try {
                paciente = conn.selectOne("Sesion.loginPaciente", paramPaciente);
                conn.commit();
                if(paciente != null && paciente.getIdPaciente() > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("paciente = " + paciente.getIdPaciente());
                } else {
                    Medico medico =  new Medico();
                    HashMap<String,Object> paramMedico = new HashMap<>();
                    paramMedico.put("usuario", usuario);
                    paramMedico.put("contrasena", contrasena);
                    medico = conn.selectOne("Sesion.loginMedico", paramMedico);
                    conn.commit();
                    if(medico != null && medico.getIdMedico() > 0){
                        respuesta.setError(false);
                        respuesta.setMensaje("medico = " + medico.getIdMedico());
                    } else{
                        respuesta.setError(true);
                        respuesta.setMensaje("Las credenciales son incorrectas...");
                    }
                }
            } catch (Exception e) {
                respuesta.setError(true);
                respuesta.setMensaje(e.getMessage());
            }
        }else{
            respuesta.setError(true);
            respuesta.setMensaje("No se puede conectar con la base de datos...");
        }
        return respuesta;
    }
}
