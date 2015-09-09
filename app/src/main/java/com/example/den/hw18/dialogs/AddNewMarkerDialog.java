package com.example.den.hw18.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.den.hw18.callbacks.CallbackAddMarker;
import com.example.den.hw18.R;

/**
 * Created by Den on 08.09.15.
 */
public class AddNewMarkerDialog extends DialogFragment implements View.OnClickListener {

    private ImageView mImageView;
    private EditText mEditTxt;

    private Button mBtnDone;
    private Button mBtnCancel;

    private final int Pick_image = 1;

    Uri imageUri;

    private CallbackAddMarker mCallbackAddMarker;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbackAddMarker = (CallbackAddMarker) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement CallbackAddMarker");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_new_marker, container);

        getDialog().setTitle("Add new marker:");
        getDialog().setCancelable(false);

        findViews(view);

        veiwListeners();

        return view;
    }

    private void veiwListeners() {
        mBtnDone.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
        mImageView.setOnClickListener(this);
    }

    private void findViews(View view) {
        mImageView = (ImageView) view.findViewById(R.id.imgView);
        mEditTxt = (EditText) view.findViewById(R.id.editTxt);
        mBtnDone = (Button) view.findViewById(R.id.btnDone);
        mBtnCancel = (Button) view.findViewById(R.id.btnCancel);
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
            case R.id.btnDone:
                if(imageUri != null) {
                    mCallbackAddMarker.addMarker(mEditTxt.getText().toString(), imageUri);
                }else{
                    Uri uri = Uri.parse("android.resource://com.example.den.hw18/" + R.drawable.non_image);
                    mCallbackAddMarker.addMarker(mEditTxt.getText().toString(), uri);
                }
                dismiss();
                break;
            case R.id.btnCancel:
                dismiss();
                break;
            case R.id.imgView:
                updateImage();
                break;
        }
    }

    public void updateImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Pick_image);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Pick_image && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            mImageView.setImageURI(imageUri);
        }
    }
}
