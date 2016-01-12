<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="req" value="${pageContext.request}"/>
<c:set var="url">${req.requestURL}</c:set>
<c:set var="base" value="${fn:substring(url, 0, fn:length(url) - fn:length(req.requestURI))}${req.contextPath}/"/>


<fmt:formatDate value="${resource.date}" pattern="dd.MM.yyyy" var="Date" />

<div>	
	<form:form modelAttribute="resource">
	<c:if test="${empty resource}">
	 Ресурс із вказаним ідентифікатором не знайдено
	</c:if>
		<c:if test="${!empty resource}">
		<h1>Ресурс</h1>
			<table id="datatable" class="table display">
				<tr>
					<td><spring:message code="label.resource.description" /> :</td>
					<td>${resource.description}</td>
				</tr>
				<tr>
					<td><spring:message code="label.resource.type" />:</td>
					<td>${resource.resourceType.typeName}</td>
				</tr>
				<tr>
					<td><spring:message code="label.resource.identifier" />:</td>
					<td>${resource.identifier}</td>
				</tr>
				<tr>
					<td><spring:message code="label.resource.tome" />:</td>
					<td>${resource.tomeIdentifier}</td>
				</tr>
				<tr>
					<td><spring:message code="label.resource.registrator" />:</td>
					<td>${resource.registratorName}</td>
				</tr>
				<tr>
					<td><spring:message code="label.resource.reasonInclusion" />:</td>
					<td>${resource.reasonInclusion}</td>
				</tr>
				<tr>
					<td><spring:message code="label.resource.date" />:</td>
					<td>${Date}</td>
				</tr>
				<tr>
					<td><spring:message code="label.resource.coordinates" />:</td>
				</tr>
				<c:forEach var="poligon" items="${resource.resourceArea.poligons}">
					<c:forEach var="point" items="${poligon.points}">
						<tr class="coordinates">
							<td>
                                <span class="latitudeDegrees">${point.latitudeDegrees}</span>°
                                <span class="latitudeMinutes">${point.latitudeMinutes}</span>'
                                <span class="latitudeSeconds"><fmt:formatNumber type="number" maxFractionDigits="2" value="${point.latitudeSeconds}" /></span>"
                            </td>
							<td>
                                <span class="longitudeDegrees">${point.longitudeDegrees}</span>°
                                <span class="longitudeMinutes">${point.longitudeMinutes}</span>'
                                <span class="longitudeSeconds"><fmt:formatNumber type="number" maxFractionDigits="2" value="${point.longitudeSeconds}" /></span>"
                            </td>
						</tr>
					</c:forEach>
				</c:forEach>
				<c:forEach var="linear" items="${resource.resourceLinear}">
					<tr>
						<td>${linear.linearParameterDescription},${linear.linearParameterUnit}</td>
						<c:forEach var="segment" items="${linear.segments}">
							<td>${segment.begin}-${segment.end}</td>
						</c:forEach>
					</tr>
				</c:forEach>
				<c:forEach var="discrete" items="${resource.resourceDiscrete}">
					<tr>
						<td>${discrete.discreteParameterDescription},${discrete.discreteParameterUnit}</td>
						<c:forEach var="valueDiscrete" items="${discrete.valueDiscretes}">
							<td>${valueDiscrete.value}</td>
							<td>${valueDiscrete.comment}</td>
						</c:forEach>
					</tr>
				</c:forEach>
			</table>
		</c:if>
	</form:form>

    <%--Connect Google Maps--%>



    <div id="map_canvas" class="container" style="height: 500px;"
         centerLat="<c:out value="${
        resource.resourceArea.poligons[0].points[0].latitudeDegrees +
        resource.resourceArea.poligons[0].points[0].latitudeMinutes/60 +
        resource.resourceArea.poligons[0].points[0].latitudeSeconds/3600}"/>"
         centerLng="<c:out value="${
        resource.resourceArea.poligons[0].points[0].longitudeDegrees +
        resource.resourceArea.poligons[0].points[0].longitudeMinutes/60 +
        resource.resourceArea.poligons[0].points[0].longitudeSeconds/3600}"/>">
    </div>
    <script src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
    <script type="text/javascript" src="${base}resource/js/showResourceMap.js"></script>

</div>