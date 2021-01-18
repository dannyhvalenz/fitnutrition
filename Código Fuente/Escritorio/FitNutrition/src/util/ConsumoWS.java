package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.RespuestaWS;

public class ConsumoWS {
    
    public static RespuestaWS consumoWSGET(String url){
        RespuestaWS resp = new RespuestaWS();
        try {            
            URL urlWS = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlWS.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(Constantes.TIME_OUT);
            conn.connect();
            
            System.out.println("Respuesta codigo: "+conn.getResponseCode());
            resp.setCodigo(conn.getResponseCode());                        
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while((output = br.readLine()) != null){
                resp.setMensaje(output);
                System.out.println(output);
            }
            conn.disconnect();
        }catch(Exception e){
            resp.setCodigo(505);
            resp.setMensaje(e.getMessage());
        }
        return resp;
    }
    
    public static RespuestaWS consumoWSPOST(String url, String parametros){
        RespuestaWS resp = new RespuestaWS();
        try{
            URL urlWS = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlWS.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(Constantes.TIME_OUT);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStream os = conn.getOutputStream();
            os.write(parametros.getBytes());
            os.flush();
            conn.connect();
            //Leer respuesta
            System.out.println("Respuesta codigo: "+conn.getResponseCode());
            resp.setCodigo(conn.getResponseCode());
            System.out.println("Respuesta mensaje: "+conn.getResponseMessage());
            
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while((output = br.readLine()) != null){
                resp.setMensaje(output);
                System.out.println(output);
            }
            conn.disconnect();
        }catch(Exception e){
            resp.setCodigo(505);
            resp.setMensaje(e.getMessage());
        }
        return resp;
    }
    
    public static RespuestaWS consumoWSPUT(String url, String parametros){
       RespuestaWS resp = new RespuestaWS();
        try{
            //Constantes.URL+"/aerolineas/login"
            URL urlWS = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlWS.openConnection();
            conn.setRequestMethod("PUT");
            conn.setConnectTimeout(Constantes.TIME_OUT);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStream os = conn.getOutputStream();
            os.write(parametros.getBytes());
            os.flush();
            conn.connect();
            //Leer respuesta
            System.out.println("Respuesta codigo: "+conn.getResponseCode());
            resp.setCodigo(conn.getResponseCode());
            System.out.println("Respuesta mensaje: "+conn.getResponseMessage());
            
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while((output = br.readLine()) != null){
                resp.setMensaje(output);
                System.out.println(output);
            }
            conn.disconnect();
        }catch(Exception e){
            resp.setCodigo(505);
            resp.setMensaje(e.getMessage());
        }
        return resp; 
    }
    
    public static RespuestaWS consumoWSDISABLE(String url, String parametros){
        RespuestaWS resp = new RespuestaWS();
        try{
            //Constantes.URL+"/aerolineas/login"
            URL urlWS = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlWS.openConnection();
            conn.setRequestMethod("PUT");
            conn.setConnectTimeout(Constantes.TIME_OUT);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStream os = conn.getOutputStream();
            os.write(parametros.getBytes());
            os.flush();
            conn.connect();
            //Leer respuesta
            System.out.println("Respuesta codigo: "+conn.getResponseCode());
            resp.setCodigo(conn.getResponseCode());
            System.out.println("Respuesta mensaje: "+conn.getResponseMessage());
            
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while((output = br.readLine()) != null){
                resp.setMensaje(output);
                System.out.println(output);
            }
            conn.disconnect();
        }catch(Exception e){
            resp.setCodigo(505);
            resp.setMensaje(e.getMessage());
        }
        return resp; 
    }
    
    public static RespuestaWS consumoWSDELETE(String url, String parametros){
        RespuestaWS resp = new RespuestaWS();
        try{
            //Constantes.URL+"/aerolineas/login"
            URL urlWS = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlWS.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setConnectTimeout(Constantes.TIME_OUT);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStream os = conn.getOutputStream();
            os.write(parametros.getBytes());
            os.flush();
            conn.connect();
            //Leer respuesta
            System.out.println("Respuesta codigo: "+conn.getResponseCode());
            resp.setCodigo(conn.getResponseCode());
            System.out.println("Respuesta mensaje: "+conn.getResponseMessage());
            
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while((output = br.readLine()) != null){
                resp.setMensaje(output);
                System.out.println(output);
            }
            conn.disconnect();
        }catch(Exception e){
            resp.setCodigo(505);
            resp.setMensaje(e.getMessage());
        }
        return resp; 
    }
    
    public static RespuestaWS consumoWSIMG(String url, byte[] img){
        RespuestaWS resp = new RespuestaWS();
        try{
            //Constantes.URL+"/aerolineas/login"
            URL urlWS = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlWS.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(Constantes.TIME_OUT);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "image/png");
            OutputStream os = conn.getOutputStream();
            os.write(img);
            os.flush();
            conn.connect();
            //Leer respuesta
            System.out.println("Respuesta codigo: "+conn.getResponseCode());
            resp.setCodigo(conn.getResponseCode());
            System.out.println("Respuesta mensaje: "+conn.getResponseMessage());
            
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while((output = br.readLine()) != null){
                resp.setMensaje(output);
                System.out.println(output);
            }
            conn.disconnect();
        }catch(Exception e){
            resp.setCodigo(505);
            resp.setMensaje(e.getMessage());
        }
        return resp;
    }
}
