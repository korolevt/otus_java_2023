package org.kt;

public class Demo {
    public static void main(String[] args) {
        ATMService service = new ATMService();

        service.put(BanknoteType.R5000, 2);
        service.put(BanknoteType.R2000, 3);
        service.put(BanknoteType.R1000, 4);
        service.put(BanknoteType.R500, 7);
        service.put(BanknoteType.R100, 1);
        service.put(BanknoteType.R50, 3);
        service.put(BanknoteType.R10, 4);

        service.withdrawCash(7230);
    }
}