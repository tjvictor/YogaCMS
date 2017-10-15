package yoga.model;

import org.apache.commons.lang3.StringUtils;

public class SubSchedule {
    private String id;
    private String scheduleId;
    private String memberId;
    private String dateTime;

    private String courseName;
    private int courseRating;
    private String teacherName;
    private String courseAvatar;
    private String memberName;
    private String memberTel;
    private String memberInfo;
    private String startDateTime;
    private String endDateTime;
    private String location;
    private int capacity;
    private int subCount;
    private String status;

    public String getDate() {
        if(StringUtils.isNotEmpty(startDateTime))
            return startDateTime.split(" ")[0];
        return "";
    }

    public String getStartTime() {
        if(StringUtils.isNotEmpty(startDateTime))
            return startDateTime.split(" ")[1];
        return "";
    }

    public String getEndTime() {
        if(StringUtils.isNotEmpty(endDateTime))
            return endDateTime.split(" ")[1];
        return "";
    }

    public int getRemainCount() { return capacity - subCount;}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getSubCount() {
        return subCount;
    }

    public void setSubCount(int subCount) {
        this.subCount = subCount;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberTel() {
        return memberTel;
    }

    public void setMemberTel(String memberTel) {
        this.memberTel = memberTel;
    }

    public String getMemberInfo() {
        return String.format("%s  -  %s", memberName, memberTel);
    }

    public int getCourseRating() {
        return courseRating;
    }

    public void setCourseRating(int courseRating) {
        this.courseRating = courseRating;
    }

    public String getCourseAvatar() {
        return courseAvatar;
    }

    public void setCourseAvatar(String courseAvatar) {
        this.courseAvatar = courseAvatar;
    }

    public void setMemberInfo(String memberInfo) {
        this.memberInfo = memberInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
