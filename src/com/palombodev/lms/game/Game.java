package com.palombodev.lms.game;

import com.palombodev.lms.kits.KitType;
import de.robingrether.idisguise.disguise.PlayerDisguise;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Game {

    private int autostart;
    private int minplayers;
    private int maxplayers;
    private List<String> allowedCommands;
    private Location lobbyLocation;
    private Location spawnLocation;
    private List<String> rewardCommands;
    private GameState gameState;
    private Set<UUID> players;
    PlayerDisguise playerDisguise;
    private Location knockbackLocation;

    private KitType kitType;

    private boolean deathmatch;

    public Game(int autostart, int minplayers, int maxplayers, List<String> allowedCommands, Location lobbyLocation, Location spawnLocation, Location knockbackLocation, List<String> rewardCommands) {
        this.autostart = autostart;
        this.minplayers = minplayers;
        this.maxplayers = maxplayers;
        this.allowedCommands = allowedCommands;
        this.rewardCommands = rewardCommands;
        this.gameState = GameState.WAITING;
        this.lobbyLocation = lobbyLocation;
        this.spawnLocation = spawnLocation;
        this.knockbackLocation = knockbackLocation;
        this.players = new HashSet<>();
        this.playerDisguise = new PlayerDisguise("Database", false);
    }

    public int getAutostart() {
        return autostart;
    }

    public int getMinplayers() {
        return minplayers;
    }

    public int getMaxplayers() {
        return maxplayers;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Set<UUID> getPlayers() {
        return players;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public List<String> getAllowedCommands() {
        return allowedCommands;
    }

    public List<String> getRewardCommands() {
        return rewardCommands;
    }

    public void setLobbyLocation(Location lobbyLocation) {
        this.lobbyLocation = lobbyLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public KitType getKitType() {
        return kitType;
    }

    public void setKitType(KitType kitType) {
        this.kitType = kitType;
    }

    public PlayerDisguise getPlayerDisguise() {
        return playerDisguise;
    }

    public void setPlayerDisguise(PlayerDisguise playerDisguise) {
        this.playerDisguise = playerDisguise;
    }

    public Location getKnockbackLocation() {
        return knockbackLocation;
    }

    public void setKnockbackLocation(Location knockbackLocation) {
        this.knockbackLocation = knockbackLocation;
    }

    public boolean isDeathmatch() {
        return deathmatch;
    }

    public void setDeathmatch(boolean deathmatch) {
        this.deathmatch = deathmatch;
    }
}
