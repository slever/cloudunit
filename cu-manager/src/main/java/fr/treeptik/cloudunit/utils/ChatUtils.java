package fr.treeptik.cloudunit.utils;

import fr.treeptik.cloudunit.model.*;
import fr.treeptik.cloudunit.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by angular5 on 24/05/16.
 */
@Component
public class ChatUtils {

    private Logger logger = LoggerFactory.getLogger(ChatUtils.class);

    @Inject
    private ChatService chatService;

    public HttpStatus writeBeforeModuleMessage(User user,
                                                   String moduleName, String applicationName, String type) {
        String body = "";
        switch (type) {
            case "CREATE":
                body = user.getFirstName() + " " + user.getLastName()
                        + " attempts to add a new module  " + moduleName
                        + " to the application " + applicationName;
                break;

            case "REMOVE":
                body = user.getFirstName() + " " + user.getLastName()
                        + " attempts to remove the module " + moduleName
                        + " from the application " + applicationName;
                break;

        }

        System.out.println("applicationName = " + applicationName);
        System.out.println("chatService.toString() = " + chatService.toString());

        if(!chatService.getIdRoomByName(applicationName).equals(""))
            return chatService.sendMessage(applicationName, body);

        logger.error("ChatUtils writeBeforeModuleMessage : error");
        return HttpStatus.BAD_REQUEST;
    }

    public HttpStatus writeAfterReturningApplicationMessage(User user,
                                                                Application application, String type) {
        String body = "";
        switch (type) {
            case "CREATE":
                body = user.getFirstName() + " " + user.getLastName()
                        + " has created a new Application : "
                        + application.getDisplayName();
                break;
            case "UPDATE":
                body = user.getFirstName() + " " + user.getLastName()
                        + " has updated the Application : " + application.getDisplayName();
                break;

            case "REMOVE":
                body = user.getFirstName() + " " + user.getLastName()
                        + " has removed the Application : " + application.getDisplayName();
                break;
            case "START":
                body = "The application " + application.getDisplayName()
                        + " was correctly started by " + user.getFirstName() + " "
                        + user.getLastName();
                break;
            case "STOP":
                body = "The application " + application.getDisplayName()
                        + " was correctly stopped by " + user.getFirstName() + " "
                        + user.getLastName();

                break;
            case "RESTART":
                body = "The application " + application.getDisplayName()
                        + " was correctly restarted by " + user.getFirstName()
                        + " " + user.getLastName();
                break;
        }
        if(!chatService.getIdRoomByName(application.getDisplayName()).equals(""))
            return chatService.sendMessage(application.getDisplayName(), body);

        logger.error("ChatUtils writeBeforeModuleMessage : error");
        return HttpStatus.BAD_REQUEST;
    }

    public HttpStatus writeServerMessage(User user, Server server,
                                             String type) {
        String body = "";
        switch (type) {

            case "CREATE":
                body = user.getFirstName() + " " + user.getLastName()
                        + " has added a new Server : " + server.getName()
                        + " for the application "
                        + server.getApplication().getDisplayName();
                break;
            case "UPDATE":
                body = user.getFirstName() + " " + user.getLastName()
                        + " has updated the Server : " + server.getName()
                        + " for the application "
                        + server.getApplication().getDisplayName();
                break;

        }
        if(!chatService.getIdRoomByName(server.getApplication().getDisplayName()).equals(""))
            return chatService.sendMessage(server.getApplication().getDisplayName(), body);


        logger.error("ChatUtils writeBeforeModuleMessage : error");
        return HttpStatus.BAD_REQUEST;
    }

    public HttpStatus writeModuleMessage(User user, Module module,
                                             String type) {
        String body = "";
        switch (type) {
            case "CREATE":
                body = user.getFirstName() + " " + user.getLastName()
                        + " has added a new Module : " + module.getName()
                        + " for the application "
                        + module.getApplication().getName();
                break;
            case "REMOVE":
                body = user.getFirstName() + " " + user.getLastName()
                        + " has remove the Module : " + module.getName()
                        + " from the application "
                        + module.getApplication().getName();
                break;
        }
        if(!chatService.getIdRoomByName(module.getApplication().getDisplayName()).equals(""))
            return chatService.sendMessage(module.getApplication().getDisplayName(), body);

        logger.error("ChatUtils writeBeforeModuleMessage : error");
        return HttpStatus.BAD_REQUEST;
    }

    public HttpStatus writeDeploymentMessage(User user,
                                                 Deployment deployment, String type) {
        String body = "";
        switch (type) {
            case "CREATE":
                body = user.getFirstName() + " " + user.getLastName()
                        + " has deployed a new Application : "
                        + deployment.getApplication().getDisplayName() + " from "
                        + deployment.getType().toString();
                break;
        }

        logger.error("ChatUtils writeBeforeModuleMessage : error");
        return chatService.sendMessage(deployment.getApplication().getDisplayName(), body);
    }

    public HttpStatus writeSnapshotMessage(User user, Snapshot snapshot,
                                               String type) {
        String body = "";
        switch (type) {
            case "CREATE":
                body = user.getFirstName() + " " + user.getLastName()
                        + " has created a new snapshot " + snapshot.getDisplayTag()
                        + " from : " + snapshot.getApplicationDisplayName();
                break;
            case "REMOVE":
                body = user.getFirstName() + " " + user.getLastName()
                        + " has removed the snapshot " + snapshot.getDisplayTag();
                break;
            case "CLONEFROMASNAPSHOT":
                body = user.getFirstName() + " " + user.getLastName()
                        + " has created a new application from : "
                        + snapshot.getDisplayTag();
                break;
        }

        if(!chatService.getIdRoomByName(snapshot.getApplicationName()).equals(""))
            return chatService.sendMessage(snapshot.getApplicationDisplayName(), body);

        logger.error("ChatUtils writeBeforeModuleMessage : error");
        return HttpStatus.BAD_REQUEST;
    }

    public HttpStatus writeBeforeDeploymentMessage(User user,
                                                       Application application, String type) {
        String body = "";
        switch (type) {
            case "CREATE":
                body = user.getFirstName() + " " + user.getLastName()
                        + " attempts to deploy a new Application : "
                        + application.getDisplayName();
                break;
        }
        if(!chatService.getIdRoomByName(application.getName()).equals(""))
            return chatService.sendMessage(application.getDisplayName(), body);

        logger.error("ChatUtils writeBeforeModuleMessage : error");
        return HttpStatus.BAD_REQUEST;
    }

}
