package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.christian.rapid_charge_quad_05.R;

/**
 * Created by Christian on 06.03.2016.
 */
public class kamera_fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_kamera, container, false);

        TextView text_kamera = (TextView)rootview.findViewById(R.id.textView4);
        return rootview;
    }
}
