package org.example;

public class Mana {
    private int currentMana;
    private int maxMana;

    public Mana(int maxMana) {
        this.maxMana = maxMana;
        this.currentMana = maxMana;
    }

    public boolean useMana(int amount) {
        if (currentMana >= amount) {
            currentMana -= amount;
            return true;
        } else {
            System.out.println("Недостаточно маны!");
            return false;
        }
    }

    public int getCurrentMana() {
        return currentMana;
    }
}
