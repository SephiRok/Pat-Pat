// Copyright Rok Breulj <sephirok@gmail.com> 2011.
// Distributed under the Boost Software License, Version 1.0.
// (See accompanying LICENSE file or http://www.boost.org/LICENSE_1_0.txt)

package patpat.core;

/**
 * Used to measure time intervals.
 */
public class Timer {
	
	private long mInterval;
	private long mRemainingTime;
	private boolean mRunning = true;
	
	/**
	 * Instances timer with given interval [ms].
	 */
	public Timer(long interval) {
		mInterval = interval;
		mRemainingTime = interval;
	}
	
	/**
	 * Returns the interval.
	 */
	public long getInterval() {
		return mInterval;
	}
	
	/**
	 * Returns the remaining time.
	 */
	public long getRemainingTime() {
		return mRemainingTime;
	}
	
	/**
	 * Returns if the timer is running.
	 */
	public boolean isRunning() {
		return mRunning;
	}
	
	/**
	 * Returns if the timer is triggered (if the remaining time is zero).
	 * A stopped timer will not trigger.
	 */
	public boolean isTriggered() {
		return (mRunning) && (mRemainingTime == 0);
	}
	
	/**
	 * Resets the remaining time to the interval.
	 */
	public void reset() {
		mRemainingTime = mInterval;
	}
	
	/**
	 * Sets the interval.
	 */
	public void setInterval(final long interval) {
		mInterval = interval;
	}
	
	/**
	 * Sets the remaining time.
	 */
	public void setRemainingTime(final long remainingTime) {
		mRemainingTime = remainingTime;
	}
	
	/**
	 * Starts the timer. Remaining time will decrease based on elapsed real
	 * time.
	 */
	public void start() {
		mRunning = true;
	}
	
	/**
	 * Stops the timer. Timer's remaining time will not change and the timer
	 * will not trigger.
	 */
	public void stop() {
		mRunning = false;
	}
	
	/**
	 * Decreases the remaining time by the given elapsed time [ms].
	 */
	public void update(final long elapsedTime) {
		if (mRunning) {
			if (mRemainingTime <= elapsedTime) {
				mRemainingTime = 0;
			} else {
				mRemainingTime -= elapsedTime;
			}
		}
	}
	
}
