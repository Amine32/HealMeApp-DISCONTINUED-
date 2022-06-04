package ru.tsu.hits.healmeapp.misc;

public class RequestMedicineName {

    private String request_ID;
    private String userName;
    private String address;
    private String status;
    private String description;

    public String getRequest_ID() {
        return request_ID;
    }

    public void setRequest_ID(String request_ID) {
        this.request_ID = request_ID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "RequestMedicineName{" +
                "request_ID='" + request_ID + '\'' +
                ", userName='" + userName + '\'' +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

