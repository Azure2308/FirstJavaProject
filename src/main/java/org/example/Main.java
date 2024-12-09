package org.example;

public class Main{
    public static void main(String[] args) {
        BattleHandler battleHandler1 = new BattleHandler();
        battleHandler1.generateHeroesAndEnemies(11,3);
        System.out.println("\n=======Файл======");
        battleHandler1.readFromFile();
    }
}