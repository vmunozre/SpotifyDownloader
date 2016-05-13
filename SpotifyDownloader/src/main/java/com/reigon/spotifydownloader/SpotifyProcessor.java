/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reigon.spotifydownloader;


import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.SimpleArtist;
import java.util.ArrayList;
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
    
    
    public void process(List<PlaylistTrack> tracks) {
        for (PlaylistTrack playlistTrack : tracks) {
            String nombre = playlistTrack.getTrack().getName();
            String album = playlistTrack.getTrack().getAlbum().getName();
            Cancion track = new Cancion(nombre, album);

            List<SimpleArtist> artistas = playlistTrack.getTrack().getArtists();

            for (SimpleArtist artista : artistas) {
                track.addArtista(artista.getName());
            }

            listaCanciones.add(track);
        }
    }

    public List<Cancion> getListaCanciones() {
        return listaCanciones;
    }
    
    
}
