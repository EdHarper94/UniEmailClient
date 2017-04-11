package egwh.uniemailclient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

/**
 * Singular Email Activity View
 * Created by eghar on 09/04/2017.
 */

public class EmailActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_view);

        // Get passed email
        Intent intent = getIntent();
        ReceivedEmail email = (ReceivedEmail) intent.getParcelableExtra("email");
        // DEBUG CODE //
        System.out.println(email);

        Button backButton = (Button)findViewById(R.id.back_button);


        // Init views
        TextView fromView = (TextView)findViewById(R.id.from_view);
        TextView dateView = (TextView)findViewById(R.id.date_view);
        TextView subjectView = (TextView)findViewById(R.id.subject_view);
        WebView messageView = (WebView) findViewById(R.id.message_view);

        // Add data to views
        dateView.setText(email.getReceivedDate().toString());
        fromView.setText(email.getFrom());
        subjectView.setText(email.getSubject());

        Log.d("ATTACHMENT", email.getAttachment().toString());

        // Pass email content to webview
        messageView.getSettings().setJavaScriptEnabled(true);
        messageView.loadDataWithBaseURL("", email.getText(), "text/html; charset=utf-8", "UTF-8", "");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });


    }
    /**
     * Goes back to previous activity
     */
    public void goBack(){
        finish();
    }
}
