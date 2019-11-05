package com.example.synergii.models;

public class Client {

    private User clientId;
    private Listing[] properties;
    private Agent assignedAgent;
    private String[] notifications;

    public boolean addToWorkspace(String propertyId)
    {
        return false;
    }

    public Client(User clientId, Listing[] properties, Agent assignedAgent, String[] notifications) {
        this.clientId = clientId;
        this.properties = properties;
        this.assignedAgent = assignedAgent;
        this.notifications = notifications;
    }

    public Client() {
    }

    public User getClientId() {
        return clientId;
    }

    public void setClientId(User clientId) {
        this.clientId = clientId;
    }

    public Listing[] getProperties() {
        return properties;
    }

    public void setProperties(Listing[] properties) {
        this.properties = properties;
    }

    public Agent getAssignedAgent() {
        return assignedAgent;
    }

    public void setAssignedAgent(Agent assignedAgent) {
        this.assignedAgent = assignedAgent;
    }

    public String[] getNotifications() {
        return notifications;
    }

    public void setNotifications(String[] notifications) {
        this.notifications = notifications;
    }
}
