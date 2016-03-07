package com.jana.karim.hw2;

import com.jana.karim.hw2.R;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ListFragment extends Fragment {
	public interface ListFragmentListener {
		void onCreate();

		void onEdit(long id);

		void onDisplay(long id);

		void onClick(long id);
	}

	private ListFragmentListener listFragmentListener;

	public void setListFragmentListener(
			ListFragmentListener listFragmentListener) {
		this.listFragmentListener = listFragmentListener;
	}

	private static final int CONTACTS_LOADER = 1;
	private SimpleCursorAdapter cursorAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, container, false);

		listView = (ListView) view.findViewById(R.id.listView1);

		String[] from = { ContactsContentProvider.ID,
				ContactsContentProvider.FIRST_NAME,
				ContactsContentProvider.LAST_NAME,
				ContactsContentProvider.BIRTHDAY,
				ContactsContentProvider.HOME_PHONE,
				ContactsContentProvider.WORK_PHONE,
				ContactsContentProvider.MOBILE_PHONE,
				ContactsContentProvider.EMAIL_ADDRESS };
		int[] to = { -1, // id not displayed in the layout
				R.id.firstName, // FIRST_NAME
				R.id.lastName,// LAST_NAME
				-1, -1, -1, R.id.mobilePhone,// MOBILE_PHONE
				-1 };
		/*
		 * How to customize text views inside cursor adapters
		 * http://stackoverflow
		 * .com/questions/25465450/how-to-customize-textviews
		 * -inside-simplecursoradapter-android
		 * http://developer.android.com/reference
		 * /android/widget/Adapter.html#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		cursorAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.contact_row, null, from, to, 0) {
			@Override
			public void setViewText(TextView v, String text) {
				Log.d("TEST", "TextView id is: " + v.getId());
				Log.d("TEST", "TextView tag is: " + v.getTag());
				Log.d("TEST", "TextView text is: " + text);
				if (v.getTag().toString().equals("firstName")) {
					Log.d("TEST", "firstName FOUND! " + text);
					text = text + ", ";
				}
				super.setViewText(v, text);
			}
		};
		listView.setAdapter(cursorAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (listFragmentListener == null)
					throw new RuntimeException(
							"You must register a ListFragmentListener");
				
				listFragmentListener.onClick(getSelectedId());
				listFragmentListener.onDisplay(id);
			}
		});

		// start asynchronous loading of the cursor
		getActivity().getSupportLoaderManager().initLoader(CONTACTS_LOADER,
				null, loaderCallbacks);

		setHasOptionsMenu(true);
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_user_add:
			// bring up the edit activity
			if (listFragmentListener == null)
				throw new RuntimeException(
						"You must register a ListFragmentListener");
			listFragmentListener.onCreate();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private LoaderCallbacks<Cursor> loaderCallbacks = new LoaderCallbacks<Cursor>() {
		@Override
		public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
			String[] projection = { ContactsContentProvider.ID,
					ContactsContentProvider.FIRST_NAME,
					ContactsContentProvider.LAST_NAME,
					ContactsContentProvider.BIRTHDAY,
					ContactsContentProvider.HOME_PHONE,
					ContactsContentProvider.WORK_PHONE,
					ContactsContentProvider.MOBILE_PHONE,
					ContactsContentProvider.EMAIL_ADDRESS };
			return new CursorLoader(getActivity(),
					ContactsContentProvider.CONTENT_URI, projection, null,
					null, // groupby, having
					ContactsContentProvider.FIRST_NAME + " asc");

		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			cursorAdapter.swapCursor(cursor); // set
		}

		@Override
		public void onLoaderReset(Loader<Cursor> cursor) {
			cursorAdapter.swapCursor(null); // clear the data
		}
	};
	private ListView listView;

	public long getSelectedId() {
		return listView.getAdapter().getItemId(
				listView.getCheckedItemPosition());
	}

}
