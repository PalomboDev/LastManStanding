package com.palombodev.lms.game.customevents;

import com.palombodev.lms.LMS;
import com.palombodev.lms.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStopEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    Game game;
    Player winner;

    public GameStopEvent(Player winner) {
        this.game = LMS.getGameManager().getGame();
        this.winner = winner;
    }

    public Game getGame() {
        return game;
    }

    public Player getWinner() {
        return winner;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}