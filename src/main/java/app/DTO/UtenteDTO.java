package app.DTO;

import app.model.Utente;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class UtenteDTO {
    private static final int lunghezzaCampoCognome = 80;
    private static final int lunghezzaCampoNome = 80;
    private static final int lunghezzaCampoCodiceFiscale = 16;
    private static final int lunghezzaCampoPassword = 42;

    @Size(min = lunghezzaCampoCodiceFiscale, max = lunghezzaCampoCodiceFiscale)
    private String codiceFiscale;

    @Size(max = lunghezzaCampoCognome)
    private String cognome;

    @Size(max = lunghezzaCampoNome)
    private String nome;

    @Past
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate dataNascita;

    private boolean superuser;

    @Size(max=lunghezzaCampoPassword)
    private String password;

    @Size(max=256)
    private String immagine;

    public UtenteDTO(String codiceFiscale, String cognome, String nome, LocalDate dataNascita, String password, boolean superuser) {
        this.codiceFiscale = codiceFiscale;
        this.cognome = cognome;
        this.nome = nome;
        this.dataNascita = dataNascita;
        this.superuser = superuser;
        this.password = password;
    }
    public UtenteDTO() {}

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

    public LocalDate getDataNascita() { return dataNascita; }

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

    public static int getLunghezzaCampoCognome() {
        return lunghezzaCampoCognome;
    }

    public static int getLunghezzaCampoNome() {
        return lunghezzaCampoNome;
    }

    public static int getLunghezzaCampoCodiceFiscale() {
        return lunghezzaCampoCodiceFiscale;
    }

    public static int getLunghezzaCampoPassword() {
        return lunghezzaCampoPassword;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public static UtenteDTO toDto(Utente utente){
        UtenteDTO uDTO = new UtenteDTO();
        uDTO.setCodiceFiscale(utente.getCodiceFiscale());
        uDTO.setNome(utente.getNome());
        uDTO.setCognome(utente.getCognome());
        uDTO.setDataNascita(utente.getDataNascita());
        uDTO.setSuperuser(utente.isSuperuser());
        uDTO.setImmagine(utente.getImmagine());
        // uDTO.setPassword(utente.getPassword());
        return uDTO;
    }
}
