package org.kt;

public class ATMService {
    private Cash cash;

    public ATMService() {
        cash = new Cash();
    }

    public void put(BanknoteType type, int count) {
        cash.put(type, count);
    }

    public String getBalance() {
        return cash.toString();
    }

    public Cash withdrawCash(int amount) {
        System.out.println("В банкомате находится:");
        System.out.println(cash.toString());
        System.out.println("---------");
        System.out.println("Запрос на снятие " + amount + " рублей");

        Cash result = new Cash();
        Cash after = cash.copy();

        var e = cash.getFirst();
        while (e != null) {
            BanknoteType banknote = e.getKey();
            int count = e.getValue();
            if (count > 0 && amount > banknote.getValue()) {
                int c = amount / banknote.getValue();
                if (c > count)
                    c = count;
                result.put(banknote, c);
                after.put(banknote, count - c);
                amount -= c * banknote.getValue();
            }
            e = cash.getNext(banknote);
        }
        System.out.println("---------");
        if (amount == 0) {
            cash = after;
            System.out.println("Было выдано: ");
            System.out.println(result);
            System.out.println("---------");
            System.out.println("В банкомате осталось: ");
            System.out.println(cash);
            return result;
        }
        System.out.println("Не хватает денег на счете");
        System.out.println("---------");
        System.out.println("В банкомате осталось: ");
        System.out.println(cash);
        return null;
    }
}
