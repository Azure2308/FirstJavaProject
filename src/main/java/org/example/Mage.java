package org.example;
import java.util.Random;

public class Mage extends Hero{
    private Mana mana;
    private Spell[] spells;

    public Mage() {
        super(30, 70, 10, 20);
        this.mana = new Mana(60);
        this.spells = new Spell[] {
                new Spell("Огненный шар", 30, 20),
                new Spell("Ледяной шип", 20, 15),
                new Spell("Печать Молнии", 40, 25)
        };
    }

    public void castSpell(Enemy target, int spellIndex) {
        Spell spell = spells[spellIndex];
        if (mana.useMana(spell.getManaCost())) {
            System.out.println("Маг использует заклинание " + spell.getName() + " и наносит " + spell.getDamage() + " урона.");
            target.takeDamage(spell.getDamage());
        }
    }

    @Override
    public void attack(Character target) {
        if (target instanceof Enemy enemyTarget) {
            castSpell(enemyTarget, new Random().nextInt(spells.length));
            mana.regenerate();
        } else {
            System.out.println("Маг не может атаковать этого персонажа.");
        }
    }

    public Spell[] getSpells(){
        return spells;
    }
}