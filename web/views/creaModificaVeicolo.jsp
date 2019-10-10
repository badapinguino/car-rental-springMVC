<%@ page import="app.model.Veicolo" %>
<%@ page import="app.DAO.VeicoliDAO" %><%--
  Created by IntelliJ IDEA.
  User: SI2001
  Date: 08/08/2019
  Time: 15:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <head>
        <title>Crea o modifica veicolo</title>
    </head>
    <body>
<%--        <%@include file="../header.jsp"%>--%>
        <div class="container">
            <c:if test="${sessionScope.superuserSessione == true}">
                <br><h4 class="text-center text-primary">Crea o modifica un veicolo</h4>
                <c:if test="${requestScope.veicoloInserito != null}">
                    <c:choose>
                        <c:when test="${requestScope.veicoloInserito == true}">
                            <h5 class="text-success">Veicolo inserito correttamente</h5>
                        </c:when>
                        <c:otherwise>
                            <h5 class="text-danger">Si è verificato un errore, il veicolo non è stato inserito</h5>
                        </c:otherwise>
                    </c:choose>
                </c:if>
                <c:if test="${requestScope.erroreAnnoFuturo == true}">
                    <h5 class="text-danger">L'anno inserito è futuro, deve essere al massimo l'anno prossimo.</h5>
                </c:if>

                <form:form action="./creaModificaVeicolo" method="post" modelAttribute="veicoloDTO"> <!-- use the controller to update session attribute -->
                    <div class="form-group">
                        <div class="row">
                            <div class="col">
                                <form:label path="casaCostruttrice">Casa costruttrice:</form:label>
                                <form:input type="text" class="form-control" path="casaCostruttrice" id="casaCostruttrice" maxlength="${Veicolo.getLunghezzaCampoCasaCostruttrice()}" required="required"/>
                                <form:errors path="casaCostruttrice" cssClass="text-danger"/>
                            </div>
                            <div class="col">
                                <form:label path="modello">Modello:</form:label>
                                <form:input type="text" class="form-control" path="modello" id="modello" maxlength="${Veicolo.getLunghezzaCampoModello()}" required="required"/>
                                <form:errors path="modello" cssClass="text-danger"/><br><br>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col">
                                <form:label path="codiceMezzo">Codice mezzo:</form:label>
                                <c:choose>
                                    <c:when test="${requestScope.datiVeicoloDaModificare != null}">
                                        <c:set var="readonly" scope="page" value="true"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="readonly" scope="page" value="false"/>
                                    </c:otherwise>
                                </c:choose>
                                <form:input type="text" class="form-control" path="codiceMezzo" id="codiceMezzo" maxlength="${Veicolo.getLunghezzaCampoCodiceMezzo()}" required="required" readonly="${readonly}"/>
                                <form:errors path="codiceMezzo" cssClass="text-danger"/>
                            </div>
                            <div class="col">
                                <form:label path="targa">Targa:</form:label>
                                <form:input type="text" class="form-control" path="targa" id="targa" maxlength="${Veicolo.getLunghezzaCampoTarga()}" required="required"/>
                                <form:errors path="targa" cssClass="text-danger"/><br><br>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col">
                                <form:label path="anno">Anno:</form:label>
                                <form:input type="number" step="1" class="form-control" path="anno" id="anno" min="1900" required="required"/>
                                <form:errors path="anno" cssClass="text-danger"/>
                            </div>
                            <div class="col">
                                <form:label path="prezzoGiornata">Prezzo a giornata:</form:label>
                                <form:input type="number" step="0.01" class="form-control" path="prezzoGiornata" id="prezzoGiornata" min="0.01" required="required"/>
                                <form:errors path="prezzoGiornata" cssClass="text-danger"/><br><br> <%--<c:if test="${requestScope.datiVeicoloDaModificare != null}"> value="${requestScope.datiVeicoloDaModificare.getPrezzoGiornata()}" </c:if> <c:if test="${requestScope.datiVeicoloDaModificare == null}">value="0.01"</c:if>--%>
                            </div>
                        </div>
                        <div class="form-group">
                            <c:if test="${requestScope.datiVeicoloDaModificare != null}">
                                <form:label path="tipologia">Nuova tipologia:</form:label>
                            </c:if>
                            <c:if test="${requestScope.datiVeicoloDaModificare == null}">
                                <form:label path="tipologia">Tipologia:</form:label>
                            </c:if>
                            <form:select class="form-control text-center" path="tipologia" id="tipologia">
                                <form:option value="Berlina">Berlina</form:option>
                                <form:option value="City car" label="City car">City car</form:option>
                                <form:option value="Furgone">Furgone</form:option>
                                <form:option value="Lusso">Lusso</form:option>
                                <form:option value="Monovolume">Monovolume</form:option>
                                <form:option value="Sportiva">Sportiva</form:option>
                                <form:option value="Supercar">Supercar</form:option>
                                <form:option value="Suv">Suv</form:option>
                            </form:select>
                            <form:errors path="tipologia" cssClass="text-danger"/>
                        </div>
                        <div class="form-group">
                            <br><form:button type="submit" class="btn btn-primary">Inserisci veicolo</form:button>
                        </div>
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
