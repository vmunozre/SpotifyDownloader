/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reigon.spotifydownloader;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.PlaylistRequest;
import com.wrapper.spotify.methods.PlaylistTracksRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.Playlist;
import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.SimpleArtist;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 *
 * @author Reiner
 */
public class SpotifyProcessor {

    List<Cancion> listaCanciones;
    private int numTracks;
    Api api;
    TextUI textui;

    public SpotifyProcessor(TextUI t) {
        listaCanciones = new ArrayList<>();
        int numTracks = 0;
        this.textui = t;
    }

    public void process(String url) throws UnsupportedEncodingException {
        //tenemos que decodear y encodear nombres de usuario por reasons
        
        String user = URLEncoder.encode(URLDecoder.decode(getUser(url), "UTF-8"), "UTF-8");
        String idP = getIdPlayList(url);

        System.out.println("Usuario: " + user + " - ID: " + idP);
        textui.printText("Usuario: " + user + " - ID: " + idP);

        // Create an API instance. The default instance connects to https://api.spotify.com/.
        api = Api.builder()
                .clientId("0f7de34ee8774ddeab2c1894434c5a01")
                .clientSecret("6ab44cdc4aed44dc9ae0455a30118d17")
                .redirectURI("https://www.spotify.com/es/")
                .build();
        final ClientCredentialsGrantRequest request = api.clientCredentialsGrant().build();

        /* Use the request object to make the request, either asynchronously (getAsync) or synchronously (get) */
        final SettableFuture<ClientCredentials> responseFuture = request.getAsync();

        /* Add callbacks to handle success and failure */
        Futures.addCallback(responseFuture, new FutureCallback<ClientCredentials>() {
            @Override
            public void onSuccess(ClientCredentials clientCredentials) {

                /* Set access token on the Api object so that it's used going forward */
                api.setAccessToken(clientCredentials.getAccessToken());
                textui.printText("Procediendo a analizar la playList...");
                //Sacamos el numero de tracks que tiene la playlist
                final PlaylistRequest infoPlayListRequest = api.getPlaylist(user, idP).build();

                try {
                    final Playlist playlist = infoPlayListRequest.get();
                    numTracks = playlist.getTracks().getTotal();
                    System.out.println("Num Tracks PlayList: " + numTracks);
                    textui.printText("Num Tracks PlayList: " + numTracks);
                } catch (Exception e) {
                    System.out.println("Something went wrong!" + e.getMessage());
                    textui.printText("Something went wrong!" + e.getMessage());
                }
                for (int i = 0; i <= numTracks - 1; i += 50) {
                    cargarCanciones(i, 50, user, idP,textui);
                }
         
            }

            @Override
            public void onFailure(Throwable throwable) {
                /* An error occurred while getting the access token. This is probably caused by the client id or
     * client secret is invalid. */
            }
        });

    }

    private void cargarCanciones(int offset, int limit, String user, String idP, TextUI textui) {
        //https://open.spotify.com/user/reiner13/playlist/2plTFnZFDDIhyhGIGy377e
        final PlaylistTracksRequest playListTrackRequest = api.getPlaylistTracks(user, idP).offset(offset).limit(limit).build();
        //DA UN ERROR AQUI. ALGUNOS NO FUNCIONAN, REVISAR (he reducido el limite a 50 para miminizar el impacto del error de momento)

        try {

            Page<PlaylistTrack> page = playListTrackRequest.get();

            List<PlaylistTrack> playlistTracks = page.getItems();
            System.out.println("OFFSET = " + offset + " - Numero de tracks encontrados = " + playlistTracks.size());
            //textui.printText("OFFSET = " + offset + " - Numero de tracks encontrados = " + playlistTracks.size());
            
            for (PlaylistTrack playlistTrack : playlistTracks) {
                String nombre = playlistTrack.getTrack().getName();
                String album = playlistTrack.getTrack().getAlbum().getName();
                int duracion = playlistTrack.getTrack().getDuration();
                textui.printText("Canción: " + nombre + " - Album: " + album + " AÑADIDA A LA BUSQUEDA!");
                //System.out.println("Cancion: " + nombre +" - Duracion: " + duracion);
                Cancion track = new Cancion(nombre, album, duracion);

                List<SimpleArtist> artistas = playlistTrack.getTrack().getArtists();

                for (SimpleArtist artista : artistas) {
                    track.addArtista(artista.getName());
                }

                listaCanciones.add(track);
            }
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Something went wrong! : " + e.getMessage());
            textui.printText("Something went wrong! : " + e.getMessage());
            //cargarCanciones(offset+1, limit-1, user, idP);
            
           // e.printStackTrace();
        }
    }

    public String getIdPlayList(String url) {
        //Ejemplo url: https://open.spotify.com/user/reiner13/playlist/2plTFnZFDDIhyhGIGy377e
        String idP = "";
        String[] partes = url.split("/");
        idP = partes[partes.length - 1];

        return idP;
    }

    public String getUser(String url) {
        //Ejemplo url: https://open.spotify.com/user/reiner13/playlist/2plTFnZFDDIhyhGIGy377e
        String user;
        String[] partes = url.split("/");
        user = partes[partes.length - 3];

        return user;
    }

    public List<Cancion> getListaCanciones() {
        return listaCanciones;
    }

}
