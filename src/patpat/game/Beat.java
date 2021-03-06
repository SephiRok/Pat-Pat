// Copyright Rok Breulj <sephirok@gmail.com> 2011.
// Distributed under the Boost Software License, Version 1.0.
// (See accompanying LICENSE file or http://www.boost.org/LICENSE_1_0.txt)

package patpat.game;

/**
 * Beatmap beats. Used to spawn patpat.scene.Beat.
 */
public class Beat implements Comparable<Beat> {
	
	private int mLane;
	private int mTime; 
	
	/**
	 * Constructor.
	 */
	public Beat(int time, int lane) {
		mTime = time;
		mLane = lane;
	}

	/**
	 * Compare function.
	 */
	@Override public int compareTo(Beat beat) {
		if (mTime < beat.mTime) {
			return -1;
		} else if (mTime > beat.mTime) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * Returns the lane where to spawn a patpat.scene.Beat in.
	 */
	public int getLane() {
		return mLane;
	}
	
	/**
	 * Returns the time of the beat.
	 */
	public int getTime() {
		return mTime;
	}
	
}
