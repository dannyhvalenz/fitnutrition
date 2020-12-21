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
@Path("medicos")
public class MedicoWS {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CatalogoWS
     */
    public MedicoWS() {
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
    
    
    @Path("registrarMedico")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje crearMedico(@FormParam("nombre") String nombre, @FormParam("apellidos") String apellidos, 
            @FormParam("genero") String genero, @FormParam("domicilio") String domicilio, @FormParam("num_personal") String num_personal, 
            @FormParam("num_cedula") String num_cedula, @FormParam("contrasena") String contrasena, 
            @FormParam("fecha_nacimiento") Date fecha_nacimiento){
        Mensaje respuesta = new Mensaje();        
        Medico medico = new Medico(nombre, apellidos, genero, domicilio, num_personal, num_cedula, contrasena, "Activo", fecha_nacimiento);
        SqlSession conn = MyBatisUtil.getSession();
        if(conn != null){
            try {
                int resultado = conn.insert("Medico.registrarMedico", medico);
                conn.commit();
                if(resultado > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Medico agregado con éxito");
                    List<Medico> list = null;
                    list = conn.selectList("Medico.getAllMedicos");
                    int i = list.size();
                    Medico medi = list.get(i-1);
                    respuesta.setMensaje("Registro agregado con Exito " + resultado + ", " + medi.getIdMedico());
                    conn.clearCache();
                }else{
                    respuesta.setError(true);
                    respuesta.setMensaje("No se pudo agregar al medico");
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
     * Actualizar la informacion del medico
     * @param nombre
     * @param apellidos
     * @param genero
     * @param domicilio
     * @param num_personal
     * @param num_cedula
     * @param contrasena
     * @param idMedico
     * @param fecha_nacimiento
     * @return 
     */
    @Path("actualizarMedico")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje editarMedico(@FormParam("nombre") String nombre, @FormParam("apellidos") String apellidos, 
            @FormParam("genero") String genero, @FormParam("domicilio") String domicilio, @FormParam("num_personal") String num_personal, 
            @FormParam("num_cedula") String num_cedula, @FormParam("contrasena") String contrasena, @FormParam("idMedico") Integer idMedico, 
            @FormParam("fecha_nacimiento") Date fecha_nacimiento){
        Mensaje respuesta = new Mensaje();        
        Medico medico = new Medico(nombre, apellidos, genero, domicilio, num_personal, num_cedula, contrasena, idMedico, fecha_nacimiento);
        SqlSession conn = MyBatisUtil.getSession();
        if(conn != null){
            try {
                int resultado = conn.update("Medico.actualizarMedico", medico);
                conn.commit();
                if(resultado > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Medico actualizado con éxito... ");
                    conn.clearCache();
                }else{
                    respuesta.setError(true);
                    respuesta.setMensaje("El medico no pudo ser actualizado ");
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
     * Cambiar estatus del medico a inactivo
     * @param idMedico
     * @param status
     * @return 
     */
    @Path("eliminarMedico")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje eliminarPaciente (@FormParam("idMedico") Integer idMedico){
        Mensaje respuesta = new Mensaje();        
        Medico medico = new Medico(idMedico, "Inactivo");
        SqlSession conn = MyBatisUtil.getSession();
        if(conn != null){
            try {
                int resultado = conn.update("Medico.eliminarMedico", medico);
                conn.commit();
                if(resultado > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Medico dado de baja con éxito... ");
                    conn.clearCache();
                }else{
                    respuesta.setError(true);
                    respuesta.setMensaje("El medico no se pudo dar de baja");
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
     * Listar todos los medicos
     * @return medicos
     */
    @Path("getAllMedicos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Medico> getAllMedicos(){
        List<Medico> medicos = null;
        SqlSession conn = MyBatisUtil.getSession();
        
        if(conn != null){
            try {
                medicos = conn.selectList("Medico.getAllMedicos");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return medicos;
    }
    
    @Path("getAllMedicosActivos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Medico> getAllMedicosActivos(){
        List<Medico> medicos = null;
        SqlSession conn = MyBatisUtil.getSession();
        
        if(conn != null){
            try {
                medicos = conn.selectList("Medico.getAllMedicosActivos");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return medicos;
    }
    
    /**
     * Recuperar informacion de un medico en especifico
     * @param idMedico
     * @return 
     */
    @Path("getMedicoByID")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Medico getMedicoByID(@FormParam("idMedico") Integer idMedico){
        SqlSession conn = MyBatisUtil.getSession();
        Medico medico =  new Medico();
        if(conn != null){
            try {
                medico = conn.selectOne("Medico.getMedicoByID", idMedico);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return medico;
    }
    
    @Path("subirFotografia/{idMedico}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje subirFotografiaMedico(byte[] fotografia, @PathParam("idMedico") Integer idMedico){
        SqlSession conn = MyBatisUtil.getSession();
        Medico medico = new Medico();
        medico.setIdMedico(idMedico);
        medico.setFotografia(fotografia);
        Mensaje msj = new Mensaje();
        try {
            int resultado = conn.update("Medico.enviarFotografiaMedico", medico);
            conn.commit();
            if(resultado > 0){
                msj.setError(false);
                msj.setMensaje("Imagen subida con éxito...");
            }else{
                msj.setError(true);
                msj.setMensaje("No se pudo guardar la imagen...");
            }
        } catch (Exception e) {
            msj.setError(true);
            msj.setMensaje(e.getMessage());
        } finally{
            conn.close();
        }
        
        return msj;
    }
    
    
    @Path("getFotografia/{idMedico}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje getFotografiaMedico(@PathParam("idMedico") Integer idMedico){
        Mensaje msj = new Mensaje();
        SqlSession conn = MyBatisUtil.getSession();
        String PATH = "D:\\Documentos\\UV\\7moSemestre\\Integracion_Soluciones\\Imagenes\\"+idMedico+".png";
        if(conn != null){
            try{
                Medico medico = conn.selectOne("Medico.getFotografiaMedico", idMedico);
                System.out.println("Medico: " + medico.getNombre());
                boolean isSave = escribeImagenMedico(PATH, medico.getFotografia());
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
    
    private boolean escribeImagenMedico(String path, byte[] bytes){
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
