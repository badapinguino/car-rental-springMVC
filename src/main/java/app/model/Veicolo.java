package app.model;

import app.exceptions.veicolo.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="Veicolo")
public class Veicolo implements Serializable {
    private static final int lunghezzaCampoCodiceMezzo = 15;
    private static final int lunghezzaCampoTarga = 10;
    private static final int lunghezzaCampoCasaCostruttrice = 80;
    private static final int lunghezzaCampoModello = 80;
    private static final int lunghezzaCampoTipologia = 20;

    public static int getLunghezzaCampoCodiceMezzo() {
        return lunghezzaCampoCodiceMezzo;
    }

    public static int getLunghezzaCampoTarga() {
        return lunghezzaCampoTarga;
    }

    public static int getLunghezzaCampoModello() {
        return lunghezzaCampoModello;
    }

    public static int getLunghezzaCampoTipologia() {
        return lunghezzaCampoTipologia;
    }

    public static int getLunghezzaCampoCasaCostruttrice() {
        return lunghezzaCampoCasaCostruttrice;
    }

    @Id
    @Size(max = lunghezzaCampoCodiceMezzo)
    @Column(name="codice_mezzo", nullable = false)
    private String codiceMezzo;

    @NotNull
    @Size(max = lunghezzaCampoTarga)
    @Column(name="targa", nullable = false)
    private String targa;

    @NotNull
    @Size(max = lunghezzaCampoModello)
    @Column(name="modello", nullable = false)
    private String modello;

    @NotNull
    @Size(max = lunghezzaCampoCasaCostruttrice)
    @Column(name="casa_costruttrice", nullable = false)
    private String casaCostruttrice;

    @NotNull
    @Min(value = 1900)
    @Column(name="anno", nullable = false)
    private int anno;

    @NotNull
    @Size(max = lunghezzaCampoTipologia)
    @Column(name="tipologia", nullable = false)
    private String tipologia;

    @NotNull
    @Positive
    @Column(name="prezzo_giornata", nullable = false)
    private float prezzoGiornata;

    @OneToMany(mappedBy = "veicolo", cascade=CascadeType.REMOVE)
    private Set<Prenotazione> prenotazioni;

    public String getCodiceMezzo() {
        return codiceMezzo;
    }

    public void setCodiceMezzo(String codiceMezzo) {
        this.codiceMezzo = codiceMezzo;
    }

    public String getTarga() {
        return targa;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }

    public String getModello() {
        return modello;
    }

    public void setModello(String modello) {
        this.modello = modello;
    }

    public String getCasaCostruttrice() {
        return casaCostruttrice;
    }

    public void setCasaCostruttrice(String casaCostruttrice) {
        this.casaCostruttrice = casaCostruttrice;
    }

    public int getAnno() {
        return anno;
    }

    //si può impostare un anno come l'anno prossimo, pensando alla fine dell'anno, nella quale magari un dipendente vuole cominciare ad inserire un auto che verrà immatricolata l'anno dopo
    public void setAnno(int anno) throws AnnoFuturoException {
        if(anno<=Calendar.getInstance().get(Calendar.YEAR)+1){
            this.anno = anno;
        }else{
            throw new AnnoFuturoException();
        }
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public float getPrezzoGiornata() {
        return prezzoGiornata;
    }

    public void setPrezzoGiornata(float prezzoGiornata){
        this.prezzoGiornata = prezzoGiornata;
    }

    public Set<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public void setPrenotazioni(Set<Prenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Veicolo)) return false;
        Veicolo veicolo = (Veicolo) o;
        return codiceMezzo.equals(veicolo.codiceMezzo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceMezzo);
    }
}
