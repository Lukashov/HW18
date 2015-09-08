package com.example.den.hw18.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DialerFilter;
import android.widget.TextView;

import com.example.den.hw18.R;


/**
 * Created by Den on 08.09.15.
 */
public class ShowMyLocationDialog extends DialogFragment implements View.OnClickListener {

    private TextView mTxtMyLocation;
    private TextView mTxtLatitude;
    private TextView mTxtLongitude;
    private Button mBtnOk;
    private Bundle mBundle;

    public ShowMyLocationDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_show_my_location, container);

        getDialog().setTitle("Your location:");

        mTxtMyLocation = (TextView) view.findViewById(R.id.txtMyLocation);
        mTxtLatitude = (TextView) view.findViewById(R.id.txtLatitude);
        mTxtLongitude = (TextView) view.findViewById(R.id.txtLongitude);

        mBtnOk = (Button) view.findViewById(R.id.btnOk);
        mBtnOk.setOnClickListener(this);

        setData();

        return view;
    }

    private void setData() {
        mBundle = getArguments();
        mTxtMyLocation.setText(mBundle.getString("location"));
        mTxtLatitude.setText("Latitude: "+ mBundle.get("latitude"));
        mTxtLongitude.setText("Longitude: "+ mBundle.get("longitude"));
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                dismiss();
                break;
        }
    }
}
