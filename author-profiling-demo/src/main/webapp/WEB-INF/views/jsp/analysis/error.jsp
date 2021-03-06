<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="en">

    <jsp:include page="../fragments/header.jsp" />

    <body>
        
        <div id="grayLayer" class="vcenter hidden"></div>

        <div id="containerDiv" class="container vcenter">
            <h1>${message}</h1>
        </div>

        <jsp:include page="../fragments/footer.jsp" />

    </body>
</html>