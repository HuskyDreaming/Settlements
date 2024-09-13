package com.huskydreaming.settlements.database.persistence;

import org.bukkit.Color;
import org.bukkit.Location;

import java.util.Set;

public record BorderData(Color color, Set<Location> locations) {

}