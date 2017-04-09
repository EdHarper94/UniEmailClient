package egwh.uniemailclient;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by eghar on 28/03/2017.
 */

public class Email implements Parcelable {

    private Long UID;
    private String subject;
    private String message;

    public Email(){

    }

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

    protected Email(Parcel in) {
        UID = in.readByte() == 0x00 ? null : in.readLong();
        subject = in.readString();
        message = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (UID == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(UID);
        }
        dest.writeString(subject);
        dest.writeString(message);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Email> CREATOR = new Parcelable.Creator<Email>() {
        @Override
        public Email createFromParcel(Parcel in) {
            return new Email(in);
        }

        @Override
        public Email[] newArray(int size) {
            return new Email[size];
        }
    };
}