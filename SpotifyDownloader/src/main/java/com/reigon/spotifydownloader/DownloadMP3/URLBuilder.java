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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;


/*
 * @author Victor_Reiner_&_Gonzalo_Ruanes
 */
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