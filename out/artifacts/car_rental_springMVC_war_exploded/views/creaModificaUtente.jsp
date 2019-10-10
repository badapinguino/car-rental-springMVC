<%@ page import="app.model.Utente" %><%--
  Created by IntelliJ IDEA.
  User: SI2001
  Date: 09/09/2019
  Time: 16:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <title>Crea Nuovo Utente</title>
</head>
<body>
<%--<%@include file="../header.jsp"%>--%>
<div class="container">
    <%--        <c:if test="${sessionScope.superuserSessione == true}">--%>
    <c:choose>
        <c:when test="${requestScope.datiUtenteDaModificare != null && requestScope.datiUtenteDaModificare.codiceFiscale == sessionScope.codiceFiscaleSessione}">
            <br><h4 class="text-center text-primary">Profilo utente</h4>
        </c:when>
        <c:otherwise>
            <br><h4 class="text-center text-primary">Crea o modifica un utente</h4>
        </c:otherwise>
    </c:choose>
    <c:if test="${requestScope.utenteDTO != null && requestScope.utenteDTO.getImmagine()!=null && !requestScope.utenteDTO.getImmagine().equals(\"\")}">
<%--        ${requestScope.datiUtenteDaModificare.getImmagine()}--%>
        <img src="<c:url value="/immagineProfilo">
                    <c:param name="CFUtente" value="${requestScope.utenteDTO.getCodiceFiscale()}"/>
                </c:url>" alt="Immagine del profilo" style="width: 20%; margin-left: auto; margin-right: auto; display: block;"
                class="img-thumbnail" >
<%--        <img src="/immagineProfilo?CFUtente=${requestScope.datiUtenteDaModificare.getCodiceFiscale()}" alt="Immagine del profilo" class="img-thumbnail">--%>
    </c:if>
    <c:if test="${requestScope.utenteInserito != null}">
        <c:choose>
            <c:when test="${requestScope.utenteInserito == true}">
                <h5 class="text-success">Utente inserito correttamente</h5>
            </c:when>
            <c:otherwise>
                <h5 class="text-danger">Si è verificato un errore, l'utente non è stato inserito</h5>
            </c:otherwise>
        </c:choose>
    </c:if>
    <c:if test="${(requestScope.datiUtenteDaModificare != null && requestScope.datiUtenteDaModificare.codiceFiscale == sessionScope.codiceFiscaleSessione) || sessionScope.superuserSessione}">
        <form:form action="./creaModificaUtente" method="post" modelAttribute="utenteDTO" enctype="multipart/form-data"> <!-- use the controller to update session attribute -->
            <div class="form-group">
                <div class="row">
                    <div class="col">
                        <form:label path="nome">Nome:</form:label>
                        <form:input type="text" class="form-control" path="nome" id="nome" maxlength="${utenteDTO.getLunghezzaCampoNome()}" required="required"/>
                        <form:errors path="nome" cssClass="text-danger"/>

                    </div>
                    <div class="col">
                        <form:label path="cognome">Cognome:</form:label>
                        <form:input type="text" class="form-control" path="cognome" id="cognome" maxlength="${utenteDTO.getLunghezzaCampoCognome()}" required="required"/>
                        <form:errors path="cognome" cssClass="text-danger"/>
                    </div>
                </div><br>
                <div class="form-group">
                    <form:label path="dataNascita">Data di nascita:</form:label>
                    <form:input type="date" class="form-control" path="dataNascita" id="dataNascita" onchange="TDate()" required="required"/>
                    <form:errors path="dataNascita" cssClass="text-danger"/>
                </div>
                <div class="form-group">
                    <form:label path="codiceFiscale">Codice Fiscale:</form:label>
                    <c:choose>
                        <c:when test="${requestScope.datiUtenteDaModificare != null}">
                            <form:input type="text" class="form-control" path="codiceFiscale" id="codiceFiscale" minlength="${utenteDTO.getLunghezzaCampoCodiceFiscale()}" maxlength="${utenteDTO.getLunghezzaCampoCodiceFiscale()}" required="required" readonly="true"/>
                        </c:when>
                        <c:otherwise>
                            <form:input type="text" class="form-control" path="codiceFiscale" id="codiceFiscale" minlength="${utenteDTO.getLunghezzaCampoCodiceFiscale()}" maxlength="${utenteDTO.getLunghezzaCampoCodiceFiscale()}" required="required"/> <!-- pattern="(.){16,16}" title="Il codice fiscale deve essere di 16 caratteri" -->
                        </c:otherwise>
                    </c:choose>
                    <form:errors path="codiceFiscale" cssClass="text-danger"/>
                </div>
                <div class="form-group">
                    <c:choose>
                        <c:when test="${requestScope.datiUtenteDaModificare != null}">
                            <form:label path="password">Inserisci la nuova password:</form:label>
                        </c:when>
                        <c:otherwise>
                            <form:label path="password">Password:</form:label>
                        </c:otherwise>
                    </c:choose>
                    <form:input type="password" class="form-control" id="password" path="password" maxlength="${utenteDTO.getLunghezzaCampoPassword()}" required="required"/>
                    <form:errors path="password" cssClass="text-danger"/>
                </div>
                <div class="form-group">
                    <label for="immagine">Seleziona una foto del profilo (opzionale):</label>
                    <input type="file" name="file" id="immagine" size="50" class="form-control-file"/>
<%--                    <form:errors path="immagine" cssClass="text-danger"/>--%>
                    <input type="hidden" name="vecchioFile" value="${requestScope.datiUtenteDaModificare.getImmagine()}">
                </div>
                <div class="form-group">
                    <c:choose>
                        <c:when test="${requestScope.datiUtenteDaModificare != null && requestScope.datiUtenteDaModificare.codiceFiscale == sessionScope.codiceFiscaleSessione}">
                            <br><button type="submit" class="btn btn-primary">Aggiorna profilo</button>
                        </c:when>
                        <c:otherwise>
                            <br><button type="submit" class="btn btn-primary">Inserisci utente</button>
                        </c:otherwise>
                    </c:choose>

                </div>
            </div>
        </form:form>
    </c:if>
    <%--        </c:if>--%>
</div>
<script>
    Date.prototype.toDateInputValue = (function() {
        var local = new Date(this);
        local.setMinutes(this.getMinutes() - this.getTimezoneOffset());
        return local.toJSON().slice(0,10);
    });

    function TDate() {
        var UserDate = document.getElementById("dataNascita").value;
        var ToDate = new Date();

        if (new Date(UserDate).getTime() > ToDate.getTime()) {
            alert("Non puoi essere nato nel futuro");
            document.getElementById("dataNascita").value = new Date().toDateInputValue();;
            return false;
        }
        return true;
    }
</script>
<!-- JS -->
<script src="//code.jquery.com/jquery.js"></script>
<script src="resources/js/bootstrap.min.js"></script>
<!-- JS -->
</body>
</html>
