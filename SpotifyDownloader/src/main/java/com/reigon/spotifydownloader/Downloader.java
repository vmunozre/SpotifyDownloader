/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reigon.spotifydownloader;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.reigon.spotifydownloader.DownloadMP3.DownloadRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gonzalo
 */
public class Downloader {
    
    String spotifyURL;
    String folderpath;
    List<Cancion> failedsongs;
    
    public Downloader(String url,String path){
        this.spotifyURL = url;
        this.folderpath = path;
        this.failedsongs = new ArrayList();
    }
    
    public void start(){
        
        TextUI textui = new TextUI();
        textui.setVisible(true);
        textui.printText("Recuperando lista de Spotify");
        
        SpotifyProcessor spoti = new SpotifyProcessor(textui);
        
        //Processing of the Spotify Playlist
        try {
            spoti.process(spotifyURL);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        textui.clearText();
        
        //Youtube Search of the songs
        textui.printText("Buscando Canciones");
        YoutubeSearch yout = new YoutubeSearch(textui);
        List<Cancion> canciones;
        canciones = yout.process(spoti.getListaCanciones());
        
        //Then we download the songs
        ExecutorService service = Executors.newFixedThreadPool(5);
        textui.clearText();
        textui.printText("Descargando Canciones");
        
        for (Cancion cancion : canciones) {

            try {
                if (!cancion.getVideoID().isEmpty()) {
                    String nombretemp;
                    nombretemp = Utils.cleanString(cancion.getNombre() + " - " + cancion.getArtistas().get(0));
                    //Comprobamos que no exista
                    File src = new File(folderpath + cancion.getNombre() + ".mp3");
                    if (!src.exists()) {
                        service.submit(new DownloadRequest(cancion.getVideoID(), folderpath, nombretemp, textui)).get();
                        
                        try {
                            // the file we are going to modify
                            cancion.saveMetadata(folderpath , nombretemp);
                        } catch (UnsupportedTagException | InvalidDataException | NotSupportedException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        System.out.println("REPETIDA!");
                        textui.printText("La canción "+ cancion.getNombre() +" Está repetida");
                    }

                } 

            } catch (Exception ex) {
                System.out.println("El video " + cancion.getUrl() + " No ha podido descargarse, por favor bajalo manualmente");
                textui.printText("La canción " + cancion.getNombre() + " No ha podido descargarse, por favor, descargala manualmente");
                failedsongs.add(cancion);
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        service.shutdown();
        textui.clearText();
        textui.printText("Estas son las canciones que no se han podido descargar automáticamente");
        textui.printText("puedes bajartelas manualmente utilizando el link en youtube-mp3.org");
        textui.printText("Canciones fallidas: " + failedsongs.size());
        FileWriter fi = null;

        PrintWriter pw = null;
        
        //Guardamos en un fichero error_log.txt las canciones que han fallado.
        try {
            fi = new FileWriter(folderpath + "__error_log.txt");
            pw = new PrintWriter(fi);
            pw.println("Canciones fallidas: " + failedsongs.size());
            pw.println();
            for (Cancion s : failedsongs) {
                textui.printText("Titulo de la canción: " + s.getNombre() + " - " + s.getArtistas().get(0));
                textui.printText("Link de descarga: " + s.getUrl());

                pw.println("Titulo de la canción: " + s.getNombre() + " - " + s.getArtistas().get(0));
                pw.println("Link de descarga: " + s.getUrl());
                pw.println("---------------------------------------------------------------");
                pw.println();

            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fi.close();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        textui.printText("Se ha terminado de descargar ------ Ya puede cerrar la aplicación ");
        Scanner in = new Scanner(System.in);
        String end = in.nextLine();
        System.exit(0);
        
    }
    
}
