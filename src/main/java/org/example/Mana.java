package org.example;

import java.util.Random;

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

    public void regenerate() {
        Random random = new Random();
        int regenAmount = 10 + random.nextInt(11);
        currentMana = Math.min(currentMana + regenAmount, maxMana);
        System.out.println("Мана восстановлена на " + regenAmount + " единиц. Текущая мана: " + currentMana);
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public int getMaxMana() {
        return maxMana;
    }
}