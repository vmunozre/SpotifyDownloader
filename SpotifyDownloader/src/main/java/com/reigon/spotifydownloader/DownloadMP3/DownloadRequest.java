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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reigon.spotifydownloader.TextUI;

public class DownloadRequest implements Callable<File> {
	
	/**
	 * The logger for this class
	 */
	private static final Logger logger = LogManager.getLogger(DownloadRequest.class);
	
	/**
	 * Base URL for all the youtube-mp3 API calls
	 */
	public static final String YOUTUBE_MP3 = "http://www.youtubeinmp3.com";
	
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
        
        TextUI textui;
	/**
	 * 
	 * @param videoId
         * @param path
         * @param nombreCancion
	 */
	public DownloadRequest(String videoId, String path, String nombreCancion, TextUI t) {
		this.videoId = videoId;
                this.path = path;
                this.nombreCancion = nombreCancion;
                this.textui = t;
	}

	@Override
	public File call() throws Exception {
		logger.info("looking up video with id: {}", videoId);
                textui.printText("looking up video with id: " +videoId);
		
		/*
		 * First we have to create the lookup request consisting of the video id, the current timestamp, a random hash generated by the
		 * Youtube-MP3 Javascript and some filler variables
		 */

		/*
		 * Create a second request to get information about the file.
		 */
		URLBuilder file_builder = new URLBuilder("/fetch/").addParameter("video", "https://www.youtube.com/watch?v="+videoId);

		HttpURLConnection connection = (HttpURLConnection) file_builder.build(YOUTUBE_MP3).openConnection();
		InputStream file_stream = connection.getInputStream();
		ReadableByteChannel remote = Channels.newChannel(file_stream);
		
		/*
		 * Create the mp3 file and download it to the correct folder.
		 */
                
		File file = new File(this.path + this.nombreCancion + ".mp3");
		logger.info("downloading file {} from {}", file.getAbsolutePath(), connection.getURL());
                textui.printText("downloading file " + file.getAbsolutePath() + " from "+ connection.getURL());
		try (RandomAccessFile local = new RandomAccessFile(file, "rw")) {
			local.getChannel().transferFrom(remote, 0, Integer.MAX_VALUE);
			local.close();
			logger.info("download complete of: " + videoId);
                        textui.printText("download complete of: " + videoId);
                        textui.printText("");
			return file;
		} catch (Exception ex) {
			logger.warn("could not download video, removing file...");
                        textui.printText("could not download video, removing file...");
			file.delete();
			throw new IOException(ex);
		}
	}

}