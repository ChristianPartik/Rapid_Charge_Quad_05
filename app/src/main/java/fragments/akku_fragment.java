package fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christian.rapid_charge_quad_05.MainActivity;
import com.example.christian.rapid_charge_quad_05.R;


/**
 * Created by Christian on 06.03.2016.
 */
public class akku_fragment extends Fragment {




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_akku, container, false);





        return rootview;
    }

    private class eingabe extends MainActivity{




    }
}
