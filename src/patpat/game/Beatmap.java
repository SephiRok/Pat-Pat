package patpat.game;

import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import patpat.gui.GUI;
import android.content.res.XmlResourceParser;

/**
 * A beat map (level) of a track.
 */
public class Beatmap {
	
	static public int UNIVERSAL_OFFSET = 250;

	private LinkedList<Beat> mBeats = new LinkedList<Beat>();
	private int mTravelTime;
	
	/**
	 * Constructs map from an xml asset.
	 */
	public Beatmap(final String asset, int height) {
		parse(asset, height);
	}

	/**
	 * Returns the beats.
	 */
	public LinkedList<Beat> getBeats() {
		return mBeats;
	}
	
	/**
	 * Returns the travel time.
	 */
	public int getTravelTime() {
		return mTravelTime;
	}

	/**
	 * Parses an xml asset for beats.
	 */
	public void parse(final String asset, int height) {
		setTravelTime(height);
		try {
			XmlPullParser parser 
					= XmlPullParserFactory.newInstance().newPullParser();
			InputStream input = GUI.getInstance().getContext().getResources()
					.getAssets().open(asset);
			parser.setInput(input, null);
			int eventType = parser.getEventType();
			while (eventType != XmlResourceParser.END_DOCUMENT) {
				if (eventType == XmlResourceParser.START_TAG) {
					String name = parser.getName();
					if (name.equals("beat")) {
						mBeats.add(new Beat(Integer.valueOf(
								parser.getAttributeValue(null, "time")),
								Integer.valueOf(
								parser.getAttributeValue(null, "lane"))));
					} else if (name.equals("speed")) {
						setTravelTime(height, 
								Integer.parseInt(parser.nextText()));
					}
				} else if (eventType == XmlResourceParser.END_TAG) {
				} else if (eventType == XmlResourceParser.TEXT) {
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(mBeats);
	}
	
	private void setTravelTime(int height) {
		setTravelTime(height, 1);
	}
	
	private void setTravelTime(int height, int speed) {
		mTravelTime = height / speed * 10;
	}

}
