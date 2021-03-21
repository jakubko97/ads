public class Key {
    private int id;                         //1st key has index 1, index = 0 is for the non existent p_0 and q_0 of the dummy key
    private double probabilityOfKey;        //pi aka p
    private double probabilityOfDummyKey;   //qi aka q
    private String keyString;

    public Key(int id, double pi, double qi, String k){
        this.id = id;
        probabilityOfKey = pi;
        probabilityOfDummyKey = qi;
        keyString = k;
    }

    public double getProbabilityOfKey() {
        return probabilityOfKey;
    }

    public double getProbabilityOfDummyKey() {
        return probabilityOfDummyKey;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyString(){
        return keyString;
    }

}
