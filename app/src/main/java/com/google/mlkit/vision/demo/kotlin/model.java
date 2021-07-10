package com.google.mlkit.vision.demo.kotlin;

public class model {

    private String userid;
    private String day;
    private String year;
    private String month;
    private String hour;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public model(String id, String day, String month, String year,String hour,int count,String type) {

        this.userid = id;
        this.day=day;
        this.month=month;
        this.year=year;
        this.hour=hour;
        this.count=count;
        this.type=type;
    }


    public String getUserid() {
        return userid;
    }

    public void setId(String userid) {
        this.userid = userid;
    }



    public model()
    {

    }
}
