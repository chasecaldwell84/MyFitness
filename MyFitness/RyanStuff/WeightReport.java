package MyFitness.RyanStuff;

public class WeightReport {
    private double weight;

    public WeightReport() {
        this.weight = 0.0;
    }

    public void setWeight(double weight) {
        if(weight > 0){
            this.weight = weight;
        }
    }

    public double getWeight() {
        return weight;
    }

    public void reset(){
        this.weight = 0.0;
    }
}
