package app.DTO;

import app.model.Prenotazione;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDate;

public class MultaDTO {
    private static final int lunghezzaCampoDescrizione = 256;

    public static int getLunghezzaCampoDescrizione() {
        return lunghezzaCampoDescrizione;
    }

    private int codice;
    @Size(max = lunghezzaCampoDescrizione)
    private String descrizione;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate dataProblema;
    private Prenotazione prenotazione;

    public int getCodice() {
        return codice;
    }

    public void setCodice(int codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public LocalDate getDataProblema() {
        return dataProblema;
    }

    public void setDataProblema(LocalDate dataProblema) {
        this.dataProblema = dataProblema;
    }

    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }


    private String numeroPrenotazione;

    public String getNumeroPrenotazione() {
        return numeroPrenotazione;
    }

    public void setNumeroPrenotazione(String numeroPrenotazione) {
        this.numeroPrenotazione = numeroPrenotazione;
    }
}
