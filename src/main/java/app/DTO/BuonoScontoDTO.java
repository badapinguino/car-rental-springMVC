package app.DTO;

import app.model.Prenotazione;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Set;

public class BuonoScontoDTO {
    private static final int lunghezzaCodiceSconto=15;

    public static int getLunghezzaCodiceSconto() {
        return lunghezzaCodiceSconto;
    }

    @Size(max = lunghezzaCodiceSconto)
    private String codiceSconto;

    @Positive
    @Digits(integer=8, fraction=2)
    private float valore;

    private boolean percentuale;
    private Set<Prenotazione> prenotazioni;

    public String getCodiceSconto() {
        return codiceSconto;
    }

    public void setCodiceSconto(String codiceSconto) {
        this.codiceSconto = codiceSconto;
    }

    public float getValore() {
        return valore;
    }

    public void setValore(float valore) {
        this.valore = valore;
    }

    public boolean isPercentuale() {
        return percentuale;
    }

    public void setPercentuale(boolean percentuale) {
        this.percentuale = percentuale;
    }

    public Set<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public void setPrenotazioni(Set<Prenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }
}
