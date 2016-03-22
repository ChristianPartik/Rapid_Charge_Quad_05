package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.christian.rapid_charge_quad_05.R;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;

import java.util.logging.Handler;


/**
 * Created by Christian on 06.03.2016.
 */
public class beleuchtung_fragment extends Fragment implements ColorPicker.OnColorChangedListener {





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_beleuchtung, container, false);




        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ColorPicker colorPicker = (ColorPicker)getActivity().findViewById(R.id.color_picker);
        SaturationBar saturationBar = (SaturationBar)getActivity().findViewById(R.id.saturationbar);

        colorPicker.addSaturationBar(saturationBar);
        colorPicker.getColor();

    }

    @Override
    public void onColorChanged(int color) {
        final int c = color;

        new Handler().postDelayed(new Runnable()){

        }
    }
}
