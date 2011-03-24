// Copyright Rok Breulj <sephirok@gmail.com> 2011.
// Distributed under the Boost Software License, Version 1.0.
// (See accompanying LICENSE file or http://www.boost.org/LICENSE_1_0.txt)

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
