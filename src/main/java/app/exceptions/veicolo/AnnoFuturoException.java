package app.exceptions.veicolo;

public class AnnoFuturoException extends Exception {
    String error = "L'anno inserito non è valido, non può essere maggiore dell'anno prossimo.";
    public AnnoFuturoException(){}

    public String getError() {
        return error;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
}