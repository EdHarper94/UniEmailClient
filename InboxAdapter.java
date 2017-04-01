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


/**
 * Created by eghar on 31/03/2017.
 */

public class InboxAdapter extends BaseAdapter{

    Context context;
    ArrayList<ReceivedEmail> emails = new ArrayList<>();
    LayoutInflater inflater;

    public InboxAdapter(Context context, ArrayList<ReceivedEmail> emails){
        this.context = context;
        this.emails = emails;
        inflater = (LayoutInflater.from(context));
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

    public View getView(int id, View view, ViewGroup viewGroup){
        view = inflater.inflate(R.layout.inbox_email, viewGroup, false);

        if(emails.get(id).getRead() == false) {
            ImageView unread = (ImageView) view.findViewById(R.id.unread_view);
            unread.setBackgroundColor(ContextCompat.getColor(context, R.color.unreadBlue));
        }

        TextView from = (TextView) view.findViewById(R.id.from_view);
        TextView date = (TextView) view.findViewById(R.id.date_view);
        TextView subject = (TextView) view.findViewById(R.id.subject_view);

        from.setText(emails.get(id).getFrom().toString());
        date.setText(emails.get(id).getReceivedDate().toString());
        subject.setText(emails.get(id).getSubject());

        return view;
    }
}
