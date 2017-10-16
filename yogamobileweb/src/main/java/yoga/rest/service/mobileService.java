package yoga.rest.service;

import yoga.dao.MemberDao;
import yoga.dao.SubScheduleDao;
import yoga.model.SubSchedule;
import yoga.rest.model.ResponseEntity;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/mobileService")
public class mobileService {
    private Logger logger =  LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MemberDao memberDaoImp;

    @Autowired
    private SubScheduleDao subScheduleDaoImp;

    @RequestMapping("/getCourseList")
    public ResponseEntity getCourseList(@RequestParam(value="dateindex") int dateindex, @RequestParam(value="userId") String userId) {

        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String realDateStr = df.format(DateUtils.addDays(date, dateindex));

        try {
            List<SubSchedule> subSchedules = subScheduleDaoImp.getMobileSubSchedulesByFilter(realDateStr, userId);
            return new ResponseEntity("ok", "查询成功", subSchedules);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping("/insertSubSchedule")
    public ResponseEntity insertSubSchedule(@RequestParam(value="scheduleId") String scheduleId, @RequestParam(value="memberId") String memberId) {

        SubSchedule item = new SubSchedule();
        item.setId(UUID.randomUUID().toString());
        item.setScheduleId(scheduleId);
        item.setMemberId(memberId);
        try {
            subScheduleDaoImp.insertSubSchedule(item);
            return new ResponseEntity("ok", "预约成功", item.getId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping("/deleteSubSchedule")
    public ResponseEntity deleteSubSchedule(@RequestParam(value="scheduleId") String scheduleId, @RequestParam(value="memberId") String memberId) {

        SubSchedule item = new SubSchedule();
        item.setScheduleId(scheduleId);
        item.setMemberId(memberId);
        try {
            subScheduleDaoImp.deleteSubSchedule(item);
            return new ResponseEntity("ok", "预约取消", item.getId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }
}
