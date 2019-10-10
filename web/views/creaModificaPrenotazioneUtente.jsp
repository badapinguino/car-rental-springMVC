<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="app.model.BuonoSconto" %><%--
  Created by IntelliJ IDEA.
  User: SI2001
  Date: 03/09/2019
  Time: 09:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <head>
        <title>Crea prenotazione</title>
    </head>
    <body>
<%--        <%@include file="../header.jsp"%>--%>
        <div class="container">
            <br><h4 class="text-center text-primary">Crea una nuova prenotazione</h4>

            <c:if test="${requestScope.prenotazioneInserita != null}">
                <c:choose>
                    <c:when test="${requestScope.prenotazioneInserita == true}">
                        <h5 class="text-success">Prenotazione inserita correttamente.</h5><br>
                        <c:choose>
                            <c:when test="${requestScope.valoreCodiceSconto==null}">
                                <h6 class="text-success">Importo totale prenotazione: ${requestScope.costoFinalePrenotazione}&#0128;.</h6>
                            </c:when>
                            <c:otherwise>
                                <h6 class="text-success">Importo prenotazione prima dello sconto: ${requestScope.costoTotalePreScontoPrenotazione}&#0128;.
                                    Sconto applicato: ${requestScope.valoreCodiceSconto}<c:if test="${requestScope.percentualeCodiceSconto==true}">%</c:if><c:if test="${requestScope.percentualeCodiceSconto==false}">&#0128;</c:if></h6><br>
                                <h6 class="text-success">Importo finale: ${requestScope.costoFinalePrenotazione}&#0128;</h6>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <h5 class="text-danger">Si è verificato un errore, la prenotazione non è stata inserita!</h5>
                    </c:otherwise>
                </c:choose>
            </c:if>
            <c:if test="${requestScope.erroreVeicoloNonDisponibile == true}">
                <h5 class="text-danger">Il veicolo scelto non è disponibile per le date selezionate.</h5>
            </c:if>
            <c:if test="${requestScope.erroreDataInizioMaggioreDataFine == true}">
                <h5 class="text-danger">La data di inizio prenotazione è futura rispetto alla data di fine.</h5>
            </c:if>
            <c:if test="${requestScope.erroreDataNonFutura == true}">
                <h5 class="text-danger">La prenotazione non può essere effettuata per dei giorni già trascorsi oppure per la data odierna.</h5>
            </c:if>
            <c:if test="${requestScope.erroreCodiceScontoInesistente == true}">
                <h5 class="text-danger">Il codice sconto inserito non corrisponde a nessun buono sconto esistente.</h5>
            </c:if>

            <%
                DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                pageContext.setAttribute("pattern", pattern);
                //LocalDate oggi = LocalDate.now();
                //pageContext.setAttribute("oggi", oggi);
            %>
            <br>
            <form:form action="./creaModificaPrenotazioneUtente" method="post" modelAttribute="prenotazioneDTO"> <!-- use the controller to update session attribute -->
                <div class="form-group">
                    <c:choose>
                        <c:when test="${requestScope.prenotazioneDTO.getNumero()<=0}">
                            <form:label path="veicolo">Scegli il veicolo</form:label>
                        </c:when>
                        <c:otherwise>
                            <form:label path="veicolo" class="text-warning">Seleziona un nuovo veicolo</form:label>
                        </c:otherwise>
                    </c:choose>

<%--                    <form:select class="form-control text-center" path="veicolo" id="veicolo">--%>
<%--                    <c:if test="${requestScope.prenotazioneDTO.getVeicolo()!=null}">--%>
<%--                        <form:option value="${requestScope.prenotazioneDTO.getVeicolo().getCodiceMezzo()}"--%>
<%--                                     label="${requestScope.prenotazioneDTO.getVeicolo().getCasaCostruttrice()}--%>
<%--                                         ${requestScope.prenotazioneDTO.getVeicolo().getModello()}.--%>
<%--                                         Tipologia: ${requestScope.prenotazioneDTO.getVeicolo().getTipologia()}.--%>
<%--                                         Al prezzo di ${requestScope.prenotazioneDTO.getVeicolo().getPrezzoGiornata()}€--%>
<%--                                         al giorno."/>--%>
<%--                    </c:if>--%>
<%--                    <form:options items="${requestScope.veicoliHashMap}"/>--%>
                        <%--                        <c:forEach items="${requestScope.listaVeicoliDTO}" var="item">--%>
                        <%--                            <option value="${item.getCodiceMezzo()}">${item.getCasaCostruttrice()} ${item.getModello()} &emsp;&emsp; Tipologia: ${item.getTipologia()} &emsp;&emsp; Al prezzo di ${item.getPrezzoGiornata()}€ al giorno.</option>--%>
                        <%--                        </c:forEach>--%>
<%--                    </form:select>--%>
                    <form:select class="form-control text-center" path="codiceMezzo" id="veicolo">
                        <c:if test="${requestScope.prenotazioneDTO.getVeicolo()!=null}">
                            <form:option value="${requestScope.prenotazioneDTO.getVeicolo().getCodiceMezzo()}"
                                         label="${requestScope.prenotazioneDTO.getVeicolo().getCasaCostruttrice()}
                                         ${requestScope.prenotazioneDTO.getVeicolo().getModello()}.
                                         Tipologia: ${requestScope.prenotazioneDTO.getVeicolo().getTipologia()}.
                                         Al prezzo di ${requestScope.prenotazioneDTO.getVeicolo().getPrezzoGiornata()}&#0128;
                                         al giorno."/>
                        </c:if>
                        <form:options items="${requestScope.listaVeicoliDTO}" itemValue="codiceMezzo" itemLabel="dettagli"/>
<%--                        <c:forEach items="${requestScope.listaVeicoliDTO}" var="item">--%>
<%--                            <option value="${item.getCodiceMezzo()}">${item.getCasaCostruttrice()} ${item.getModello()} &emsp;&emsp; Tipologia: ${item.getTipologia()} &emsp;&emsp; Al prezzo di ${item.getPrezzoGiornata()}€ al giorno.</option>--%>
<%--                        </c:forEach>--%>
                    </form:select>
                    <form:errors path="veicolo" cssClass="text-danger"/>
                </div>
                <div class="form-group">
                    <form:label path="dataInizio">Data di inizio della prenotazione:</form:label>
                    <form:input type="date" class="form-control" path="dataInizio" id="dataInizio" onchange="TDate()" required="required"/> <%--<c:if test="${requestScope.dataInizioDaModificare != null}"> value="${requestScope.dataInizioDaModificare}" </c:if>--%>
                    <form:errors path="dataInizio" cssClass="text-danger"/>
                </div>
                <div class="form-group">
                    <form:label path="dataFine">Data del termine della prenotazione:</form:label>
                    <form:input type="date" class="form-control" path="dataFine" id="dataFine" onchange="TDate2()" required="required"/> <%--<c:if test="${requestScope.dataFineDaModificare != null}"> value="${requestScope.dataFineDaModificare}" </c:if>--%>
                    <form:errors path="dataFine" cssClass="text-danger"/>
                </div>

                <div class="form-group">
                    <form:label path="codiceSconto">Codice Sconto (opzionale):</form:label>
                    <form:input type="text" class="form-control" path="codiceSconto" id="codiceSconto" maxlength="${BuonoSconto.getLunghezzaCodiceSconto()}" autocomplete="off"/> <%-- <c:if test="${requestScope.codiceScontoDaModificare != null}"> value="${requestScope.codiceScontoDaModificare}" </c:if>--%>
                    <form:errors path="codiceSconto" cssClass="text-danger"/>
                </div>
                <input type="hidden" name="codiceFiscaleUtentePrenotazione" value="${requestScope.codiceFiscaleUtentePrenotazione}">
                <form:hidden path="codiceFiscale" value="${codiceFiscaleUtentePrenotazione}"/>
<%--                <c:if test="${requestScope.numeroPrenotazioneDaModificare!=null}">--%>
<%--                    <input type="hidden" name="numeroPrenotazioneDaModificare" value="${requestScope.numeroPrenotazioneDaModificare}">--%>
<%--                </c:if>--%>
                <c:if test="${requestScope.numeroPrenotazioneDaModificare!=null}">
                    <form:hidden path="numero" value="${numeroPrenotazioneDaModificare}"/>
                </c:if>
                <div class="form-group">
                    <br><form:button type="submit" class="btn btn-primary">Crea prenotazione</form:button>
                </div>
            </form:form>
        </div>

        <script>
            Date.prototype.toDateInputValue = (function() {
                var local = new Date(this);
                local.setMinutes(this.getMinutes() - this.getTimezoneOffset());
                return local.toJSON().slice(0,10);
            });

            function TDate() {
                var UserDate = document.getElementById("formDataInizioPrenotazione").value;
                var ToDate = new Date();

                if (new Date(UserDate).getTime() <= ToDate.getTime()) {
                    alert("Non puoi effettuare prenotazioni per giorni passati o il giorno attuale");
                    var today = new Date();
                    var tomorrow = new Date();
                    tomorrow.setDate(today.getDate()+1);
                    document.getElementById("formDataInizioPrenotazione").value = tomorrow.toDateInputValue();
                    return false;
                }
                return true;
            }

            function TDate2() {
                var UserDate = document.getElementById("formDataFinePrenotazione").value;
                var ToDate = new Date();

                if (new Date(UserDate).getTime() <= ToDate.getTime()) {
                    alert("Non puoi effettuare prenotazioni per giorni passati o il giorno attuale");
                    var today = new Date();
                    var tomorrow = new Date();
                    tomorrow.setDate(today.getDate()+1);
                    document.getElementById("formDataFinePrenotazione").value = tomorrow.toDateInputValue();
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
