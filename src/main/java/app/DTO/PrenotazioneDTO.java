package app.DTO;

import app.model.BuonoSconto;
import app.model.Multa;
import app.model.Utente;
import app.model.Veicolo;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

public class PrenotazioneDTO {
    private int numero;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate dataInizio;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate dataFine;
    private boolean approvata;
    private Set<Multa> multe;
    private Veicolo veicolo;
    private Utente utente;
    private BuonoSconto buonoSconto;

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    public Set<Multa> getMulte() {
        return multe;
    }

    public void setMulte(Set<Multa> multe) {
        this.multe = multe;
    }

    public Veicolo getVeicolo() {
        return veicolo;
    }

    public void setVeicolo(Veicolo veicolo) {
        this.veicolo = veicolo;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public BuonoSconto getBuonoSconto() {
        return buonoSconto;
    }

    public void setBuonoSconto(BuonoSconto buonoSconto) {
        this.buonoSconto = buonoSconto;
    }

    public boolean isApprovata() {
        return approvata;
    }

    public void setApprovata(boolean approvata) {
        this.approvata = approvata;
    }


    //aggiungo un id del codice sconto
    private static final int lunghezzaCodiceSconto=15;
    @Size(max = lunghezzaCodiceSconto)
    private String codiceSconto;

    public static int getLunghezzaCodiceSconto() {
        return lunghezzaCodiceSconto;
    }

    public String getCodiceSconto() {
        return codiceSconto;
    }

    public void setCodiceSconto(String codiceSconto) {
        this.codiceSconto = codiceSconto;
    }

    //aggiungo un id per il veicolo
    private static final int lunghezzaCampoCodiceMezzo = 15;

    @Size(max = lunghezzaCampoCodiceMezzo)
    private String codiceMezzo;

    public static int getLunghezzaCampoCodiceMezzo() {
        return lunghezzaCampoCodiceMezzo;
    }

    public String getCodiceMezzo() {
        return codiceMezzo;
    }

    public void setCodiceMezzo(String codiceMezzo) {
        this.codiceMezzo = codiceMezzo;
    }

    private final int lunghezzaCodiceFiscale = 16;

    @Size(max = lunghezzaCodiceFiscale)
    private String codiceFiscale;

    public int getLunghezzaCodiceFiscale() {
        return lunghezzaCodiceFiscale;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    private String dettagli;

    public String getDettagli() {
        return dettagli;
    }

    public void setDettagli(String dettagli) {
        this.dettagli = dettagli;
    }
}
