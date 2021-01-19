<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 단순 응답<br>
jsp EL은 데이터가 없을 경우 blank로 출력<br>
client로 부터 전송되는 데이터 (query string)를 받을 때는 getParameter() 메소드로 데이터 받아서 출력<br>
--%>
${param.name}