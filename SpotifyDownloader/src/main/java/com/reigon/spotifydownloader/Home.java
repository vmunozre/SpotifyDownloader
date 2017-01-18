/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reigon.spotifydownloader;



import java.io.File;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import javafx.application.Platform;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
        System.out.println("Path: " + path + " ,url: " + url);
        this.downloadStatus = new DownloadStatusObject();
        //Lanzarlo todo
        Thread thDescarga = new Thread(new Runnable() {
                    public void run() {
                        try {
                            Downloader d = new Downloader(url, path, downloadStatus);
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
    public String status() throws InterruptedException{
        JSONObject output = new JSONObject();
        JSONArray arraymessages = new JSONArray();
        JSONArray arraydownloads = new JSONArray();
        for (String s:this.downloadStatus.retrievebuffer()){
            arraymessages.add(s);
        }
        for (String n:this.downloadStatus.getCurrentDownloadsCopy()){
            arraydownloads.add(n);
        }
        output.put("stage", this.downloadStatus.getStageStatus());
        output.put("messages", arraymessages);
        output.put("downloads", arraydownloads);
        output.put("total", this.downloadStatus.getTotalsongs());
        output.put("downloaded", this.downloadStatus.getDownloaded());
        System.out.println("Return JSON: " + output.toString());
        return output.toString();
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
