package ru.atom.gameserver.gsession;

import com.fasterxml.jackson.databind.node.NumericNode;
import ru.atom.gameserver.component.ConnectionHandler;
import ru.atom.gameserver.message.Message;
import ru.atom.gameserver.message.Topic;
import ru.atom.gameserver.tick.Ticker;
import ru.atom.gameserver.util.JsonHelper;

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

    public void stop() {
        ticker.stopGameLoop();
    }

    public Message addPlayer(String login) {
        int possess = gameMechanics.addPlayer();
        loginOnIdMap.put(login, possess);
        return new Message(Topic.POSSESS, JsonHelper.nodeFactory.numberNode(possess));
    }

    /**
     * @param login
     * @return true if the last player was been removed from loginOnIdMap
     */
    public boolean removePlayer(String login) {
        Integer id = loginOnIdMap.remove(login);
        //set pawn with id dead
        return loginOnIdMap.isEmpty();
    }

}
