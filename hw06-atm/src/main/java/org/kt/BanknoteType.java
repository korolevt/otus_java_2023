package org.kt;

public enum BanknoteType {
    R10(10),
    R50(50),
    R100(100),
    R500(500),
    R1000(1000),
    R2000(2000),
    R5000(5000);

    private final int value;

    BanknoteType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue() + " рублей";
    }
}
