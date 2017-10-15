package yoga.rest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import yoga.dao.*;
import yoga.model.*;
import yoga.rest.model.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/websiteService")
public class websiteService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MemberDao memberDaoImp;

    @Autowired
    private TeacherDao teacherDaoImp;

    @Autowired
    private CourseDao courseDaoImp;

    @Autowired
    private ScheduleDao scheduleDaoImp;

    @Autowired
    private SubScheduleDao subScheduleDaoImp;

    @RequestMapping(value = "/insertMember", method = RequestMethod.POST)
    public ResponseEntity insertMember(@FormParam("name") String name, @FormParam("sex") String sex, @FormParam("tel") String tel,
                                       @FormParam("pwd") String pwd, @FormParam("joinDate") String joinDate, @FormParam("expireDate") String expireDate,
                                       @FormParam("fee") int fee, @FormParam("remark") String remark) {

        try {
            if (memberDaoImp.isMobileExisted(tel)) {
                return new ResponseEntity("error", "此用户的手机号已经注册过了");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }

        Member item = new Member();
        item.setId(UUID.randomUUID().toString());
        item.setName(name);
        item.setSex(sex);
        item.setTel(tel);
        item.setPassword(pwd);
        item.setJoinDate(joinDate);
        item.setExpireDate(expireDate);
        item.setFee(fee);
        item.setRemark(remark);

        try {
            memberDaoImp.insertMember(item);
            return new ResponseEntity("ok", "插入成功", item);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/updateMember", method = RequestMethod.POST)
    public ResponseEntity updateMember(@FormParam("id") String id, @FormParam("name") String name, @FormParam("sex") String sex, @FormParam("tel") String tel,
                                       @FormParam("pwd") String pwd, @FormParam("joinDate") String joinDate, @FormParam("expireDate") String expireDate,
                                       @FormParam("fee") int fee, @FormParam("remark") String remark) {

        try {
            if (!memberDaoImp.isMemberExistedById(id)) {
                return new ResponseEntity("error", "会员不存在");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }

        try {
            if (memberDaoImp.isMobileDuplicated(id, tel)) {
                return new ResponseEntity("error", "电话号码已经存在");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }

        Member item = new Member();
        item.setId(id);
        item.setName(name);
        item.setSex(sex);
        item.setTel(tel);
        item.setPassword(pwd);
        item.setJoinDate(joinDate);
        item.setExpireDate(expireDate);
        item.setFee(fee);
        item.setRemark(remark);

        try {
            memberDaoImp.updateMember(item);
            return new ResponseEntity("ok", "修改成功", item);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/getMembers", method = RequestMethod.GET)
    public ResponseEntity getMembers(@RequestParam("name") String name, @RequestParam("tel") String tel) {
        try {
            List<Member> members = memberDaoImp.getMembers(name, tel);
            return new ResponseEntity("ok", "查询成功", members);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/deleteMember", method = RequestMethod.GET)
    public ResponseEntity deleteMembers(@RequestParam("id") String id) {

        try {
            memberDaoImp.deleteMember(id);
            return new ResponseEntity("ok", "删除成功", id);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }

    @PostMapping("/singleFileUpload/{requestFileName}")
    public ResponseEntity uploadAvatar(@PathVariable String requestFileName, HttpServletRequest request, HttpServletResponse response) {
        StandardMultipartHttpServletRequest fileRequest = (StandardMultipartHttpServletRequest) request;
        if (fileRequest == null) {
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }

        MultipartFile sourceFile = fileRequest.getFile(requestFileName);
        String savePath = request.getSession().getServletContext().getRealPath("/") + "/upload/";
        String saveUrl = request.getContextPath() + "/upload/";

        String randomString = UUID.randomUUID().toString();
        String randomFileName = savePath + randomString;
        String randomFileUrl = saveUrl + randomString;
        File targetFile = new File(randomFileName);
        try (OutputStream f = new FileOutputStream(targetFile)) {
            f.write(sourceFile.getBytes());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }

        return new ResponseEntity("ok", "上传成功", randomFileUrl);
    }


    @RequestMapping(value = "/insertTeacher", method = RequestMethod.POST)
    public ResponseEntity insertTeacher(@FormParam("name") String name, @FormParam("avatar") String avatar, @FormParam("introduction") String introduction) {

        Teacher item = new Teacher();
        item.setId(UUID.randomUUID().toString());
        item.setName(name);
        item.setAvatar(avatar);
        item.setIntroduction(introduction);
        try {
            teacherDaoImp.insertTeacher(item);
            return new ResponseEntity("ok", "插入成功", item);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }


    @RequestMapping(value = "/getTeachers", method = RequestMethod.GET)
    public ResponseEntity getTeachers() {

        try {
            List<Teacher> teachers = teacherDaoImp.getTeachers();
            return new ResponseEntity("ok", "查询成功", teachers);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/updateTeacher", method = RequestMethod.POST)
    public ResponseEntity updateTeacher(@FormParam("id") String id, @FormParam("name") String name, @FormParam("avatar") String avatar, @FormParam("introduction") String introduction) {

        Teacher item = new Teacher();
        item.setId(id);
        item.setName(name);
        item.setAvatar(avatar);
        item.setIntroduction(introduction);
        try {
            teacherDaoImp.updateTeacher(item);
            return new ResponseEntity("ok", "修改成功", item);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/deleteTeacher", method = RequestMethod.GET)
    public ResponseEntity deleteTeacher(@RequestParam("id") String id) {

        try {
            teacherDaoImp.deleteTeacher(id);
            return new ResponseEntity("ok", "删除成功", id);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/insertCourse", method = RequestMethod.POST)
    public ResponseEntity insertCourse(@FormParam("name") String name, @FormParam("avatar") String avatar,
                                       @FormParam("introduction") String introduction, @FormParam("rating") int rating) {

        Course item = new Course();
        item.setId(UUID.randomUUID().toString());
        item.setName(name);
        item.setAvatar(avatar);
        item.setIntroduction(introduction);
        item.setRating(rating);
        try {
            courseDaoImp.insertCourse(item);
            return new ResponseEntity("ok", "插入成功", item);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }


    @RequestMapping(value = "/getCourses", method = RequestMethod.GET)
    public ResponseEntity getCourses() {

        try {
            List<Course> courses = courseDaoImp.getCourses();
            return new ResponseEntity("ok", "查询成功", courses);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/updateCourse", method = RequestMethod.POST)
    public ResponseEntity updateCourse(@FormParam("id") String id, @FormParam("name") String name, @FormParam("avatar") String avatar,
                                       @FormParam("introduction") String introduction, @FormParam("rating") int rating) {

        Course item = new Course();
        item.setId(id);
        item.setName(name);
        item.setAvatar(avatar);
        item.setIntroduction(introduction);
        item.setRating(rating);
        try {
            courseDaoImp.updateCourse(item);
            return new ResponseEntity("ok", "修改成功", item);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/deleteCourse", method = RequestMethod.GET)
    public ResponseEntity deleteCourse(@RequestParam("id") String id) {

        try {
            courseDaoImp.deleteCourse(id);
            return new ResponseEntity("ok", "删除成功", id);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/insertSchedule", method = RequestMethod.POST)
    public ResponseEntity insertSchedule(@FormParam("teacherId") String teacherId, @FormParam("courseId") String courseId,
                                         @FormParam("location") String location, @FormParam("capacity") int capacity,
                                         @FormParam("dateList") String dateList, @FormParam("startTime") String startTime,
                                         @FormParam("endTime") String endTime, @FormParam("status") String status) {

        List<Schedule> scheduleList = new ArrayList<Schedule>();
        String[] dates = dateList.split(",");
        for (String dateItem : dates) {
            Schedule scheduleItem = new Schedule();
            scheduleItem.setId(UUID.randomUUID().toString());
            scheduleItem.setTeacherId(teacherId);
            scheduleItem.setCourseId(courseId);
            scheduleItem.setLocation(location);
            scheduleItem.setCapacity(capacity);
            scheduleItem.setStatus(status);
            scheduleItem.setStartDateTime(String.format("%s %s", dateItem, startTime));
            scheduleItem.setEndDateTime(String.format("%s %s", dateItem, endTime));
            scheduleList.add(scheduleItem);
        }

        try {
            for (Schedule item : scheduleList)
                scheduleDaoImp.insertSchedule(item);
            return new ResponseEntity("ok", "插入成功, 请刷新查看", null);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }


    @RequestMapping(value = "/getSchedules", method = RequestMethod.GET)
    public ResponseEntity getSchedules(@RequestParam("courseId") String courseId, @RequestParam("teacherId") String teacherId,
                                       @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {

        try {
            List<Schedule> schedules = scheduleDaoImp.getSchedulesByFilter(courseId, teacherId, startDate, endDate);
            return new ResponseEntity("ok", "查询成功", schedules);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/updateSchedule", method = RequestMethod.POST)
    public ResponseEntity updateSchedule(@FormParam("id") String id, @FormParam("teacherId") String teacherId, @FormParam("courseId") String courseId,
                                         @FormParam("location") String location, @FormParam("capacity") int capacity,
                                         @FormParam("dateList") String dateList, @FormParam("startTime") String startTime,
                                         @FormParam("endTime") String endTime, @FormParam("status") String status) {


        List<Schedule> scheduleList = new ArrayList<Schedule>();
        String[] dates = dateList.split(",");
        for (String dateItem : dates) {
            Schedule scheduleItem = new Schedule();
            scheduleItem.setId(UUID.randomUUID().toString());
            scheduleItem.setTeacherId(teacherId);
            scheduleItem.setCourseId(courseId);
            scheduleItem.setLocation(location);
            scheduleItem.setCapacity(capacity);
            scheduleItem.setStatus(status);
            scheduleItem.setStartDateTime(String.format("%s %s", dateItem, startTime));
            scheduleItem.setEndDateTime(String.format("%s %s", dateItem, endTime));
            scheduleList.add(scheduleItem);
        }

        try {
            scheduleDaoImp.physicalDeleteSchedule(id);
            for (Schedule item : scheduleList)
                scheduleDaoImp.insertSchedule(item);
            return new ResponseEntity("ok", "修改成功, 请刷新查看", id);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/deleteSchedule", method = RequestMethod.GET)
    public ResponseEntity deleteSchedule(@RequestParam("id") String id) {

        try {
            scheduleDaoImp.deleteSchedule(id);
            return new ResponseEntity("ok", "删除成功", id);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/insertSubSchedule", method = RequestMethod.POST)
    public ResponseEntity insertSubSchedule(@FormParam("scheduleId") String scheduleId, @FormParam("memberId") String memberId) {

        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SubSchedule subScheduleItem = new SubSchedule();
        subScheduleItem.setId(UUID.randomUUID().toString());
        subScheduleItem.getSchedule().setId(scheduleId);
        subScheduleItem.getMember().setId(memberId);
        subScheduleItem.setDateTime(sdf.format(new Date()));

        try {
            subScheduleDaoImp.insertSubSchedule(subScheduleItem);
            return new ResponseEntity("ok", "插入成功, 请刷新查看", subScheduleItem);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }*/
        return new ResponseEntity("ok", "修改成功, 请刷新查看", null);
    }


    @RequestMapping(value = "/getSubSchedules", method = RequestMethod.GET)
    public ResponseEntity getSubSchedules(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {

        try {
            List<SubSchedule> subSchedules = subScheduleDaoImp.getSubSchedulesByFilter(startDate, endDate);
            return new ResponseEntity("ok", "查询成功", subSchedules);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }


    @RequestMapping(value = "/getSubScheduledMembers", method = RequestMethod.GET)
    public ResponseEntity getSubScheduledMembers(@RequestParam("subScheduleId") String subScheduleId) {

        try {
            List<SubSchedule> subSchedules = subScheduleDaoImp.getSubScheduledMembers(subScheduleId);
            return new ResponseEntity("ok", "查询成功", subSchedules);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/updateSubSchedule", method = RequestMethod.POST)
    public ResponseEntity updateSubSchedule(@FormParam("id") String id, @FormParam("scheduleId") String scheduleId, @FormParam("memberId") String memberId) {

        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SubSchedule subScheduleItem = new SubSchedule();
        subScheduleItem.setId(id);
        subScheduleItem.getSchedule().setId(scheduleId);
        subScheduleItem.getMember().setId(memberId);
        subScheduleItem.setDateTime(sdf.format(new Date()));*/

        /*try {
            //subScheduleDaoImp.insertSubSchedule(subScheduleItem);
            return new ResponseEntity("ok", "修改成功, 请刷新查看", null);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }*/
        return new ResponseEntity("ok", "修改成功, 请刷新查看", null);
    }

    @RequestMapping(value = "/deleteSubSchedule", method = RequestMethod.GET)
    public ResponseEntity deleteSubSchedule(@RequestParam("id") String id) {

        try {
            subScheduleDaoImp.deleteSubSchedule(id);
            return new ResponseEntity("ok", "删除成功", id);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }
}