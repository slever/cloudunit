package fr.treeptik.cloudunit.docker.core;

import fr.treeptik.cloudunit.docker.builders.ConfigBuilder;
import fr.treeptik.cloudunit.docker.builders.ContainerBuilder;
import fr.treeptik.cloudunit.docker.builders.HostConfigBuilder;
import fr.treeptik.cloudunit.docker.builders.ImageBuilder;
import fr.treeptik.cloudunit.docker.model.Config;
import fr.treeptik.cloudunit.docker.model.Container;
import fr.treeptik.cloudunit.docker.model.HostConfig;
import fr.treeptik.cloudunit.docker.model.Image;
import fr.treeptik.cloudunit.exception.DockerJSONException;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by guillaume on 22/10/15.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ImageCommandTests {

    static String DOCKER_HOST;
    static Boolean isTLS;

    private static DockerClient dockerClient;
    private static final String CONTAINER_NAME = "myContainer";
    private final int RUNNING_CONTAINERS = 7;
    private final String TAG = "mytag";
    private final String REPOSITORY = "localhost:5000/";
    private final String REGISTRY_HOST = "192.168.50.4:5000";

    @BeforeClass
    public static void setup() {

        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("mac") >= 0) {
            DOCKER_HOST = "cloudunit.dev:4243";
            isTLS = false;
        } else {
            DOCKER_HOST = "cloudunit.dev:2676";
            isTLS = false;
        }

        dockerClient = new DockerClient();
        HostConfig hostConfig = HostConfigBuilder.aHostConfig()
                .withVolumesFrom(new ArrayList<>())
                .build();
        Config config = ConfigBuilder.aConfig()
                .withAttachStdin(Boolean.FALSE)
                .withAttachStdout(Boolean.TRUE)
                .withAttachStderr(Boolean.TRUE)
                .withCmd(Arrays.asList("/bin/bash", "/cloudunit/scripts/start-service.sh", "johndoe", "abc2015",
                        "192.168.2.116", "172.17.0.221", "aaaa",
                        "AezohghooNgaegh8ei2jabib2nuj9yoe", "main"))
                .withImage("cloudunit/git:dev")
                .withHostConfig(hostConfig)
                .withExposedPorts(new HashMap<>())
                .withMemory(0L)
                .withMemorySwap(0L)
                .build();
        Container container = ContainerBuilder.aContainer().withName(CONTAINER_NAME).withConfig(config).build();
        try {
            dockerClient.setDriver(new SimpleDockerDriver("../../../cu-vagrant/certificats", isTLS));
            dockerClient.createContainer(container, DOCKER_HOST);
        } catch (DockerJSONException e) {
            Assert.fail();
        }
    }

    @AfterClass
    public static void tearDown() throws DockerJSONException {
        Container container = ContainerBuilder.aContainer()
                .withName(CONTAINER_NAME)
                .build();
        dockerClient.removeContainer(container, DOCKER_HOST);
    }

    @Test
    public void test00_commitAnImage() throws DockerJSONException {
        Container container = ContainerBuilder.aContainer()
                .withName("myContainer")
                .build();
        dockerClient.findContainer(container, DOCKER_HOST);
        dockerClient.commitImage(container, DOCKER_HOST, TAG, REPOSITORY);
    }

    @Test
    public void test00_findAnImage() throws DockerJSONException {
        Image image = ImageBuilder.anImage().withName(REPOSITORY + TAG + ":" + TAG).build();
        dockerClient.findAnImage(image, DOCKER_HOST);
    }

    @Test
    public void test02_pushAnImage() throws DockerJSONException {
        dockerClient.pushImage(DOCKER_HOST, TAG, REPOSITORY);
    }

    @Test
    public void test03_pullAnImage() throws DockerJSONException {
        dockerClient.pullImage(DOCKER_HOST, TAG, REPOSITORY);
    }

    @Test
    public void test04_removeImageIntoRegistry() throws DockerJSONException {
        Image image = ImageBuilder.anImage().withName(REPOSITORY + TAG + ":" + TAG).build();
        image = dockerClient.findAnImage(image, DOCKER_HOST);
        dockerClient.removeImageIntoTheRegistry(image, DOCKER_HOST, REGISTRY_HOST);
    }

    @Test
    public void test05_removeImage() throws DockerJSONException {
        Image image = ImageBuilder.anImage().withName(REPOSITORY + TAG + ":" + TAG).build();
        image = dockerClient.findAnImage(image, DOCKER_HOST);
        dockerClient.removeImage(image, DOCKER_HOST);
    }


}
