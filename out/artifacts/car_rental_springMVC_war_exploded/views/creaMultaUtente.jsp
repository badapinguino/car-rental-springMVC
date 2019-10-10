<%@ page import="app.DTO.UtenteDTO" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="app.model.Multa" %>
<%--
  Created by IntelliJ IDEA.
  User: SI2001
  Date: 07/08/2019
  Time: 10:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <head>
        <title>Inserimento multa</title>
    </head>
    <body>
<%--        <%@include file="../header.jsp"%>--%>
        <div class="container">
            <% UtenteDTO utenteDTODaMultare = (UtenteDTO) request.getAttribute("utenteDTODaMultare");%>
            <br><h4 class="text-center text-primary">Crea una multa per l'utente <%=utenteDTODaMultare.getNome()%> <%=utenteDTODaMultare.getCognome()%></h4>

            <!-- Stampa messaggi di successo o errore dopo l'inserimento -->
            <c:if test="${requestScope.multaInserita != null}">
                <c:choose>
                    <c:when test="${requestScope.multaInserita == true}">
                        <h5 class="text-success">Multa inserita correttamente</h5>
                    </c:when>
                    <c:otherwise>
                        <h5 class="text-danger">Si è verificato un errore, la multa non è stata inserita</h5>
                    </c:otherwise>
                </c:choose>
            </c:if>
            <c:if test="${requestScope.erroreDataNonCompresa == true}">
                <h5 class="text-danger">La data selezionata non è compresa tra la data di inizio e di fine della prenotazione.</h5>
            </c:if>


            <!-- se ha delle prenotazioni e sono tutte future oppure non ha prenotazioni stampo un messaggio -->
            <c:if test="${requestScope.prenotazioniDTOUtenteDaMultare == null || requestScope.prenotazioniDTOUtenteDaMultare.isEmpty()}">
                <br><h5 class="text-center text-warning">L'utente non ha effettuato prenotazioni, quindi non può essere multato</h5>
            </c:if>

            <c:if test="${requestScope.prenotazioniDTOUtenteDaMultare != null && !(requestScope.prenotazioniDTOUtenteDaMultare.isEmpty())}">
                <!-- controllo se esistono prenotazioni nel passato, altrimenti l'utente non ha prenotazioni da multare se ne ha solo future -->
                <%
                    boolean tuttePrenotazioniFuture = true;
                %>
                <c:forEach items="${requestScope.prenotazioniDTOUtenteDaMultare}" var="item">
                    <c:if test="${(item.getDataFine()).compareTo(LocalDate.now())<0}">
                        <% tuttePrenotazioniFuture = false; %>
                    </c:if>
                </c:forEach>
                <% pageContext.setAttribute("tuttePrenotazioniFuture", tuttePrenotazioniFuture); %>
                <c:if test="${tuttePrenotazioniFuture}">
                    <br><h5 class="text-center text-warning">L'utente ha prenotazioni solo future, oppure non ancora terminate. Quindi non può essere multato</h5>
                </c:if>
            </c:if>
            <!-- se utente ha prenotazioni passate creo il form -->
            <c:if test="${requestScope.prenotazioniDTOUtenteDaMultare != null && !(requestScope.prenotazioniDTOUtenteDaMultare.isEmpty()) && !tuttePrenotazioniFuture}">
                <br>
                <form:form action="./creaMultaUtente" method="post" modelAttribute="multaDTO"> <!-- use the controller to update session attribute -->
                    <div class="form-group">
                        <form:select class="form-control text-center" path="numeroPrenotazione" id="numeroPrenotazione">
                            <form:options items="${requestScope.prenotazioniDTOUtenteDaMultare}" itemValue="numero" itemLabel="dettagli"/>
                        </form:select>
                        <form:errors path="numeroPrenotazione" cssClass="text-danger"/>
                    </div>
                    <div class="form-group">
                        <form:label path="descrizione">Descrizione:</form:label>
                        <form:input type="text" class="form-control" path="descrizione" id="descrizione" maxlength="${Multa.getLunghezzaCampoDescrizione()}" required="required"/>
                    </div>
                    <div class="form-group">
                        <!-- Qui inserire data problema opzionale e fare controllo che sia compresa tra le date inizio e fine prenotazione INCLUSE -->
                        <form:label path="dataProblema">Data problema (opzionale):</form:label>
                        <form:input type="date" class="form-control" path="dataProblema" id="dataProblema" maxlength="${Multa.getLunghezzaCampoDescrizione()}" />
                    </div>
                    <input type="hidden" name="codiceFiscaleUtenteDaMultare" value="${requestScope.codiceFiscaleUtenteDaMultare}">
                    <div class="form-group">
                        <br><form:button type="submit" class="btn btn-warning">Inserisci multa</form:button>
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
