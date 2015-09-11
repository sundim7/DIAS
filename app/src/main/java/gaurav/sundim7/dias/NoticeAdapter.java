package gaurav.sundim7.dias;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

public class NoticeAdapter extends SimpleCursorAdapter {
	SqlHandler sqlophandler;
	Context ctx;
	ListView lv;

	public NoticeAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags,
			ListView lv) {
		super(context, layout, c, from, to, flags);
		this.ctx = context;
		this.lv = lv;
	}

	// @SuppressLint("NewApi")
	@Override
	public void bindView(View arg0, final Context arg1, final Cursor arg2) {
		super.bindView(arg0, arg1, arg2);

		sqlophandler = new SqlHandler(arg1);

		final ImageView attachImage = (ImageView) arg0.findViewById(R.id.imageAttach);
		final String attachString = arg2.getString(arg2.getColumnIndex(Notice.KEY_ATTACHMENT));
		// if (!attachString.contains("null")) {
		// attachImage.setVisibility(View.VISIBLE);
	}

	@Override
	public int getViewTypeCount() {
		return getCount();
	}

	@Override
	public int getItemViewType(int position) {
		return position;
	}

	@Override
	// this is called when no recycled view is available, refresh changed
	// bookmark status components here before inflating view
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final LayoutInflater inflater = LayoutInflater.from(context);
		final View v = inflater.inflate(R.layout.list_item, parent, false);
		return v;
	}

	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

}