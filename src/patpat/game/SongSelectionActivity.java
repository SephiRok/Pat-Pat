package patpat.game;

import java.util.ArrayList;
import java.util.TreeMap;

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
 * Song selection.
 */
public class SongSelectionActivity extends ListActivity 
		implements OnItemClickListener {
	
	/**
	 * Called when the activity is first created.
	 */
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] directoryEntries = null;
		try {
			directoryEntries = getResources().getAssets().list("");
		} catch(Exception e) {
			e.printStackTrace();
		}
		ArrayList<String> options = new ArrayList<String>(
				directoryEntries.length);
		for (String entry : directoryEntries) {
			if (entry.contains(" - ")) {
				options.add(entry);
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
		StringManager.getInstance().set("song", 
				String.valueOf(textView.getText()));
		startActivity(new Intent(this, BeatmapSelectionActivity.class));
	}

}