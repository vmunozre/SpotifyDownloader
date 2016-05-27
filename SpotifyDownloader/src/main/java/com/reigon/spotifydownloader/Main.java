/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reigon.spotifydownloader;

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
import javax.swing.JFileChooser;

import org.cmc.music.myid3.*;
import org.cmc.music.common.*;
import org.cmc.music.metadata.IMusicMetadata;
import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;

/**
 *
 * @author Reiner
 */
public class Main {

    public static void main(String[] args) {
        ArrayList<Cancion> failedsongs = new ArrayList<Cancion>();
        //url: https://open.spotify.com/user/reiner13/playlist/2plTFnZFDDIhyhGIGy377e
        //Url Prompt
        UrlInputDialog input = new UrlInputDialog(new javax.swing.JFrame(), true);
        input.setVisible(true);
        String url = input.getUrl();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.showOpenDialog(null);
        String path = fileChooser.getSelectedFile().getAbsolutePath() + File.separator;
        System.out.println("Path: " + path);
        TextUI textui = new TextUI();
        textui.setVisible(true);
        textui.printText("Recuperando lista de Spotify");

        SpotifyProcessor spoti = new SpotifyProcessor(textui);

        try {
            spoti.process(url);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        textui.clearText();
        textui.printText("Buscando Canciones");
        YoutubeSearch yout = new YoutubeSearch(textui);
        List<Cancion> canciones;
        canciones = yout.process(spoti.getListaCanciones());
        ExecutorService service = Executors.newFixedThreadPool(5);
        textui.clearText();
        textui.printText("Descargando Canciones");
        //Comento para pruebas
        for (Cancion cancion : canciones) {

            try {
                if (!cancion.getVideoID().isEmpty()) {
                    String nombreArchivo = (cancion.getNombre() + " - " + cancion.getArtistas().get(0)).replace("|", "").replace("/", "").replace(":", "").replace("*", "").replace("?", "").replace("<", "").replace(">", "");
                    //Comprobamos que no exista
                    File src = new File(path + nombreArchivo + ".mp3");
                    if (!src.exists()) {
                        service.submit(new DownloadRequest(cancion.getVideoID(), path, nombreArchivo, textui)).get();
                        // the file we are going to modify
                        if (src.exists()) {
                            //METADATOS
                            /*MusicMetadataSet src_set = new MyID3().read(src); // read metadata
                            IMusicMetadata metadata = src_set.getSimplified();
                            metadata.setAlbum(cancion.getAlbum());
                            metadata.setArtist(cancion.getArtistas().get(0));
                            metadata.setDiscNumber(cancion.getNumDisc());
                            metadata.setSongTitle(cancion.getNombre());
                            metadata.setTrackCount(cancion.getNumCancion());
                            File dst = new File(path + nombreArchivo + ".mp3");
                            new MyID3().write(src, dst, src_set, metadata);*/
                        }

                    }else{
                        System.out.println("REPE!");
                    }
                    

                }
            } catch (InterruptedException | ExecutionException ex) {
                System.out.println("El video " + cancion.getUrl() + " No ha podido descargarse, por favor bajalo manualmente");
                textui.printText("La canción " + cancion.getNombre() + " No ha podido descargarse, por favor, descargala manualmente");
                failedsongs.add(cancion);
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            /*} catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ID3ReadException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ID3WriteException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            */}
            //cancion.mostrarCancion();

            //cancion.mostrarCancion();
        }
        service.shutdown();
        textui.clearText();
        textui.printText("Estas son las canciones que no se han podido descargar automáticamente");
        textui.printText("puedes bajartelas manualmente utilizando el link en youtube-mp3.org");
        textui.printText("Canciones fallidas: " + failedsongs.size());
        FileWriter fi = null;

        PrintWriter pw = null;

        try {
            fi = new FileWriter(path + "__error_log.txt");
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
        String dameunrespirochicoqueterminasmuyrapido = in.nextLine();
        System.exit(0);
    }
}
