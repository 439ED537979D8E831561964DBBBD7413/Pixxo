package com.example.photoeditor;


import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShareSaveBottomFragment extends BottomSheetDialogFragment {

    TextView mShareText, mSaveText, mDeleteText;

    public ShareSaveBottomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_save_bottom, container, false);
        mShareText = view.findViewById(R.id.shareTextView);
        mSaveText = view.findViewById(R.id.saveTextView);
        mDeleteText = view.findViewById(R.id.deleteTextView);

        return view;
    }

}
