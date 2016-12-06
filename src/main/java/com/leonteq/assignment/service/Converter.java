package com.leonteq.assignment.service;

import java.net.MalformedURLException;
import java.net.URL;

public class Converter {

	//TODO insert blackWord list in dictionary file and read it in the InitiSystem
	public static String[] blackWord = {"stupid","idiot","fuck"};
	
	/**
	 * Function to short and URL. The function use the cache and DB to retrieve the URL.
	 * If not present calculate the shortUrl and store it in Cache and DB
	 * */
	public static String shortUrl(String urlToShort){
		URL url;
		try {
			url = new URL(urlToShort);
			String protocol = url.getProtocol();
			String authority = url.getAuthority();
			int idx = authority.indexOf("www") ;
			if (idx >= 0 && idx < 3){
				authority = authority.replaceFirst("www", "");
			}
			String path = url.getPath();
			String query = url.getQuery();
			String pathAndQuery = (path != null ? path : "") + (query != null ? "?" + query : "");
			String longUrl = authority + pathAndQuery;
			String shortUrl = DataLeonteq.retrieveShortUrl(longUrl);
			StringBuffer sb = new StringBuffer();
			sb.append(protocol);
			sb.append("://");
			if (shortUrl == null){
				shortUrl = convertAuthority(authority)+"/"+convertString(pathAndQuery);
				DataLeonteq.insertConversion(longUrl, shortUrl);
			}
			sb.append(shortUrl);
			return sb.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Function to unshort and URL. The function use the cache and DB to retrieve the URL.
	 * If not present return NULL
	 * */
	public static String unshortUrl(String urlToUnshort){
		try{
			URL url = new URL(urlToUnshort);
			String protocol = url.getProtocol();
			String shortAuthority = url.getAuthority();
			String shortPath = url.getPath();
			String unshortUrl = DataLeonteq.retrieveLongUrl(shortAuthority + shortPath);
			if (unshortUrl == null){
				return null;
			}
			return protocol + "://" + unshortUrl;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private static String convertString(String s) {
		String converted = hashF(s);
		return checkBlackList(converted);
	}

	/**
	 * Function convert the Authority. The function use the cache and DB to retrieve the Authority.
	 * If not present calculate the shortAuthority and store it in Cache and DB
	 * */
	private static String convertAuthority(String authority){
		String shortAuthority = DataLeonteq.getShortAuthority(authority);
		if (shortAuthority == null){
			String[] splitted = authority.split("[-_.]");
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < splitted.length;i++){
				if (splitted[i].length() < 3){
					//TODO check this. Could be better to remove this if, can create conflict 
					continue;
				}
				int l = splitted[i].length() > 15 ? splitted[i].length() / 4 : 3;
				sb.append(splitted[i].subSequence(0, l));
				sb.append(".");
			}
			shortAuthority = sb.substring(0,sb.length()-1);
			DataLeonteq.insertShortAuthority(authority, shortAuthority);
		}
		return shortAuthority;
	}
	
	/**
	 * Simple hash function 
	 * */
	public static String hashF(String s){
		if (s == null){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		int base = 61;
		int l = s.length();
		int interval = l/5+1;
		for (int i=0;i<l;i= i + interval){
			int end = i + interval > l ? l : i + interval;
			int value = s.substring(i, end).hashCode();
			if (value < 0) value = 0 - value;
			while (value != 0){
				int r = value % base;
				char c;
				if (r < 10){
					c = (char) (r+'0');
				}else if (10<=r && r < 35){
					c = (char) (r-10+'a');
				}else if (35<=r && r < 60){
					c = (char) (r-35+'A');
				}else{
					c = '!';
				}
				value = (value - r) / 64;
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * Function to check if the hash result is a bad word. In this case change the hash result
	 * */
	public static String checkBlackList(String hashString) {
		char[] tmp = hashString.toLowerCase().toCharArray();
		//change the number similar to letter (eg. 1 -> I)
		for (int i = 0; i < tmp.length; i++){
			char c = tmp[i];
			switch (c){
				case '1':
					tmp[i] = 'i';
					break;
				case '3':
					tmp[i] = 'e';
					break;
				case '4':
					tmp[i] = 'a';
					break;
				case '5':
					tmp[i] = 's';
					break;
				case '7':
					tmp[i] = 't';
					break;
				case '0':
					tmp[i] = 'o';
					break;
			}
		}
		String tmpString = new String(tmp);
		char[] hashChar = hashString.toCharArray();
		//search if the string contains somewhere the bad words
		for (int j = 0; j < blackWord.length; j++){
			int idx = tmpString.indexOf(blackWord[j]); 
			if (idx >= 0){
				//invert letter 2 by 2 of the bad word (eg. 'abSTUP1D' -> 'abTSPUD1')
				int l = blackWord[j].length();
				char[] toReplace = new char[l];
				for (int i = idx+1; i < l;i = i + 2){
					char tmpC = hashChar[i];
					hashChar[i] = hashChar[i-1];
					hashChar[i-1] = tmpC;
					toReplace[i] = '0';
					toReplace[i-1] = '1';
				}
				toReplace[l-1] = 'x';
				tmpString = tmpString.replaceFirst(blackWord[j], new String(toReplace));
				j--;
			}
		}
		return new String(hashChar);
	}
	
}
