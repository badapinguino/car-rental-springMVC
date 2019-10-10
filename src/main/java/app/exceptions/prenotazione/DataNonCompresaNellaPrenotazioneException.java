package app.exceptions.prenotazione;

public class DataNonCompresaNellaPrenotazioneException extends Exception{
    String error = "La data selezionata non è compresa tra la data di inizio e di fine della prenotazione.";
    public DataNonCompresaNellaPrenotazioneException(){}

    public String getError() {
        return error;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
}