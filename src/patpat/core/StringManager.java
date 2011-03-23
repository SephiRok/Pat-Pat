package patpat.core;

import java.util.HashMap;

/**
 * Manages strings.
 */
public class StringManager {
	
	private final HashMap<String, String> mData = new HashMap<String, String>();
	
	/**
	 * Singleton implementation.
	 */
	private static class Singleton {
		public static final StringManager INSTANCE = new StringManager();
	}
	
	/**
	 * Returns String value of given key.
	 */
	public String get(final String key) {
		return mData.get(key);
	}
	
	/**
	 * Returns singleton instance.
	 */
	public static StringManager getInstance() {
		return Singleton.INSTANCE;
	}
	
	/**
	 * Returns int value of given key.
	 */
	public int getInt(final String key) {
		return Integer.parseInt(mData.get(key));
	}
	
	/**
	 * Sets String value of given key.
	 */
	public void set(final String key, final String value) {
		mData.put(key, value);
	}
	
	/**
	 * Sets int value of given key.
	 */
	public void set(final String key, int value) {
		mData.put(key, Integer.toString(value));
	}
	
}
