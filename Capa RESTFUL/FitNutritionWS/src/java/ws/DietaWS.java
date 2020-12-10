/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

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
import pojos.Consulta;
import pojos.Dieta;
import pojos.Mensaje;

/**
 * REST Web Service
 * http://localhost:8080/FitNutritionWS/ws/dietas/
 */
@Path("dietas")
public class DietaWS {

    @Context
    private UriInfo context;

    public DietaWS() {
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
    
    @Path("getAllDietas")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dieta> getAllDietas(){
        List<Dieta> dietas = null;
        SqlSession conn = MyBatisUtil.getSession();
        
        if(conn != null){
            try {
                dietas = conn.selectList("Dieta.getAllDietas");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dietas;
    }
    
    @Path("getDietaByID")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Dieta getDietaByID(@FormParam("idDieta") Integer idDieta){
        SqlSession conn = MyBatisUtil.getSession();
        Dieta dieta =  new Dieta();
        if(conn != null){
            try {
                dieta = conn.selectOne("Dieta.getDietaByID", idDieta);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dieta;
    }
    
    
    @Path("registrarDieta")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje registrarDieta(@FormParam("nombre") String nombre, @FormParam("idAlimento") Integer idAlimento, @FormParam("cantidad") String cantidad, @FormParam("horaDia") String horaDia, @FormParam("caloriasDieta") Float caloriasDieta, @FormParam("observaciones") String observaciones){
        Mensaje respuesta = new Mensaje();        
        Dieta dieta = new Dieta(idAlimento, nombre, observaciones, cantidad, horaDia, caloriasDieta);
        SqlSession conn = MyBatisUtil.getSession();
        if(conn != null){
            try {
                int resultado = conn.insert("Dieta.registrarDieta", dieta);
                conn.commit();                
                if(resultado > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Dieta agregada con Exito " + resultado);
                    conn.clearCache();
                }else{
                    respuesta.setError(true);
                    respuesta.setMensaje("No se pudo agregar la dieta");
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
    
    @Path("actualizarDieta")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje actualizarDieta(@FormParam("idDieta") Integer idDieta, @FormParam("idAlimento") Integer idAlimento, @FormParam("nombre") String nombre, @FormParam("observaciones") String observaciones, @FormParam("cantidad") String cantidad, @FormParam("horaDia") String horaDia, @FormParam("caloriasDieta") Float caloriasDieta){
        Mensaje respuesta = new Mensaje();        
        Dieta dieta = new Dieta(idDieta, idAlimento, nombre, observaciones, cantidad, horaDia, caloriasDieta);
        SqlSession conn = MyBatisUtil.getSession();
        if(conn != null){
            try {
                int resultado = conn.update("Dieta.actualizarDieta", dieta);
                conn.commit();
                if(resultado > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Dieta actualizado con éxito... ");
                    conn.clearCache();
                }else{
                    respuesta.setError(true);
                    respuesta.setMensaje("El dieta no pudo ser actualzado ");
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
    
    @Path("eliminarDieta")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje eliminarDieta(@FormParam("idDieta") Integer idDieta){
        Mensaje respuesta = new Mensaje();
        SqlSession conn = MyBatisUtil.getSession();
        if(conn != null){
            try {
                int resultado = conn.delete("Dieta.eliminarDieta", idDieta);
                conn.commit();
                if(resultado > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Dieta eliminada con éxito...");
                    conn.clearCache();
                }else{
                    respuesta.setError(true);
                    respuesta.setMensaje("La dieta no pudo ser eliminada");
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
}
