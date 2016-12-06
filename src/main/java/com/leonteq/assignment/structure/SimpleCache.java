package com.leonteq.assignment.structure;

import org.apache.commons.collections4.map.LRUMap;

public class SimpleCache{
	
	private static LRUMap simpleCache;
	private static SimpleCache sc;

	public static SimpleCache getInstance(){
		if (sc == null){
			simpleCache = new LRUMap(1500); 
			sc = new SimpleCache();
		}
		return sc;
	}
	
	@SuppressWarnings("unchecked")
	public <K,V> V get(K key){
		synchronized (simpleCache){
			if (simpleCache.containsKey(key)){
				return (V) simpleCache.get(key);
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <K,V> V put(K key, V value){
		synchronized (simpleCache){
			return (V) simpleCache.put(key, value);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <K,V> V remove(K key){
		synchronized (simpleCache){
			return (V) simpleCache.remove(key);
		}
	}

	public <V> boolean containsKey(V authority) {
		synchronized (simpleCache){
			return simpleCache.containsKey(authority);
		}
	}

}
