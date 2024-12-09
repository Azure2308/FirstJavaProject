package org.example;
import java.util.Random;

public abstract class Character {
    protected int health;
    protected int damage;

    public Character(int minHealth, int maxHealth, int minDamage, int maxDamage) {
        Random random = new Random();
        this.health = random.nextInt(maxHealth - minHealth + 1) + minHealth;
        this.damage = random.nextInt(maxDamage - minDamage + 1) + minDamage;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    public void attack(Character target) {
        System.out.println(this.getClass().getSimpleName() + " наносит " + getDamage() + " урона.");
        target.takeDamage(getDamage());
    }

    public void printInfo() {
        System.out.println(this.getClass().getSimpleName() + ": Здоровье = " + health + ", Урон = " + damage);
    }
}