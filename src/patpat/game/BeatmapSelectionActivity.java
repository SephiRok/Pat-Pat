package patpat.game;

import java.util.ArrayList;

import patpat.core.StringManager;
import patpat.game.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Beatmap selection.
 */
public class BeatmapSelectionActivity extends ListActivity 
		implements OnItemClickListener {
	
	/**
	 * Called when the activity is first created.
	 */
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String directoryEntries[] = null;
		try {
			directoryEntries = getResources().getAssets().list(
					StringManager.getInstance().get("song"));
		} catch(Exception e) {
			e.printStackTrace();
		}
		ArrayList<String> options = new ArrayList<String>(
				directoryEntries.length);
		for (String entry : directoryEntries) {
			if (entry.endsWith(".xml")) {
				options.add(entry.substring(0, entry.length() - 4));
			}
		}
		setListAdapter(new ArrayAdapter<String>(this, R.layout.main, 
				options.toArray(new String[options.size()])));
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(this);
	}
	
	/**
	 * Item click handler.
	 */
	@Override public void onItemClick(AdapterView<?> parent, View view, 
			int position, long id) {
		TextView textView = (TextView)(view);
		StringManager.getInstance().set("beatmap", 
				String.valueOf(textView.getText()));
		if (StringManager.getInstance().get("mode") == "play") {
			startActivity(new Intent(this, WorldActivity.class));
		} else if (StringManager.getInstance().get("mode") == "highScores") {
			startActivity(new Intent(this, HighScoresActivity.class));
		}
	}

}