package egwh.uniemailclient;

import java.util.Date;

/**
 * Created by eghar on 28/03/2017.
 */

public class Email {

    private Long UID;
    private String subject;
    private String message;

    public Email(Long UID, String subject, String message){
        this.UID = UID;
        this.subject = subject;
        this.message = message;
    }

    public long getUID(){
        return UID;
    }

    public String getSubject(){
        return subject;
    }

    public String getMessage(){
        return message;
    }

    public String toString(){
        return "ID: " + getUID() + ". Subject: " + getSubject(); // + ". Message: " + getMessage();
    }
}
