package egwh.uniemailclient;

import java.util.Date;

import javax.mail.Address;

/**
 * Received Email - Email received by user. (Inbox)
 *
 * Created by eghar on 31/03/2017.
 */

public class ReceivedEmail extends Email {

    private Address from;
    private Date receivedDate;
    private Boolean unread;

    public ReceivedEmail(Long UID, String subject, String message, Address from, Date receivedDate, Boolean unread){
        super(UID, subject, message);
        this.from = from;
        this.receivedDate = receivedDate;
        this.unread = unread;
    }

    public Address getFrom(){
        return from;
    }

    public Date getReceivedDate(){
        return receivedDate;
    }

    public Boolean getUnread(){
        return unread;
    }

    public void setUnread(Boolean unread){
        this.unread = unread;
    }

    @Override
    public String toString(){
        return " From: " + getFrom() + ". Date: " + getReceivedDate() + ". Read: " + getUnread();
    }
}
