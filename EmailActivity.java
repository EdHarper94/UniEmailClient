package egwh.uniemailclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;
import javax.mail.internet.MimeBodyPart;

/**
 * Singular Email Activity View
 * Created by eghar on 09/04/2017.
 */

public class EmailActivity extends Activity {

    private ImapSettings imapSettings = new ImapSettings();
    private Properties props = new Properties();

    private Folder inbox;
    private Session session;
    private Store store;
    private List<File> attachments = new ArrayList<File>();

    private Context context = EmailActivity.this;

    ReceivedEmail email;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_view);

        // Allow File:// URI
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        // Get passed email
        Intent intent = getIntent();
        email = (ReceivedEmail) intent.getParcelableExtra("email");
        // DEBUG CODE //
        System.out.println(email);

        Button backButton = (Button)findViewById(R.id.back_button);
        Button downloadButton = (Button)findViewById(R.id.download_button);

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

        // If email has attachment show download button
        if(email.getAttachment()== true){
            downloadButton.setVisibility(View.VISIBLE);
        }

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new downloadAttachment().execute();
            }
        });
    }

    public class downloadAttachment extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                // Init settings and inbox
                props = new ServerProperties().getInboxProperties();
                session = Session.getInstance(props, null);
                store = session.getStore("imaps");
                store.connect(imapSettings.getServerAddress(), EmailUser.getEmailAddress(), EmailUser.getPassword());

                inbox = store.getFolder("Inbox");
                UIDFolder uf = (UIDFolder) inbox;
                inbox.open(Folder.READ_WRITE);

                Long UID = email.getUID();
                Message message = uf.getMessageByUID(UID);

                // Get multipart of message
                Multipart multipart = (Multipart)message.getContent();

                // Loop through multipart to find the attachments
                for(int i=0; i<multipart.getCount(); i++){
                    Part bodyPart = multipart.getBodyPart(i);
                    if(!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())){
                        continue;
                    }

                    // DEBUG CODE //
                    System.out.println("WE HAVE AN ATTACHMENT");
                    // Save attachment
                    File file = File.createTempFile((bodyPart.getFileName()),null,context.getCacheDir());
                    ((MimeBodyPart)bodyPart).saveFile(file);
                    attachments.add(file);
                }

                inbox.close(false);
                store.close();
            }catch(MessagingException me){
                me.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // DEBUG CODE //
            for(int i=0; i<attachments.size(); i++){
                System.out.println(attachments);
                File file = attachments.get(i);
                try {
                    openFile(context, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            File dir = context.getCacheDir();
            String files[] = dir.list();
            for(String s:files){
                System.out.println("FILE : " +s);
            }
            //viewAttachment();

        }
    }

    public void viewAttachment(){
        File dir = context.getCacheDir();
        File file = new File(dir + "/test.txt");
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(ext);

        if (type == null) {
            type = "*/*";
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.fromFile(file);

        intent.setDataAndType(data, type);

        startActivity(intent);
    }

    public static void openFile(Context context, File url) throws IOException {
        // Create URI
        File file=url;
        Uri uri = Uri.fromFile(file);


        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if(url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if(url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if(url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if(url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
            System.out.println("HERE 2");
        } else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            System.out.println("HERE");
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * Delete attachments from cache
     * @return
     */
    public void deleteFiles(){
        File dir = context.getCacheDir();
        File files[] = dir.listFiles();
        for(File s:files){
            s.delete();
        }
    }

    /**
     * Goes back to previous activity on successful cache deletion
     */
    public void goBack(){
        /*
        if(deleteFiles()==true && email.getAttachment()== true){
            attachments.clear();

            // DEBUG CODE //
            File dir = getApplicationContext().getCacheDir();
            String files[] = dir.list();
            for(String s:files){
                System.out.println("FILE : " +s);
            }

        }else{
            Toast.makeText(this, "ERROR CLEARING ATTACHMENTS", Toast.LENGTH_SHORT);
        }*/
        deleteFiles();
        finish();
    }
}
