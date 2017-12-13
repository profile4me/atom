package ru.atom.gameserver.gsession;

import ru.atom.gameserver.component.ConnectionHandler;
import ru.atom.gameserver.tick.Ticker;

import java.util.HashMap;
import java.util.Map;

public class GameSession {

    private final Ticker ticker;
    private final GameMechanics gameMechanics;
    private final Replicator replicator;
    private final InputQueue inputQueue;

    private final Map<String, Integer> loginOnIdMap;

    public GameSession(Long gameId, ConnectionHandler connectionHandler) {
        this.ticker = new Ticker();
        this.replicator = new Replicator(gameId, connectionHandler);
        this.gameMechanics = new GameMechanics(ticker, replicator);
        this.inputQueue = new InputQueue();
        this.loginOnIdMap = new HashMap<>();

        //gameMechanics must be last tickable in the list of tickable
        ticker.registerTickable(gameMechanics);
    }

    public MessagesOffering messagesOffering() {
        return inputQueue;
    }

    public void start() {
        new Thread(ticker::gameLoop).start();
    }

    public void addPlayer(String login) {
        loginOnIdMap.put(login, new Integer(gameMechanics.addPlayer()));
    }

}
