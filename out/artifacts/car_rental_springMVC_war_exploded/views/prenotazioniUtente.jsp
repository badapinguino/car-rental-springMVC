<%@ page import="app.DTO.PrenotazioneDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="app.DTO.UtenteDTO" %>
<%@ page import="app.service.MulteService" %>
<%@ page import="java.time.LocalDate" %><%--
  Created by IntelliJ IDEA.
  User: SI2001
  Date: 06/08/2019
  Time: 09:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <head>
        <title>Prenotazioni</title>
    </head>
    <body>
<%--        <%@include file="../header.jsp"%>--%>
        <div class="container">
<%--        visualizzare i due messaggi per approvazione e per eliminazione se esistono--%>
<%--            <c:if test="${requestScope.utenteDTOEliminato != null}">--%>
<%--                <h5 class="text-success">Utente <%=((UtenteDTO) request.getAttribute("utenteDTOEliminato")).getNome()%> <%=((UtenteDTO) request.getAttribute("utenteDTOEliminato")).getCognome()%> eliminato</h5><br>--%>
<%--            </c:if>--%>
            <c:choose>
                <c:when test="${sessionScope.superuserSessione}">
                    <br><h4 class="text-center text-primary">Prenotazioni per l'utente <%=((UtenteDTO) request.getAttribute("utenteDTOPrenotazioni")).getNome()%> <%=((UtenteDTO) request.getAttribute("utenteDTOPrenotazioni")).getCognome()%></h4><br>
                </c:when>
                <c:otherwise>
                    <br><h3 class="text-center text-primary">Benvenuto <%=((UtenteDTO) request.getAttribute("utenteDTOPrenotazioni")).getNome()%> <%=((UtenteDTO) request.getAttribute("utenteDTOPrenotazioni")).getCognome()%>. Ecco le tue prenotazioni:</h3><br>
                    <button onclick="location.href='./creaModificaPrenotazioneUtente?codiceFiscaleUtentePrenotazione=${sessionScope.codiceFiscaleSessione}'" class="btn btn-primary">Crea una prenotazione</button><br><br>
                </c:otherwise>
            </c:choose>

            <c:if test="${requestScope.prenotazioneApprovata != null}">
                <c:if test="${requestScope.prenotazioneApprovata == true}">
                    <h5 class="text-success">Prenotazione approvata con successo</h5>
                </c:if>
            </c:if>
            <c:if test="${requestScope.prenotazioneEliminata != null}">
                <c:if test="${requestScope.prenotazioneEliminata == true}">
                    <h5 class="text-danger">Prenotazione eliminata con successo</h5>
                </c:if>
            </c:if>
            <!-- tabella -->
            <table id="tabellaUtenti" class="table table-striped table-bordered">
                <%--                <caption>--%>
                <%--                    <p>Tabella Utenti</p>--%>
                <%--                </caption>--%>
                <thead>
                <tr>
                    <th scope="col" class="text-center">Numero prenotazione</th>
                    <th scope="col" class="text-center">Data inizio</th>
                    <th scope="col" class="text-center">Data fine</th>
                    <th scope="col" class="text-center">Codice Veicolo</th>
                    <th scope="col" class="text-center">Targa</th>
                    <th scope="col" class="text-center">Modello</th>
                    <th scope="col" class="text-center">Buono sconto</th>
                    <th scope="col" class="text-center">Approvata</th>
                    <c:choose>
                        <c:when test="${sessionScope.superuserSessione == true}">
                            <th scope="col" class="text-center">Approva</th>
                        </c:when>
                        <c:otherwise>
                            <th scope="col" class="text-center">Modifica</th>
                        </c:otherwise>
                    </c:choose>
                    <th scope="col" class="text-center">Elimina</th>
                </tr>
                </thead>
                <tbody>
                <%
                    List<PrenotazioneDTO> listaPrenotazioni = (List<PrenotazioneDTO>) request.getAttribute("prenotazioniDTOUtente");
                    if(listaPrenotazioni!=null && !listaPrenotazioni.isEmpty()){
                        int contatore = 0;
                        for (PrenotazioneDTO p : listaPrenotazioni) {
                %>
                <tr>
                    <%--                                    <th scope="row"></th>--%>
                    <td class="text-center"><%=p.getNumero()%></td>
                    <td class="text-center"><%
                        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        String dataInizioStringDDMMYYYY = p.getDataInizio().format(pattern);
                    %><%=dataInizioStringDDMMYYYY%>
                    <td class="text-center"><%
                        String dataFineStringDDMMYYYY = p.getDataFine().format(pattern);
                    %><%=dataFineStringDDMMYYYY%>
                    </td>
                    <td class="text-center"><%=p.getVeicolo().getCodiceMezzo()%></td>
                    <td class="text-center"><%=p.getVeicolo().getTarga()%></td>
                    <td class="text-center"><%=p.getVeicolo().getCasaCostruttrice()%> <%=p.getVeicolo().getModello()%></td>
                    <td class="text-center">
                        <%
                            String buonoScontoCodice = "";
                            if(p.getBuonoSconto()!=null && p.getBuonoSconto().getCodiceSconto()!=null){
                                buonoScontoCodice = p.getBuonoSconto().getCodiceSconto();
                            }
                        %><%=buonoScontoCodice%>
                    </td>
                    <td class="text-center">
                        <%
                            if(p.isApprovata()){
                                %>Sì<%
                            }else{
                                %>No<%
                            }
                        %>
                    </td>
                    <c:if test="${sessionScope.superuserSessione == true}">
                        <!-- Inserire qui il pulsante per approvare se l'utente è moroso -->
                        <%
                            // va bene o devo mettere plusDays(2)???
                             if((p.getDataInizio()).compareTo(LocalDate.now())>=0){
                                //ATTENZIONE, qui uso del codice per accedere al db dalla jsp, di solito lo facevo nella servlet ma non ho altre idee, va bene?
                                 //prima avevo messo solo: se utente è moroso E se non è stata approvata, poi ho preferito generalizzare con un: se non è stata approvata
                                 MulteService multeService = (MulteService) request.getAttribute("multeService");
                                 if((multeService.verificaUtenteMorosoByCF(p.getUtente().getCodiceFiscale()) && !(p.isApprovata())) || !(p.isApprovata())){%>
                                    <td>
                                        <form action="./prenotazioniUtente" method="post"> <!-- Riutilizzo stessa pagina della creazione/modifica -->
                                            <input type="hidden" name="numeroPrenotazioneDaApprovare" value="<%=p.getNumero()%>"/>
                                            <input type="hidden" name="codiceFiscaleUtentePerPrenotazioni" value="${requestScope.codiceFiscaleUtentePerPrenotazioni}">
                                            <button type="submit" class="btn btn-warning">Approva</button>
                                        </form>
                                    </td>
                            <%  }else{
                                    %><td></td><%
                                }%>
                                <td class="text-center">
                                    <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#eliminaPrenotazioneModal<%=contatore%>">Elimina</button>
                                </td>

                                <div class="modal fade" tabindex="-1" id="eliminaPrenotazioneModal<%=contatore%>"
                                     data-keyboard="false" data-backdrop="static">
                                    <div class="modal-dialog modal-lg">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h4 class="modal-title text-danger">Eliminazione buono sconto</h4>
                                                <button type="button" class="close" data-dismiss="modal">
                                                    &times;
                                                </button>
                                            </div>
                                            <div class="modal-body">
                                                <p>Sei sicuro di voler eliminare la prenotazione <%=p.getNumero()%> da te selezionata? Verranno eliminate anche tutte le multe ad essa associate</p>
                                            </div>
                                            <div class="modal-footer">
                                                <form action="./prenotazioniUtente" method="post"> <!-- Riutilizzo stessa pagina della creazione/modifica -->
                                                    <input type="hidden" name="numeroPrenotazioneDaEliminare" value="<%=p.getNumero()%>"/>
                                                    <input type="hidden" name="codiceFiscaleUtentePerPrenotazioni" value="${requestScope.codiceFiscaleUtentePerPrenotazioni}">
                                                    <button type="submit" class="btn btn-danger">Elimina</button>
                                                </form>
                                                <button type="button" class="btn btn-secondary"
                                                        data-dismiss="modal">Annulla</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                        <%  }else{
                                %><td></td><td></td><%
                            }%>
                    </c:if>
                    <c:if test="${sessionScope.superuserSessione==false}">
                        <%
                            if((p.getDataInizio()).compareTo(LocalDate.now().plusDays(2))>=0){%>
                                <td>
                                    <form action="./creaModificaPrenotazioneUtente" method="get"> <!-- Riutilizzo stessa pagina della creazione/modifica -->
                                        <input type="hidden" name="numeroPrenotazioneDaModificare" value="<%=p.getNumero()%>"/>
                                        <input type="hidden" name="codiceFiscaleUtentePrenotazione" value="${requestScope.codiceFiscaleUtentePerPrenotazioni}">
                                        <button type="submit" class="btn btn-outline-secondary">Modifica</button>
                                    </form>
                                </td>
                                <td class="text-center">
                                    <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#eliminaPrenotazioneModal<%=contatore%>">Elimina</button>
                                </td>

                                <div class="modal fade" tabindex="-1" id="eliminaPrenotazioneModal<%=contatore%>"
                                     data-keyboard="false" data-backdrop="static">
                                    <div class="modal-dialog modal-lg">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h4 class="modal-title text-danger">Eliminazione buono sconto</h4>
                                                <button type="button" class="close" data-dismiss="modal">
                                                    &times;
                                                </button>
                                            </div>
                                            <div class="modal-body">
                                                <p>Sei sicuro di voler eliminare la prenotazione <%=p.getNumero()%> da te selezionata? Verranno eliminate anche tutte le multe ad essa associate</p>
                                            </div>
                                            <div class="modal-footer">
                                                <form action="./prenotazioniUtente" method="post">
                                                    <input type="hidden" name="numeroPrenotazioneDaEliminare" value="<%=p.getNumero()%>"/>
                                                    <input type="hidden" name="codiceFiscaleUtentePerPrenotazioni" value="${requestScope.codiceFiscaleUtentePerPrenotazioni}">
                                                    <button type="submit" class="btn btn-danger">Elimina</button>
                                                </form>
                                                <button type="button" class="btn btn-secondary"
                                                        data-dismiss="modal">Annulla</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                        <%  }else{
                            %><td></td><td></td><%
                        }%>
                    </c:if>
                </tr>
                <%
                        contatore++;
                    }
                }else{
                %>
                <tr>
                    <td>L'utente non ha effettuato prenotazioni</td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
        <!-- JS -->
        <script src="//code.jquery.com/jquery.js"></script>
        <script src="resources/js/bootstrap.min.js"></script>
        <!-- JS -->
    </body>
</html>
