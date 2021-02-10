import javafx.util.Pair;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static Map<String, IncreaseInfo> hashMap = new HashMap<>();
    public static PriorityQueue<Pair<String, Double>> maxIncreaseQueue = new PriorityQueue<>(Comparator.comparing(Pair::getValue));
    public static PriorityQueue<Pair<String, Double>> minIncreaseQueue = new PriorityQueue<>((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

    private static Trade stringToTrade(String string) { return new Trade(string.split("\t")); }

    public static void main(String[] args) throws IOException {
        Files.lines(Paths.get("C:\\Users\\User\\IdeaProjects\\Stream API\\src\\com\\company\\trades.txt"))
                .skip(1)
                .map(Main::stringToTrade)
                .forEach(t -> {
                    if(!hashMap.containsKey(t.seccode)) {
                        hashMap.put(t.seccode, new IncreaseInfo(Double.MIN_VALUE, Double.MAX_VALUE, t.price));
                        return;
                    }

                    IncreaseInfo increaseInfo = hashMap.get(t.seccode);
                    increaseInfo.overallValue += t.value;
                    Double currentIncrease = t.price - increaseInfo.lastPrice;

                    if (increaseInfo.maxIncrease < currentIncrease) {
                        increaseInfo.maxIncrease = currentIncrease;
                    } else if (increaseInfo.minIncrease > currentIncrease) {
                        increaseInfo.minIncrease = currentIncrease;
                    }

                    increaseInfo.lastPrice = t.price;
                });

        for (Map.Entry<String, IncreaseInfo> entry : hashMap.entrySet()) {

            if (maxIncreaseQueue.size() < 10) {
                maxIncreaseQueue.add(new Pair<>(entry.getKey(), entry.getValue().maxIncrease));
            } else if (entry.getValue().maxIncrease > maxIncreaseQueue.peek().getValue()) {
                maxIncreaseQueue.poll();
                maxIncreaseQueue.add(new Pair<>(entry.getKey(), entry.getValue().maxIncrease));
            }

            if (minIncreaseQueue.size() < 10) {
                minIncreaseQueue.add(new Pair<>(entry.getKey(), entry.getValue().minIncrease));
            } else if (entry.getValue().minIncrease < minIncreaseQueue.peek().getValue()) {
                minIncreaseQueue.poll();
                minIncreaseQueue.add(new Pair<>(entry.getKey(), entry.getValue().minIncrease));
            }

        }

        System.out.println("Удачливые акции:");
        for (Pair<String, Double> pair : maxIncreaseQueue) {
            System.out.printf("%8s:  %12.3f%n", pair.getKey(), pair.getValue());
        }

        System.out.println("Неудачливые акции:");
        for (Pair<String, Double> pair : minIncreaseQueue) {
            System.out.printf("%8s:  %12.3f%n", pair.getKey(), pair.getValue());
        }

        for (Map.Entry<String, IncreaseInfo> entry : hashMap.entrySet()) {
            System.out.printf("%8s:  открытие: %8.3f руб. - закрытие: %12.3f руб. - измененение в процентах: %.1f %n",
                    entry.getKey(),entry.getValue().openPrice, entry.getValue().lastPrice,
                    entry.getValue().openPrice/entry.getValue().lastPrice);
            System.out.printf("Общий объем сделок:  %8.3f руб. %n%n", entry.getValue().overallValue);
        }
    }
}
