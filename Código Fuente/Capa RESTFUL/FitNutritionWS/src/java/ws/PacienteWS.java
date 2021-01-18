package ws;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojos.Medico;

import pojos.Mensaje;
import pojos.Paciente;

/**
 * REST Web Service
 * http://localhost:8080/FitNutritionWS/ws/pacientes/
 */
@Path("pacientes")
public class PacienteWS {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CatalogoWS
     */
    public PacienteWS() {
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
    
    
    @Path("registrarPaciente")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje registrarPaciente(@FormParam("peso") Float peso, @FormParam("estatura") Integer estatura, 
            @FormParam("idMedico") Integer idMedico, @FormParam("nombre") String nombre, @FormParam("apellidos") String apellidos, 
            @FormParam("talla") String talla, @FormParam("genero") String genero, @FormParam("email") String email, 
            @FormParam("telefono") String telefono, @FormParam("domicilio") String domicilio, @FormParam("usuario") String usuario, 
            @FormParam("contrasena") String contrasena, @FormParam("fecha_nacimiento") String fecha_nacimiento, @FormParam("status") String estatus){
        Mensaje respuesta = new Mensaje();        
        Paciente paciente = new Paciente(peso, estatura, idMedico, nombre, apellidos, talla, genero, email, telefono, domicilio, usuario, contrasena, estatus, fecha_nacimiento);
        SqlSession conn = MyBatisUtil.getSession();
        if(conn != null){
            try {
                int resultado = conn.insert("Paciente.registrarPaciente", paciente);
                conn.commit();
                if(resultado > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Paciente agregado con éxito");
                    List<Paciente> list = null;
                    list = conn.selectList("Paciente.getAllPacientes");
                    int i = list.size();
                    Paciente paci = list.get(i-1);
                    respuesta.setMensaje("Registro agregado con Exito " + resultado + ", " + paci.getIdPaciente());
                    conn.clearCache();
                }else{
                    respuesta.setError(true);
                    respuesta.setMensaje("No se pudo agregar al paciente");
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
    
    
    
    @Path("actualizarPaciente")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje editarPaciente(@FormParam("idPaciente") Integer idPaciente, @FormParam("peso") Float peso, @FormParam("estatura") Integer estatura, 
            @FormParam("idMedico") Integer idMedico, @FormParam("nombre") String nombre, @FormParam("apellidos") String apellidos, 
            @FormParam("talla") String talla, @FormParam("genero") String genero, @FormParam("email") String email, 
            @FormParam("telefono") String telefono, @FormParam("domicilio") String domicilio, @FormParam("usuario") String usuario, 
            @FormParam("contrasena") String contrasena, @FormParam("fecha_nacimiento") String fecha_nacimiento, @FormParam("status") String estatus){
        Mensaje respuesta = new Mensaje();        
        Paciente paciente = new Paciente(idPaciente, peso, estatura, idMedico, nombre, apellidos, talla, genero, email, telefono, domicilio, usuario, contrasena, estatus, fecha_nacimiento);
        SqlSession conn = MyBatisUtil.getSession();
        if(conn != null){
            try {
                int resultado = conn.update("Paciente.actualizarPaciente", paciente);
                conn.commit();
                if(resultado > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Paciente actualizado con éxito... ");
                    conn.clearCache();
                }else{
                    respuesta.setError(true);
                    respuesta.setMensaje("El paciente no pudo ser actualizado ");
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
     * Cambiar estatus del paciente a inactivo
     * @param idPaciente
     * @param status
     * @return 
     */
    @Path("eliminarPaciente")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje eliminarPaciente (@FormParam("idPaciente") Integer idPaciente){
        Mensaje respuesta = new Mensaje();        
        Paciente paciente = new Paciente(idPaciente, "Inactivo");
        SqlSession conn = MyBatisUtil.getSession();
        if(conn != null){
            try {
                int resultado = conn.update("Paciente.eliminarPaciente", paciente);
                conn.commit();
                if(resultado > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Paciente dado de baja con éxito... ");
                    conn.clearCache();
                }else{
                    respuesta.setError(true);
                    respuesta.setMensaje("El paciente no se pudo dar de baja");
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
     * Listar todos los pacientes
     * @return pacientes
     */
    @Path("getAllPacientes")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Paciente> getAllPacientes(){
        List<Paciente> pacientes = null;
        SqlSession conn = MyBatisUtil.getSession();
        
        if(conn != null){
            try {
                pacientes = conn.selectList("Paciente.getAllPacientes");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pacientes;
    }
    
    @Path("getAllPacientesActivos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Paciente> getAllPacientesActivos(){
        List<Paciente> pacientes = null;
        SqlSession conn = MyBatisUtil.getSession();
        
        if(conn != null){
            try {
                pacientes = conn.selectList("Paciente.getAllPacientesActivos");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pacientes;
    }
    
    /**
     * Recuperar informacion de un paciente en especifico
     * @param idPaciente
     * @return 
     */
    @Path("getPacienteByID")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Paciente getPacienteByID(@FormParam("idPaciente") Integer idPaciente){
        Mensaje respuesta = new Mensaje();
        SqlSession conn = MyBatisUtil.getSession();
        Paciente paciente =  new Paciente();
        if(conn != null){
            try {
                paciente = conn.selectOne("Paciente.getPacienteByID", idPaciente);
                //conn.commit();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return paciente;
    }
    
    @Path("subirFotografia/{idPaciente}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje subirFotografiaPaciente(byte[] fotografia, @PathParam("idPaciente") Integer idPaciente){
        SqlSession conn = MyBatisUtil.getSession();
        Paciente paciente = new Paciente();
        paciente.setIdPaciente(idPaciente);
        paciente.setFotografia(fotografia);
        Mensaje msj = new Mensaje();
        try {
            int resultado = conn.update("Paciente.enviarFotografiaPaciente", paciente);
            conn.commit();
            if(resultado > 0){
                msj.setError(false);
                msj.setMensaje("Imagen de paciente subida con éxito...");
            }else{
                msj.setError(true);
                msj.setMensaje("No se pudo guardar la imagen de paciente...");
            }
        } catch (Exception e) {
            msj.setError(true);
            msj.setMensaje(e.getMessage());
        } finally{
            conn.close();
        }
        
        return msj;
    }
    
    
    @Path("getFotografia/{idPaciente}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje getFotografiaPaciente(@PathParam("idPaciente") Integer idPaciente){
        Mensaje msj = new Mensaje();
        SqlSession conn = MyBatisUtil.getSession();
        String PATH = "D:\\Documentos\\UV\\7moSemestre\\Integracion_Soluciones\\Imagenes\\"+idPaciente+".png";
        if(conn != null){
            try{
                Paciente paciente = conn.selectOne("Paciente.getFotografiaPaciente", idPaciente);
                System.out.println("Paciente: " + paciente.getNombre() + ", Recuperando IMAGEN");
                boolean isSave = escribeImagenPaciente(PATH, paciente.getFotografia());
                if(isSave){
                   msj.setError(false);
                   msj.setMensaje(PATH);
                }else{
                   msj.setError(true);
                   msj.setMensaje("No se pudo obtener la imagen...");
                }
            }catch(Exception e){
                
            }
        }else{
            msj.setError(true);
            msj.setMensaje("Sin conexion a la BD");
        }
        return msj;
    }
    
    private boolean escribeImagenPaciente(String path, byte[] bytes){
        InputStream in = new ByteArrayInputStream(bytes);
        try {
            BufferedImage buffImage = ImageIO.read(in);
            ImageIO.write(buffImage, "png", new File(path));
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return false;
    }
}
