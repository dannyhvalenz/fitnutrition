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
import pojos.Mensaje;

/**
 * REST Web Service
 * http://localhost:8080/FitNutritionWS/ws/alimentos/
 */
@Path("alimentos")
public class AlimentoWS {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CatalogoWS
     */
    public AlimentoWS() {
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
    
    /**
     * Registrar alimento
     * @param nombre
     * @param calorias_por_porcion
     * @param porcion
     * @return 
     */
    @Path("registrarAlimento")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje crearAlimentoBD(@FormParam("nombre") String nombre, @FormParam("calorias_por_porcion") Integer calorias_por_porcion, @FormParam("porcion") String porcion){
        Mensaje respuesta = new Mensaje();        
        Alimento alimento = new Alimento(nombre, calorias_por_porcion, porcion);
        SqlSession conn = MyBatisUtil.getSession();
        if(conn != null){
            try {
                int resultado = conn.insert("Alimento.registrarAlimento", alimento);
                conn.commit();                
                if(resultado > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Alimento agregado con Exito " + resultado);
                    conn.clearCache();
                }else{
                    respuesta.setError(true);
                    respuesta.setMensaje("No se pudo agregar el alimento");
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
    
    
    /**
     * Actualizar alimento
     * @param idAlimento
     * @param nombre
     * @param calorias_por_porcion
     * @param porcion
     * @return 
     */
    @Path("actualizarAlimento")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje editarAlimentoBD(@FormParam("idAlimento") Integer idAlimento, @FormParam("nombre") String nombre, @FormParam("calorias_por_porcion") Integer calorias_por_porcion, @FormParam("porcion") String porcion){
        Mensaje respuesta = new Mensaje();        
        Alimento alimento = new Alimento(idAlimento,nombre, calorias_por_porcion, porcion);
        SqlSession conn = MyBatisUtil.getSession();
        if(conn != null){
            try {
                int resultado = conn.update("Alimento.actualizarAlimento", alimento);
                conn.commit();
                if(resultado > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Alimento actualizado con éxito... ");
                    conn.clearCache();
                }else{
                    respuesta.setError(true);
                    respuesta.setMensaje("El alimento no pudo ser actualzado ");
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
    
    /**
     * Eliminar alimento
     * @param idAlimento
     * @return 
     */
    @Path("eliminarAlimento")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje eliminarAlimentoBD(@FormParam("idAlimento") Integer idAlimento){
        Mensaje respuesta = new Mensaje();
        SqlSession conn = MyBatisUtil.getSession();
        if(conn != null){
            try {
                int resultado = conn.delete("Alimento.eliminarAlimento", idAlimento);
                conn.commit();
                if(resultado > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Alimento eliminado con éxito...");
                    conn.clearCache();
                }else{
                    respuesta.setError(true);
                    respuesta.setMensaje("El aliemento no pudo ser eliminado");
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
    @Path("getAllAlimentos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Alimento> getAlimentos(){
        List<Alimento> alimentos = null;
        SqlSession conn = MyBatisUtil.getSession();
        
        if(conn != null){
            try {
                alimentos = conn.selectList("Alimento.getAllAlimentos");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return alimentos;
    }
    
    @Path("getAlimentoByID")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Alimento getAlimentoByID(@FormParam("idAlimento") Integer idAlimento){
        Mensaje respuesta = new Mensaje();
        SqlSession conn = MyBatisUtil.getSession();
        Alimento alimento =  new Alimento();
        if(conn != null){
            try {
                alimento = conn.selectOne("Alimento.getAlimentoByID", idAlimento);
                //conn.commit();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return alimento;
    }
}
