package fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.example.christian.rapid_charge_quad_05.MainActivity;
import com.example.christian.rapid_charge_quad_05.R;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.ValueBar;


/**
 * Created by Christian on 06.03.2016.
 */
public class beleuchtung_fragment extends Fragment
        implements ColorPicker.OnColorChangedListener
        {

       OnChangeListener mListener;


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

        ValueBar valueBar = (ValueBar)getActivity().findViewById(R.id.valuebar);


        colorPicker.addValueBar(valueBar);
        colorPicker.getColor();

        colorPicker.setOldCenterColor(colorPicker.getColor());

        colorPicker.setOnColorChangedListener(this);

        colorPicker.setShowOldCenterColor(false);




    }

    @Override
    public void onColorChanged(final int color) {

        final int c;

        final int[] colorrgb = new int[3];

        final TextView text = (TextView)getActivity().findViewById(R.id.Rot);
        final TextView text2 = (TextView)getActivity().findViewById(R.id.Grün);
        final TextView text3 = (TextView)getActivity().findViewById(R.id.Blau);

        c = color;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // text.setTextColor(Color.rgb(Color.red(c), Color.green(c), Color.blue(c)));
                // text.setText("Rot" + Color.red(c)+",Blau"+Color.blue(c)+",Grün"+Color.green(c));

                text.setText("" + Color.red(c));
                text2.setText("" + Color.green(c));
                text3.setText("" + Color.blue(c));
                colorrgb[0] = Color.red(c);
                colorrgb[1] = Color.blue(c);
                colorrgb[2] = Color.green(c);

                mListener.onChangeListener(colorrgb);

            }
        },200);

    }

            @Override
            public void onAttach(Activity activity) {
                super.onAttach(activity);
                try {
                    mListener = (OnChangeListener) activity;
                } catch (ClassCastException e) {
                    throw new ClassCastException(activity.toString() + " must implement OnColorChangeListener");
                }
            }

            public interface OnChangeListener{
                void onChangeListener(int[] color);
            }
        }


