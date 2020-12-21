/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fitnutrition;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import interfaz.NotificaCambios;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import pojos.Medico;
import pojos.Mensaje;
import pojos.Paciente;
import pojos.RespuestaWS;
import util.Constantes;
import util.ConsumoWS;



public class FXMLFormularioMedicoController implements Initializable {

    
    private NotificaCambios notificacion;
    private boolean isNuevo;
    private Medico medico;
    private File archivoSel;
    @FXML
    private Label lbTitulo;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidos;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private ComboBox<String> cbGenero;
    @FXML
    private TextField tfDomicilio;
    @FXML
    private TextField tfNumPersonal;
    @FXML
    private TextField tfNumCedula;
    @FXML
    private PasswordField pfContrasena;
    //private ComboBox<String> cbEstatus;
    @FXML
    private ImageView ivMedico;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargaElementosCombo();
    }
    
    public void inicializaCampos(NotificaCambios notificacion,boolean isNuevo,Medico medico){
        this.notificacion = notificacion;
        this.isNuevo = isNuevo;
        this.medico = medico;
        if(!isNuevo){
            lbTitulo.setText("Editar Medico");
            cargaDatosEdicion();
        }
    }
    
    private void cargaDatosEdicion(){
        if(medico != null){
            tfNombre.setText(medico.getNombre());
            tfApellidos.setText(medico.getApellidos());
            dpFecha.setValue(medico.getFecha_nacimiento().toLocalDate());            
            int posCombo = getIndexListaGenero(medico.getGenero());
            cbGenero.getSelectionModel().select(posCombo);
            tfDomicilio.setText(medico.getDomicilio());
            tfNumPersonal.setText(medico.getNum_personal());
            tfNumCedula.setText(medico.getNum_cedula());
            pfContrasena.setText(medico.getContrasena());
            descargaImg(medico.getIdMedico());
            //posCombo = getIndexListaEstatus(medico.getStatus());
            //cbEstatus.getSelectionModel().select(posCombo);
        }
    }
    
    //Metodo auxiliar
    private int getIndexListaGenero(String genero){
        int i = 0;
        if(genero.equals("Hombre")){                
            return i;
        }else{
            i = 1;
        }
        return i;
    }
    
    /*
    private int getIndexListaEstatus(String estatus){
        int i = 0;
        if(estatus.equals("Activo")){                
            return i;
        }else{
            i = 1;
        }
        return i;
    }*/
    
    private void descargaImg(int idMedico){
            String url = Constantes.URL + "medicos/getFotografia/" + idMedico;
        RespuestaWS resp = ConsumoWS.consumoWSGET(url);
        if(resp.getCodigo() == 200){
            Gson gson = new Gson();
            Mensaje msj = gson.fromJson(resp.getMensaje(), Mensaje.class);
            if(!msj.isError() && msj.getMensaje() != null){
                cargaImgView(msj.getMensaje());
            }else{
                muestraDialogoError("Error con la imagen", "Error al obtener la imagen del servicio web");
            }
        }else{
            muestraDialogoError("Error de conexión","Lo sentimos, tenemos problemas para conectar con el servidor");
        }              
    }
    
    private void cargaImgView(String path){
        archivoSel = new File(path);
        try{
            BufferedImage bufferedImage = ImageIO.read(archivoSel);
            Image imAerolinea = SwingFXUtils.toFXImage(bufferedImage, null);
            
            ivMedico.setImage(imAerolinea);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    private void cargaElementosCombo(){
        ObservableList<String> genero = FXCollections.observableArrayList();
        genero.addAll("Hombre", "Mujer");
        
        cbGenero.setItems(genero);
        
        /*ObservableList<String> estatus = FXCollections.observableArrayList();
        estatus.addAll("Activo", "Inactivo");
        
        cbEstatus.setItems(estatus);*/
       
    }

    @FXML
    private void clicEnviar(ActionEvent event) throws IOException {
        String seleccionG = cbGenero.getSelectionModel().getSelectedItem();
        //String seleccionE = cbEstatus.getSelectionModel().getSelectedItem();
        
            if(isNuevo){
                if(archivoSel != null){
                    String url = Constantes.URL + "medicos/registrarMedico";
                    String parametros = String.format("nombre=%s&apellidos=%s&fecha_nacimiento=%s&genero=%s&domicilio=%s&num_personal=%s&num_cedula=%s&contrasena=%s",
                            tfNombre.getText(), tfApellidos.getText(), dpFecha.getValue(), seleccionG, tfDomicilio.getText(), tfNumPersonal.getText(), tfNumCedula.getText(), pfContrasena.getText());
                    RespuestaWS respServicio = ConsumoWS.consumoWSPOST(url, parametros);
                    if(respServicio.getCodigo() == 200){
                        Gson gson = new Gson();
                        Mensaje msj = gson.fromJson(respServicio.getMensaje(), Mensaje.class);                   
                        if(msj.isError()){
                            muestraDialogoError("El registro no pudo ser realizado", msj.getMensaje());
                        }else{
                            //Mandar la imagen despues de que se guarda el registro
                            String[] sepa = msj.getMensaje().split(", ");
                            url = Constantes.URL + "medicos/subirFotografia/" + sepa[1];
                            byte[] img = Files.readAllBytes(archivoSel.toPath());
                            respServicio = ConsumoWS.consumoWSIMG(url, img);
                            if(respServicio.getCodigo() == 200){
                                //Si la imagen se puede almacenar el codigo será 200
                                gson = new Gson();
                                msj = gson.fromJson(respServicio.getMensaje(), Mensaje.class);
                                if(msj.isError()){
                                    muestraDialogoError("El registro no pudo ser realizado", msj.getMensaje());
                                }else{
                                    cerrarScena();
                                }
                            }                        
                        }
                    }else{
                        muestraDialogoError("Error de conexión", "Lo sentimos, tenemos problemas con el servidor");
                    }
                }else{
                    muestraDialogoError("Error de imagen", "Lo sentimos, pero necesitas seleccionar una imagen");
                }
            }else{
                String url = Constantes.URL + "medicos/actualizarMedico";
                String parametros = String.format("idMedico=%d&nombre=%s&apellidos=%s&fecha_nacimiento=%s&genero=%s&domicilio=%s&num_personal=%s&num_cedula=%s&contrasena=%s",
                            medico.getIdMedico(), tfNombre.getText(), tfApellidos.getText(), dpFecha.getValue(), seleccionG, tfDomicilio.getText(), tfNumPersonal.getText(), tfNumCedula.getText(), pfContrasena.getText());
                RespuestaWS respServicio = ConsumoWS.consumoWSPUT(url, parametros);
                if(respServicio.getCodigo() == 200){
                    Gson gson = new Gson();
                    Mensaje msj = gson.fromJson(respServicio.getMensaje(), Mensaje.class);
                    if(msj.isError()){
                        muestraDialogoError("Error al editar", msj.getMensaje());
                    }else{
                        muestraDialogoInfo("Registro editado", msj.getMensaje());
                        //Mandar la imagen despues de que se guarda el registro
                        url = Constantes.URL + "medicos/subirFotografia/" + medico.getIdMedico();
                        byte[] img = Files.readAllBytes(archivoSel.toPath());
                        respServicio = ConsumoWS.consumoWSIMG(url, img);
                        if(respServicio.getCodigo() == 200){
                            //Si la imagen se puede almacenar el codigo será 200
                            gson = new Gson();
                            msj = gson.fromJson(respServicio.getMensaje(), Mensaje.class);
                            if(msj.isError()){
                                muestraDialogoError("El registro no pudo ser realizado", msj.getMensaje());
                            }else{
                                cerrarScena();   
                            }
                        }
                    }
                }else{
                   muestraDialogoError("Error de conexión", "Lo sentimos, tenemos problemas para conectar con el servidor"); 
                }
            } 
    }
    
    @FXML
    private void clicSeleccionar(ActionEvent event) {
        FileChooser seleccionaImg = new FileChooser();
        seleccionaImg.setTitle("Seleccionar logo");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        seleccionaImg.getExtensionFilters().add(extFilterPNG);
        
        Stage stage = (Stage) tfNombre.getScene().getWindow();
        archivoSel = seleccionaImg.showOpenDialog(stage);
        
        if(archivoSel != null){
            try {
                BufferedImage bufferedImage = ImageIO.read(archivoSel);
                Image logo = SwingFXUtils.toFXImage(bufferedImage, null);
                ivMedico.setImage(logo);
            } catch (IOException ex) {
                System.out.println("Error al cargar Imagen " + ex.getMessage());
            }
        }else{
            System.out.println("No se selecciono imagen");
        }
    }
    
    private void muestraDialogoError(String titulo, String mensaje){
        Alert alertError = new Alert(Alert.AlertType.ERROR);
        alertError.setTitle(titulo);
        alertError.setHeaderText(null);
        alertError.setContentText(mensaje);
        alertError.showAndWait();            
    }
    
    private void cerrarScena(){
        Stage stage = (Stage) this.tfNombre.getScene().getWindow();
        stage.close();
        
        notificacion.refrescarTabla(true);
    }
    
    private void muestraDialogoInfo(String titulo, String mensaje){
        Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
        alertInfo.setTitle(titulo);
        alertInfo.setHeaderText(null);
        alertInfo.setContentText(mensaje);
        alertInfo.showAndWait();
    }
}
