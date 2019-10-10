<%@ page import="app.model.BuonoSconto" %><%--
  Created by IntelliJ IDEA.
  User: SI2001
  Date: 09/08/2019
  Time: 12:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <head>
        <title>Crea buono sconto</title>
    </head>
    <body>
<%--        <%@include file="../header.jsp"%>--%>
        <div class="container">
            <c:if test="${sessionScope.superuserSessione == true}">
                <br><h4 class="text-center text-primary">Crea o modifica un buono sconto</h4>
                <c:if test="${requestScope.buonoScontoInserito != null}">
                    <c:choose>
                        <c:when test="${requestScope.buonoScontoInserito == true}">
                            <h5 class="text-success">Buono sconto inserito correttamente</h5>
                        </c:when>
                        <c:otherwise>
                            <h5 class="text-danger">Si è verificato un errore, il buono sconto non è stato inserito</h5>
                        </c:otherwise>
                    </c:choose>
                </c:if>
                <c:if test="${requestScope.erroreCodiceScontoLungo == true}">
                    <h5 class="text-danger">Codice sconto troppo lungo, deve essere di massimo <%=BuonoSconto.getLunghezzaCodiceSconto()%> caratteri.</h5>
                </c:if>
                <c:if test="${requestScope.valoreNegativoOZero == true}">
                    <h5 class="text-danger">Il valore del buono sconto non può essere 0 o minore di 0.</h5>
                </c:if>
                <c:if test="${requestScope.valorePercentualeOltreCento == true}">
                    <h5 class="text-danger">Il valore non può superare il cento percento se è stato impostato "percentuale" come tipo di buono sconto.</h5>
                </c:if>
                <form:form action="./creaModificaBuonoSconto" method="post" modelAttribute="buonoScontoDTO"> <!-- use the controller to update session attribute -->
                    <div class="form-group">
                        <c:choose>
                            <c:when test="${requestScope.datiBuonoScontoDaModificare != null}">
                                <c:set var="readonly" scope="page" value="true"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="readonly" scope="page" value="false"/>
                            </c:otherwise>
                        </c:choose>
                        <form:label path="codiceSconto">Codice sconto:</form:label>
                        <form:input type="text" class="form-control" path="codiceSconto" id="codiceSconto" maxlength="${BuonoSconto.getLunghezzaCodiceSconto()}" readonly="${readonly}" required="required"/>
                        <form:errors path="codiceSconto" cssClass="text-danger"/>
                    </div>
                    <div class="form-group">
                        <div class="form-group col-md-6">

                            <form:radiobutton path="percentuale" value="true" />
                            <form:label class="form-check-label" path="percentuale">
                                Sconto percentuale
                            </form:label>
                        </div>
                        <div class="form-group col-md-6">
                            <form:radiobutton path="percentuale" value="false" /> <%--label="No"--%>
                            <form:label class="form-check-label" path="percentuale">
                                Sconto contante
                            </form:label>
                            <form:errors path="percentuale" cssClass="text-danger"/>
<%--                            </div>--%>
                        </div>
                    </div>
                    <div class="form-group">
                        <form:label path="valore">Valore:</form:label>
                        <form:input type="number" step="0.01" class="form-control" path="valore" id="valore" min="0.01" required="readonly"/>
                        <form:errors path="valore" cssClass="text-danger"/>
                    </div>
                    <div class="form-group">
                        <br><form:button type="submit" class="btn btn-primary">Inserisci buono sconto</form:button>
                    </div>
                </form:form>
            </c:if>
        </div>

        <!-- JS -->
        <script src="//code.jquery.com/jquery.js"></script>
        <script src="resources/js/bootstrap.min.js"></script>
        <!-- JS -->
    </body>
</html>
