package fr.treeptik.cloudunit.docker.core;

import fr.treeptik.cloudunit.docker.builders.ConfigBuilder;
import fr.treeptik.cloudunit.docker.builders.ContainerBuilder;
import fr.treeptik.cloudunit.docker.builders.HostConfigBuilder;
import fr.treeptik.cloudunit.docker.model.Config;
import fr.treeptik.cloudunit.docker.model.DockerContainer;
import fr.treeptik.cloudunit.docker.model.HostConfig;
import fr.treeptik.cloudunit.exception.DockerJSONException;
import fr.treeptik.cloudunit.utils.ContainerUtils;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by guillaume on 21/10/15.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContainerCommandTests {

    public static String DOCKER_HOST;
    public static Boolean isTLS;

    private static DockerClient dockerClient;
    private static final String CONTAINER_NAME = "myContainer";
    private final int RUNNING_CONTAINERS = 7;
    private static DockerContainer container;

    @BeforeClass
    public static void setupClass() {

        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("mac") >= 0) {
            DOCKER_HOST = "cloudunit.dev:4243";
            isTLS = false;
        } else {
            DOCKER_HOST = "cloudunit.dev:2376";
            isTLS = true;
        }

        dockerClient = new DockerClient();
        dockerClient.setDriver(new SimpleDockerDriver("../cu-vagrant/certificats", isTLS));
    }

    @Before
    public void setup() throws Exception {
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
                .withImage("cloudunit/git:latest")
                .withHostConfig(hostConfig)
                .withExposedPorts(new HashMap<>())
                .withMemory(0L)
                .withMemorySwap(0L)
                .build();
        container = ContainerBuilder.aContainer()
                .withName(CONTAINER_NAME)
                .withConfig(config)
                .build();
        dockerClient.createContainer(container, DOCKER_HOST);
    }

    @After
    public void tearDown() throws Exception {
        DockerContainer container = ContainerBuilder.aContainer()
                .withName(CONTAINER_NAME)
                .build();
        dockerClient.removeContainer(container, DOCKER_HOST);
    }

    @Test
    public void test01_lifecycle() throws DockerJSONException {

        Assert.assertNotNull(dockerClient.findContainer(container, DOCKER_HOST).getId());

        dockerClient.findContainer(container, DOCKER_HOST);

        container = ContainerUtils.newStartInstance(container.getName(), null, null, null);

        dockerClient.startContainer(container, DOCKER_HOST);

        Assert.assertTrue(dockerClient.findContainer(container, DOCKER_HOST).getState().getRunning());

        dockerClient.stopContainer(container, DOCKER_HOST);
        Assert.assertFalse(dockerClient.findContainer(container, DOCKER_HOST).getState().getRunning());

        dockerClient.startContainer(container, DOCKER_HOST);
        dockerClient.killContainer(container, DOCKER_HOST);
        dockerClient.startContainer(container, DOCKER_HOST);

    }

    @Test
    public void test20_createContainerWithVolumeFrom() throws DockerJSONException, InterruptedException {
        container = ContainerUtils.newStartInstance(container.getName(), null, Arrays.asList("tomcat-8"), null);
        dockerClient.startContainer(container, DOCKER_HOST);
        container = dockerClient.findContainer(container, DOCKER_HOST);
        Assert.assertTrue(dockerClient.execCommand(container, Arrays.asList("bash", "-c", "ls /cloudunit/binaries"), DOCKER_HOST).getBody()
               .contains("bin"));
        Assert.assertTrue(dockerClient.execCommand(container, Arrays.asList("date"), DOCKER_HOST).getBody()
                .contains("2016"));
        container = ContainerUtils.newStartInstance(container.getName(), null, null, null);
        dockerClient.killContainer(container, DOCKER_HOST);


    }

    @Test
    public void test30_createContainerWithVolumes() throws DockerJSONException {
        container = ContainerUtils.newStartInstance(container.getName(),
                null, null, Arrays.asList("/etc/localtime:/etc/localtime:ro"));
        dockerClient.startContainer(container, DOCKER_HOST);
        List mounts = dockerClient.findContainer(container, DOCKER_HOST).getMounts();
        Assert.assertTrue(mounts.toString().contains("localtime"));
    }

    @Test
    public void test40_startContainerWithPorts() throws DockerJSONException {
        container = ContainerUtils.newStartInstance(container.getName(),
                new HashMap() {{
                    put("22/tcp", "2000");
                }}, null,
                null);
        dockerClient.startContainer(container, DOCKER_HOST);
        Assert.assertTrue((dockerClient.findContainer(container, DOCKER_HOST)
                .getNetworkSettings().getPorts().toString().contains("2000")));

    }
}
