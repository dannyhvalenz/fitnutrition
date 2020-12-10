package ws;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojos.Consulta;
import pojos.Dieta;
import pojos.Mensaje;

@Path("consultas")
public class ConsultaWS {
    @Context
    private UriInfo context;

    public ConsultaWS() {
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
    
    @Path("getAllConsultas")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Consulta> getAllConsultas(){
        List<Consulta> consultas = null;
        SqlSession conn = MyBatisUtil.getSession();
        
        if(conn != null){
            try {
                consultas = conn.selectList("Consulta.getAllConsultas");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return consultas;
    }
    
    @Path("getConsultaByID")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Consulta getConsultaByID(@FormParam("idConsulta") Integer idConsulta){
        SqlSession conn = MyBatisUtil.getSession();
        Consulta consulta =  new Consulta();
        if(conn != null){
            try {
                consulta = conn.selectOne("Consulta.getConsultaByID", idConsulta);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return consulta;
    }
    
    @Path("registrarConsulta")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje registrarConsulta(@FormParam("idPaciente") Integer idPaciente, @FormParam("idDieta") Integer idDieta, @FormParam("imc") Integer imc, @FormParam("talla") String talla, @FormParam("peso") float peso, @FormParam("observaciones") String observaciones){
        Mensaje respuesta = new Mensaje();        
        Consulta consulta = new Consulta(idPaciente, idDieta, peso, imc, observaciones, talla);
        SqlSession conn = MyBatisUtil.getSession();
        if(conn != null){
            try {
                int resultado = conn.insert("Consulta.registrarConsulta", consulta);
                conn.commit();                
                if(resultado > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Consulta agregada con éxito " + resultado);
                    conn.clearCache();
                }else{
                    respuesta.setError(true);
                    respuesta.setMensaje("No se pudo agregar la consulta");
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
    
    @Path("actualizarConsulta")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje actualizarConsulta(@FormParam("idConsulta") Integer idConsulta, @FormParam("idPaciente") Integer idPaciente, @FormParam("idDieta") Integer idDieta, @FormParam("imc") Integer imc, @FormParam("talla") String talla, @FormParam("peso") float peso, @FormParam("observaciones") String observaciones){
        Mensaje respuesta = new Mensaje();        
        Consulta consulta = new Consulta(idConsulta, idPaciente, idDieta, peso, imc, observaciones, talla);
        SqlSession conn = MyBatisUtil.getSession();
        if(conn != null){
            try {
                int resultado = conn.update("Consulta.actualizarConsulta", consulta);
                conn.commit();
                respuesta.setError(false);
                respuesta.setMensaje("Consulta actualizada con éxito... ");
                if(resultado > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Consulta actualizada con éxito... ");
                    conn.clearCache();
                }else{
                    respuesta.setError(true);
                    respuesta.setMensaje("No se actualizó la consulta");
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
    
    @Path("eliminarConsulta")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje eliminarConsulta(@FormParam("idConsulta") Integer idConsulta){
        Mensaje respuesta = new Mensaje();
        SqlSession conn = MyBatisUtil.getSession();
        if(conn != null){
            try {
                int resultado = conn.delete("Consulta.eliminarConsulta", idConsulta);
                conn.commit();
                if(resultado > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Consulta eliminada con éxito...");
                    conn.clearCache();
                }else{
                    respuesta.setError(true);
                    respuesta.setMensaje("La consulta no pudo ser eliminada");
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
