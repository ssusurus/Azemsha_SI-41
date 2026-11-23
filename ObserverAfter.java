// Observer: станция оповещает подписчиков о прибытии поезда.
// Вариант 1: событие трактуется как «вагон подан под погрузку»
// (номер поезда/вагона передаётся в trainNumber).
import java.util.ArrayList;
import java.util.List;

public class ObserverAfter {

    private static int failedChecks = 0;

    public static void main(String[] args) {

        Station station = new Station("Гомель");

        BoardListener board = new BoardListener();
        JournalListener journal = new JournalListener();
        MailingListener mailing = new MailingListener();

        station.addListener(board);
        station.addListener(journal);
        station.addListener(mailing);

        System.out.println("--- Событие 1: поезд 3022 ---");
        station.trainArrived("3022");

        System.out.println();
        System.out.println("--- Проверки ---");

        // 1. После одного события журнал получил ровно одну запись
        check("журнал получил ровно одну запись", journal.getEntries().size() == 1);

        // 2. После отписки наблюдателя его записи больше не появляются
        station.removeListener(journal);
        station.trainArrived("6701");
        check("после отписки журнал не пополнился", journal.getEntries().size() == 1);

        // 3. Все подписчики получили одно и то же событие
        //    (табло и рассылка видели оба события, журнал — только первое)
        check("табло получило оба события", board.getEvents().size() == 2);
        check("рассылка получила оба события", mailing.getEvents().size() == 2);
        check("все получили одинаковое первое событие",
              board.getEvents().get(0).equals(mailing.getEvents().get(0))
              && board.getEvents().get(0).equals(journal.getEntries().get(0)));

        // 4. Четвёртый наблюдатель добавлен без правки Station
        AsuListener asu = new AsuListener();
        station.addListener(asu);
        station.trainArrived("8800");
        check("четвёртый наблюдатель (АСУ) получил событие", asu.getEvents().size() == 1);
        check("Station не знает про АСУ — он просто в списке наблюдателей",
              asu.getEvents().get(0).contains("8800"));

        if (failedChecks > 0) {
            throw new AssertionError("не пройдено проверок: " + failedChecks);
        }
        System.out.println();
        System.out.println("Все проверки пройдены.");
    }

    static void check(String checkName, boolean passed) {
        System.out.println((passed ? "[OK]   " : "[FAIL] ") + checkName);
        if (!passed) failedChecks++;
    }
}

// ---------- наблюдатель ----------
interface ArrivalListener {
    void onTrainArrived(String stationName, String trainNumber);
}

// ---------- субъект ----------
// Station НЕ знает, какие именно наблюдатели существуют.
class Station {

    private final String stationName;
    private final List<ArrivalListener> listeners = new ArrayList<>();

    Station(String stationName) {
        this.stationName = stationName;
    }

    void addListener(ArrivalListener listener) {
        listeners.add(listener);
    }

    void removeListener(ArrivalListener listener) {
        listeners.remove(listener);
    }

    void trainArrived(String trainNumber) {
        for (ArrivalListener listener : listeners) {
            listener.onTrainArrived(stationName, trainNumber);
        }
    }
}

// ---------- наблюдатели ----------
class BoardListener implements ArrivalListener {
    private final List<String> events = new ArrayList<>();
    public void onTrainArrived(String stationName, String trainNumber) {
        String event = stationName + ":" + trainNumber;
        events.add(event);
        System.out.println("[табло]  Поезд " + trainNumber + " прибыл на станцию " + stationName);
    }
    List<String> getEvents() { return events; }
}

class JournalListener implements ArrivalListener {
    private final List<String> entries = new ArrayList<>();
    public void onTrainArrived(String stationName, String trainNumber) {
        String entry = stationName + ":" + trainNumber;
        entries.add(entry);
        System.out.println("[журнал] " + stationName + ": зафиксировано прибытие поезда " + trainNumber);
    }
    List<String> getEntries() { return entries; }
}

class MailingListener implements ArrivalListener {
    private final List<String> events = new ArrayList<>();
    public void onTrainArrived(String stationName, String trainNumber) {
        String event = stationName + ":" + trainNumber;
        events.add(event);
        System.out.println("[рассылка] уведомление о поезде " + trainNumber + " на станции " + stationName);
    }
    List<String> getEvents() { return events; }
}

// Четвёртый наблюдатель — добавлен новым классом, Station не менялся
class AsuListener implements ArrivalListener {
    private final List<String> events = new ArrayList<>();
    public void onTrainArrived(String stationName, String trainNumber) {
        String event = stationName + ":" + trainNumber;
        events.add(event);
        System.out.println("[АСУ]     передано событие: " + event);
    }
    List<String> getEvents() { return events; }
}
