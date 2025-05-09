package MyFitness.Statistics;

import java.time.LocalDate;
import java.util.Objects;

public class Statistic {

    private final double statValue;
    private final String statName;
    private final LocalDate date;

    public Statistic( String statName, double statValue, LocalDate date) {
        this.statName = statName;
        this.statValue = statValue;
        this.date = date;
    }

    public double getStatValue() {
        return statValue;
    }

    public String getStatName() {
        return statName;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "value=" + statValue +
                ", label='" + statName + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Statistic statistic = (Statistic) o;
        return Double.compare(statValue, statistic.statValue) == 0 && Objects.equals(statName, statistic.statName) && Objects.equals(date, statistic.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statValue, statName, date);
    }
}
