package org.example;
import java.util.Random;

public class Archer extends Hero {
    private Pet pet;

    public Archer() {
        super(50, 90, 10, 30);
        this.pet = generateRandomPet();
    }

    private Pet generateRandomPet() {
        Random random = new Random();
        int choice = random.nextInt(3);
        switch (choice) {
            case 0:
                return new Pet("Волк", 15);
            case 1:
                return new Pet("Единорог", 10);
            case 2:
                return new Pet("Ястреб", 20);
            default:
                return null;
        }
    }

    @Override
    public void attack(Character target) {
        int totalDamage = getDamage();
        if (pet != null) {
            totalDamage += pet.getDamage();
            System.out.println("Лучник и его " + pet.getName() + " атакуют врага, нанося " + totalDamage + " урона.");
        } else {
            System.out.println("Лучник атакует врага, нанося " + totalDamage + " урона.");
        }

        if (target instanceof Enemy) {
            target.takeDamage(totalDamage);
        }
    }

    public void printPetInfo() {
        if (pet != null) {
            System.out.println("У Лучника есть питомец: " + pet.getName() + ", Урон: " + pet.getDamage());
        } else {
            System.out.println("У лучника нет питомца.");
        }
    }

    public Pet getPet(){
        return pet;
    }

    public class Pet {
        private String name;
        private int damage;

        public Pet(String name, int damage) {
            this.name = name;
            this.damage = damage;
        }

        public String getName() {
            return name;
        }

        public int getDamage() {
            return damage;
        }
    }
}