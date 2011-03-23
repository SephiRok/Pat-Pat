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
public class HighScoresActivity extends ListActivity 
		implements OnItemClickListener {
	
	/**
	 * Called when the activity is first created.
	 */
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] options = getResources().getStringArray(
				R.array.highScores);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.main, 
				options));
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(this);
	}
	
	/**
	 * Item click handler.
	 */
	@Override public void onItemClick(AdapterView<?> parent, View view, 
			int position, long id) {
	}

}