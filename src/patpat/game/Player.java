// Copyright Rok Breulj <sephirok@gmail.com> 2011.
// Distributed under the Boost Software License, Version 1.0.
// (See accompanying LICENSE file or http://www.boost.org/LICENSE_1_0.txt)

package patpat.game;

/**
 * Player representation.
 */
public class Player {
	
	public static final int SCORE_BASE_MODIFIER = 10;
	public static final int SCORE_MULTIPLIER_MAX = 8;
	
	private int mScore = 0;
	private int mScoreMultiplier = 1;
	
	/**
	 * Constructor.
	 */
	public Player() {
	}
	
	/**
	 * Returns the current score.
	 */
	public int getScore() {
		return mScore;
	}
	
	/**
	 * Returns the current score multiplier.
	 */
	public int getScoreMultiplier() {
		return mScoreMultiplier;
	}
	
	/**
	 * Handles a Beat hit.
	 */
	public int handleHit(double accuracy) {
		int scoreModifier = SCORE_BASE_MODIFIER * mScoreMultiplier;
		mScore += scoreModifier;
		if (mScoreMultiplier < SCORE_MULTIPLIER_MAX) {
			++mScoreMultiplier;
		}
		return scoreModifier;
	}
	
	/**
	 * Handles a Beat miss.
	 */
	public int handleMiss() {
		mScoreMultiplier = 1;
		int scoreModifier = -SCORE_BASE_MODIFIER;
		mScore += scoreModifier;
		return scoreModifier;
	}
	
}
