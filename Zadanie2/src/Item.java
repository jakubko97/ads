public class Item {
    private int id;
    private int value;
    private int weight;
    private int fragility;

    public Item(int id, int value, int weight, int fragility) {
        this.id = id;
        this.value = value;
        this.weight = weight;
        this.fragility = fragility;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getFragility() {
        return fragility;
    }

    public void setFragility(int fragility) {
        this.fragility = fragility;
    }

    @Override
    public String toString() {
        return "\nItem{" +
                "id=" + id +
                ", value=" + value +
                ", weight=" + weight +
                ", fragility=" + fragility +
                "}";
    }
}
