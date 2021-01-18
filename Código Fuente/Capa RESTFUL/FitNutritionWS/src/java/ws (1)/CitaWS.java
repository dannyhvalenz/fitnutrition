/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import java.sql.Date;
import java.sql.Time;
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

import pojos.Cita;
import pojos.Mensaje;

/**
 * REST Web Service
 * http://localhost:8080/FitNutritionWS/ws/citas/
 */
@Path("citas")
public class CitaWS {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CatalogoWS
     */
    public CitaWS() {
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
    
    @Path("registrarCita")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje crearCita(@FormParam("fecha") Date fecha, @FormParam("hora") String hora, @FormParam("idPaciente") Integer idPaciente, @FormParam("observaciones") String observaciones){
        Mensaje respuesta = new Mensaje();        
        Cita cita = new Cita(idPaciente, observaciones, fecha, hora);
        SqlSession conn = MyBatisUtil.getSession();
        if(conn != null){
            try {
                int resultado = conn.insert("Cita.registrarCita", cita);
                conn.commit();                
                if(resultado > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Cita agregada con Exito " + resultado);
                    conn.clearCache();
                }else{
                    respuesta.setError(true);
                    respuesta.setMensaje("No se pudo agregar la cita");
                }
            }catch(Exception e){
                respuesta.setError(true);
                respuesta.setMensaje(e.getMessage());
            }finally{
                conn.close();
            }
        }else{
            respuesta.setError(true);
            respuesta.setMensaje("No se pudo conectar con la Base de datos...");
        }
        return respuesta;
    }
    
    @Path("actualizarCita")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje actualizarCita(@FormParam("idCita") Integer idCita, @FormParam("fecha") Date fecha, @FormParam("hora") String hora, @FormParam("idPaciente") Integer idPaciente, @FormParam("observaciones") String observaciones){
        Mensaje respuesta = new Mensaje();        
        Cita cita = new Cita(idCita, idPaciente, observaciones, fecha, hora);
        SqlSession conn = MyBatisUtil.getSession();
        if(conn != null){
            try {
                int resultado = conn.update("Cita.actualizarCita", cita);
                conn.commit();
                if(resultado > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Cita actualizada con éxito... ");
                    conn.clearCache();
                }else{
                    respuesta.setError(true);
                    respuesta.setMensaje("La cita no pudo ser actualizada ");
                }                
            }catch (Exception e){
                respuesta.setError(true);
                respuesta.setMensaje(e.getMessage());
            }finally{
                conn.close();
            }
        }else{
            respuesta.setError(true);
            respuesta.setMensaje("No hay conexión con la BD");
        }
        
        return respuesta;
    }
    
    @Path("eliminarCita")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje eliminarCita(@FormParam("idCita") Integer idCita){
        Mensaje respuesta = new Mensaje();
        SqlSession conn = MyBatisUtil.getSession();
        if(conn != null){
            try {
                int resultado = conn.delete("Cita.eliminarCita", idCita);
                conn.commit();
                if(resultado > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Cita eliminada con éxito...");
                    conn.clearCache();
                }else{
                    respuesta.setError(true);
                    respuesta.setMensaje("La cita no pudo ser eliminada");
                }
            }catch(Exception e){
                respuesta.setError(true);
                respuesta.setMensaje(e.getMessage());
            }finally{
                conn.close();
            }
        }else{
            respuesta.setError(true);
            respuesta.setMensaje("No hay conexión con la BD");
        }
        
        return respuesta;
    }
    
    /**
     * Listar todos los alimentos
     * @return 
     */
    @Path("getAllCitas")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cita> getCitas(){
        List<Cita> citas = null;
        SqlSession conn = MyBatisUtil.getSession();
        
        if(conn != null){
            try {
                citas = conn.selectList("Cita.getAllCitas");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return citas;
    }
    
    @Path("getAllCitasAsc")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cita> getCitasAsc(){
        List<Cita> citas = null;
        SqlSession conn = MyBatisUtil.getSession();
        
        if(conn != null){
            try {
                citas = conn.selectList("Cita.getAllCitasAsc");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return citas;
    }
    
    @Path("getAllCitasDesc")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cita> getCitasDesc(){
        List<Cita> citas = null;
        SqlSession conn = MyBatisUtil.getSession();
        
        if(conn != null){
            try {
                citas = conn.selectList("Cita.getAllCitasDesc");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return citas;
    }
    
    @Path("getCitaByID")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Cita getCitaByID(@FormParam("idCita") Integer idCita){
        SqlSession conn = MyBatisUtil.getSession();
        Cita cita =  new Cita();
        if(conn != null){
            try {
                cita = conn.selectOne("Cita.getCitaByID", idCita);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cita;
    }
    
    @Path("getUltimaCitaByPaciente")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Cita getUltimaCitaByPaciente(@FormParam("idPaciente") Integer idPaciente){
        SqlSession conn = MyBatisUtil.getSession();
        Cita cita =  new Cita();
        if(conn != null){
            try {
                cita = conn.selectOne("Cita.getUltimaCitaByPaciente", idPaciente);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cita;
    }
}
