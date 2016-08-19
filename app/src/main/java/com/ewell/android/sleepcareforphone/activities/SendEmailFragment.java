package com.ewell.android.sleepcareforphone.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.ewell.android.sleepcareforphone.R;

/**
 * Created by lillix on 8/1/16.
 */
public class SendEmailFragment extends DialogFragment {

    private EditText emailaddressTxt;

    public interface InputListener
    {
        void onInputComplete(String address);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_sendemail, null);
        emailaddressTxt = (EditText)view.findViewById(R.id.emailaddress);

        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("发送",
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                InputListener listener = (InputListener) getActivity();
                                listener.onInputComplete(emailaddressTxt.getText().toString());
                            }
                        }).setNegativeButton("取消", null);
        return builder.create();
    }
}
