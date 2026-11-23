// 3.3 Ваша реализация Factory Method
// Factory Method: создание вагона по типу через отдельных создателей.
import java.util.List;

public class FactoryAfter {

    private static int failedChecks = 0;

    public static void main(String[] args) {

        List<WagonCreator> creators = List.of(
            new CoveredWagonCreator(),
            new PlatformCreator(),
            new TankWagonCreator(),
            new HopperWagonCreator()
        );

        System.out.println("--- Все типы вагонов ---");
        for (WagonCreator creator : creators) {
            Wagon wagon = creator.createWagon(70);
            System.out.println(wagon.description());
        }

        System.out.println();
        System.out.println("--- Проверки ---");

        for (WagonCreator creator : creators) {
            Wagon wagon = creator.createWagon(70);
            check("описание содержит грузоподъёмность: " + wagon.description(),
                  wagon.description().contains("70"));
        }

        check("CoveredWagonCreator -> CoveredWagon",
              new CoveredWagonCreator().createWagon(68) instanceof CoveredWagon);
        check("PlatformCreator -> Platform",
              new PlatformCreator().createWagon(71) instanceof Platform);
        check("TankWagonCreator -> TankWagon",
              new TankWagonCreator().createWagon(66) instanceof TankWagon);
        check("HopperWagonCreator -> HopperWagon (четвёртый тип)",
              new HopperWagonCreator().createWagon(70) instanceof HopperWagon);

                failIfNeeded();
        System.out.println("Все проверки пройдены.");
    }

    static void check(String checkName, boolean passed) {
        System.out.println((passed ? "[OK]   " : "[FAIL] ") + checkName);
        if (!passed) failedChecks++;
    }
        static void failIfNeeded() {
        if (failedChecks > 0) {
            throw new AssertionError("не пройдено проверок: " + failedChecks);
        }
    }
}

// ---------- продукты ----------
interface Wagon {
    String description();
    double capacityTons();
}

class CoveredWagon implements Wagon {
    private final double capacityTons;
    CoveredWagon(double capacityTons) { this.capacityTons = capacityTons; }
    public String description() {
        return "Крытый вагон: грузоподъёмность " + capacityTons + " т, тарные и штучные грузы";
    }
    public double capacityTons() { return capacityTons; }
}

class Platform implements Wagon {
    private final double capacityTons;
    Platform(double capacityTons) { this.capacityTons = capacityTons; }
    public String description() {
        return "Платформа: грузоподъёмность " + capacityTons + " т, длинномерные и тяжеловесные грузы";
    }
    public double capacityTons() { return capacityTons; }
}

class TankWagon implements Wagon {
    private final double capacityTons;
    TankWagon(double capacityTons) { this.capacityTons = capacityTons; }
    public String description() {
        return "Цистерна: грузоподъёмность " + capacityTons + " т, наливные грузы";
    }
    public double capacityTons() { return capacityTons; }
}

class HopperWagon implements Wagon {
    private final double capacityTons;
    HopperWagon(double capacityTons) { this.capacityTons = capacityTons; }
    public String description() {
        return "Зерновоз (хоппер): грузоподъёмность " + capacityTons + " т, сыпучие грузы";
    }
    public double capacityTons() { return capacityTons; }
}

// ---------- создатели ----------
abstract class WagonCreator {
    abstract Wagon createWagon(double capacityTons);
}

class CoveredWagonCreator extends WagonCreator {
    Wagon createWagon(double capacityTons) { return new CoveredWagon(capacityTons); }
}
class PlatformCreator extends WagonCreator {
    Wagon createWagon(double capacityTons) { return new Platform(capacityTons); }
}
class TankWagonCreator extends WagonCreator {
    Wagon createWagon(double capacityTons) { return new TankWagon(capacityTons); }
}
class HopperWagonCreator extends WagonCreator {
    Wagon createWagon(double capacityTons) { return new HopperWagon(capacityTons); }
}
