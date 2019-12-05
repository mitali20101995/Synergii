package com.example.synergii.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Client implements Parcelable {

    private String clientId;
    private Listing[] properties;
    private String assignedAgent;
    private String[] notifications;
    private String firstName;
    private String lastName;
    private String phone;
    private String clientPassword;
    private String email;
    private String listingId;

    public Client(String clientId, Listing[] properties, String assignedAgent, String[] notifications, String firstName, String lastName, String phone, String clientPassword, String email, String listingId) {
        this.clientId = clientId;
        this.properties = properties;
        this.assignedAgent = assignedAgent;
        this.notifications = notifications;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.clientPassword = clientPassword;
        this.email = email;
        this.listingId = listingId;
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


    public Client() {
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Listing[] getProperties() {
        return properties;
    }

    public void setProperties(Listing[] properties) {
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
                "clientId=" + clientId +
                ", properties=" + Arrays.toString(properties) +
                ", assignedAgent=" + assignedAgent +
                ", notifications=" + Arrays.toString(notifications) +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", clientPassword='" + clientPassword + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    protected Client(Parcel in) {
        clientId = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        listingId = in.readString();
        clientPassword = in.readString();
        phone = in.readString();
        assignedAgent = in.readParcelable(Client.class.getClassLoader());
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
        dest.writeString(clientId);
        dest.writeString(assignedAgent);
        dest.writeString(phone);
        dest.writeString(clientPassword);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("clientId",clientId);
        result.put("firstName",firstName);
        result.put("lastName",lastName);
        result.put("email",email);
        result.put("phone",phone);
        result.put("assignedAgent",assignedAgent);
        result.put("clientPassword", clientPassword);
        result.put("listingId",listingId);

        return result;
    }
}
