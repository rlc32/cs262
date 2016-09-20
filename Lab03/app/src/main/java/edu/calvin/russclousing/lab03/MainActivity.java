package edu.calvin.russclousing.lab03;
//Imports
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageView image = (ImageView) findViewById(R.id.imageView);//link the var image to the image
        image.setVisibility(View.INVISIBLE);                             //set the image to be invisible
        final TextView incor = (TextView) findViewById(R.id.textView3);  //links the var to the incorrect password text
        incor.setVisibility(View.INVISIBLE);                             //sets the incorrect password to invisible
        Button login = (Button) findViewById(R.id.button5);              //links login to the button widget
        login.setOnClickListener(new OnClickListener() {                 //create listiner for button interaction
            @Override
            public void onClick(View v) {
                String pass = "password";                               // the password that we are looking for
                EditText input = (EditText) findViewById(R.id.editText3);//link the password field widget and input var
                if (input.getText().toString().equalsIgnoreCase(pass)) { //if the password is correct
                    image.setVisibility(View.VISIBLE);                   // set image to be visible
                    incor.setVisibility(View.INVISIBLE);                 //set incorrect message to invisible if it was visible
                } else {
                    incor.setVisibility(View.VISIBLE);                   //set the incorrect password text visible
                    image.setVisibility(View.INVISIBLE);                 //hide the image if it was currently visible
                }
            }

        });
    }
}
