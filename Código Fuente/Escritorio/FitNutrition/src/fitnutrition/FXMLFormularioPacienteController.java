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
import java.sql.Date;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

/**
 * FXML Controller class
 *
 * @author PC
 */
public class FXMLFormularioPacienteController implements Initializable {

    
    private NotificaCambios notificacion;
    private boolean isNuevo;
    private Paciente paciente;
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
    private TextField tfPeso;
    @FXML
    private TextField tfEstatura;
    @FXML
    private TextField tfTalla;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfDomicilio;
    @FXML
    private ComboBox<Medico> cbMedico;
    @FXML
    private ComboBox<String> cbEstatus;
    @FXML
    private ImageView ivPaciente;
    
    private ObservableList<Medico> medicos;
    
    @FXML
    private TextField tfUsuario;
    @FXML
    private PasswordField pfContrasena;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargaElementosCombo();
    }

    public void inicializaCampos(NotificaCambios notificacion,boolean isNuevo,Paciente paciente){
        this.notificacion = notificacion;
        this.isNuevo = isNuevo;
        this.paciente = paciente;
        if(!isNuevo){
            lbTitulo.setText("Editar Paciente");
            cargaDatosEdicion();
        }
    }
    
    private void cargaDatosEdicion(){
        if(paciente != null){
            tfNombre.setText(paciente.getNombre());
            tfApellidos.setText(paciente.getApellidos());
            dpFecha.setValue(paciente.getFecha_nacimiento().toLocalDate());            
            int posCombo = getIndexListaGenero(paciente.getGenero());
            cbGenero.getSelectionModel().select(posCombo);
            tfPeso.setText(""+paciente.getPeso());
            tfEstatura.setText(""+paciente.getEstatura());
            tfTalla.setText(paciente.getTalla());
            tfEmail.setText(paciente.getEmail());
            tfTelefono.setText(paciente.getTelefono());
            tfDomicilio.setText(paciente.getDomicilio());
            tfUsuario.setText(paciente.getUsuario());
            pfContrasena.setText(paciente.getContrasena());
            descargaImg(paciente.getIdPaciente());
            
            posCombo = getIndexListaDoctor(paciente.getIdMedico());
            cbMedico.getSelectionModel().select(posCombo);
            posCombo = getIndexListaEstatus(paciente.getStatus());
            cbEstatus.getSelectionModel().select(posCombo);
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
    
    private int getIndexListaDoctor(int idMedico){
        for(int i=0; i < medicos.size(); i++){
            if(medicos.get(i).getIdMedico() == idMedico){
                return i;
            }
        }
        return 0;
    }
    
    private int getIndexListaEstatus(String estatus){
        int i = 0;
        if(estatus.equals("Activo")){                
            return i;
        }else{
            i = 1;
        }
        return i;
    }
    
    private void descargaImg(int idPaciente){
            String url = Constantes.URL + "pacientes/getFotografia/" + idPaciente;
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
            ivPaciente.setImage(imAerolinea);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    private void cargaElementosCombo(){
        ObservableList<String> genero = FXCollections.observableArrayList();
        genero.addAll("Hombre", "Mujer");
        
        cbGenero.setItems(genero);
        
        ObservableList<String> estatus = FXCollections.observableArrayList();
        estatus.addAll("Activo", "Inactivo");
        
        cbEstatus.setItems(estatus);
        
        String url = Constantes.URL + "medicos/getAllMedicosActivos";
        RespuestaWS resp = ConsumoWS.consumoWSGET(url);
        if(resp.getCodigo() == 200){
            Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
            Type medicoLista = new TypeToken<ArrayList<Medico>>(){}.getType();
            System.out.println(medicoLista);
            System.out.println(resp.getMensaje());
            ArrayList<Medico> tipoBD = gson.fromJson(resp.getMensaje(), medicoLista);
            System.out.println("CONTENIDO DE tipoBD" + tipoBD.get(0));
            medicos = FXCollections.observableArrayList(tipoBD);
            //System.out.println(medicos.size());
            cbMedico.setItems(medicos);
        }else{
            muestraDialogoError("Error de conexión", "Lo sentimos, tenemos problemas con el servidor");
        }        
    }

    @FXML
    private void clicEnviar(ActionEvent event) throws IOException{
        int seleccionM = medicos.get(cbMedico.getSelectionModel().getSelectedIndex()).getIdMedico();
        String seleccionG = cbGenero.getSelectionModel().getSelectedItem();
        String seleccionE = cbEstatus.getSelectionModel().getSelectedItem();
        
            if(isNuevo){
                if(archivoSel != null){
                    String url = Constantes.URL + "pacientes/registrarPaciente";
                    String parametros = String.format("nombre=%s&apellidos=%s&fecha_nacimiento=%s&genero=%s&peso=%f&estatura=%d&talla=%s&email=%s&telefono=%s&domicilio=%s&usuario=%s&contrasena=%s&idMedico=%d&status=%s",
                            tfNombre.getText(), tfApellidos.getText(), dpFecha.getValue(), seleccionG, Float.valueOf(tfPeso.getText()), Integer.valueOf(tfEstatura.getText()), tfTalla.getText(), tfEmail.getText(), tfTelefono.getText(), 
                            tfDomicilio.getText(), tfUsuario.getText(), pfContrasena.getText(), seleccionM, seleccionE);
                    RespuestaWS respServicio = ConsumoWS.consumoWSPOST(url, parametros);
                    if(respServicio.getCodigo() == 200){
                        Gson gson = new Gson();
                        Mensaje msj = gson.fromJson(respServicio.getMensaje(), Mensaje.class);                   
                        if(msj.isError()){
                            muestraDialogoError("El registro no pudo ser realizado", msj.getMensaje());
                        }else{
                            //Mandar la imagen despues de que se guarda el registro
                            String[] sepa = msj.getMensaje().split(", ");
                            url = Constantes.URL + "pacientes/subirFotografia/" + sepa[1];
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
                String url = Constantes.URL + "pacientes/actualizarPaciente";
                String parametros = String.format("idPaciente=%d&nombre=%s&apellidos=%s&fecha_nacimiento=%s&genero=%s&peso=%f&estatura=%d&talla=%s&email=%s&telefono=%s&domicilio=%s&usuario=%s&contrasena=%s&idMedico=%d&status=%s",
                        paciente.getIdPaciente(), tfNombre.getText(), tfApellidos.getText(), dpFecha.getValue(), seleccionG, Float.valueOf(tfPeso.getText()), Integer.valueOf(tfEstatura.getText()), tfTalla.getText(), tfEmail.getText(), tfTelefono.getText(), 
                            tfDomicilio.getText(), tfUsuario.getText(), pfContrasena.getText(), seleccionM, seleccionE);
                RespuestaWS respServicio = ConsumoWS.consumoWSPUT(url, parametros);
                if(respServicio.getCodigo() == 200){
                    Gson gson = new Gson();
                    Mensaje msj = gson.fromJson(respServicio.getMensaje(), Mensaje.class);
                    if(msj.isError()){
                        muestraDialogoError("Error al editar", msj.getMensaje());
                    }else{
                        muestraDialogoInfo("Registro editado", msj.getMensaje());
                        //Mandar la imagen despues de que se guarda el registro
                        url = Constantes.URL + "pacientes/subirFotografia/" + paciente.getIdPaciente();
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
                ivPaciente.setImage(logo);
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
