/*Common Parameter*/
var notificationKindeditor;
var teacherKindeditor;
var courseKindeditor;
var memberKindeditor;
/*Common Parameter*/


/*member region*/
function memberSearch(){
    var name = "name="+$('#nameViewTxt').val();
    var tel = "&tel="+$('#telViewTxt').val();

    callAjax('/websiteService/getMembers', '', 'memberSearchCallback', '', '', name+tel, '.window-mask')
}

function clearMember(){
    if(!memberKindeditor){
        memberKindeditor = KindEditor.create('#m_remarkTxt',{
            items: [
                    'undo', 'redo', '|', 'preview', 'cut', 'copy', 'paste',
                    'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
                    'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                    'superscript', 'quickformat', 'selectall', '|', 
                    'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
                    'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|',
                    'table', 'hr', 'emoticons', 'pagebreak',
                    'anchor', 'link', 'unlink'
                    ],
            width: "100%",
            height: "100px",
            resizeType : 0,
            }
        );
    }
    $('#m_idTxt').val('');
    $('#m_nameTxt').textbox('setValue', '');
    $('#m_telTxt').textbox('setValue', '');
    $('#m_passTxt').textbox('setValue', '');
    $('#m_joinDateTxt').textbox('setValue', '');
    $('#m_expireDateTxt').textbox('setValue', '');
    $('#m_feeTxt').textbox('setValue', '0');
    /*$('#m_remarkTxt').textbox('setValue', '');*/
    memberKindeditor.html('');
    $('#addMemberBtn').css('display','block');
    $('#updateMemberBtn').css('display','block');
}

function openMemberPanel(mode){
    clearMember();
    if(mode == "add"){
        $('#memberUpdateView').dialog('open');
        $('#updateMemberBtn').css('display','none');
    }
    else if(mode == 'edit'){
        var row = $('#memberView').datagrid('getSelected');
        if(row){
            $('#memberUpdateView').dialog('open');
            $('#m_idTxt').val(row.id);
            $('#m_nameTxt').textbox('setValue', row.name);
            $('#m_sexSelect').textbox('setValue', row.sex);
            $('#m_telTxt').textbox('setValue', row.tel);
            $('#m_passTxt').textbox('setValue', row.password);
            $('#m_joinDateTxt').textbox('setValue', row.joinDate);
            $('#m_expireDateTxt').textbox('setValue', row.expireDate);
            $('#m_feeTxt').textbox('setValue', row.fee);
            /*$('#m_remarkTxt').textbox('setValue', $('<div>').html(row.remark).text());*/
            memberKindeditor.html(row.remark);
            $('#addMemberBtn').css('display','none');
        }
    }
}

function updateMember() {

    var id = $('#m_idTxt').val();
    var name = $('#m_nameTxt').textbox('getValue');
    var sex = $('#m_sexSelect').textbox('getValue');
    var tel = $('#m_telTxt').textbox('getValue');
    var pwd = $('#m_passTxt').textbox('getValue');
    var joinDate = $('#m_joinDateTxt').textbox('getValue');
    var expireDate = $('#m_expireDateTxt').textbox('getValue');
    var fee = $('#m_feeTxt').textbox('getValue');
    /*var remark = $('#m_remarkTxt').textbox('getValue');*/
    var remark = memberKindeditor.html();

    var postValue = {
        "id": id,
        "name": name,
        "sex": sex,
        "tel": tel,
        "pwd": pwd,
        "joinDate": joinDate,
        "expireDate": expireDate,
        "fee": fee,
        "remark": remark
    };

    callAjax('/websiteService/updateMember', '', 'updateMemberCallback', '', 'POST', postValue, '.window-mask');
}

function deleteMember(){
    var row = $('#memberView').datagrid('getSelected');
    if(row){
        $.messager.confirm('删除会员', '确认删除会员:'+ row.name +'吗?',
            function(result) {
                if (result) {
                    callAjax('/websiteService/deleteMember', '', 'memberDeleteCallback', '', '', 'id='+row.id, '.window-mask');
                }
            }
        );
    }
}

function addMember() {

    var name = $('#m_nameTxt').textbox('getValue');
    var sex = $('#m_sexSelect').textbox('getValue');
    var tel = $('#m_telTxt').textbox('getValue');
    var pwd = $('#m_passTxt').textbox('getValue');
    var joinDate = $('#m_joinDateTxt').textbox('getValue');
    var expireDate = $('#m_expireDateTxt').textbox('getValue');
    var fee = $('#m_feeTxt').textbox('getValue');
    /*var remark = $('#m_remarkTxt').textbox('getValue');*/
    var remark = memberKindeditor.html();

    var postValue = {
        "name": name,
        "sex": sex,
        "tel": tel,
        "pwd": pwd,
        "joinDate": joinDate,
        "expireDate": expireDate,
        "fee": fee,
        "remark": remark
    };

    callAjax('/websiteService/insertMember', '', 'addMemberCallback', '', 'POST', postValue, '.window-mask');

}

function memberSearchCallback(data) {
    $.messager.show({
        title: '操作提示',
        msg: data.prompt,
        timeout: 5000,
    });
    if (data.status == "ok") {
        if (data.callBackData) $('#memberView').datagrid('loadData', data.callBackData);
    }
}

function memberDeleteCallback(data){
    $.messager.show({
        title: '操作提示',
        msg: data.prompt,
        timeout: 5000,
    });
    if(data.status == "ok"){
        var rowIndex = $('#memberView').datagrid('getRowIndex', data.callBackData);
        $('#memberView').datagrid('deleteRow', rowIndex);
    }
}

function addMemberCallback(data){
    $.messager.show({
            title: '操作提示',
            msg: data.prompt,
            timeout: 5000,
        });
        if(data.status == "ok"){
            $('#memberView').datagrid('insertRow', {index : 0, row : data.callBackData});
            $('#memberUpdateView').dialog('close');
        }
}

function updateMemberCallback(data){
    $.messager.show({
            title: '操作提示',
            msg: data.prompt,
            timeout: 5000,
        });
        if(data.status == "ok"){
            var rowIndex = $('#memberView').datagrid('getRowIndex', data.callBackData.id);
            $('#memberView').datagrid('updateRow', {index : rowIndex, row : data.callBackData});
            $('#memberUpdateView').dialog('close');
        }
}
/*member region*/

/*teacher region*/
function teacherSearch(){

    callAjax('/websiteService/getTeachers', '', 'teacherSearchCallback', '', '', '', '.window-mask')
}

function teacherSearchCallback(data) {
    $.messager.show({
        title: '操作提示',
        msg: data.prompt,
        timeout: 5000,
    });
    if (data.status == "ok") {
        if (data.callBackData) $('#teacherView').datagrid('loadData', data.callBackData);
    }
}

function clearTeacher(){
    if(!teacherKindeditor){
        teacherKindeditor = KindEditor.create('#t_introTxt',{
            items: [
                    'undo', 'redo', '|', 'preview', 'cut', 'copy', 'paste',
                    'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
                    'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                    'superscript', 'quickformat', 'selectall', '|', 
                    'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
                    'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|',
                    'table', 'hr', 'emoticons', 'pagebreak',
                    'anchor', 'link', 'unlink'],
            width: "100%",
            height: "200px",
            resizeType : 0,
            }
        );
    }
    $('#t_idTxt').val('');
    $('#t_nameTxt').textbox('setValue', '');
    $('#t_avatarImg').attr('src', '../res/img/avatar.png');
    /*$('#t_introTxt').textbox('setValue', '');*/
    teacherKindeditor.html('');
    $('#addTeacherBtn').css('display','block');
    $('#updateTeacherBtn').css('display','block');
}

function uploadAvatar(fileElementId, imgId) {
    $('#windowsMaskDiv').css('display', 'block');
    $.ajaxFileUpload({
        url: '/websiteService/singleFileUpload/'+fileElementId,
        secureuri: false,
        dataType: 'json',
        fileElementId: fileElementId,
        success: function(data, status)
        {
            if(data.status == "ok")
                $('#'+imgId).attr('src', data.callBackData);
        },
        error: function(data, status, e) {alert(e);},
        complete: function(data) { $('#windowsMaskDiv').css('display', 'none'); }
    });
}

function openTeacherPanel(mode){
    clearTeacher();
    if(mode == "add"){
        $('#teacherUpdateView').dialog('open');
        $('#updateTeacherBtn').css('display','none');
    }
    else if(mode == 'edit'){
        var row = $('#teacherView').datagrid('getSelected');
        if(row){
            $('#teacherUpdateView').dialog('open');
            $('#t_idTxt').val(row.id);
            $('#t_nameTxt').textbox('setValue', row.name);
            $('#t_avatarImg').attr('src', row.avatar);
            /*$('#t_introTxt').textbox('setValue', $('<div>').html(row.introduction).text());*/
            teacherKindeditor.html(row.introduction);
            $('#addTeacherBtn').css('display','none');
        }
    }
}

function addTeacher() {
    var name = $('#t_nameTxt').textbox('getValue');
    var img = $('#t_avatarImg').attr('src');
    /*var intro = $('#t_introTxt').textbox('getValue');*/
    var intro = teacherKindeditor.html();

    var postValue = {
        "name": name,
        "avatar": img,
        "introduction": intro
    };

    callAjax('/websiteService/insertTeacher', '', 'addTeacherCallback', '', 'POST', postValue, '.window-mask');
}

function updateTeacher(){
        var id = $('#t_idTxt').val();
        var name = $('#t_nameTxt').textbox('getValue');
        var img = $('#t_avatarImg').attr('src');
        /*var intro = $('#t_introTxt').textbox('getValue');*/
        var intro = teacherKindeditor.html();

        var postValue = {
            "id" : id,
            "name": name,
            "avatar": img,
            "introduction": intro
        };

        callAjax('/websiteService/updateTeacher', '', 'updateTeacherCallback', '', 'POST', postValue, '.window-mask')
}

function addTeacherCallback(data) {
    $.messager.show({
        title: '操作提示',
        msg: data.prompt,
        timeout: 5000,
    });
    if (data.status == "ok") {
        $('#teacherView').datagrid('insertRow', {
            index: 0,
            row: data.callBackData
        });
        $('#teacherUpdateView').dialog('close');
    }
}

function updateTeacherCallback(data){
    $.messager.show({
            title: '操作提示',
            msg: data.prompt,
            timeout: 5000,
        });
        if(data.status == "ok"){
            var rowIndex = $('#teacherView').datagrid('getRowIndex', data.callBackData.id);
            $('#teacherView').datagrid('updateRow', {index : rowIndex, row : data.callBackData});
            $('#teacherUpdateView').dialog('close');
        }
}

function deleteTeacher(){
    var row = $('#teacherView').datagrid('getSelected');
    if(row){
        $.messager.confirm('删除教练', '确认删除教练:'+ row.name +'吗?',
            function(result) {
                if (result) {
                    callAjax('/websiteService/deleteTeacher', '', 'teacherDeleteCallback', '', '', 'id='+row.id, '.window-mask');
                }
            }
        );
    }
}

function teacherDeleteCallback(data){
    $.messager.show({
        title: '操作提示',
        msg: data.prompt,
        timeout: 5000,
    });
    if(data.status == "ok"){
        var rowIndex = $('#teacherView').datagrid('getRowIndex', data.callBackData);
        $('#teacherView').datagrid('deleteRow', rowIndex);
    }
}
/*teacher region*/

/*course region*/
function courseSearch(){

    callAjax('/websiteService/getCourses', '', 'courseSearchCallback', '', '', '', '.window-mask')
}

function courseSearchCallback(data) {
    $.messager.show({
        title: '操作提示',
        msg: data.prompt,
        timeout: 5000,
    });
    if (data.status == "ok") {
        if (data.callBackData) $('#courseView').datagrid('loadData', data.callBackData);
    }
}

function clearCourse(){
    if(!courseKindeditor){
        courseKindeditor = KindEditor.create('#c_introTxt',{
            items: [
                    'undo', 'redo', '|', 'preview', 'cut', 'copy', 'paste',
                    'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
                    'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                    'superscript', 'quickformat', 'selectall', '|', 
                    'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
                    'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|',
                    'table', 'hr', 'emoticons', 'pagebreak',
                    'anchor', 'link', 'unlink'],
            width: "100%",
            height: "100px",
            resizeType : 0,
            }
        );
    }
    $('#c_idTxt').val('');
    $('#c_nameTxt').textbox('setValue', '');
    $('#c_avatarImg').attr('src', '../res/img/avatar.png');
    /*$('#c_introTxt').textbox('setValue', '');*/
    courseKindeditor.html('');
    $('#addCourseBtn').css('display','block');
    $('#updateCourseBtn').css('display','block');
}

function openCoursePanel(mode){
    clearCourse();
    if(mode == "add"){
        $('#courseUpdateView').dialog('open');
        $('#updateCourseBtn').css('display','none');
    }
    else if(mode == 'edit'){
        var row = $('#courseView').datagrid('getSelected');
        if(row){
            $('#courseUpdateView').dialog('open');
            $('#c_idTxt').val(row.id);
            $('#c_nameTxt').textbox('setValue', row.name);
            $('#c_avatarImg').attr('src', row.avatar);
            $('#c_rating').combobox('select', row.rating);
            /*$('#c_introTxt').textbox('setValue', $('<div>').html(row.introduction).text());*/
            courseKindeditor.html(row.introduction);
            $('#addCourseBtn').css('display','none');
        }
    }
}

function addCourse() {
    var name = $('#c_nameTxt').textbox('getValue');
    var img = $('#c_avatarImg').attr('src');
    /*var intro = $('#c_introTxt').textbox('getValue');*/
    var intro = courseKindeditor.html();
	var rating = $('#c_rating').textbox('getValue');

    var postValue = {
        "name": name,
        "avatar": img,
        "introduction": intro,
		"rating": rating
    };

    callAjax('/websiteService/insertCourse', '', 'addCourseCallback', '', 'POST', postValue, '.window-mask');
}

function updateCourse(){
        var id = $('#c_idTxt').val();
        var name = $('#c_nameTxt').textbox('getValue');
        var img = $('#c_avatarImg').attr('src');
        /*var intro = $('#c_introTxt').textbox('getValue');*/
        var intro = courseKindeditor.html();
		var rating = $('#c_rating').textbox('getValue');

        var postValue = {
            "id" : id,
            "name": name,
            "avatar": img,
            "introduction": intro,
			"rating": rating
        };

        callAjax('/websiteService/updateCourse', '', 'updateCourseCallback', '', 'POST', postValue, '.window-mask')
}

function addCourseCallback(data) {
    $.messager.show({
        title: '操作提示',
        msg: data.prompt,
        timeout: 5000,
    });
    if (data.status == "ok") {
        $('#courseView').datagrid('insertRow', {
            index: 0,
            row: data.callBackData
        });
        $('#courseUpdateView').dialog('close');
    }
}

function updateCourseCallback(data){
    $.messager.show({
            title: '操作提示',
            msg: data.prompt,
            timeout: 5000,
        });
        if(data.status == "ok"){
            var rowIndex = $('#courseView').datagrid('getRowIndex', data.callBackData.id);
            $('#courseView').datagrid('updateRow', {index : rowIndex, row : data.callBackData});
            $('#courseUpdateView').dialog('close');
        }
}

function deleteCourse(){
    var row = $('#courseView').datagrid('getSelected');
    if(row){
        $.messager.confirm('删除课程', '确认删除课程:'+ row.name +'吗?',
            function(result) {
                if (result) {
                    callAjax('/websiteService/deleteCourse', '', 'courseDeleteCallback', '', '', 'id='+row.id, '.window-mask');
                }
            }
        );
    }
}

function courseDeleteCallback(data){
    $.messager.show({
        title: '操作提示',
        msg: data.prompt,
        timeout: 5000,
    });
    if(data.status == "ok"){
        var rowIndex = $('#courseView').datagrid('getRowIndex', data.callBackData);
        $('#courseView').datagrid('deleteRow', rowIndex);
    }
}
/*course region*/

/*schedule region*/
function scheduleSearch(){
    var courseId = "courseId="+$('#s_courseViewSelect').combobox('getValue');
    var teacherId = "&teacherId="+$('#s_teacherViewSelect').combobox('getValue');
    var startDate = "&startDate="+$('#s_startDateViewTxt').textbox('getValue');
    var endDate = "&endDate="+$('#s_endDateViewTxt').textbox('getValue');

    callAjax('/websiteService/getSchedules', '', 'scheduleSearchCallback', '', '', courseId+teacherId+startDate+endDate, '.window-mask')
}

function resetScheduleSearch(){
    $('#s_courseViewSelect').combobox('setValue', '');
    $('#s_teacherViewSelect').combobox('setValue', '');
    $('#s_startDateViewTxt').textbox('setValue', '');
    $('#s_endDateViewTxt').textbox('setValue', '');
}

function scheduleSearchCallback(data) {
    $.messager.show({
        title: '操作提示',
        msg: data.prompt,
        timeout: 5000,
    });
    if (data.status == "ok") {
        if (data.callBackData) $('#scheduleView').datagrid('loadData', data.callBackData);
    }
}

function clearSchedule(){

    $('#s_idTxt').val('');
    searchAllCourse();
    searchAllTeacher();
    $('#s_status').combobox('select', '开放');
    $('#s_capcityTxt').textbox('setValue', '3');
    var currentDate = new Date();
    var dateString = currentDate.getFullYear()+"-"+(currentDate.getMonth()+1)+"-"+currentDate.getDate();
    $('#s_calendarbox').tagbox({value: dateString});
    $('#addScheduleBtn').css('display','block');
    $('#updateScheduleBtn').css('display','block');
    $('#s_viewMode').css('display','');
    $('#s_editMode').css('display','');
}

function openSchedulePanel(mode){
    clearSchedule();
    if(mode == "add"){
        $('#scheduleUpdateView').dialog('open');
        $('#updateScheduleBtn').css('display','none');
        $('#s_editMode').css('display','none');
    }
    else if(mode == 'edit'){
        var row = $('#scheduleView').datagrid('getSelected');
        if(row){
            $('#scheduleUpdateView').dialog('open');

            $('#s_idTxt').val(row.id);
            $('#s_teacherSelect').combobox('select', row.teacherId);
            $('#s_courseSelect').combobox('select', row.courseId);
            $('#s_locationSelect').combobox('select', row.location);
            $('#s_capacityTxt').textbox('setValue', row.capcity);
            $('#s_calendar_edit').calendar({'current': new Date(Date.parse(row.date.replace(/-/g,"/")))});
            $('#s_calendarbox_edit').tagbox({value: row.date});
            $('#s_startTimeHourSelect').combobox('select', row.startTime.split(":")[0])
            $('#s_startTimeMinuteSelect').combobox('select', row.startTime.split(":")[1]);
            $('#s_endTimeHourSelect').combobox('select', row.endTime.split(":")[0])
            $('#s_endTimeMinuteSelect').combobox('select', row.endTime.split(":")[1]);
            $('#s_status').combobox('select', row.status);

            $('#addScheduleBtn').css('display','none');
            $('#s_viewMode').css('display','none');
        }
    }
}

function addSchedule() {
    var teacherId = $('#s_teacherSelect').combobox('getValue');
    var courseId = $('#s_courseSelect').combobox('getValue');
    var location = $('#s_locationSelect').combobox('getValue');
	var capacity = $('#s_capcityTxt').textbox('getValue');
	var dateList = getValuesFromTagbox('s_calendarbox').join(",");
	var startTime = $('#s_startTimeHourSelect').combobox('getValue')+':'+$('#s_startTimeMinuteSelect').combobox('getValue');
	var endTime = $('#s_endTimeHourSelect').combobox('getValue')+':'+$('#s_endTimeMinuteSelect').combobox('getValue');
	var status = $('#s_status').combobox('getValue');

    var postValue = {
        "teacherId": teacherId,
        "courseId": courseId,
        "location": location,
		"capacity" : capacity,
		"dateList" : dateList,
		"startTime" : startTime,
		"endTime" : endTime,
		"status" : status
    };

    callAjax('/websiteService/insertSchedule', '', 'addScheduleCallback', '', 'POST', postValue, '.window-mask');
}

function updateSchedule(){
    var id = $('#s_idTxt').val();
    var teacherId = $('#s_teacherSelect').combobox('getValue');
    var courseId = $('#s_courseSelect').combobox('getValue');
    var location = $('#s_locationSelect').combobox('getValue');
	var capacity = $('#s_capcityTxt').textbox('getValue');
	var dateList = getValuesFromTagbox('s_calendarbox_edit').join(",");
	var startTime = $('#s_startTimeHourSelect').combobox('getValue')+':'+$('#s_startTimeMinuteSelect').combobox('getValue');
	var endTime = $('#s_endTimeHourSelect').combobox('getValue')+':'+$('#s_endTimeMinuteSelect').combobox('getValue');
	var status = $('#s_status').combobox('getValue');

    var postValue = {
        "id" : id,
        "teacherId": teacherId,
        "courseId": courseId,
        "location": location,
		"capacity" : capacity,
		"dateList" : dateList,
		"startTime" : startTime,
		"endTime" : endTime,
		"status" : status
    };

    callAjax('/websiteService/updateSchedule', '', 'updateScheduleCallback', '', 'POST', postValue, '.window-mask')
}

function addScheduleCallback(data) {
    $.messager.show({
        title: '操作提示',
        msg: data.prompt,
        timeout: 5000,
    });
    if(data.status == "ok")
        $('#scheduleUpdateView').dialog('close');
        scheduleSearch();
}

function updateScheduleCallback(data){
    $.messager.show({
            title: '操作提示',
            msg: data.prompt,
            timeout: 5000,
        });
        if(data.status == "ok"){
            var rowIndex = $('#scheduleView').datagrid('getRowIndex', data.callBackData.id);
            $('#scheduleView').datagrid('updateRow', {index : rowIndex, row : data.callBackData});
            $('#scheduleUpdateView').dialog('close');
        }
}

function deleteSchedule(){
    var row = $('#scheduleView').datagrid('getSelected');
    if(row){
        $.messager.confirm('删除课程安排', '确认删除此课程安排吗?',
            function(result) {
                if (result) {
                    callAjax('/websiteService/deleteSchedule', '', 'scheduleDeleteCallback', '', '', 'id='+row.id, '.window-mask');
                }
            }
        );
    }
}

function scheduleDeleteCallback(data){
    $.messager.show({
        title: '操作提示',
        msg: data.prompt,
        timeout: 5000,
    });
    if(data.status == "ok"){
        var rowIndex = $('#scheduleView').datagrid('getRowIndex', data.callBackData);
        $('#scheduleView').datagrid('deleteRow', rowIndex);
    }
}

//this is for edit page combobox
function searchAllCourse(){
    callAjax('/websiteService/getCourses', '', 'searchAllCourseCallback', '', '', '', '.window-mask');
}

function searchAllCourseCallback(data){
    if(data.status == "ok"){
        $('#s_courseSelect').combobox('loadData', data.callBackData)
    }
}

function searchAllTeacher(){
    callAjax('/websiteService/getTeachers', '', 'searchAllTeacherCallback', '', '', '', '.window-mask');
}

function searchAllTeacherCallback(data){
    if(data.status == "ok"){
        $('#s_teacherSelect').combobox('loadData', data.callBackData)
    }
}
//this is for edit page combobox


//this is for tool view page combobox
function searchAllCourseForTool(){
    callAjax('/websiteService/getCourses', '', 'searchAllCourseForToolCallback', '', '', '', '.window-mask');
}

function searchAllCourseForToolCallback(data){
    if(data.status == "ok"){
        $('#s_courseViewSelect').combobox('loadData', data.callBackData)
    }
}

function searchAllTeacherForTool(){
    callAjax('/websiteService/getTeachers', '', 'searchAllTeacherForToolCallback', '', '', '', '.window-mask');
}

function searchAllTeacherForToolCallback(data){
    if(data.status == "ok"){
        $('#s_teacherViewSelect').combobox('loadData', data.callBackData)
    }
}
//this is for tool view page combobox
/*schedule region*/

/*subSchedule region*/
function subScheduleSearch(){
    var startDate = "startDate="+$('#sub_startDateViewTxt').val();
    var endDate = "&endDate="+$('#sub_endDateViewTxt').val();

    callAjax('/websiteService/getSubSchedules', '', 'subScheduleSearchCallback', '', '', startDate+endDate, '.window-mask')
}

function resetSubScheduleSearch(){

}

function clearSubSchedule(){
        $('#sub_idTxt').val('');
        $('#sub_courseNameTxt').textbox('setValue', '');
        $('#sub_teacherNameTxt').textbox('setValue', '');
        $('#sub_locationTxt').textbox('setValue', '');
        $('#sub_dateTimeTxt').textbox('setValue', '');
        $('#sub_subCountTxt').textbox('setValue', '0');
        $('#sub_remainCountTxt').textbox('setValue', '0');
}

function openSubSchedulePanel(mode){
    clearSubSchedule();
    if(mode == "add"){
        $('#subScheduleUpdateView').dialog('open');
        $('#updateSubScheduleBtn').css('display','none');
    }
    else if(mode == 'edit'){
        var row = $('#subScheduleView').datagrid('getSelected');
        if(row){
            $('#subScheduleUpdateView').dialog('open');
            $('#sub_idTxt').val(row.id);
            $('#sub_nameTxt').textbox('setValue', row.name);
            $('#sub_sexSelect').textbox('setValue', row.sex);
            $('#sub_telTxt').textbox('setValue', row.tel);
            $('#sub_passTxt').textbox('setValue', row.password);
            $('#sub_joinDateTxt').textbox('setValue', row.joinDate);
            $('#sub_expireDateTxt').textbox('setValue', row.expireDate);
            $('#sub_feeTxt').textbox('setValue', row.fee);
            $('#sub_remarkTxt').textbox('setValue', $('<div>').html(row.remark).text());
            $('#addSubScheduleBtn').css('display','none');
        }
    }
    else if(mode == 'view'){
        var row = $('#subScheduleView').datagrid('getSelected');
        if(row){
            $('#subScheduleUpdateView').dialog('open');
            $('#sub_idTxt').val(row.scheduleId);
            $('#sub_courseNameTxt').textbox('setValue', row.courseName);
            $('#sub_teacherNameTxt').textbox('setValue', row.teacherName);
            $('#sub_locationTxt').textbox('setValue', row.location);
            $('#sub_dateTimeTxt').textbox('setValue', row.date + ' ' + row.startTime + ' - ' + row.endTime);
            $('#sub_subCountTxt').textbox('setValue', row.subCount);
            $('#sub_remainCountTxt').textbox('setValue', row.remainCount);
            getSubScheduledMembers("subScheduleId="+row.scheduleId);

        }
    }
}

function getSubScheduledMembers(subScheduleId){
    callAjax('/websiteService/getSubScheduledMembers', '', 'getSubScheduledMembersCallback', '', '', subScheduleId, '.window-mask');
}

function getSubScheduledMembersCallback(data){
    if (data.status == "ok") {
        if (data.callBackData) $('#sub_memberList').datagrid('loadData', data.callBackData);
    }
}

function updateSubSchedule() {

    var id = $('#sub_idTxt').val();
    var name = $('#sub_nameTxt').textbox('getValue');
    var sex = $('#sub_sexSelect').textbox('getValue');
    var tel = $('#sub_telTxt').textbox('getValue');
    var pwd = $('#sub_passTxt').textbox('getValue');
    var joinDate = $('#sub_joinDateTxt').textbox('getValue');
    var expireDate = $('#sub_expireDateTxt').textbox('getValue');
    var fee = $('#sub_feeTxt').textbox('getValue');
    var remark = $('#sub_remarkTxt').textbox('getValue');

    var postValue = {
        "id": id,
        "name": name,
        "sex": sex,
        "tel": tel,
        "pwd": pwd,
        "joinDate": joinDate,
        "expireDate": expireDate,
        "fee": fee,
        "remark": remark
    };

    callAjax('/websiteService/updateSubSchedule', '', 'updateSubScheduleCallback', '', 'POST', postValue, '.window-mask');
}

function deleteSubSchedule(){
    var row = $('#subScheduleView').datagrid('getSelected');
    if(row){
        $.messager.confirm('删除会员', '确认删除会员:'+ row.name +'吗?',
            function(result) {
                if (result) {
                    callAjax('/websiteService/deleteSubSchedule', '', 'subScheduleDeleteCallback', '', '', 'id='+row.id, '.window-mask');
                }
            }
        );
    }
}

function addSubSchedule() {

    var name = $('#sub_nameTxt').textbox('getValue');
    var sex = $('#sub_sexSelect').textbox('getValue');
    var tel = $('#sub_telTxt').textbox('getValue');
    var pwd = $('#sub_passTxt').textbox('getValue');
    var joinDate = $('#sub_joinDateTxt').textbox('getValue');
    var expireDate = $('#sub_expireDateTxt').textbox('getValue');
    var fee = $('#sub_feeTxt').textbox('getValue');
    var remark = $('#sub_remarkTxt').textbox('getValue');

    var postValue = {
        "name": name,
        "sex": sex,
        "tel": tel,
        "pwd": pwd,
        "joinDate": joinDate,
        "expireDate": expireDate,
        "fee": fee,
        "remark": remark
    };

    callAjax('/websiteService/insertSubSchedule', '', 'addSubScheduleCallback', '', 'POST', postValue, '.window-mask');

}

function subScheduleSearchCallback(data) {
    $.messager.show({
        title: '操作提示',
        msg: data.prompt,
        timeout: 5000,
    });
    if (data.status == "ok") {
        if (data.callBackData) $('#subScheduleView').datagrid('loadData', data.callBackData);
    }
}

function subScheduleDeleteCallback(data){
    $.messager.show({
        title: '操作提示',
        msg: data.prompt,
        timeout: 5000,
    });
    if(data.status == "ok"){
        var rowIndex = $('#subScheduleView').datagrid('getRowIndex', data.callBackData);
        $('#subScheduleView').datagrid('deleteRow', rowIndex);
    }
}

function addSubScheduleCallback(data){
    $.messager.show({
            title: '操作提示',
            msg: data.prompt,
            timeout: 5000,
        });
        if(data.status == "ok"){
            $('#subScheduleView').datagrid('insertRow', {index : 0, row : data.callBackData});
            $('#subScheduleUpdateView').dialog('close');
        }
}

function updateSubScheduleCallback(data){
    $.messager.show({
            title: '操作提示',
            msg: data.prompt,
            timeout: 5000,
        });
        if(data.status == "ok"){
            var rowIndex = $('#subScheduleView').datagrid('getRowIndex', data.callBackData.id);
            $('#subScheduleView').datagrid('updateRow', {index : rowIndex, row : data.callBackData});
            $('#subScheduleUpdateView').dialog('close');
        }
}

function initializeSubSchedule(){
    callAjax('/websiteService/getTeachers', '', 'teacherSearchSubScheduleCallback', '', '', '', '.window-mask');
    callAjax('/websiteService/getCourses', '', 'courseSearchSubScheduleCallback', '', '', '', '.window-mask');
}

function teacherSearchSubScheduleCallback(data){
    $.messager.show({
        title: '操作提示',
        msg: data.prompt,
        timeout: 5000,
    });
    if (data.status == "ok") {
        $('#s_teacherViewSelect').combobox('loadData', data.callBackData);
    }
}

function courseSearchSubScheduleCallback(data){
    $.messager.show({
        title: '操作提示',
        msg: data.prompt,
        timeout: 5000,
    });
    if (data.status == "ok"){
        $('#s_courseViewSelect').combobox('loadData', data.callBackData);
    }
}


/*subSchedule region*/

/*notification region*/
function notificationSearch(){

    callAjax('/websiteService/getNotifications', '', 'notificationSearchCallback', '', '', '', '.window-mask')
}

function notificationSearchCallback(data) {
    $.messager.show({
        title: '操作提示',
        msg: data.prompt,
        timeout: 5000,
    });
    if (data.status == "ok") {
        if (data.callBackData) $('#notificationView').datagrid('loadData', data.callBackData);
    }
}

function clearNotification(){
    if(!notificationKindeditor){
        notificationKindeditor = KindEditor.create('#n_content',{
            items: [
                    'undo', 'redo', '|', 'preview', 'cut', 'copy', 'paste',
                    'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
                    'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                    'superscript', 'quickformat', 'selectall', '|', 
                    'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
                    'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|',
                    'table', 'hr', 'emoticons', 'pagebreak',
                    'anchor', 'link', 'unlink'
                    ],
            width: "100%",
            height: "260px",
            resizeType : 0,
            }
        );
    }
    $('#n_idTxt').val('');
    $('#n_title').textbox('setValue', '');
    /*$('#n_content').textbox('setValue', '');*/
    notificationKindeditor.html('');
    $('#addNotificationBtn').css('display','block');
    $('#updateNotificationBtn').css('display','block');
}

function openNotificationPanel(mode){
    clearNotification();
    if(mode == "add"){
        $('#notificationUpdateView').dialog('open');
        $('#updateNotificationBtn').css('display','none');
    }
    else if(mode == 'edit'){
        var row = $('#notificationView').datagrid('getSelected');
        if(row){
            $('#notificationUpdateView').dialog('open');
            $('#n_id').val(row.id);
            $('#n_title').textbox('setValue', row.title);
            /*$('#n_content').textbox('setValue', row.content);*/
            notificationKindeditor.html(row.content);
            $('#n_date').val(row.date);
            $('#addNotificationBtn').css('display','none');
        }
    }
}

function addNotification() {

    var title = $('#n_title').textbox('getValue');
    //var content = $('#n_content').textbox('getValue');
    var content = notificationKindeditor.html();

    var postValue = {
        "title": title,
        "content": content,
    };

    callAjax('/websiteService/insertNotification', '', 'addNotificationCallback', '', 'POST', postValue, '.window-mask');

}

function addNotificationCallback(data){
    $.messager.show({
            title: '操作提示',
            msg: data.prompt,
            timeout: 5000,
        });
        if(data.status == "ok"){
            $('#notificationView').datagrid('insertRow', {index : 0, row : data.callBackData});
            $('#notificationUpdateView').dialog('close');
        }
}

function updateNotification() {

    var id = $('#n_id').val();
    var title = $('#n_title').textbox('getValue');
    //var content = $('#n_content').textbox('getValue');
    var content = notificationKindeditor.html();
    var date = $('#n_date').val();

    var postValue = {
        "id" : id,
        "title": title,
        "content": content,
        "date" : date,
        };

    callAjax('/websiteService/updateNotification', '', 'updateNotificationCallback', '', 'POST', postValue, '.window-mask');
}

function updateNotificationCallback(data){
    $.messager.show({
            title: '操作提示',
            msg: data.prompt,
            timeout: 5000,
        });
        if(data.status == "ok"){
            var rowIndex = $('#notificationView').datagrid('getRowIndex', data.callBackData.id);
            $('#notificationView').datagrid('updateRow', {index : rowIndex, row : data.callBackData});
            $('#notificationUpdateView').dialog('close');
        }
}

function deleteNotification(){
    var row = $('#notificationView').datagrid('getSelected');
    if(row){
        $.messager.confirm('删除通知', '确认删除此通知吗?',
            function(result) {
                if (result) {
                    callAjax('/websiteService/deleteNotification', '', 'notificationDeleteCallback', '', '', 'id='+row.id, '.window-mask');
                }
            }
        );
    }
}

function notificationDeleteCallback(data){
    $.messager.show({
        title: '操作提示',
        msg: data.prompt,
        timeout: 5000,
    });
    if(data.status == "ok"){
        var rowIndex = $('#notificationView').datagrid('getRowIndex', data.callBackData);
        $('#notificationView').datagrid('deleteRow', rowIndex);
    }
}
/*notification region*/