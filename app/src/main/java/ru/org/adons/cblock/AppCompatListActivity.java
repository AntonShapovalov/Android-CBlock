package ru.org.adons.cblock;

import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;

public class AppCompatListActivity extends AppCompatActivity {

	private ListView mListView;

	protected ListView getListView() {
		if (mListView == null) {
			mListView = (ListView) findViewById(android.R.id.list);
		}
		return mListView;
	}

	protected void setListAdapter(ListAdapter adapter) {
		getListView().setAdapter(adapter);
	}

	protected ListAdapter getListAdapter() {
		ListAdapter adapter = getListView().getAdapter();
		return adapter;
	}
}
