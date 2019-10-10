package app.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "Utente")
public class Utente {
    private static final int lunghezzaCampoCognome = 80;
    private static final int lunghezzaCampoNome = 80;
    private static final int lunghezzaCampoCodiceFiscale = 16;
    private static final int lunghezzaCampoPassword = 42;

    public static int getLunghezzaCampoPassword() {
        return lunghezzaCampoPassword;
    }

    public static int getLunghezzaCampoCodiceFiscale(){
        return lunghezzaCampoCodiceFiscale;
    }
    public static int getLunghezzaCampoCognome(){
        return lunghezzaCampoCognome;
    }
    public static int getLunghezzaCampoNome(){
        return lunghezzaCampoNome;
    }

    @Id
    @Size(min=lunghezzaCampoCodiceFiscale, max = lunghezzaCampoCodiceFiscale)
    @Column(name = "codice_fiscale", nullable = false)
    private String codiceFiscale;

    @NotNull
    @Size(max = lunghezzaCampoCognome)
    @Column(name = "cognome", nullable = false)
    private String cognome;

    @NotNull
    @Size(max = lunghezzaCampoNome)
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Past
    @DateTimeFormat(pattern="yyyy-MM-dd") //controllare se va bene così "dd/MM/yyyy"
    @Column(name = "data_nascita", nullable = false)
    private LocalDate dataNascita;

    @NotNull
    @Column(name = "superuser", nullable = false)
    private boolean superuser;

    @NotNull
    @Size(max=lunghezzaCampoPassword)
    @Column(name = "password_utente", nullable = false)
    private String password;

    @Size(max=256)
    @Column(name = "immagine")
    private String immagine;

    @OneToMany(mappedBy = "utente", cascade=CascadeType.REMOVE)
    private Set<Prenotazione> prenotazioni;


    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public boolean isSuperuser() {
        return superuser;
    }

    public void setSuperuser(boolean superuser) {
        this.superuser = superuser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public Set<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public void setPrenotazioni(Set<Prenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }
}
