function changeColor(child, parentId){
    $(parentId).css('color','#666');
    $(child).css('color','green');
}

function footTabChange(child, parentId, page){
    $(mainContent).load(page);
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
        Cookies.set("userId", data.callBackData);
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
        $('#mi_userName').text(memberInfo.name);
        if(memberInfo.sex == '女')
            $('#mi_userSex').text("女士");
        else
            $('#mi_userSex').text("先生");
        $('#mi_userTel').text(memberInfo.tel)
        $('#mi_userAvailableDate').text(memberInfo.joinDate + " --- " + memberInfo.expireDate);


    }
}

function logout(){
    Cookies.remove("userId");
    $(mainContent).load("home.html");
}
