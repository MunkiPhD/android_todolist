package com.munkiphd.todolist;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by Menace on 25-Oct.
 */
public class ClearableEditText extends LinearLayout {
    EditText editText;
    Button clearButton;

    public ClearableEditText(Context context){
        super(context);

        String infService  = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li;
        li = (LayoutInflater)getContext().getSystemService(infService);
        li.inflate(R.layout.my_compound_view, this, true);

        editText = (EditText) findViewById(R.id.editText);
        clearButton = (Button) findViewById(R.id.clearButton);

        hookupButton();
    }

    private void hookupButton(){
        clearButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                editText.setText("");
            }
        });
    }
}
