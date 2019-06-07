package com.palombodev.lms.game.customevents;

import com.palombodev.lms.LMS;
import com.palombodev.lms.game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    Game game;

    public GameStartEvent() {
        this.game = LMS.getGameManager().getGame();
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