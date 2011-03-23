package patpat.scene;

import java.util.Iterator;
import java.util.LinkedList;

import patpat.core.StringManager;
import patpat.game.Beatmap;
import patpat.game.Player;
import patpat.game.R;
import patpat.gui.GUI;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;

/**
 * Manages entities.
 */
public class EntityManager {

	/**
	 * X distance between each lane.
	 */
	public static final int OFFSET_LANE = GUI.getInstance()
			.getDimensionPixelSize(R.dimen.laneOffset);

	private final BeatDrain[] mBeatDrains = new BeatDrain[3];
	private final LinkedList<Beat> mBeats = new LinkedList<Beat>();
	private int mHeight = GUI.getInstance().getHeight();
	private final Beatmap mBeatmap = new Beatmap(
			StringManager.getInstance().get("song") + "/"
			+ StringManager.getInstance().get("beatmap") + ".xml",
			mHeight);
	private final int mID;
	private final int[] mLanePositionX = new int[3];
	private final int[] mLaneColors = { Color.RED, Color.GREEN, Color.BLUE };
	private final Paint mLaneNotificationPaint;
	private final String[] mLaneNotifications = { "", "", "" };
	private final Player mPlayer = new Player();
	private final Paint mScorePaint;
	private int mYPosition = 0;

	/**
	 * Constructor.
	 */
	public EntityManager(int id) {
		mID = id;
		if (StringManager.getInstance().getInt("players") == 2) {
			mHeight /= 2;
			mYPosition += mHeight;
		}
		createLanes();
		createBeatDrains();
		mScorePaint = new Paint(GUI.getInstance().getDefaultPaint());
		mScorePaint.setTextSize(GUI.getInstance().getDimensionPixelSize(
				R.dimen.scoreText));
		mScorePaint.setAntiAlias(true);
		mScorePaint.setTextAlign(Align.CENTER);
		mLaneNotificationPaint = new Paint(GUI.getInstance().getDefaultPaint());
		mLaneNotificationPaint.setTextAlign(Align.CENTER);
	}
	
	private void createBeatDrains() {
		int y = GUI.getInstance().getHeight();
		mBeatDrains[0] = new BeatDrain(this, 0, y, mLaneColors[0]);
		mBeatDrains[1] = new BeatDrain(this, 1, y, mLaneColors[1]);
		mBeatDrains[2] = new BeatDrain(this, 2, y, mLaneColors[2]);
	}
	
	private void createLanes() {
		int guiWidth = GUI.getInstance().getWidth();
		if (mID == 0) {
			mLanePositionX[0] = (guiWidth / 2) - OFFSET_LANE;
			mLanePositionX[1] = guiWidth / 2;
			mLanePositionX[2] = (guiWidth / 2) + OFFSET_LANE;
		} else {
			mLanePositionX[2] = (guiWidth / 2) - OFFSET_LANE;
			mLanePositionX[1] = guiWidth / 2;
			mLanePositionX[0] = (guiWidth / 2) + OFFSET_LANE;
		}
	}

	/**
	 * Draws all entities.
	 */
	public void draw(Canvas canvas) {
		for (BeatDrain beatDrain : mBeatDrains) {
			beatDrain.draw(canvas);
		}
		for (Beat beat : mBeats) {
			if (((mID == 0) && (beat.getBounds().top > mYPosition)) 
					|| ((mID == 1) && (beat.getBounds().bottom < mYPosition))) {
				beat.draw(canvas);
			}
		}
		if (mID == 1) {
			canvas.rotate(180, GUI.getInstance().getWidth() / 2, mYPosition);
		}
		canvas.drawText("Score: " + Integer.toString(mPlayer.getScore()),
				mLanePositionX[1], mScorePaint.getFontSpacing() + mYPosition,
				mScorePaint);
		canvas.drawText(Integer.toString(mPlayer.getScoreMultiplier()) + "x",
				0, mHeight / 4 + mYPosition, 
				GUI.getInstance().getDefaultPaint());
		for (int i = 0; i < mLaneNotifications.length; ++i) {
			int x;
			if (mID == 0) {
				x = mLanePositionX[i];
			} else {
				x = mLanePositionX[mLaneNotifications.length - 1 - i];
			}
			canvas.drawText(mLaneNotifications[i], x, 
					mHeight - mBeatDrains[0].getBounds().height() 
					- GUI.getInstance().getDimensionPixelSize(
							R.dimen.defaultText) + mYPosition, 
					mLaneNotificationPaint);
		}
	}
	
	/**
	 * Returns the Beatmap used to spawn Beats.
	 */
	public final Beatmap getBeatmap() {
		return mBeatmap;
	}
	
	/**
	 * Returns the total available height.
	 */
	public int getHeight() {
		return mHeight;
	}

	/**
	 * Returns ID.
	 */
	public int getID() {
		return mID;
	}

	/**
	 * Returns color of given lane.
	 */
	public int getLaneColor(int lane) {
		return mLaneColors[lane];
	}

	/**
	 * Returns x position of given lane.
	 */
	public int getLanePositionX(int lane) {
		return mLanePositionX[lane];
	}

	/**
	 * Returns associated player.
	 */
	public final Player getPlayer() {
		return mPlayer;
	}
	
	/**
	 * Returns the Y start position.
	 */
	public int getYPosition() {
		return mYPosition;
	}

	/**
	 * Handles input events.
	 */
	public boolean handle() {
		for (BeatDrain beatDrain : mBeatDrains) {
			if (beatDrain.handle()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Handles a touch event on a BeatDrain.
	 */
	public void handlePat(BeatDrain beatDrain) {
		for (int j = 0; j < mLaneNotifications.length; ++j) {
			mLaneNotifications[j] = "";
		}
		for (Iterator<Beat> i = mBeats.iterator(); i.hasNext();) {
			Beat beat = i.next();
			if (Rect.intersects(beat.getBounds(), beatDrain.getBounds())) {
				mLaneNotifications[beat.getLane()] = "+"
						+ Integer.toString(mPlayer.handleHit(1.0));
				i.remove();
				return;
			}
		}
		mLaneNotifications[beatDrain.getLane()] = Integer.toString(mPlayer
				.handleMiss());
	}
	
	/**
	 * Spawns new beats according to the Beatmap.
	 */
	public void spawnBeats(int songTime) {
		for (Iterator<patpat.game.Beat> i = mBeatmap.getBeats()
				.iterator(); i.hasNext();) {
			patpat.game.Beat beat = i.next();
			if (beat.getTime() + Beatmap.UNIVERSAL_OFFSET 
					- mBeatmap.getTravelTime() > songTime) {
				break;
			}
			mBeats.add(new Beat(this, beat.getLane(), beat.getTime(), 
					songTime));
			i.remove();
		}
	}
	
	public int translateY(int y, int intristicHeight) {
		if (mID == 0) {
			return y - intristicHeight;
		} else {
			return 2 * mYPosition - y;
		}
	}

	/**
	 * Updates logic.
	 */
	public void update(int songTime) {
		updateBeats(songTime);
		spawnBeats(songTime);
	}

	/**
	 * Updates beats and removes them if they are no longer in the screen.
	 */
	public void updateBeats(int songTime) {
		final int displayHeight = GUI.getInstance().getHeight();
		for (Iterator<Beat> i = mBeats.iterator(); i.hasNext();) {
			Beat beat = i.next();
			beat.update(songTime);
			if ((beat.getBounds().top > displayHeight) 
					|| (beat.getBounds().bottom < 0)) {
				for (int j = 0; j < mLaneNotifications.length; ++j) {
					mLaneNotifications[j] = "";
				}
				mLaneNotifications[beat.getLane()] = Integer.toString(mPlayer
						.handleMiss());
				i.remove();
			}
		}
	}

}
