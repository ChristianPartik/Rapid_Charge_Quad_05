package fragments;

import android.app.Activity;
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
public class beleuchtung_fragment extends Fragment implements ColorPicker.OnColorChangedListener, ColorPicker.OnColorSelectedListener {





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_beleuchtung, container, false);




        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();

        Toast.makeText(getActivity(), "onResume", Toast.LENGTH_SHORT);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        ColorPicker colorPicker = (ColorPicker)getActivity().findViewById(R.id.color_picker);

        ValueBar valueBar = (ValueBar)getActivity().findViewById(R.id.valuebar);


        colorPicker.addValueBar(valueBar);
        colorPicker.getColor();

        colorPicker.setOldCenterColor(colorPicker.getColor());

        colorPicker.setOnColorSelectedListener(this);
        colorPicker.setOnColorChangedListener(this);

        colorPicker.setShowOldCenterColor(false);



    }

    @Override
    public void onColorChanged(final int color) {


        Toast.makeText(getActivity(), "onChange",Toast.LENGTH_SHORT);

        final int c;
        String r, b, g, message = null;

        final TextView text = (TextView)getActivity().findViewById(R.id.Rot);
        final TextView text2 = (TextView)getActivity().findViewById(R.id.Grün);
        final TextView text3 = (TextView)getActivity().findViewById(R.id.Blau);

        c = color;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               // text.setTextColor(Color.rgb(Color.red(c), Color.green(c), Color.blue(c)));
               // text.setText("Rot" + Color.red(c)+",Blau"+Color.blue(c)+",Grün"+Color.green(c));
                text.setText(""+Color.red(c));
                text2.setText(""+Color.green(c));
                text3.setText(""+Color.blue(c));
            }
        },200);

            r = (String) text.getText();
            b = (String) text2.getText();
            g = (String) text3.getText();

        Intent intent = new Intent(getActivity().getBaseContext(), MainActivity.class);
        intent.putExtra("" + r, message);
        getActivity().startActivity(intent);
    }


    @Override
    public void onColorSelected(int color) {
        Toast.makeText(getActivity(), "onSelect",Toast.LENGTH_SHORT);
    }
}


