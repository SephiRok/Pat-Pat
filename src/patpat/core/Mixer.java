// Copyright Rok Breulj <sephirok@gmail.com> 2011.
// Distributed under the Boost Software License, Version 1.0.
// (See accompanying LICENSE file or http://www.boost.org/LICENSE_1_0.txt)

package patpat.core;

import patpat.gui.GUI;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

/**
 * Provides audio playback.
 */
public class Mixer extends MediaPlayer {
	
	float mVolume = 1f;

	/**
	 * Singleton implementation.
	 */
	private static class Singleton {
		public static final Mixer INSTANCE = new Mixer();
	}

	/**
	 * Returns singleton instance.
	 */
	public static Mixer getInstance() {
		return Singleton.INSTANCE;
	}
	
	/**
	 * Returns the current volume.
	 */
	public float getVolume() {
		return mVolume;
	}

	/**
	 * Prepares given media asset (path from assets directory) for playback.
	 */
	public void prepare(final String asset) {
		reset();
		try {
			AssetFileDescriptor fd = GUI.getInstance().getContext()
					.getResources().getAssets().openFd(asset);
			setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd
					.getLength());
			fd.close();
			prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the volume.
	 */
	public void setVolume(float volume) {
		setVolume(volume, volume);
		mVolume = volume;
	}

}
