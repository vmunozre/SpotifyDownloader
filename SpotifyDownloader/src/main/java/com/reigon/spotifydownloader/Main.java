/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reigon.spotifydownloader;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.reigon.spotifydownloader.DownloadMP3.DownloadRequest;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.AlbumRequest;
import com.wrapper.spotify.methods.PlaylistRequest;
import com.wrapper.spotify.methods.PlaylistTracksRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.Album;
import com.wrapper.spotify.models.ClientCredentials;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.Playlist;
import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.SimpleArtist;
import java.io.File;
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
                    service.submit(new DownloadRequest(cancion.getVideoID(), path, nombreArchivo, textui)).get();
                }
            } catch (InterruptedException | ExecutionException ex) {
                System.out.println("El video " + cancion.getUrl() + " No ha podido descargarse, por favor bajalo manualmente");
                textui.printText("La canción " + cancion.getNombre() + " No ha podido descargarse, por favor, descargala manualmente");
                failedsongs.add(cancion);
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            //cancion.mostrarCancion();

            //cancion.mostrarCancion();
        }
        service.shutdown();
        textui.clearText();
        textui.printText("Estas son las canciones que no se han podido descargar automáticamente");
        textui.printText("puedes bajartelas manualmente utilizando el link en youtube-mp3.org");
        textui.printText("Canciones fallidas: "+failedsongs.size());
        
        for (Cancion s : failedsongs){
            textui.printText("Titulo de la canción: "+s.getNombre()+" - "+s.getArtistas().get(0));
            textui.printText("Link de descarga: "+s.getUrl());
        }
        
        textui.printText("Se ha terminado de descargar ------ Ya puede cerrar la aplicación ");
        Scanner in = new Scanner(System.in);
        String dameunrespirochicoqueterminasmuyrapido = in.nextLine();
        System.exit(0);
    }
}
