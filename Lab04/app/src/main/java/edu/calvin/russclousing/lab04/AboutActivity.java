package edu.calvin.russclousing.lab04;

/**
 * Created by russclousing on 9/28/16.
 */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AboutActivity extends AppCompatActivity {
    //basic activity build.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity_main);
        //changes the title on the about page to lab04-about. was defualt lab04
        setTitle("Lab04 - About");
    }
}
