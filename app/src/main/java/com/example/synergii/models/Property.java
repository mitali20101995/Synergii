package com.example.synergii.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Property implements Parcelable
{
    private String mls_number;
    private String address;
    private String city;
    private String property_type;
    private String transaction_type;
    private String price;
    private String floor_space_or_land_size;
    private String description;
    private String user_id;
    private String client_id;

    public Property(String mls_number, String address, String city, String property_type, String transaction_type, String price, String floor_space_or_land_size, String description, String user_id, String client_id) {
        this.mls_number = mls_number;
        this.address = address;
        this.city = city;
        this.property_type = property_type;
        this.transaction_type = transaction_type;
        this.price = price;
        this.floor_space_or_land_size = floor_space_or_land_size;
        this.description = description;
        this.user_id = user_id;
        this.client_id = client_id;
    }

    public Property() {
    }

    public static final Creator<Property> CREATOR = new Creator<Property>() {
        @Override
        public Property createFromParcel(Parcel in) {
            return new Property(in);
        }

        @Override
        public Property[] newArray(int size) {
            return new Property[size];
        }
    };

    public String getMls_number() {
        return mls_number;
    }

    public void setMls_number(String mls_number) {
        this.mls_number = mls_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFloor_space_or_land_size() {
        return floor_space_or_land_size;
    }

    public void setFloor_space_or_land_size(String floor_space_or_land_size) {
        this.floor_space_or_land_size = floor_space_or_land_size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    @Override
    public String toString() {
        return "Property{" +
                "mls_number='" + mls_number + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", property_type='" + property_type + '\'' +
                ", transaction_type='" + transaction_type + '\'' +
                ", price='" + price + '\'' +
                ", floor_space_or_land_size='" + floor_space_or_land_size + '\'' +
                ", description='" + description + '\'' +
                ", user_id='" + user_id + '\'' +
                ", client_id='" + client_id + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    protected Property(Parcel in)
    {
        mls_number = in.readString();
        address = in.readString();
        city = in.readString();
        property_type = in.readString();
        transaction_type = in.readString();
        price = in.readString();
        floor_space_or_land_size = in.readString();
        description = in.readString();
        user_id = in.readString();
        client_id = in.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mls_number);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(property_type);
        dest.writeString(transaction_type);
        dest.writeString(price);
        dest.writeString(floor_space_or_land_size);
        dest.writeString(description);
        dest.writeString(user_id);
        dest.writeString(client_id);
    }
}
