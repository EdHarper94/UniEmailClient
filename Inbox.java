package egwh.uniemailclient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

/**
 *
 * Inbox view. Grabs inbox messages from IMAP server and passes them to InboxAdapter
 * @see InboxAdapter
 *
 * Created by eghar on 30/03/2017.
 */

public class Inbox extends Activity {

    private ImapSettings imapSettings = new ImapSettings();
    private Properties props = new Properties();

    private Folder inbox;
    private Session session;
    private Store store;

    private Message[] messages;
    private ArrayList<ReceivedEmail> emails = new ArrayList<>();

    private InboxAdapter ia;
    ListView lv;
    ProgressDialog pd;

    static int startEmailDeducter = 10;
    static int endEmailDeducter = 0;
    boolean finished = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox);

        lv = (ListView) findViewById(R.id.email_list);
        new getEmails().execute();

        lv.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                new getEmails().execute();
                return true;

            }
        });
    }

    public class getEmails extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Inbox.this);
            pd.setMessage("Fetching Emails...");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            props = new ServerProperties().getInboxProperties();

            try {
                session = Session.getInstance(props, null);
                store = session.getStore("imaps");
                store.connect(imapSettings.getServerAddress(), EmailUser.getEmailAddress(), EmailUser.getPassword());

                inbox = store.getFolder("Inbox");
                inbox.open(Folder.READ_ONLY);

                // DEBUG CODE
                System.out.println("# of Undread Messages : " + inbox.getUnreadMessageCount());

                int totalMessages = inbox.getMessageCount();
                messages = inbox.getMessages(totalMessages - startEmailDeducter, totalMessages - endEmailDeducter);

                for (int i = messages.length - 1; i >= 0; i--) {
                    Message message = messages[i];

                    Address from = message.getFrom()[0];
                    Date date = message.getReceivedDate();
                    String subject = message.getSubject();

                    String text = message.getContent().toString();

                    Boolean unread = true;
                    if(message.isSet(Flags.Flag.SEEN)){
                        unread = false;
                    }

                    ReceivedEmail email = new ReceivedEmail(from, date, unread, subject, text);
                    emails.add(email);
                }
                inbox.close(false);
                store.close();
                System.out.println("MESSAGES DOWNLOADED");
                startEmailDeducter +=10;
                endEmailDeducter += 10;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (MessagingException me) {
                me.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            for (int i = 0; i < emails.size(); i++) {
                System.out.println(emails.get(i));
            }
            if(emails != null){
                if(ia == null) {
                    ia = new InboxAdapter(Inbox.this, emails);
                    lv.setAdapter(ia);
                }else{
                    ia.notifyDataSetChanged();
                }
            }
            if(pd.isShowing()){
                pd.dismiss();
            }
        }

    }
}