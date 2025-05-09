package MyFitness.RyanStuff;

public class WeightReport {
    private double weight;
    private int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

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
