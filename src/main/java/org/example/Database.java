package org.example;

import java.sql.*;

public class Database {
    private static final String URL = "jdbc:sqlite:game.db";
    private Connection connection;

    public Database() {
        try {
            connection = DriverManager.getConnection(URL);
            System.out.println("Соединение с базой данных установлено.");
        } catch (SQLException e) {
            System.err.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
    }

    public void createTables() {
        try (Statement stmt = connection.createStatement()) {
            String createHeroesTable = "CREATE TABLE IF NOT EXISTS heroes ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "type TEXT, "
                    + "health INTEGER, "
                    + "damage INTEGER)";
            String createEnemiesTable = "CREATE TABLE IF NOT EXISTS enemies ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "health INTEGER, "
                    + "damage INTEGER)";
            stmt.execute(createHeroesTable);
            stmt.execute(createEnemiesTable);
        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблиц: " + e.getMessage());
        }
    }

    public void clearTables() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM heroes");
            stmt.executeUpdate("DELETE FROM enemies");

            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='heroes'");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='enemies'");

            System.out.println("Таблицы очищены и счетчики ID сброшены.");
        } catch (SQLException e) {
            System.err.println("Ошибка при очистке таблиц: " + e.getMessage());
        }
    }

    public void printHeroesFromDatabase() {
        String query = "SELECT * FROM heroes";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String type = rs.getString("type");
                int health = rs.getInt("health");
                int damage = rs.getInt("damage");
                System.out.println(type + " - Здоровье: " + health + ", Урон: " + damage);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при чтении данных из таблицы heroes: " + e.getMessage());
        }
    }

    public void printEnemiesFromDatabase() {
        String query = "SELECT * FROM enemies";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int health = rs.getInt("health");
                int damage = rs.getInt("damage");
                System.out.println("Enemy - Здоровье: " + health + ", Урон: " + damage);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при чтении данных из таблицы enemies: " + e.getMessage());
        }
    }

    public void insertCharacter(Character character) {
        String query;

        if (character instanceof Hero) {
            query = "INSERT INTO heroes (type, health, damage) VALUES (?, ?, ?)";
        } else if (character instanceof Enemy) {
            query = "INSERT INTO enemies (health, damage) VALUES (?, ?)";
        } else {
            throw new IllegalArgumentException("Неподдерживаемый тип персонажа");
        }

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            if (character instanceof Hero) {
                pstmt.setString(1, character.getClass().getSimpleName());
                pstmt.setInt(2, character.getHealth());
                pstmt.setInt(3, character.getDamage());
            } else {
                pstmt.setInt(1, character.getHealth());
                pstmt.setInt(2, character.getDamage());
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при вставке персонажа: " + e.getMessage());
        }
    }

    public void printCharactersWithHigherHealthAndDamage(int minHealth, int minDamage) {
        String queryHeroes = "SELECT * FROM heroes WHERE health > ? AND damage > ?";
        String queryEnemies = "SELECT * FROM enemies WHERE health > ? AND damage > ?";

        try (PreparedStatement stmtHeroes = connection.prepareStatement(queryHeroes);
             PreparedStatement stmtEnemies = connection.prepareStatement(queryEnemies)) {

            stmtHeroes.setInt(1, minHealth);
            stmtHeroes.setInt(2, minDamage);
            stmtEnemies.setInt(1, minHealth);
            stmtEnemies.setInt(2, minDamage);

            try (ResultSet rsHeroes = stmtHeroes.executeQuery();
                 ResultSet rsEnemies = stmtEnemies.executeQuery()) {
                while (rsHeroes.next()) {
                    String type = rsHeroes.getString("type");
                    int health = rsHeroes.getInt("health");
                    int damage = rsHeroes.getInt("damage");
                    System.out.println(type + " - Здоровье: " + health + ", Урон: " + damage);
                }

                while (rsEnemies.next()) {
                    int health = rsEnemies.getInt("health");
                    int damage = rsEnemies.getInt("damage");
                    System.out.println("Enemy - Здоровье: " + health + ", Урон: " + damage);
                }
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при чтении данных: " + e.getMessage());
        }
    }

    public void printArchers() {
        String query = "SELECT * FROM heroes WHERE type = 'Archer'";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String type = rs.getString("type");
                int health = rs.getInt("health");
                int damage = rs.getInt("damage");

                System.out.println(type + " - Здоровье: " + health + ", Урон: " + damage);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при чтении данных: " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
        }
    }
}