package com.pkhansen.gol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    private Button mButton;
    private EditText mEditText;
    private static String mTextField;


    public static String getTextField() {
        return mTextField;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init mButton object
        mButton = (Button) findViewById(R.id.button);
        mEditText = (EditText) findViewById(R.id.editText);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextField = mEditText.getText().toString();
                Intent intent = (new Intent(MainActivity.this, GameOfLifeActivity.class));
                intent.putExtra("TextField", mTextField);
                startActivity(intent);
            }
        };

        mButton.setOnClickListener(listener);
    }







}
