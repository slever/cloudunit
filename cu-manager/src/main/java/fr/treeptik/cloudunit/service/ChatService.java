package fr.treeptik.cloudunit.service;

import fr.treeptik.cloudunit.model.User;
import org.springframework.http.HttpStatus;

/**
 * Created by angular5 on 24/05/16.
 */
public interface ChatService {
    void createUser(User user);

    void deleteUser(String username);

    HttpStatus createRoom(String name);

    HttpStatus deleteRoom(String applicationName);

    String getIdRoomByName(String applicationName);

    HttpStatus sendMessage(String applicationName, String message);

}
