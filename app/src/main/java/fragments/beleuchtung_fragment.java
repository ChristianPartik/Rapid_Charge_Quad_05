package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.christian.rapid_charge_quad_05.R;

/**
 * Created by Christian on 06.03.2016.
 */
public class beleuchtung_fragment extends Fragment {

    int Akkustand;

    private ProgressBar progressBar;
    private TextView textView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_beleuchtung, container, false);

        return rootview;
    }

    private void Edit_Akkustand() {

        progressBar.findViewById(R.id.circle_progress_bar);
        progressBar.setProgress(Akkustand);

        textView.findViewById(R.id.compliance_percentage);
        textView.setText(Integer.toString(Akkustand) + "%");
    }
}
