package com.example.synergii.models;

public class Listing
{
    private String listType;
    private String projectName;
    private String streetNo;
    private String street;
    private String streetAbbreviation;
    private String Area;
    private String Municipality;
    private String province;
    private String postalCode;
    private String Country;
    private String layoutName;
    private String developerName;
    private String nearestIntersection;
    private String community;
    private String propertyType;
    private String category;
    private String price;
    private String size;
    private String balcony;
    private String floors;
    private String bedrooms;
    private String washrooms;
    private String stores;
    private String contactName;
    private String constructionStatus;
    private String completionDate;
    private String salesStarted;
    private String ownership;
    private String numberOfUnits;
    private String Description;
    private String[] buildingImages;
    private String video;
    private String pdf;
    private String[] propertyImages;

    public Listing(String listType, String projectName, String streetNo, String street, String streetAbbreviation, String area, String municipality, String province, String postalCode, String country, String layoutName, String developerName, String nearestIntersection, String community, String propertyType, String category, String price, String size, String balcony, String floors, String bedrooms, String washrooms, String stores, String contactName, String constructionStatus, String completionDate, String salesStarted, String ownership, String numberOfUnits, String description, String[] buildingImages, String video, String pdf, String[] propertyImages) {
        this.listType = listType;
        this.projectName = projectName;
        this.streetNo = streetNo;
        this.street = street;
        this.streetAbbreviation = streetAbbreviation;
        Area = area;
        Municipality = municipality;
        this.province = province;
        this.postalCode = postalCode;
        Country = country;
        this.layoutName = layoutName;
        this.developerName = developerName;
        this.nearestIntersection = nearestIntersection;
        this.community = community;
        this.propertyType = propertyType;
        this.category = category;
        this.price = price;
        this.size = size;
        this.balcony = balcony;
        this.floors = floors;
        this.bedrooms = bedrooms;
        this.washrooms = washrooms;
        this.stores = stores;
        this.contactName = contactName;
        this.constructionStatus = constructionStatus;
        this.completionDate = completionDate;
        this.salesStarted = salesStarted;
        this.ownership = ownership;
        this.numberOfUnits = numberOfUnits;
        Description = description;
        this.buildingImages = buildingImages;
        this.video = video;
        this.pdf = pdf;
        this.propertyImages = propertyImages;
    }

    public Listing(){

}

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getStreetNo() {
        return streetNo;
    }

    public void setStreetNo(String streetNo) {
        this.streetNo = streetNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetAbbreviation() {
        return streetAbbreviation;
    }

    public void setStreetAbbreviation(String streetAbbreviation) {
        this.streetAbbreviation = streetAbbreviation;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getMunicipality() {
        return Municipality;
    }

    public void setMunicipality(String municipality) {
        Municipality = municipality;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public String getNearestIntersection() {
        return nearestIntersection;
    }

    public void setNearestIntersection(String nearestIntersection) {
        this.nearestIntersection = nearestIntersection;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBalcony() {
        return balcony;
    }

    public void setBalcony(String balcony) {
        this.balcony = balcony;
    }

    public String getFloors() {
        return floors;
    }

    public void setFloors(String floors) {
        this.floors = floors;
    }

    public String getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(String bedrooms) {
        this.bedrooms = bedrooms;
    }

    public String getWashrooms() {
        return washrooms;
    }

    public void setWashrooms(String washrooms) {
        this.washrooms = washrooms;
    }

    public String getStores() {
        return stores;
    }

    public void setStores(String stores) {
        this.stores = stores;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getConstructionStatus() {
        return constructionStatus;
    }

    public void setConstructionStatus(String constructionStatus) {
        this.constructionStatus = constructionStatus;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public String getSalesStarted() {
        return salesStarted;
    }

    public void setSalesStarted(String salesStarted) {
        this.salesStarted = salesStarted;
    }

    public String getOwnership() {
        return ownership;
    }

    public void setOwnership(String ownership) {
        this.ownership = ownership;
    }

    public String getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(String numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String[] getBuildingImages() {
        return buildingImages;
    }

    public void setBuildingImages(String[] buildingImages) {
        this.buildingImages = buildingImages;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String[] getPropertyImages() {
        return propertyImages;
    }

    public void setPropertyImages(String[] propertyImages) {
        this.propertyImages = propertyImages;
    }
}
