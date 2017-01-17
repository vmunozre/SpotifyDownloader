/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reigon.spotifydownloader;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Semaphore;
import javafx.application.Platform;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class Home {
    private Semaphore pDirChooserSem;
    private String strDirChooser;
    private String localPath;
    private String localUrl;
    private DownloadStatusObject downloadStatus;

    @RequestMapping(value = "/")
    @ResponseBody
    String getHome() throws IOException {
       
        String strReturn = Utils.readfile("Templates/index.html");
        return strReturn;
    }
    
    @RequestMapping(value = "/dirchooser")
    @ResponseBody
    String dirChooser(HttpServletRequest request) throws IOException, InterruptedException {
        pDirChooserSem = new Semaphore(0);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Seleccionar carpeta donde descargar las canciones");

                File selectedDirectory = chooser.showDialog(new Stage());
                strDirChooser = selectedDirectory.getAbsolutePath();
                pDirChooserSem.release();
            }
        });
        pDirChooserSem.acquire();
        return strDirChooser;
    }
    
    @RequestMapping(value = "/start")
    @ResponseBody
    String starting(HttpServletRequest request) throws IOException{
        //Se asume que estos parametros se han validado en JS
        String path = request.getParameter("path");
        String url = request.getParameter("url");
        
        //Lanzarlo todo
        Thread thDescarga = new Thread(new Runnable() {
                    public void run() {
                        try {
                            Downloader d = new Downloader(localUrl, path, downloadStatus);
                            d.start();
                        } catch (Exception e) {
                            System.out.println("Error en el Hilo: " + e.getMessage());

                        }
                    }
                }, "HiloDescarga");
                thDescarga.start();
        String strReturn = Utils.readfile("Templates/working.html");
        return strReturn;
    }
    
    @RequestMapping(value = "/getstatus")
    @ResponseBody
    JsonObject status() throws InterruptedException{
        JsonObject output = new JsonObject();
        Gson gson = new Gson();
        JsonElement e = gson.toJsonTree(this.downloadStatus.retrievebuffer());
        
        output.addProperty("stage", this.downloadStatus.getStageStatus());
        output.add("messages", e);
        
        return null;
    }
    
    public boolean isValidUrl(String input) {
        //A valid url would be
        //https://open.spotify.com/user/reiner13/playlist/2plTFnZFDDIhyhGIGy377e
        boolean valid = false;
        String[] partes = input.split("/");
        if (partes.length != 7) {
            return false;
        }
        if ("open.spotify.com".equals(partes[2]) && "user".equals(partes[3]) && "playlist".equals(partes[5])) {
            valid = true;
        }
        return valid;
    }


}
