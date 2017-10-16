function changeColor(child, parentId){
    $(parentId).css('color','#666');
    $(child).css('color','green');
}

function footTabChange(child, parentId, page){
    $(mainContent).load(page);
    changeColor(child, parentId);
}

function subTabClick(child,  parentId, ajaxParam){
    ajaxParam += "&userId="+"70e97ede-7266-4d03-9c10-8d97a7fe748a"
    callAjax('/mobileService/getCourseList', 'subContent', 'showCourse', '', '', ajaxParam, '.window-mask');
    changeColor(child, parentId);
}

function subClick(scheduleId, memberId){
    var param = "scheduleId="+scheduleId+"&memberId="+memberId;
    callAjax('/insertSubSchedule', '', '', '', '', param, '.window-mask');

    //$('#globalReminder').show(0, function(){$('#globalReminder').delay(1000).fadeOut(500);})
}

function unSubClick(scheduleId, memberId){
    var param = "scheduleId="+scheduleId+"&memberId="+memberId;
    callAjax('/deleteSubSchedule', '', '', '', '', param, '.window-mask');

    //$('#globalReminder').show(0, function(){$('#globalReminder').delay(1000).fadeOut(500);})
}

function showCourse(data){
    $('#subContent').html('');
    if(data.status == "ok"){
        for(var i=0; i < data.callBackData.length ; i++){
            var item = data.callBackData[i];
            var ratingHtml = '';
            for(var ratingCount=0; ratingCount<item.courseRating; ratingCount++ )
                ratingHtml += '<img src="res/img/full_rating.png" style="max-width:100%;height:0.4rem;padding:0">';

            var courseTitle = item.courseName;
            var subButtonStr = "预约";
            var subButtonCmd = "subClick('"+item.scheduleId+"','"+item.memberId+"')"
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
    }
}