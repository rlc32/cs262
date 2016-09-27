package edu.calvin.russclousing.homework1;

import android.support.v7.app.AppCompatActivity;//imports
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    int value1;//all of the needed variables for switching types
    int value2;
    int result;
    int position;
    String value1string;
    String value2string;
    String resultstring;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText value1Text = (EditText) findViewById(R.id.editText);//connecting text fields to variables
        final EditText value2Text = (EditText) findViewById(R.id.editText2);
        final TextView resultText = (TextView) findViewById(R.id.textView4);

        final Spinner operator = (Spinner) findViewById(R.id.spinner);//link the spinner to the variable
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(//creates the array adapter for this spinner
                this, R.array.spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//creates the layout of spinner dropdownlist
        operator.setAdapter(adapter);
        operator.setSelection(0);//sets default location on spinner


        Button calc = (Button) findViewById(R.id.button);//link the button
        calc.setOnClickListener(new View.OnClickListener() {//listener for click
            @Override
            public void onClick(View v) {

                value1string = value1Text.getText().toString();
                value2string = value2Text.getText().toString();

                if(value1string.equals("")){//convert the strings of value1 and value2 to ints
                    value1 = 0;
                }
                else {
                    value1 = Integer.parseInt(value1Text.getText().toString());
                }
                if(value2string.equals("")){
                    value2 = 0;
                } else {
                    value2 = Integer.parseInt(value2Text.getText().toString());
                }
                position = operator.getSelectedItemPosition();//gets the correct operator
                if(position == 0){//addition
                    result = value1 + value2;
                    resultstring = String.valueOf(result);
                }
                else if(position == 1){//subtraction
                    result = value1 - value2;
                    resultstring = String.valueOf(result);
                }
                else if(position == 2){//multiplicaiton
                    result = value1 * value2;
                    resultstring = String.valueOf(result);
                }
                else if(position == 3 && value2 != 0 ){//division preventing divede by zero
                    result = value1 / value2;
                    resultstring = String.valueOf(result);
                }
                else if(value2 == 0){//error msg for divide by zero
                    resultstring = "cannot divide by zero";
                }

                resultText.setText(resultstring);//changing the result on the app
            }//this is end of onclick
        });


    }
}
