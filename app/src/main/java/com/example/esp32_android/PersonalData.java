package com.example.esp32_android;

public class PersonalData {
    private String member_ID;
    private String member_DATE;
    private String member_DUST;
    private String member_HUMID;
    private String member_TEMP;


    public String getMember_ID() {
        return member_ID;
    }

    public String getMember_DATE() {
        return member_DATE;
    }

    public String getMember_DUST() { return member_DUST;}

    public String getMember_HUMID() {
        return member_HUMID;
    }

    public String getMember_TEMP() { return member_TEMP;}


    public void setMember_ID(String member_name) {
        this.member_ID = member_name;
    }

    public void setMember_DATE(String member_address) {
        this.member_DATE = member_address;
    }

    public void setMember_DUST(String member_FD) { this.member_DUST = member_FD; }

    public void setMember_HUMID(String member_HUMID) {
        this.member_HUMID = member_HUMID;
    }

    public void setMember_TEMP(String member_TEMP) { this.member_TEMP = member_TEMP;}


}