package app.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="Prenotazione")
public class Prenotazione implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="numero", nullable = false)
    private int numero;

    @NotNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    //mettere anche controllo @Future? in teoria un domani quel futuro potrebbe essere passato, in quel caso come si comporterebbe?
    @Column(name="data_inizio", nullable = false)
    private LocalDate dataInizio;

    @NotNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column(name="data_fine", nullable = false)
    private LocalDate dataFine;

    @NotNull
    @Column(name="approvata", nullable = false)
    private boolean approvata;

    @OneToMany(mappedBy = "prenotazione", cascade=CascadeType.REMOVE)
    private Set<Multa> multe;

    @NotNull
    @ManyToOne
    @JoinColumn(name="id_veicolo", nullable = false)
    private Veicolo veicolo;

    @NotNull
    @ManyToOne
    @JoinColumn(name="id_utente", nullable = false)
    private Utente utente;

    @ManyToOne
    @JoinColumn(name="id_buono_sconto", nullable = true)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prenotazione)) return false;
        Prenotazione that = (Prenotazione) o;
        return numero == that.numero;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }
}
