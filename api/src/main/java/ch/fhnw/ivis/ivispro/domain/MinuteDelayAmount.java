package ch.fhnw.ivis.ivispro.domain;

public class MinuteDelayAmount {
    private int minutesDelay;
    private int occurrencesOfDelay;

    public MinuteDelayAmount(int minutesDelay, int occurrencesOfDelay) {
        this.minutesDelay = minutesDelay;
        this.occurrencesOfDelay = occurrencesOfDelay;
    }

    public int getMinutesDelay() {
        return minutesDelay;
    }

    public void setMinutesDelay(int minutesDelay) {
        this.minutesDelay = minutesDelay;
    }

    public int getOccurrencesOfDelay() {
        return occurrencesOfDelay;
    }

    public void setOccurrencesOfDelay(int occurrencesOfDelay) {
        this.occurrencesOfDelay = occurrencesOfDelay;
    }
}