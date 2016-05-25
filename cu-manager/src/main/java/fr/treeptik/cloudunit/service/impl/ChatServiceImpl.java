package fr.treeptik.cloudunit.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.treeptik.cloudunit.service.ChatService;
import io.fabric8.letschat.LetsChatClient;
import io.fabric8.letschat.RoomDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

/**
 * Created by angular5 on 24/05/16.
 */
@Service
public class ChatServiceImpl implements ChatService {

    private Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);

    private LetsChatClient client;

    @Value("${chat.api}")
    private String chatAPI;

    @Value("${chat.token}")
    private String chatToken;

    @Value("${chat.username}")
    private String chatUsername;

    @Value("${chat.password}")
    private String chatPassword;

    public HttpStatus createRoom(String applicationName) {
        client = new LetsChatClient(chatAPI, chatUsername, chatUsername, chatToken);
        logger.info("Creation of new room : " + applicationName);

        RoomDTO dto = new RoomDTO();

        dto.setName(applicationName);
        dto.setSlug(applicationName + Calendar.getInstance().getTime().getTime());
        dto.setCreated(Calendar.getInstance().getTime());
        dto.setOwner(chatUsername);
        dto.setLastActive(Calendar.getInstance().getTime());
        dto.setDescription("");

        client.createRoom(dto);

        return HttpStatus.OK;
    }

    public HttpStatus deleteRoom(String applicationName) {
        client = new LetsChatClient(chatAPI, chatUsername, chatUsername, chatToken);
        logger.info("Deletion of room : " + applicationName);

        List<RoomDTO> roomList = getRooms();

        for(RoomDTO dto : roomList)
        {
            if(dto.getName().equals(applicationName)) {
                client.deleteRoom(dto.getId());
                return HttpStatus.OK;
            }
        }

        return HttpStatus.NOT_FOUND;
    }

    public HttpStatus sendMessage(String applicationName, String message) {
        logger.info("Sending message to the room : " + applicationName);

        String idRoom = getIdRoomByName(applicationName);

        DataOutputStream wr = null;
        HttpURLConnection connPost = null;
        HttpStatus code = HttpStatus.EXPECTATION_FAILED;

        if (chatToken == null) {
            logger.error("Cannot use this feature because no token for the chat");
            return HttpStatus.NOT_IMPLEMENTED;
        }

        if (chatAPI == null) {
            logger.error("Cannot use this feature because no URL given for the chat API");
            return HttpStatus.NOT_IMPLEMENTED;
        }

        try {
            String chatURL = chatAPI + "/messages";
            URL urlPost = new URL(chatURL);
            connPost = (HttpURLConnection) urlPost.openConnection();

            connPost.setDoOutput(true);
            connPost.setDoInput(true);
            connPost.setRequestProperty("Authorization", "bearer " + chatToken);
            connPost.setRequestProperty("Content-Type", "application/json");
            connPost.setRequestMethod("POST");
            connPost.connect();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.createObjectNode();
            ((ObjectNode) rootNode).put("text", message);
            ((ObjectNode) rootNode).put("room", idRoom);
            String jsonString = mapper.writeValueAsString(rootNode);

            wr = new DataOutputStream(connPost.getOutputStream());
            wr.writeBytes(jsonString);
            code = HttpStatus.valueOf(connPost.getResponseCode());

        } catch (IOException e) {
            logger.debug("IOException sendMessage chat : " + applicationName);
        } finally {
            try {
                if (wr != null) wr.flush();
                if (wr != null) wr.close();
            } catch (Exception ignore) {
            }
        }

        return code;
    }

    public String getIdRoomByName(String applicationName) {
        List<RoomDTO> roomList = getRooms();

        for(RoomDTO dto : roomList)
        {
            if(dto.getName().equals(applicationName)) {
                return dto.getId();
            }
        }

        return "";
    }

    public List<RoomDTO> getRooms() {
        client = new LetsChatClient(chatAPI, chatUsername, chatUsername, chatToken);
        return client.getRooms();
    }
}
