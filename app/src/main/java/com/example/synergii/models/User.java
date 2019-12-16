package com.example.synergii.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String title;
    private String brokerageName;
    private String Description;
    private String profilePhoto;
    private String profileLogo;

    public User(String userId, String firstName, String lastName, String email, String phone, String password, String title, String brokerageName, String description, String profilePhoto, String profileLogo) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.title = title;
        this.brokerageName = brokerageName;
        Description = description;
        this.profilePhoto = profilePhoto;
        this.profileLogo = profileLogo;
    }

    public User() {
    }

    protected User(Parcel in) {
        userId = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        phone = in.readString();
        password = in.readString();
        title = in.readString();
        brokerageName = in.readString();
        Description = in.readString();
        profilePhoto = in.readString();
        profileLogo = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String  getBrokerageName() { return brokerageName;}

    public void setBrokerageName(String brokerageName) {this.brokerageName = brokerageName;}

    public String getTitle(){return title;}

    public void setTitle(String title){this.title = title;}

    public String getDescription(){return Description;}

     public void setDescription(String Description){this.Description = Description;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(email);
        parcel.writeString(phone);
        parcel.writeString(password);
        parcel.writeString(title);
        parcel.writeString(Description);
        parcel.writeString(brokerageName);
        parcel.writeString(profilePhoto);
        parcel.writeString(profileLogo);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                "lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", title='" + title + '\'' +
                ", brokerageName='" + brokerageName + '\'' +
                ", Description='" + Description + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", profileLogo='" + profileLogo + '\'' +
                ", user_id='" + userId + '\'' +
                '}';
    }

    public String getProfileLogo() {
        return profileLogo;
    }

    public void setProfileLogo(String profileLogo) {
        this.profileLogo = profileLogo;
    }
}
