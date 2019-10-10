package app.model;

import app.exceptions.prenotazione.DataNonCompresaNellaPrenotazioneException;
import app.service.PrenotazioniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Problema")
public class Multa implements Serializable {
    private static final int lunghezzaCampoDescrizione = 256;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="codice", nullable = false)
    private int codice;

    @NotNull
    @Size(max = lunghezzaCampoDescrizione)
    @Column(name="descrizione", nullable = false)
    private String descrizione;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column(name="data_problema", nullable = true)
    private LocalDate dataProblema;

    @NotNull
    @ManyToOne
    @JoinColumn(name="id_prenotazione", nullable = false)
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


    public void setDataProblema(LocalDate dataProblema){
            this.dataProblema = dataProblema;
    }

    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    public static int getLunghezzaCampoDescrizione() {
        return lunghezzaCampoDescrizione;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Multa)) return false;
        Multa multa = (Multa) o;
        return codice == multa.codice;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codice);
    }
}
