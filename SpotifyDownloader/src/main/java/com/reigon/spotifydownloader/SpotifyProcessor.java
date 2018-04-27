/*
    Licencia:
    «Copyright 2016 ReiGon - Victor Reiner & Gonzalo Ruanes»

    This file is part of YouDownloadify.

    YouDownloadify is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    YouDownloadify is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.reigon.spotifydownloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Image;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsTracksRequest;

/*
 * @author Victor_Reiner_&_Gonzalo_Ruanes
 */
public class SpotifyProcessor {

	private List<Cancion> listaCanciones;
	private int numTracks;
	private SpotifyApi api;
	Interface textui;
	private String clientId = "";
	private String clientSecret = "";

	public SpotifyProcessor(Interface t) {
		listaCanciones = new ArrayList<>();
		this.numTracks = 0;
		this.textui = t;
		cargarApiKeys();
	}

	private void cargarApiKeys() {

		// Cargamos las API Keys
		try {

			InputStream in = getClass().getResourceAsStream("/YSAPI_KEYS.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			br.readLine();
			this.clientId = br.readLine();
			this.clientSecret = br.readLine();

		} catch (Exception ex) {
			Logger.getLogger(YoutubeSearch.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void process(String url) throws UnsupportedEncodingException {

		// Decodeamos y encodeamos los nombres de usuario
		final String user = URLEncoder.encode(URLDecoder.decode(getUser(url), "UTF-8"), "UTF-8");
		final String idP = getIdPlayList(url);

		System.out.println("Usuario: " + user + " - ID PlayList: " + idP);
		textui.printText("Usuario: " + user + " - ID PlayList: " + idP);

		// Create an API instance. The default instance connects to
		// https://api.spotify.com/.

		api = new SpotifyApi.Builder().setClientId(clientId).setClientSecret(clientSecret).build();

		final ClientCredentialsRequest clientCredentialsRequest = api.clientCredentials().build();
		/*
		 * Use the request object to make the request synchronously (get)
		 */
		try {
			final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
			// Set access token for further "spotifyApi" object usage
			api.setAccessToken(clientCredentials.getAccessToken());
			System.out.println("Expires in: " + clientCredentials.getExpiresIn());
		} catch (IOException | SpotifyWebApiException e) {
			System.out.println("Error: " + e.getMessage());
		}
		
		System.out.println("Procediendo a analizar la playList...");
		// Sacamos el numero de tracks que tiene la playlist
		final GetPlaylistRequest getPlaylistRequest = api.getPlaylist(user, idP).build();

		try {
			final Playlist playlist = getPlaylistRequest.execute();
			System.out.println("Nombre de la playlist " + playlist.getName());
			numTracks = playlist.getTracks().getTotal();
			System.out.println("Num Tracks PlayList: " + numTracks);
			textui.printText("Num Tracks PlayList: " + numTracks);
			// Sacamos las canciones pasando el offset y el limite
			for (int i = 0; i <= numTracks - 1; i += 30) {
				cargarCanciones(i, 30, user, idP, textui);
			}
		} catch (Exception e) {
			System.out.println("Something went wrong processing playlist!" + e.getMessage());
		}

	}

	private void cargarCanciones(int offset, int limit, String user, String idP, Interface textui) {

		try {

			final GetPlaylistsTracksRequest getPlaylistsTracksRequest = api.getPlaylistsTracks(user, idP).limit(limit)
					.offset(offset).build();
			final Paging<PlaylistTrack> playlistTrackPaging = getPlaylistsTracksRequest.execute();

			PlaylistTrack[] playlistTracks = playlistTrackPaging.getItems();
			System.out.println("OFFSET = " + offset + " - Numero de tracks encontrados = " + playlistTracks.length);

			// Sacamos los datos de cada canción
			for (PlaylistTrack playlistTrack : playlistTracks) {
				String nombre = playlistTrack.getTrack().getName();
				String album = playlistTrack.getTrack().getAlbum().getName();
				Image imagen = playlistTrack.getTrack().getAlbum().getImages()[0];

				int trackNum = playlistTrack.getTrack().getTrackNumber();
				int discNum = playlistTrack.getTrack().getDiscNumber();
				int duracion = playlistTrack.getTrack().getDurationMs();

				System.out.println("Canción: " + nombre + " - Album: " + album + " AÑADIDA A LA BUSQUEDA!");

				Cancion track = new Cancion(nombre, album, duracion, trackNum, discNum, imagen);

				ArtistSimplified[] artistas = playlistTrack.getTrack().getArtists();

				for (ArtistSimplified artista : artistas) {
					track.addArtista(artista.getName());
				}

				listaCanciones.add(track);
			}
		} catch (Exception e) {
			System.out.println("Something went wrong processing songs! : " + e.getMessage());
			System.out.println(e.getCause());
			System.out.println(e.getStackTrace());
		}
	}

	public String getIdPlayList(String url) {
		// Ejemplo url:
		// https://open.spotify.com/user/reiner13/playlist/2plTFnZFDDIhyhGIGy377e
		String idP = "";
		String[] partes0 = url.split("\\?");
		String[] partes = partes0[0].split("/");
		idP = partes[partes.length - 1];
		return idP;
	}

	public String getUser(String url) {
		// Ejemplo url:
		// https://open.spotify.com/user/reiner13/playlist/2plTFnZFDDIhyhGIGy377e
		String user = "";
		String[] partes0 = url.split("\\?");
		String[] partes = partes0[0].split("/");
		user = partes[partes.length - 3];

		return user;
	}

	public List<Cancion> getListaCanciones() {
		return listaCanciones;
	}

}
