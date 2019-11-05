package com.example.synergii.models;

import java.time.OffsetDateTime;

public class Agent
{
    private User agentId;
    private String brokerageName;
    private String title;
    private String cardNumber;
    private String companyLogo;
    private String Description;
    private String workplaceId;
    private OffsetDateTime dateCreated;
    private String numberOfUsers;
    private String clientStatus;

    public Agent(User agentId, String brokerageName, String title, String cardNumber, String companyLogo, String description, String workplaceId, OffsetDateTime dateCreated, String numberOfUsers, String clientStatus) {
        this.agentId = agentId;
        this.brokerageName = brokerageName;
        this.title = title;
        this.cardNumber = cardNumber;
        this.companyLogo = companyLogo;
        Description = description;
        this.workplaceId = workplaceId;
        this.dateCreated = dateCreated;
        this.numberOfUsers = numberOfUsers;
        this.clientStatus = clientStatus;
    }

    public Agent() {
    }

    public User getAgentId() {
        return agentId;
    }

    public void setAgentId(User agentId) {
        this.agentId = agentId;
    }

    public String getBrokerageName() {
        return brokerageName;
    }

    public void setBrokerageName(String brokerageName) {
        this.brokerageName = brokerageName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getWorkplaceId() {
        return workplaceId;
    }

    public void setWorkplaceId(String workplaceId) {
        this.workplaceId = workplaceId;
    }

    public OffsetDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(String numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    public String getClientStatus() {
        return clientStatus;
    }

    public void setClientStatus(String clientStatus) {
        this.clientStatus = clientStatus;
    }
}
