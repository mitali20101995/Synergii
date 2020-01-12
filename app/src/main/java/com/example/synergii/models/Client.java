package com.example.synergii.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Client implements Parcelable {

    @Exclude
    private String id;
    private ArrayList<String> properties;
    private String assignedAgent;
    private String[] notifications;
    private String firstName;
    private String lastName;
    private String phone;
    private String clientPassword;
    private String email;
    private String listingId;
    private String profilePhoto;

    public Client(String id, ArrayList<String> properties, String assignedAgent, String[] notifications, String firstName, String lastName, String phone, String clientPassword, String email, String listingId, String profilePhoto) {
        this.id = id;
        this.properties = properties;
        this.assignedAgent = assignedAgent;
        this.notifications = notifications;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.clientPassword = clientPassword;
        this.email = email;
        this.listingId = listingId;
        this.profilePhoto = profilePhoto;
    }

    public String getClientPassword() {
        return clientPassword;
    }

    public void setClientPassword(String clientPassword) {
        this.clientPassword = clientPassword;
    }

    public static Creator<Client> getCREATOR() {
        return CREATOR;
    }

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public boolean addToWorkspace(String propertyId)
    {
        return false;
    }

    public static final Creator<Client> CREATOR = new Creator<Client>() {
        @Override
        public Client createFromParcel(Parcel in) {
            return new Client(in);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
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

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public Client() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getProperties() {
        return properties == null ? new ArrayList<>() : properties;
    }

    public void setProperties(ArrayList<String> properties) {
        this.properties = properties;
    }

    public String getAssignedAgent() {
        return assignedAgent;
    }

    public void setAssignedAgent(String assignedAgent) {
        this.assignedAgent = assignedAgent;
    }

    public String[] getNotifications() {
        return notifications;
    }

    public void setNotifications(String[] notifications) {
        this.notifications = notifications;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + id +
                ", properties=" + properties +
                ", assignedAgent=" + assignedAgent +
                ", notifications=" + Arrays.toString(notifications) +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", phone='" + phone + '\'' +
                ", clientPassword='" + clientPassword + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    protected Client(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        listingId = in.readString();
        clientPassword = in.readString();
        profilePhoto = in.readString();
        phone = in.readString();
        assignedAgent = in.readParcelable(Client.class.getClassLoader());
        properties = in.readArrayList(String.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(id);
        dest.writeString(assignedAgent);
        dest.writeString(phone);
        dest.writeString(profilePhoto);
        dest.writeString(clientPassword);
        dest.writeString(properties.toString());
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("clientId",id);
        result.put("firstName",firstName);
        result.put("lastName",lastName);
        result.put("email",email);
        result.put("phone",phone);
        result.put("assignedAgent",assignedAgent);
        result.put("clientPassword", clientPassword);
        result.put("listingId",listingId);
        result.put("properties", properties);

        return result;
    }
}
