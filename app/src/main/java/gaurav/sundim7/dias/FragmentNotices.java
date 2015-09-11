package gaurav.sundim7.dias;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.Toast;

public class FragmentNotices extends Fragment {

	private ListView noticesListview;
	private NoticeAdapter currentAdapter;
	private Menu myMenu;
	private MyBroadcastReceiver serviceCompleteBroadcastReceiver;
	public static Fragment newInstance() {
		final FragmentNotices fragment = new FragmentNotices();
		return fragment;
	}

	public FragmentNotices() {
	}


	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.fragment_notices, container, false);
		setHasOptionsMenu(true);
		noticesListview = (ListView) rootView.findViewById(R.id.mainListView);
		currentAdapter = getLocalDBAdapter();
		if (!currentAdapter.isEmpty())
			noticesListview.setAdapter(currentAdapter);


		//Toast.makeText(getActivity(), "Searching Online For Notices", Toast.LENGTH_SHORT).show();
		serviceCompleteBroadcastReceiver = new MyBroadcastReceiver();
		final IntentFilter intentFilter = new IntentFilter(ServiceUpdateDatabase.ACTION_MyUpdate);
		intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
		getActivity().registerReceiver(serviceCompleteBroadcastReceiver, intentFilter);

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		noticesListview.setAdapter(getLocalDBAdapter());

	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(serviceCompleteBroadcastReceiver);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
	}



	private NoticeAdapter getLocalDBAdapter() {
		final SqlHandler currentSqlHandler = new SqlHandler(getActivity());
		currentSqlHandler.open();
		final Cursor c = currentSqlHandler.getAllNotices();
		c.moveToFirst();
		final String[] columns = new String[] { Notice.KEY_SUBJECT, Notice.KEY_DATE, Notice.KEY_MESSAGE };
		final int[] to = new int[] { R.id.subject, R.id.date, R.id.message };
		final NoticeAdapter adapter = new NoticeAdapter(getActivity(), R.layout.list_item, c, columns, to, 0,
				noticesListview);
		adapter.setFilterQueryProvider(new FilterQueryProvider() {
			public Cursor runQuery(CharSequence constraint) {
				currentSqlHandler.open();
				return currentSqlHandler.fetchNoticesBySubject(constraint.toString());
			}
		});
		currentSqlHandler.close();
		return adapter;
	}
	public class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			noticesListview.setAdapter(getLocalDBAdapter());

			final int newN = intent.getIntExtra(ServiceUpdateDatabase.EXTRA_KEY_NEW_NOTICES, 0);
			if (newN > 0) {
				noticesListview.setAdapter(getLocalDBAdapter());
				Toast.makeText(getActivity(), String.valueOf(newN) + " new notice(s) added",
						Toast.LENGTH_SHORT).show();
			} else if (intent.getBooleanExtra(ServiceUpdateDatabase.FLAG_DOWNLOAD_FAILED, false) == true)
				Toast.makeText(getActivity(), "Unable to fetch new notices. No internet connectivity",
						Toast.LENGTH_SHORT).show();
			else if (intent.getBooleanExtra(ServiceUpdateDatabase.FLAG_SERVER_DOWN, false) == true)
				Toast.makeText(getActivity(), "Server not responding", Toast.LENGTH_SHORT).show();

		}

}}
