package fr.treeptik.cloudunit.docker.core;

import fr.treeptik.cloudunit.docker.builders.ConfigBuilder;
import fr.treeptik.cloudunit.docker.builders.ContainerBuilder;
import fr.treeptik.cloudunit.docker.builders.HostConfigBuilder;
import fr.treeptik.cloudunit.docker.model.Config;
import fr.treeptik.cloudunit.docker.model.Container;
import fr.treeptik.cloudunit.docker.model.HostConfig;
import fr.treeptik.cloudunit.exception.DockerJSONException;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.*;

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
    private Container container;

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
        dockerClient.setDriver(new SimpleDockerDriver("/home/guillaume/cloudunit/cu-vagrant/certificats", isTLS));
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
                .withImage("cloudunit/git:dev")
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
        dockerClient.findContainer(container, DOCKER_HOST);
        int status = dockerClient.removeContainer(container, DOCKER_HOST).getStatus();
        Assert.assertEquals(status, 204);
    }

    @Test
    public void test01_lifecycle() throws DockerJSONException {

        Assert.assertNotNull(dockerClient.findContainer(container, DOCKER_HOST).getId());

        dockerClient.findContainer(container, DOCKER_HOST);

        HostConfig hostConfig = HostConfigBuilder.aHostConfig()
                .withLinks(new ArrayList<>())
                .withBinds(new ArrayList<>())
                .withPortBindings(new HashMap<>())
                .withPrivileged(Boolean.FALSE)
                .withPublishAllPorts(Boolean.TRUE)
                .withVolumesFrom(new ArrayList<>()).build();
        Config config = ConfigBuilder.aConfig()
                .withHostConfig(hostConfig)
                .build();
        container = ContainerBuilder.aContainer()
                .withName(CONTAINER_NAME)
                .withConfig(config).build();
        dockerClient.startContainer(container, DOCKER_HOST);
        Assert.assertTrue(dockerClient.findContainer(container, DOCKER_HOST).getState().getRunning());

        dockerClient.stopContainer(container, DOCKER_HOST);
        Assert.assertFalse(dockerClient.findContainer(container, DOCKER_HOST).getState().getRunning());

        dockerClient.startContainer(container, DOCKER_HOST);
        dockerClient.killContainer(container, DOCKER_HOST);
        Assert.assertEquals(new Long(137), dockerClient.findContainer(container, DOCKER_HOST).getState().getExitCode());
    }

    @Test
    public void test20_createContainerWithVolumeFrom() throws DockerJSONException {
        HostConfig hostConfig = HostConfigBuilder.aHostConfig()
                .withVolumesFrom(new ArrayList() {{
                    add("tomcat-8");
                }})
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
        Container container = ContainerBuilder.aContainer()
                .withName(CONTAINER_NAME + "" + new Random().nextInt())
                .withConfig(config)
                .build();
        dockerClient.createContainer(container, DOCKER_HOST);
        dockerClient.startContainer(container, DOCKER_HOST);

        container = dockerClient.findContainer(container, DOCKER_HOST);
        Assert.assertTrue(dockerClient.execCommand(container, Arrays.asList("bash", "-c", "ls /cloudunit/binaries"), DOCKER_HOST).getBody()
                .contains("bin"));
        dockerClient.findContainer(container, DOCKER_HOST);
        dockerClient.killContainer(container, DOCKER_HOST);
        dockerClient.removeContainer(container, DOCKER_HOST);
    }

    @Test
    public void test30_createContainerWithVolumes() throws DockerJSONException {
        HostConfig hostConfig = HostConfigBuilder.aHostConfig()
                .withBinds(new ArrayList() {{
                    add("/etc/localtime:/etc/localtime:ro");
                }})
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
        Container container = ContainerBuilder.aContainer()
                .withName(CONTAINER_NAME + "" + new Random().nextInt())
                .withConfig(config)
                .build();
        dockerClient.createContainer(container, DOCKER_HOST);
        dockerClient.startContainer(container, DOCKER_HOST);

        container = dockerClient.findContainer(container, DOCKER_HOST);
        List mounts = dockerClient.findContainer(container, DOCKER_HOST).getMounts();
        Assert.assertTrue(mounts.toString().contains("localtime"));
        dockerClient.findContainer(container, DOCKER_HOST);
        dockerClient.killContainer(container, DOCKER_HOST);
        dockerClient.removeContainer(container, DOCKER_HOST);
    }

    @Test
    public void test40_startContainerWithPorts() throws DockerJSONException {

        /*
        Ports definition 22/tcp -> 2000
         */

        String hostPort = "2000";
        String hostIP = "172.17.42.1";
        String portKey = "22/tcp";

        Map<String, String> mapForHostPort = new HashMap() {{
            put("HostPort", hostPort);
        }};


        List<Map<String, String>> params = Arrays.asList(mapForHostPort);


        HostConfig hostConfig = HostConfigBuilder.aHostConfig()
                .withBinds(new ArrayList())
                .withPortBindings(new HashMap() {{
                    put(portKey, params);
                }})
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
        Container container = ContainerBuilder.aContainer()
                .withName(CONTAINER_NAME + "" + new Random().nextInt())
                .withConfig(config)
                .build();
        dockerClient.createContainer(container, DOCKER_HOST);
        dockerClient.startContainer(container, DOCKER_HOST);

        container = dockerClient.findContainer(container, DOCKER_HOST);
        dockerClient.findContainer(container, DOCKER_HOST);
        dockerClient.killContainer(container, DOCKER_HOST);
        dockerClient.removeContainer(container, DOCKER_HOST);
    }
}
