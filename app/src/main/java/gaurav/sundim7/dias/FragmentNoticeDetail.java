package gaurav.sundim7.dias;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class FragmentNoticeDetail extends Fragment {

	private static TextView messageTv;
	private TextView subjectTv;
	private TextView dateTv;
	private Button attachButton;
	private SqlHandler db;

	public FragmentNoticeDetail() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		final View rootView = inflater.inflate(R.layout.fragment_notice_detail, container, false);

		db = new SqlHandler(getActivity());
		db.open();
		final Notice n = db.getNotice(this.getArguments().getString("rowid"));

		messageTv = (TextView) rootView.findViewById(R.id.tvmessage);
		subjectTv = (TextView) rootView.findViewById(R.id.tvsubject);
		dateTv = (TextView) rootView.findViewById(R.id.tvdate);
		attachButton = (Button) rootView.findViewById(R.id.attachButton);
		if (!n.attachment.contentEquals("null")) {
			attachButton.setVisibility(View.VISIBLE);
			attachButton.setText(n.attachment);
		}
		messageTv.setText(n.message);
		subjectTv.setText(n.subject);
		dateTv.setText(n.date);
		return rootView;
	}

	public static Fragment newInstance() {
		final FragmentNoticeDetail fragment = new FragmentNoticeDetail();
		return fragment;
	}

}
