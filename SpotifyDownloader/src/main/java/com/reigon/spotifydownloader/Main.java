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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Reiner
 */
public class Main {

    public static void main(String[] args) {
        
        
        
        //url: https://open.spotify.com/user/reiner13/playlist/2plTFnZFDDIhyhGIGy377e
        //Url Prompt
        UrlInputDialog input = new UrlInputDialog(new javax.swing.JFrame(), true);
        input.setVisible(true);
        String url = input.getUrl();
        
        SpotifyProcessor spoti = new SpotifyProcessor();
        
        spoti.process(url);

        YoutubeSearch yout = new YoutubeSearch();
        List<Cancion> canciones;
        canciones = yout.process(spoti.getListaCanciones());
        ExecutorService service = Executors.newCachedThreadPool();
        for (Cancion cancion : canciones) {
            
            try {
                if(!cancion.getVideoID().isEmpty()){
                    String nombreArchivo = (cancion.getNombre() + " - " + cancion.getAlbum()).replace("|", "").replace("/", "").replace(":", "").replace("*", "").replace("?", "").replace("<", "").replace(">", "");
                    service.submit(new DownloadRequest(cancion.getVideoID(),"./downloads/",nombreArchivo)).get();
                }
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            //cancion.mostrarCancion();
            
            //cancion.mostrarCancion();
        }
        service.shutdown();
        System.exit(0);
    }
}
