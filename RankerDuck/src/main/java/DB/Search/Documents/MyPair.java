package DB.Search.Documents;

public class MyPair{
    private final String key;
    private final Double value;

    public MyPair(String key, Double value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Double getValue() {
        return value;
    }
}
