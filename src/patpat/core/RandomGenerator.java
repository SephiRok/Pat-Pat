package patpat.core;

import java.util.Random;

public class RandomGenerator extends Random {

	/**
	 * Singleton implementation.
	 */
	private static class Singleton {
		public static final RandomGenerator INSTANCE = new RandomGenerator();
	}
	
	/**
	 * Returns singleton instance.
	 */
	public static RandomGenerator getInstance() {
		return Singleton.INSTANCE;
	}
	
}
