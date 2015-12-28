<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

 <c:set var="req" value="${pageContext.request}" />
<c:set var="url">${req.requestURL}</c:set>
<c:set var="base"
	value="${fn:substring(url, 0, fn:length(url) - fn:length(req.requestURI))}${req.contextPath}/" />


<div style="text-align: center;">
	<h4>
		<!-- <spring:message code="label.inquiry.output.pagename" /> -->
		Запит на внесення ресурсу
	</h4>
</div>


		<form:form method="POST" action="addInputInquiry" >
			<table class="table display">
					<tr>
						<td><spring:message code="label.resource.registrator" />
							(<spring:message code="label.resource.tome" />):</td>
						<td><select name="tomeIdentifier">
								<option value=""><spring:message
										code="label.resource.registrator.select" />:
								</option>
								<c:forEach items="${tomes}" var="tome">
									<option value="${tome.tomeIdentifier}">${tome.registratorLastName} ${tome.registratorFirstName} 
										${tome.registratorMiddleName} (номер тому: ${tome.tomeIdentifier})</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					
				<!-- 	<tr>
						<td><spring:message code="label.resource.type" />:</td>
						<td> <select name="resourceType">
								<option value=""><spring:message
										code="label.resource.type" />:
								</option>
									<c:forEach items="${listOfResourceType}" var="restype">
									<option value="${restype.typeId}">${restype.typeName} </option>
									</c:forEach>
							</select>
						</td>
					</tr>	-->									   			
			</table>				
			<input type="submit"  value="Надіслати запит" class="btn btn-success"/>
		
		</form:form>