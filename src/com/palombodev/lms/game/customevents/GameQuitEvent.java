package com.palombodev.lms.game.customevents;

import com.palombodev.lms.LMS;
import com.palombodev.lms.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameQuitEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    Player player;
    Game game;

    public GameQuitEvent(Player player) {
        this.player = player;
        this.game = LMS.getGameManager().getGame();
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}