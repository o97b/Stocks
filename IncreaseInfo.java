public class IncreaseInfo {
    public Double maxIncrease, minIncrease;
    public Double openPrice, lastPrice, overallValue;

    public IncreaseInfo(Double maxIncrease, Double minIncrease, Double price) {
        this.overallValue = 0.0;
        this.maxIncrease = maxIncrease;
        this.minIncrease = minIncrease;
        this.openPrice = price;
        this.lastPrice = price;
    }
}
