package com.reigon.spotifydownloader.DownloadMP3;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class URLBuilder {

	/**
	 * The base of the url
	 */
	private final String base;

	/**
	 * The collection of parameters used in the URL
	 */
	private final Map<String, Object> parameters = new LinkedHashMap<>();

	/**
	 * Create a new URL builder for a given base URL
	 * 
	 * @param base
	 */
	public URLBuilder(String base) {
		this.base = base;
	}
	
	/**
	 * Creates a new URL builder where the base of the URL is empty
	 */
	public URLBuilder() {
		this ("");
	}

	/**
	 * Parses the URL parameters and attempts to forge a valid URL with a given prefix
	 * @return
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException 
	 */
	public URL build(String prefix) throws MalformedURLException, UnsupportedEncodingException {
		return new URL(new StringBuilder(prefix).append(parseParameters()).toString());
	}

	/**
	 * Parses the URL parameters and attempts to forge a valid URL without a given prefix
	 * 
	 * @return
	 * @throws MalformedURLException 
	 * @throws UnsupportedEncodingException 
	 */
	public URL build() throws MalformedURLException, UnsupportedEncodingException {
		return build("");
	}

	private String parseParameters() throws UnsupportedEncodingException {
		StringBuilder builder = new StringBuilder(base).append(!parameters.isEmpty() ? "?" : "");
		for (Iterator<Entry<String, Object>> iterator = parameters.entrySet().iterator(); iterator.hasNext(); ) {
			Entry<String, Object> entry = iterator.next();
			builder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), "UTF-8")).append(iterator.hasNext() ? "&" : "");
		}
		return builder.toString();
	}

	/**
	 * adds a new GET parameter
	 * 
	 * @param key
	 * @param value
	 * @return this instance for chaining
	 */
	public URLBuilder addParameter(String key, Object value) {
		parameters.put(key, value);
		return this;
	}
	
	/**
	 * 
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public URLBuilder hash() throws UnsupportedEncodingException {
		return addParameter("s", Hash.hash(parseParameters()));
	}


}