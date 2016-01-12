<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="req" value="${pageContext.request}" />
<c:set var="url">${req.requestURL}</c:set>
<c:set var="base"
    value="${fn:substring(url, 0, fn:length(url) - fn:length(req.requestURI))}${req.contextPath}/" />

<spring:url value="/resource/js/addArea.js" var="addAreaJs" />
<spring:url value="/resource/js/lib/jquery.autocomplete.min.js"
    var="autocompleteJs" />
<spring:url value="/resource/js/descriptionAutocomplete.js"
    var="descAutocomplete" />
<spring:url value="/resource/css/suggestion.css" var="suggestionCss" />

<script src="${autocompleteJs}"></script>
<script src="${descAutocomplete}"></script>
<script src="${addAreaJs}"></script>
<link rel="stylesheet" type="text/css" href="${suggestionCss}">
<link rel="stylesheet" type="text/css" href="${base}resource/css/cssload.css">


<script src=" http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.3/jquery-ui.js"></script>
<script> 
$(document).ready(function() {
	$( "#datepicker" ).datepicker({ dateFormat: 'dd.mm.yy' });
	
     if( "${user.willDocument}" === "") {
        $('#will').attr('disabled', 'disabled');        
    }
    if( "${user.passport}" === "") {
        $('#pass').attr('disabled', 'disabled');        
    } 
    
    if( "${user.otherDocuments}" === "") {
        $('#otherDocs').attr('disabled', '');       
    } 
    
    
    $(".checkbox input").click(function(){
        $("#reasonInclusion").text('');
        $(".checkbox :checked").each(function(){
            var id =  $(this).attr('id');
             if(id === "pass") {                 
                 var seria = "${user.passport.seria} " + "${user.passport.number}";
                 var name = "${user.firstName} " + "${user.middleName} " +"${user.lastName}";
                 var published ="${user.passport.published_by_data}";
                 var passportInfo = "паспорт громадянина України " 
                     + seria +", виданий на ім’я " + name + " " + published;               
                if("${user.passport.comment}" !== "") {
                    var comment ="${user.passport.comment}";
                    passportInfo = passportInfo + "; " + comment;
                }
                $("#reasonInclusion").append(passportInfo + ";\n");
            }
            if(id === "will") {
                var date = '${user.willDocument.accessionDate}';
                var stringDate = $.datepicker.formatDate('dd.mm.yy', new Date(date));
                var willDocument = "документ волевиявлення від " + stringDate;
                if("${user.willDocument.comment}" !== "") {
                    var comment ="${user.willDocument.comment}";
                    willDocument = willDocument + "; " + comment;
                }
                $("#reasonInclusion").append( willDocument+ ";\n");                
            }
            if(id === "otherDocs") {
                var docs = '${user.otherDocuments}';
                $("#reasonInclusion").append(docs + ";\n");                
            }
             else if(id === "tytul"){
                 var inputString = "титул власності на природні ресурси України від 02 квітня 2015 року;";
                 $("#reasonInclusion").append( inputString + "\n");
             }
             else if(id === "delivery"){
                 var inputDelivery = "доручення;";
                 $("#reasonInclusion").append(inputDelivery + "\n");                
            }
        });
   });        

});
</script>



<div class="container">
    <h2>
        <spring:message code="label.resource.add" />
    </h2>
    <form:form method="POST" action="addresource"
        modelAttribute="newresource" class="form-horizontal">
        <div class="form-group">
            <label class="control-label col-sm-3"><spring:message
                    code="label.resource.description" />:</label>
            <div class="col-sm-3">
                <input class="form-control" name="description" id="w-input-search"
                    value="${newresource.description}">
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-3"><spring:message
                    code="label.resource.type" />:</label>
            <div class="col-sm-3">
                <select id="resourcesTypeSelect" name="resourceType"
                    class="form-control" required>
                    <option value=""><spring:message
                            code="label.resource.selectType" />:
                    </option>
                    <c:forEach items="${listOfResourceType}" var="restype">
                        <option value="${restype.typeName}">${restype.typeName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div id="typeParameters"></div>
        <div class="form-group">
            <label class="control-label col-sm-3"><spring:message
                    code="label.resource.identifier" />:</label>
            <div class="col-sm-3">
                <input class="form-control" name="identifier" required
                    value="${newresource.identifier}">
            </div>
            <div class="control-group error">
                <form:errors path="identifier" cssClass="error" style="color:red" />
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-3"><spring:message
                    code="label.resource.reasonInclusion" />:</label>
            <div class="col-sm-3">
                <textarea id="reasonInclusion" class="form-control" rows="5"
                    name="reasonInclusion" required>${newresource.reasonInclusion}</textarea>
            </div>
            <div class="checkbox">
                <label><input id="pass" type="checkbox">паспорт</label><br />
                <label><input id="will" type="checkbox">волевиявлення
                    людини</label><br /> 
                <label><input id="otherDocs" type="checkbox">інші документи</label><br /> 
                    
                    <label><input id="tytul" type="checkbox">титул
                    власності</label><br /> <label><input id="delivery"
                    type="checkbox">доручення</label>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-3"><spring:message
                    code="label.resource.date" />:</label>
            <div class="col-sm-3">
                <input class="form-control" type="text" id="datepicker" name="date" required
                    value="${date}">
            </div>
            <div class="control-group error">
                <form:errors path="date" cssClass="error" style="color:red" />
            </div>
        </div>
        <div class="form-group ">
            <label class="control-label col-sm-3"><spring:message
                    code="label.resource.registrator" />(<spring:message
                    code="label.resource.tome" />):</label>
            <div class="col-sm-3">
                <select name="tomeIdentifier" class="form-control" required>
                    <option value=""><spring:message
                            code="label.resource.registrator.select" />:
                    </option>
                    <c:forEach items="${tomes}" var="tome">
                        <option value="${tome.identifier}">${tome.registrator.lastName}
                            ${tome.registrator.middleName} ${tome.registrator.firstName}
                            (номер тому: ${tome.identifier})</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <legend>
            <spring:message code="label.resource.coordinates" />
        </legend>

        <%--Container for Google map--%>
        <p>
            <i>Щоб виділити на мапі область, оберіть інструмент "Намалювати
                фігуру" в верхній частині мапи.</i>
        </p>

        <div id="map_canvas" class="container"
            style="height: 500px; padding: 20px 0px;"></div>

         <div class="form-group">
            <label class="control-label col-sm-1"><spring:message
                    code="label.resource.orderPoint" /></label> <label
                class="control-label col-sm-3"><spring:message
                    code="label.resource.latitude" /></label> <label
                class="control-label col-sm-3"><spring:message
                    code="label.resource.longitude" /></label>
        </div>
        <div id="areaInput1" class="form-group clonedAreaInput">
            <div class="col-sm-1" style="width: 130px; height: 34px">
                <input id="pointNumber" class="form-control"
                    name="resourceArea.poligons[0].points[0].orderNumber" value="${1}"
                    readonly>
            </div>
            <div class="col-sm-1" style="width: 130px; height: 34px">
                <input id="myparam1" class="form-control"
                    name="resourceArea.poligons[0].points[0].latitudeDegrees" value="${0}"required>
            </div>
            <div class="col-sm-1" style="width: 130px; height: 34px">
                <input id="myparam2" class="form-control"
                    name="resourceArea.poligons[0].points[0].latitudeMinutes" value="${0}" required>
            </div>
            <div class="col-sm-1" style="width: 130px; height: 34px">
                <input id="myparam3" class="form-control"
                    name="resourceArea.poligons[0].points[0].latitudeSeconds" value="${0.0}" required>
            </div>
            <div class="col-sm-1" style="width: 130px; height: 34px">
                <input id="myparam4" class="form-control"
                    name="resourceArea.poligons[0].points[0].longitudeDegrees" value="${0}" required>
            </div>
            <div class="col-sm-1" style="width: 130px; height: 34px">
                <input id="myparam5" class="form-control"
                    name="resourceArea.poligons[0].points[0].longitudeMinutes" value="${0}" required>
            </div>
            <div class="col-sm-1" style="width: 130px; height: 34px">
                <input id="myparam6" class="form-control"
                    name="resourceArea.poligons[0].points[0].longitudeSeconds" value="${0.0}" required>
            </div>
        </div>
        <div class="control-group error">
            <form:errors
                path="resourceArea.poligons[0].points[0].latitudeDegrees"
                cssClass="error" style="color:red" />
        </div>
        <div id="mybuttontype">
            <input type="button" id="btnAddAreaPoint" value="+"
                class="btn btn-primary" /> <input type="button"
                id="btnDelAreaPoint" value="-" class="btn btn-primary deleteButton" />
        </div>
        <br />
        <div class="button">
            <input type="submit" class="btn btn-success" value=<spring:message code="label.save"/> >
            <button type="reset" class="btn btn-default" id="hahaha">
                <spring:message code="label.clearall" />
            </button>
        </div>
    </form:form>

<%--Scripts for Google Map--%>
    <p>
        <input id="gmaps-input" class="controls gmap-input"
            style="width: 300px;" type="text"
            placeholder="Пошук на мапі">
    </p>
    <p><a id="gmaps-show-res" class="controls gmap-button">Показати ресурси</a></p>

<%--AJAX Loader on the dark display--%>
    <div id="dark_bg">
        <div class="windows8">
            <div class="wBall" id="wBall_1">
                <div class="wInnerBall"></div>
            </div>
            <div class="wBall" id="wBall_2">
                <div class="wInnerBall"></div>
            </div>
            <div class="wBall" id="wBall_3">
                <div class="wInnerBall"></div>
            </div>
            <div class="wBall" id="wBall_4">
                <div class="wInnerBall"></div>
            </div>
            <div class="wBall" id="wBall_5">
                <div class="wInnerBall"></div>
            </div>
        </div>
    </div>


    <script
        src="http://maps.googleapis.com/maps/api/js?sensor=false&libraries=drawing,places"></script>
    <script type="text/javascript"
        src="${base}resource/js/addResourceOnMap.js"></script>
</div>