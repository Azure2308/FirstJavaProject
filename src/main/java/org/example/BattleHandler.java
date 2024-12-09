package org.example;
import javax.swing.*;
import java.io.*;
import java.util.*;

class BattleHandler {

    private Random random = new Random();
    private List<Hero> heroes = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private Database db = new Database();

    public void generateHeroesAndEnemies(int numHeroes, int numEnemies) {
        db.clearTables();
        db.createTables();

        Map<String, Integer> heroCounts = new HashMap<>();

        for (int i = 0; i < numHeroes; i++) {
            Hero hero = random.nextBoolean() ? new Mage() : new Archer();
            heroes.add(hero);
            db.insertCharacter(hero);

            heroCounts.put(
                    hero.getClass().getSimpleName(),
                    heroCounts.getOrDefault(hero.getClass().getSimpleName(), 0) + 1
            );
        }

        for (int i = 0; i < numEnemies; i++) {
            Enemy enemy = new Enemy(50, 100, 10, 20);
            enemies.add(enemy);
            db.insertCharacter(enemy);
        }

        writeToFile();

        Character strongestHero = findStrongestCharacter(heroes);
        Character strongestEnemy = findStrongestCharacter(enemies);

        System.out.println("Сильнейший герой: " + strongestHero.getClass().getSimpleName());
        System.out.println("Здоровье: " + strongestHero.getHealth() + ", Урон: " + strongestHero.getDamage());
        if (strongestHero instanceof Archer archer) {
            archer.printPetInfo();
        }

        System.out.println("\nСильнейший враг: " + strongestEnemy.getClass().getSimpleName());
        System.out.println("Здоровье: " + strongestEnemy.getHealth() + ", Урон: " + strongestEnemy.getDamage());

        fight(strongestHero, strongestEnemy);

        System.out.println("\n=======Данные из БД========");
        db.printHeroesFromDatabase();
        db.printEnemiesFromDatabase();
        System.out.println("\n=======1 SQL Запрос======");
        db.printCharactersWithHigherHealthAndDamage(70,25);
        System.out.println("\n=======2 SQL Запрос======");
        db.printArchers();

        db.closeConnection();

        Graph graph = new Graph(heroCounts);
        graph.setVisible(true);
    }

    private Character findStrongestCharacter(List<? extends Character> characters) {
        Character strongest = characters.getFirst();

        for (Character character : characters) {
            int characterTotalPower = character.getHealth() + character.getDamage();

            if (character instanceof Archer archer) {
                if (archer.getPet() != null) {
                    characterTotalPower += archer.getPet().getDamage();
                }
            }

            int strongestTotalPower = strongest.getHealth() + strongest.getDamage();
            if (characterTotalPower > strongestTotalPower) {
                strongest = character;
            }
        }

        return strongest;
    }

    private void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("battle_info.txt"))) {
            writer.write("Герои:\n");
            for (Hero hero : heroes) {
                int totalDamage = hero.getDamage();
                if (hero instanceof Archer) {
                    Archer archer = (Archer) hero;
                    if (archer.getPet() != null) {
                        totalDamage += archer.getPet().getDamage();
                    }
                }
                writer.write(hero.getClass().getSimpleName() +
                        " - Здоровье: " + hero.getHealth() +
                        ", Урон: " + totalDamage +
                        (hero instanceof Archer && ((Archer) hero).getPet() != null
                                ? ", Питомец: " + ((Archer) hero).getPet().getName()
                                : "") +
                        "\n");
            }

            writer.write("\nВраги:\n");
            for (Enemy enemy : enemies) {
                writer.write(enemy.getClass().getSimpleName() +
                        " - Здоровье: " + enemy.getHealth() +
                        ", Урон: " + enemy.getDamage() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("battle_info.txt"))) {
            String row;
            while ((row = reader.readLine()) != null) {
                System.out.println(row);
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения из файла: " + e.getMessage());
        }
    }

    public void fight(Character hero, Character enemy) {
        System.out.println("\nБитва началась!");

        hero.printInfo();
        enemy.printInfo();

        while (hero.isAlive() && enemy.isAlive()) {
            System.out.println("Герой атакует врага.");
            hero.attack(enemy);

            if (!enemy.isAlive()) {
                System.out.println("Враг побежден! Герой победил.");
                break;
            }

            System.out.println("Враг атакует героя.");
            enemy.attack(hero);

            if (!hero.isAlive()) {
                System.out.println("Герой побежден! Враг победил.");
                break;
            }

            System.out.println("Здоровье героя: " + hero.getHealth());
            System.out.println("Здоровье врага: " + enemy.getHealth());
            System.out.println("---------------------------------");
        }
    }
}