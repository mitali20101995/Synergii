package com.example.synergii;

import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class ChatFragment extends Fragment {
View v;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_chat, container, false);
        super.onCreate(savedInstanceState);

//        TextView permClientLink = (TextView) v.findViewById(R.id.permClientLink);
//        permClientLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MakePermanentDialog dialog = new MakePermanentDialog();
//                dialog.show(getFragmentManager(), "dialog_password_reset");
//            }
//        });
        return v;
    }

}
