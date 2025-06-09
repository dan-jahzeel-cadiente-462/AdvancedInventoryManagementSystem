package com.inventory.model;

import java.util.Date;

public class LoginHistory {
    private int loginHistoryID;
    private int userID;
    private Date loginTime;
    private Date logoutTime;

    public LoginHistory() {
    }

    public LoginHistory(int loginHistoryID, int userID, Date loginTime, Date logoutTime) {
        this.loginHistoryID = loginHistoryID;
        this.userID = userID;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
    }

    public int getLoginHistoryID() {
        return loginHistoryID;
    }

    public void setLoginHistoryID(int loginHistoryID) {
        this.loginHistoryID = loginHistoryID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }

     @Override
    public String toString() {
        return "LoginHistory{" +
                "loginHistoryID=" + loginHistoryID +
                ", userID=" + userID +
                ", loginTime=" + loginTime +
                ", logoutTime=" + logoutTime +
                "}";
    }
}