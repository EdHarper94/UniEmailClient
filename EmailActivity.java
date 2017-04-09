package egwh.uniemailclient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * Created by eghar on 09/04/2017.
 */

public class EmailActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        ReceivedEmail email = (ReceivedEmail) intent.getParcelableExtra("email");

        System.out.println(email);

    }

    /*
    public getEmailContent extends AsyncTask<Long, Void, Void>(){

    }
    */
}
