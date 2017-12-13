package ru.atom.gameserver.component;

import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.atom.gameserver.gsession.GameSession;
import ru.atom.gameserver.message.Message;
import ru.atom.gameserver.service.GameRepository;
import ru.atom.gameserver.util.JsonHelper;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@CrossOrigin
@Component
public class ConnectionHandler extends TextWebSocketHandler implements WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionHandler.class);
    private final Map<WebSocketSession, Long> sessionMap = new ConcurrentHashMap<>();

    @Autowired
    private GameRepository gameRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Pair<Long, String> idLoginPair = getParameters(session.getUri().toString());
        sessionMap.put(session, idLoginPair.getKey());
        GameSession gameSession = gameRepository.getGameById(idLoginPair.getKey());
        gameSession.addPlayer(idLoginPair.getValue());
        logger.info("new ws connection gameid=" + idLoginPair.getKey() + " login=" + idLoginPair.getValue());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionMap.remove(session);
        logger.info("ws connection has been closed with status code " + status.getCode());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        Message message = JsonHelper.fromJson(textMessage.getPayload(), Message.class);
        gameRepository.getGameById(sessionMap.get(session)).messagesOffering().offerMessage(message);

        logger.info("text message has been received");
    }

    public void sendMessage(long gameId, Message message) {
        for (Map.Entry<WebSocketSession, Long> entry : sessionMap.entrySet()) {
            if (!Long.valueOf(gameId).equals(entry.getValue())) {
                continue;
            }
            try {
                entry.getKey().sendMessage(new TextMessage(JsonHelper.toJson(message)));
            } catch (IOException exception) {
                logger.warn(exception.getMessage());
            }
        }

        logger.info("text message has been sent");
    }

    private Pair<Long, String> getParameters(String uri) {
        String uriParameters = uri.split("[?]")[1];
        String[] parameters = uriParameters.split("[&]");
        Long gameId = Long.valueOf(parameters[0].split("[=]")[1]);
        String login = parameters[1].split("[=]")[1];
        return new Pair<>(gameId, login);
    }

}
