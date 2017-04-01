package egwh.uniemailclient;

import java.util.Date;

import javax.mail.Address;

/**
 * Created by eghar on 31/03/2017.
 */

public class ReceivedEmail extends Email {

    private Address from;
    private Date receivedDate;
    private Boolean read;

    public ReceivedEmail(Address from, Date receivedDate, Boolean read, String subject, String message){
        super(subject, message);
        this.from = from;
        this.receivedDate = receivedDate;
        this.read = read;
    }

    public Address getFrom(){
        return from;
    }

    public Date getReceivedDate(){
        return receivedDate;
    }

    public Boolean getRead(){
        return read;
    }

    public void setRead(){
        this.read = true;
    }

    @Override
    public String toString(){
        return " From: " + getFrom() + ". Date: " + getReceivedDate() + ". Read: " + getRead();
    }
}
