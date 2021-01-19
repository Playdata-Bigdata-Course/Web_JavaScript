<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="model.DeptDAO, java.util.ArrayList, model.domain.DeptDTO"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% 
	//컨트롤러에서 개발했다 가정
	DeptDAO dao = new DeptDAO();
	ArrayList<DeptDTO> all = dao.deptAll();
	request.setAttribute("all", all);
%> 
<table border="1">
	<tr><th>부서번호</th><th>부서명</th><th>지역</th></tr>
	
	<c:forEach items="${requestScope.all}" var="data">
		<tr><td>${data.deptno}</td><td>${data.dname}</td><td>${data.loc}</td></tr>
	</c:forEach>
	
	



</table>
