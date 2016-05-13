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

/**
 *
 * @author Reiner
 */
public class Main {

    public static void main(String[] args) {
        //url: https://open.spotify.com/user/reiner13/playlist/2plTFnZFDDIhyhGIGy377e
        SpotifyProcessor spoti = new SpotifyProcessor();
        spoti.process("https://open.spotify.com/user/reiner13/playlist/2plTFnZFDDIhyhGIGy377e");

        YoutubeSearch yout = new YoutubeSearch();
        List<Cancion> canciones = new ArrayList<>();

        canciones = yout.process(spoti.getListaCanciones());

        for (Cancion cancion : canciones) {
            cancion.mostrarCancion();
        }

    }
}
