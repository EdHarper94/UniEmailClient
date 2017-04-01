package egwh.uniemailclient;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by eghar on 31/03/2017.
 */

public class InboxAdapter extends BaseAdapter{

    Context context;
    ArrayList<ReceivedEmail> emails = new ArrayList<>();
    LayoutInflater inflater;
    List<Integer> unreadPos;

    public InboxAdapter(Context context, ArrayList<ReceivedEmail> emails){
        this.context = context;
        this.emails = emails;
        inflater = (LayoutInflater.from(context));
        unreadPos = new ArrayList<>();
    }


    public int getCount(){
        return emails.size();
    }

    public Object getItem(int id){
        System.out.println("THIS IS THE ERROR");
        return emails.get(id);
    }

    public long getItemId(int id){
        System.out.println("THIS IS THE ERROR 2");
        return id;
    }

    /**
     * Holds items for view recycling
     */
    public class ViewHolder{
        public long id;
        public TextView fromText;
        public TextView dateText;
        public TextView subjectText;
        public ImageView unreadImage;
        public boolean unread;
    }

    public void setUnread(ViewHolder viewHolder, boolean unread){
        if(unread){
            viewHolder.unreadImage.setBackgroundColor(ContextCompat.getColor(context, R.color.unreadBlue));
        }else{
            viewHolder.unreadImage.setBackgroundColor(0);
        }
    }

    /**
     * Inflates inbox_email.xml and goes through data and adds to view
     * @see Inbox
     * @param id
     * @param currentView
     * @param viewGroup
     * @return
     */
    public View getView(int id, View currentView, ViewGroup viewGroup){
        ViewHolder viewHolder;
        // New view
        if(currentView == null) {
            // Init new view holder
            viewHolder = new ViewHolder();
            // Inflate view
            currentView = inflater.inflate(R.layout.inbox_email, viewGroup, false);

            // Add to views
            viewHolder.fromText = (TextView) currentView.findViewById(R.id.from_view);
            viewHolder.dateText = (TextView) currentView.findViewById(R.id.date_view);
            viewHolder.subjectText = (TextView) currentView.findViewById(R.id.subject_view);
            viewHolder.unreadImage = (ImageView) currentView.findViewById(R.id.unread_view);

            currentView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) currentView.getTag();
        }
        // Set data
        viewHolder.fromText.setText(emails.get(id).getFrom().toString());
        viewHolder.dateText.setText(emails.get(id).getReceivedDate().toString());
        viewHolder.subjectText.setText(emails.get(id).getSubject());
        viewHolder.unread = (emails.get(id).getUnread());
        setUnread(viewHolder, viewHolder.unread);

        return currentView;
    }
}
