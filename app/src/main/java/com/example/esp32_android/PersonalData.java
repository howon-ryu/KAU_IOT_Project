package com.example.esp32_android;

public class PersonalData {
    private String member_ID;
    private String member_DATE;
    private String member_FD;
    private String member_HUMID;
    private String member_TEMP;
    private String member_WC;


    public String getMember_ID() {
        return member_ID;
    }

    public String getMember_DATE() {
        return member_DATE;
    }

    public String getMember_FD() { return member_FD;}

    public String getMember_HUMID() {
        return member_HUMID;
    }

    public String getMember_TEMP() { return member_TEMP;}

    public String getMember_WC() { return member_WC;}


    public void setMember_ID(String member_name) {
        this.member_ID = member_name;
    }

    public void setMember_DATE(String member_address) {
        this.member_DATE = member_address;
    }

    public void setMember_FD(String member_FD) { this.member_FD = member_FD; }

    public void setMember_HUMID(String member_HUMID) {
        this.member_HUMID = member_HUMID;
    }

    public void setMember_TEMP(String member_TEMP) { this.member_TEMP = member_TEMP;}

    public void setMember_WC(String member_WC) { this.member_WC = member_WC;}

}