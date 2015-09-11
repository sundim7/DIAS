package gaurav.sundim7.dias;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by gaurav on 9/6/2015.
 */
public class TimetableFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
final View root=inflater.inflate(R.layout.fragment_timetable,container,false);
        return root;
    }
}
