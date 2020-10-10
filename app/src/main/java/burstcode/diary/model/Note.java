package burstcode.diary.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Note implements Serializable {
    private String uid;
    private int hour;
    private int minute;
    private int day;
    private int month;
    private int year;
    private String title;
    private String content;
    private int color;

    public Note(long hour, long minute, long day, long month, long year, String title, String content, long color) {
        this.uid = "";
        this.hour = (int) hour;
        this.minute = (int) minute;
        this.day = (int) day;
        this.month = (int) month;
        this.year = (int) year;
        this.title = title;
        this.content = content;
        this.color = (int) color;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("hour", hour);
        result.put("minute", minute);
        result.put("day", day);
        result.put("month", month);
        result.put("year", year);
        result.put("title", title);
        result.put("content", content);
        result.put("color", color);

        return result;
    }
}
