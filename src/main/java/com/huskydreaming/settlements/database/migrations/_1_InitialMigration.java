package com.huskydreaming.settlements.database.migrations;

import com.huskydreaming.huskycore.interfaces.database.base.DatabaseMigration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class _1_InitialMigration implements DatabaseMigration {

    @Override
    public void migrate(Connection connection, String table) {
        // CREATE TABLE SETTLEMENT
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS " + table + "_settlement (" +
                    "id INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */," +
                    "name TEXT NOT NULL," +
                    "description TEXT NULL DEFAULT NULL," +
                    "tag TEXT NULL DEFAULT NULL," +
                    "role_id INTEGER NULL," +
                    "owner_uuid TEXT NOT NULL," +
                    "world_uid TEXT NOT NULL," +
                    "x DOUBLE NOT NULL," +
                    "y DOUBLE NOT NULL," +
                    "z DOUBLE NOT NULL," +
                    "yaw FLOAT NOT NULL," +
                    "pitch FLOAT NOT NULL)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // CREATE TABLE CLAIM
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE " + table + "_claim (" +
                    "id INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */," +
                    "settlement_id INTEGER NOT NULL," +
                    "world_uid TEXT NOT NULL," +
                    "x INTEGER NOT NULL," +
                    "z INTEGER NOT NULL)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // CREATE TABLE CONTAINER
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE " + table + "_container (" +
                    "id INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */," +
                    "settlement_id INTEGER NOT NULL," +
                    "max_claims INTEGER NOT NULL," +
                    "max_homes INTEGER NOT NULL," +
                    "max_members INTEGER NOT NULL," +
                    "max_roles INTEGER NOT NULL)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // CREATE TABLE FLAG
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE " + table + "_flag (" +
                    "id INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */," +
                    "type TEXT NOT NULL," +
                    "settlement_id INTEGER NOT NULL)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // CREATE TABLE HOME
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE " + table + "_home (" +
                    "id INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */," +
                    "name TEXT NOT NULL," +
                    "settlement_id INTEGER NOT NULL," +
                    "world_uid TEXT NOT NULL," +
                    "x DOUBLE NOT NULL," +
                    "y DOUBLE NOT NULL," +
                    "z DOUBLE NOT NULL," +
                    "yaw FLOAT NOT NULL," +
                    "pitch FLOAT NOT NULL)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // CREATE TABLE ROLE
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE " + table + "_role (" +
                    "id INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */," +
                    "name TEXT NOT NULL," +
                    "weight INTEGER NOT NULL," +
                    "settlement_id INT NOT NULL)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // CREATE TABLE MEMBER
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE " + table + "_member (" +
                    "id INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */," +
                    "player_uuid TEXT NOT NULL," +
                    "settlement_id INTEGER NOT NULL," +
                    "role_id INTEGER NOT NULL," +
                    "last_online TEXT NOT NULL," +
                    "auto_claim TINYINT NOT NULL)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // CREATE TABLE PERMISSION
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE " + table + "_permission (" +
                    "id INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */," +
                    "type TEXT NOT NULL," +
                    "role_id INTEGER NOT NULL)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // CREATE TABLE TRUST
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE " + table + "_trust (" +
                    "id INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */," +
                    "player_uuid TEXT NOT NULL," +
                    "settlement_id INTEGER NOT NULL)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getVersion() {
        return 1;
    }
}