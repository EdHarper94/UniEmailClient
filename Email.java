package egwh.uniemailclient;

import java.util.Date;

/**
 * Created by eghar on 28/03/2017.
 */

public class Email {

    private String subject;
    private String message;

    public Email(String subject, String message){
        this.subject = subject;
        this.message = message;
    }

    public String getSubject(){
        return subject;
    }

    public String getMessage(){
        return message;
    }

    public String toString(){
        return "Subject: " + getSubject(); // + ". Message: " + getMessage();
    }
}
