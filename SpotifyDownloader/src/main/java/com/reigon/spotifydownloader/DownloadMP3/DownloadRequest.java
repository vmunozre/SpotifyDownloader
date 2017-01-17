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
package com.reigon.spotifydownloader.DownloadMP3;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;



import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reigon.spotifydownloader.DownloadStatusObject;

import com.reigon.spotifydownloader.Interface;

/*
 * @author Victor_Reiner_&_Gonzalo_Ruanes
 */
public class DownloadRequest implements Callable<File> {
	
	/**
	 * The logger for this class
	 */
	
	/**
	 * Base URL for all the youtube-mp3 API calls
	 */
	public static final String YOUTUBE_MP3 = "http://www.youtube-mp3.org";
	
	/**
	 * The Google JSON parser instance
	 */
	private static final Gson gson = new Gson();

	/**
	 * The ID of the video the user wants to download
	 */
	private final String videoId;
        
        private final String nombreCancion;
        
        private final String path;
        
        DownloadStatusObject status;
	/**
	 * 
	 * @param videoId
         * @param path
         * @param nombreCancion
	 */
	public DownloadRequest(String videoId, String path, String nombreCancion, DownloadStatusObject status) {
		this.videoId = videoId;
                this.path = path;
                this.nombreCancion = nombreCancion;
                this.status = status;
	}

	@Override
	public File call() throws Exception {
                System.out.println("looking up video with id: " + videoId);
		
		/*
		 * First we have to create the lookup request consisting of the video id, the current timestamp, a random hash generated by the
		 * Youtube-MP3 Javascript and some filler variables
		 */
		URLBuilder lookup_builder = new URLBuilder("/a/itemInfo/").addParameter("video_id", videoId).addParameter("ac", "www").addParameter("t", "grp")
				.addParameter("r", new BigDecimal(new Date().getTime()).toString()).hash();

		/*
		 * Send the request and read the JSON file
		 */
		StringBuilder json_builder = new StringBuilder();
		try (Scanner scanner = new Scanner(lookup_builder.build(YOUTUBE_MP3).openStream())) {
			while (scanner.hasNext()) json_builder.append(scanner.nextLine());
		}
		
		/*
		 * www.youtube-mp3.org gives back faulty JSON data, need to remove "info = " at the start and remove the ; at the end
		 */
		json_builder.delete(0, 7).deleteCharAt(json_builder.length() - 1);

		/*
		 * Parse the JSON to a hashmap
		 */
                System.out.println("Video ID: " + this.videoId);
                
		Map<String, Object> response = gson.fromJson(json_builder.toString(), new TypeToken<Map<String, Object>>() {}.getType());
                
		
		/*
		 * Create a second request to get information about the file.
		 */
		double timestamp = (double) response.get("ts_create");
		URLBuilder file_builder = new URLBuilder("/get").addParameter("video_id", videoId).addParameter("ts_create", (long) timestamp)
				.addParameter("r", response.get("r")).addParameter("h2", response.get("h2")).hash();

		HttpURLConnection connection = (HttpURLConnection) file_builder.build(YOUTUBE_MP3).openConnection();
		InputStream file_stream = connection.getInputStream();
		ReadableByteChannel remote = Channels.newChannel(file_stream);
		
		/*
		 * Create the mp3 file and download it to the correct folder.
		 */
                
		File file = new File(this.path + this.nombreCancion);
                System.out.println("downloading file " + file.getAbsolutePath() + " from " + connection.getURL());
                status.addcurrentdownload(nombreCancion);
		try (RandomAccessFile local = new RandomAccessFile(file, "rw")) {
			local.getChannel().transferFrom(remote, 0, Integer.MAX_VALUE);
			local.close();
                        System.out.println("download complete of: " + response.get("title"));
                        status.removecurrentdownload(nombreCancion);
                        status.addmessage("Descargada: " + this.nombreCancion);
                        status.setDownloaded(status.getDownloaded()+1);
			return file;
		} catch (Exception ex) {
                        System.out.println("could not download video, removing file...");
                        Scanner in = new Scanner(System.in);
                        String enlace = in.nextLine();
			file.delete();
			throw new IOException(ex);
		}
	}

}