package com.example.synergii;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.annotations.Nullable;

public class MakePermanentDialog extends DialogFragment {
    private static final String TAG = "MakePermanentDialog";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_make_permanent, container, false);
        EditText clientEmail = (EditText) view.findViewById(R.id.make_permanent_email);
        EditText clientPassword = (EditText) view.findViewById(R.id.make_permanent_password);

        TextView confirmDialog = (TextView) view.findViewById(R.id.dialogConfirm);
        confirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);

            }
        });
        return view;
    }
}
