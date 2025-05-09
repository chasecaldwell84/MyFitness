package MyFitness.Statistics;

import java.util.Objects;

public class Statistic {

    private final double statValue;
    private final String statName;

    public Statistic( String statName, double statValue) {
        this.statName = statName;
        this.statValue = statValue;
    }

    public double getStatValue() {
        return statValue;
    }

    public String getStatName() {
        return statName;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "value=" + statValue +
                ", label='" + statName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Statistic statistic = (Statistic) o;
        return Double.compare(statValue, statistic.statValue) == 0 && Objects.equals(statName, statistic.statName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statValue, statName);
    }
}
