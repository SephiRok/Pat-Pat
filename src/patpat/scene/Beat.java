package patpat.scene;

import android.graphics.Rect;
import patpat.game.Beatmap;
import patpat.game.R;

/**
 * Represents a gameplay entity spawned on music beats. The Player is awarded
 * points for touching corresponding BeatDrains when a Beat is over it.
 */
public class Beat extends BitmapEntity {

	private final EntityManager mEntityManager;
	private final int mLane;
	private final int mTime;

	/**
	 * Constructs a Beat.
	 */
	public Beat(final EntityManager entityManager, int lane, int time,
			int songTime) {
		super(R.drawable.beat);
		mEntityManager = entityManager;
		mLane = lane;
		mTime = time;
		Rect bounds = copyBounds();
		bounds.offsetTo(mEntityManager.getLanePositionX(lane)
				- getIntrinsicWidth() / 2, calculateYPosition(songTime));
		setBounds(bounds);
		setColor(mEntityManager.getLaneColor(lane));
	}

	/**
	 * Calculates the required Y position from the given song time.
	 */
	private int calculateYPosition(int songTime) {
		// FIXME: YPosition = bottom of screen at the beat, but beatDrain
		// has a border so the beat is not in the beatDrain's center on the beat.
		int y = (int)(((float) (songTime - (mTime + Beatmap.UNIVERSAL_OFFSET 
				- mEntityManager.getBeatmap().getTravelTime())) 
				/ (float) mEntityManager.getBeatmap().getTravelTime()) 
				* mEntityManager.getHeight() + mEntityManager.getYPosition());
		return mEntityManager.translateY(y, getIntrinsicHeight());
	}

	/**
	 * Returns the lane.
	 */
	public int getLane() {
		return mLane;
	}

	/**
	 * Updates logic.
	 */
	public void update(int songTime) {
		Rect bounds = copyBounds();
		bounds.offsetTo(bounds.left, calculateYPosition(songTime));
		setBounds(bounds);
	}

}
