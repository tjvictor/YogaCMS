function changeColor(child, parentId){
    $(parentId).css('color','#666');
    $(child).css('color','green');
}

function loadPageToMain(page){
    $('#mainContent').load(page);
}

function loadPageToMessageDialog(page){
    $('#messageDialogContent').load(page);
    $('#messageDialog').css('display','block')
}

function closeMessageDialog(){
    $('#messageDialog').css('display','none');
}

function footTabChange(child, parentId, page){
    loadPageToMain(page);
    changeColor(child, parentId);
}

function subTabClick(child,  parentId, ajaxParam){
    $('#dateindex').val(ajaxParam);
    var userId = checkUserCookie();
    if(userId){
        changeColor(child, parentId);
        ajaxParam += "&userId="+userId;
        callAjax('/mobileService/getCourseList', 'subContent', 'showCourse', '', '', ajaxParam, '.window-mask');
    }
}

function subClick(scheduleId){
    var userId = checkUserCookie();
    if(userId){
        var param = "scheduleId="+scheduleId+"&memberId="+userId;
        callAjax('/mobileService/insertSubSchedule', '', 'subScheduleCallback', '', '', param, '.window-mask');
    }
}

function unSubClick(scheduleId, memberId){
    var userId = checkUserCookie();
    if(userId){
        var param = "scheduleId="+scheduleId+"&memberId="+memberId;
        callAjax('/mobileService/deleteSubSchedule', '', 'subScheduleCallback', '', '', param, '.window-mask');
    }
}

function showCourse(data){
    $('#subContent').html('');
    if(data.status == "ok" && data.callBackData.length > 0){
        for(var i=0; i < data.callBackData.length ; i++){
            var item = data.callBackData[i];
            var ratingHtml = '';
            for(var ratingCount=0; ratingCount<item.courseRating; ratingCount++ )
                ratingHtml += '<img src="res/img/full_rating.png" style="max-width:100%;height:0.4rem;padding:0">';

            var scheduleStatus = item.status;
            var courseTitle = item.courseName;
            var subButtonStr = "预约";
            var subButtonCmd = "subClick('"+item.scheduleId+"')"
            if(item.memberId){
                courseTitle += "(已预约)";
                subButtonStr = "取消";
                subButtonCmd = "unSubClick('"+item.scheduleId+"','"+item.memberId+"')"
            }

            var courseTemp =
            '<div style="width:100%">'+
                '<div style="width:20%;float:left;vertical-align: middle;text-align: center;">'+
                    '<img src="'+item.courseAvatar+'" style="max-width:100%">'+
                '</div>'+
                '<div style="width:80%;float:left">'+
                    '<div>'+courseTitle+'</div>'+
                    '<div style="float:left;vertical-align: middle;text-align: center;">'+
                        ratingHtml+
                    '</div>'+
                    '<div style="width:1rem;vertical-align: middle;text-align: center;float:right;background:green;color:white;border-radius:0.5rem" onclick="'+subButtonCmd+'">'+subButtonStr+'</div>'+
                    '<div style="clear:both;padding:0"></div>'+
                    '<div>教练: '+item.teacherName+'</div>'+
                    '<div>地点: '+item.location+'</div>'+
                    '<div style="float:left">时间 11:30 - 12:30</div>'+
                    '<div style="float:right">已报'+item.subCount+'人 剩余'+item.remainCount+'人</div>'+
                '</div>'+
                '<div style="clear:both;width:100%;height:0.05rem;background-color:white;"></div>'+
            '</div>';
            var currentItem = $('#subContent').html();
            $('#subContent').html(currentItem+courseTemp);
        }
    }else {
        $('#subContent').html("今日无课程");
    }
}

function checkUserCookie(){
    var userId = Cookies.get("userId");
    if(!userId){
        $(mainContent).load("login.html");
        return null;
    }
    return userId;
}

function login(){
    var tel = $('#login_telTxt').val();
    var pwd = $('#login_pwdTxt').val();
    if(tel == '' || pwd == ''){
        $('#globalError p').val("用户名或密码不能为空");
        $('#globalError').show(0, function(){$('#globalError').delay(1000).fadeOut(500);})
        return;
    }
    var param = "tel="+tel+"&pwd="+pwd;
    callAjax('/mobileService/mobileLogin', '', 'loginCallback', '', '', param, '.window-mask');
}

function loginCallback(data){
    if(data.status == "error"){
        $('#globalError p').text(data.prompt);
        $('#globalError').show(0, function(){$('#globalError').delay(1000).fadeOut(500);})
    } else{
        Cookies.set("userId", data.callBackData, { expires: 1 });
        $(mainContent).load("home.html");
    }

}

function subScheduleCallback(data){
    if(data.status == "error"){
        $('#globalError p').text(data.prompt);
        $('#globalError').show(0, function(){$('#globalError').delay(1000).fadeOut(500);})
    } else{
        var userId = checkUserCookie();
        if(userId){
            var ajaxParam = $('#dateindex').val()+"&userId="+userId;
            callAjax('/mobileService/getCourseList', 'subContent', 'showCourse', '', '', ajaxParam, '.window-mask');
        }
    }
}

function getMemberInfo(userId){
    var userId = checkUserCookie();
    if(userId){
        callAjax('/mobileService/getMemberInfo', '', 'getMemberInfoCallback', '', '', '&userId='+userId, '.window-mask');
    }
}

function getMemberInfoCallback(data){
    if(data.status == "error"){
        $('#globalError p').text(data.prompt);
        $('#globalError').show(0, function(){$('#globalError').delay(1000).fadeOut(500);})
    }else{
        var memberInfo = data.callBackData;

        if(memberInfo.sex == '女')
            $('#mi_userName').text(memberInfo.name+" 女士");
        else
            $('#mi_userName').text(memberInfo.name+" 先生");
        $('#mi_userTel').text(memberInfo.tel)
        $('#mi_userAvailableDate').text("注册: "+memberInfo.joinDate + " 截止: " + memberInfo.expireDate);


    }
}

function logout(){
    Cookies.remove("userId");
    $(mainContent).load("home.html");
}

function changePwd(){
    var newPwd = $('#mi_userNewPwd').val();
    var newConPwd = $('#mi_userConfirmNewPwd').val();
    if(newPwd == ''){
        $('#globalError p').text('新密码不能为空');
        $('#globalError').show(0, function(){$('#globalError').delay(1000).fadeOut(500);})
        return;
    }
    if(newPwd != newConPwd){
        $('#globalError p').text('两次输入密码不一致');
        $('#globalError').show(0, function(){$('#globalError').delay(1000).fadeOut(500);})
        return;
    }

    var userId = checkUserCookie();
    if(userId){
        callAjax('/mobileService/changeMemberPwd', '', 'changeMemberPwdCallback', '', '', 'id='+userId+'&newPwd='+newPwd, '.window-mask');
    }
}

function changeMemberPwdCallback(){
    if(data.status == "error"){
        $('#globalError p').text(data.prompt);
        $('#globalError').show(0, function(){$('#globalError').delay(1000).fadeOut(500);})
    } else {
        $('#globalReminder p').text(data.prompt);
        $('#globalReminder').show(0, function(){$('#globalReminder').delay(1000).fadeOut(500);})
    }
}

function getTop5Notification(){
    callAjax('/mobileService/getNotificationBriefByCount', '', 'getTop5NotificationCallback', '', '', 'topCount=5', '');
}

function getTop5NotificationCallback(data){
    $('#notificationView').html('');
    if(data.status == "ok" && data.callBackData.length > 0){
        var notificationTemp = '<ul style="background:rgb(255, 255, 255);border-radius:0.2rem">';
        for(var i=0; i < data.callBackData.length ; i++){
            var item = data.callBackData[i];
            var li_temp = '<li>';
            var idLink = "'type=notification&id="+item.id+"'"
            li_temp += '<a onclick="getMessageContentById('+idLink+')" style="list-style:none;height:0.6rem;line-height:0.6rem;">';
            li_temp += '<span style="float:left;">'+ item.title +'</span>';
            li_temp += '<span style="float:right;">'+ item.date +'</span>';
            li_temp += '</a>';
            li_temp += '</li>';

            notificationTemp += li_temp;
        }

        notificationTemp += '</ul>';
        $('#notificationView').html(notificationTemp);
    }
}

function getMessageContentById(param){
    callAjax('/mobileService/getContentByType', '', 'getContentByTypeCallback', '', '', param, '');
}

function getContentByTypeCallback(data){
    $('#messageDialogContent').html('');
    $('#messageDialog').css('display','block')
    if(data.status == "error"){
        $('#messageDialogContent').html(data.prompt);
    }else {
        $('#messageDialogContent').html(data.callBackData);
    }
}


function getNotificationByIdCallback(data){
    $('#n_page_title').html(data.callBackData.title);
    $('#n_page_content').html(data.callBackData.content);
}

function loadCoachTeamCallback(data){
    $('#coachIconGroup').html('');
    if(data.status == "ok" && data.callBackData.length > 0){
        var teacherTemp = '';

        var divHead = '<div style="width: 100%; margin: 0 auto;vertical-align: middle;text-align: center;background:rgb(255, 255, 255);border-radius:0.2rem; height:2.8rem">';
        var divTail = '</div>';

        for(var i = 0 ; i < data.callBackData.length ; i++){
            var dataItem = data.callBackData[i];
            var idLink = "'type=teacher&id="+dataItem.id+"'"
            teacherTemp += '<a style="width:33%; float:left; vertical-align: middle;text-align: center; height:2.72rem;padding-top:0.5rem" onclick="getMessageContentById('+idLink+')")>';
            teacherTemp += '<div style="width:1.2rem;height:1.2rem;margin: 0 auto;">';
            teacherTemp += '<img style="max-width:100%" src="'+dataItem.avatar+'"/></div>';
            teacherTemp += '<p style="font-size:0.32rem;margin-top:0.2rem">'+dataItem.name+'</p>';
            teacherTemp += '</a>';

            if(i>0 && (i+1)%3 == 0){
                $('#coachIconGroup').html($('#coachIconGroup').html()+divHead+teacherTemp+divTail);
                teacherTemp = '';
            }
        }

        if(teacherTemp != '')
            $('#coachIconGroup').html($('#coachIconGroup').html()+divHead+teacherTemp+divTail);
    }
}

function loadCourseCallback(data){
    $('#courseIconGroup').html('');
    if(data.status == "ok" && data.callBackData.length > 0){
        var courseTemp = '';

        var divHead = '<div style="width: 100%; margin: 0 auto;vertical-align: middle;text-align: center;background:rgb(255, 255, 255);border-radius:0.2rem; height:2.8rem">';
        var divTail = '</div>';

        for(var i = 0 ; i < data.callBackData.length ; i++){
            var dataItem = data.callBackData[i];
            var idLink = "'type=course&id="+dataItem.id+"'"
            courseTemp += '<a style="width:33%; float:left; vertical-align: middle;text-align: center; height:2.72rem;padding-top:0.5rem" onclick="getMessageContentById('+idLink+')")>';
            courseTemp += '<div style="width:1.2rem;height:1.2rem;margin: 0 auto;">';
            courseTemp += '<img style="max-width:100%" src="'+dataItem.avatar+'"/></div>';
            courseTemp += '<p style="font-size:0.32rem;margin-top:0.2rem">'+dataItem.name+'</p>';
            courseTemp += '</a>';

            if(i>0 && (i+1)%3 == 0){
                $('#courseIconGroup').html($('#courseIconGroup').html()+divHead+courseTemp+divTail);
                courseTemp = '';
            }
        }

        if(courseTemp != '')
            $('#courseIconGroup').html($('#courseIconGroup').html()+divHead+courseTemp+divTail);
    }
}