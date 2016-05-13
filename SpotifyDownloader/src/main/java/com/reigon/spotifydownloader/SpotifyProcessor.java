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
import com.wrapper.spotify.methods.PlaylistTracksRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.SimpleArtist;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Reiner
 */
public class SpotifyProcessor {

    List<Cancion> listaCanciones;

    public SpotifyProcessor() {
        listaCanciones = new ArrayList<>();
    }

    public void process(String url) {
        
        String user = getUser(url);
        String idP = getIdPlayList(url);
        
        System.out.println("Usuario: " + user + " - ID: " + idP);
        
        // Create an API instance. The default instance connects to https://api.spotify.com/.
        Api api = Api.builder()
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
                //https://open.spotify.com/user/reiner13/playlist/2plTFnZFDDIhyhGIGy377e
                final PlaylistTracksRequest PlayListTrackRequest = api.getPlaylistTracks(user, idP).build();

                try {
                    Page<PlaylistTrack> page = PlayListTrackRequest.get();

                    List<PlaylistTrack> playlistTracks = page.getItems();

                    for (PlaylistTrack playlistTrack : playlistTracks) {
                        String nombre = playlistTrack.getTrack().getName();
                        String album = playlistTrack.getTrack().getAlbum().getName();
                        Cancion track = new Cancion(nombre, album);

                        List<SimpleArtist> artistas = playlistTrack.getTrack().getArtists();

                        for (SimpleArtist artista : artistas) {
                            track.addArtista(artista.getName());
                        }

                        listaCanciones.add(track);
                    }

                } catch (Exception e) {
                    System.out.println("Something went wrong!" + e.getMessage());
                }

            }

            @Override
            public void onFailure(Throwable throwable) {
                /* An error occurred while getting the access token. This is probably caused by the client id or
     * client secret is invalid. */
            }
        });

    }
    
    public String getIdPlayList(String url){
        //Ejemplo url: https://open.spotify.com/user/reiner13/playlist/2plTFnZFDDIhyhGIGy377e
        String idP = "";
        String[] partes = url.split("/");
        idP = partes[partes.length - 1];
        
        return idP;
    }
    
    public String getUser(String url){
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
