package fr.treeptik.cloudunit.service;

import org.springframework.http.HttpStatus;

/**
 * Created by angular5 on 24/05/16.
 */
public interface ChatService {
    HttpStatus createRoom(String name);

    HttpStatus deleteRoom(String applicationName);

    String getIdRoomByName(String applicationName);

    HttpStatus sendMessage(String applicationName, String message);

}
