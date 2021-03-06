/*
 * LICENCE : CloudUnit is available under the Affero Gnu Public License GPL V3 : https://www.gnu.org/licenses/agpl-3.0.html
 *     but CloudUnit is licensed too under a standard commercial license.
 *     Please contact our sales team if you would like to discuss the specifics of our Enterprise license.
 *     If you are not sure whether the GPL is right for you,
 *     you can always test our software under the GPL and inspect the source code before you contact us
 *     about purchasing a commercial license.
 *
 *     LEGAL TERMS : "CloudUnit" is a registered trademark of Treeptik and can't be used to endorse
 *     or promote products derived from this project without prior written permission from Treeptik.
 *     Products or services derived from this software may not be called "CloudUnit"
 *     nor may "Treeptik" or similar confusing terms appear in their names without prior written permission.
 *     For any questions, contact us : contact@treeptik.fr
 */

package fr.treeptik.cloudunit.modules;

import fr.treeptik.cloudunit.exception.ServiceException;
import fr.treeptik.cloudunit.initializer.CloudUnitApplicationContext;
import fr.treeptik.cloudunit.model.User;
import fr.treeptik.cloudunit.service.UserService;
import junit.framework.TestCase;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.Filter;
import java.util.Random;

import static fr.treeptik.cloudunit.utils.TestUtils.getUrlContentPage;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for Module lifecycle
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {
    CloudUnitApplicationContext.class,
    MockServletContext.class
})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("integration")
public abstract class AbstractModuleControllerTestIT extends TestCase {

    private final Logger logger = LoggerFactory
        .getLogger(AbstractModuleControllerTestIT.class);

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Inject
    private AuthenticationManager authenticationManager;

    @Autowired
    private Filter springSecurityFilterChain;

    @Inject
    private UserService userService;

    @Value("${cloudunit.instance.name}")
    private String cuInstanceName;

    private MockHttpSession session;

    private static String applicationName;

    @Value("${suffix.cloudunit.io}")
    private String domainSuffix;

    @Value("#{systemEnvironment['CU_SUB_DOMAIN']}")
    private String subdomain;

    private String domain;

    @PostConstruct
    public void init () {
        if (subdomain != null) {
            domain = subdomain + domainSuffix;
        } else {
            domain = domainSuffix;
        }
    }

    protected String server;
    protected String module;
    protected String managerPrefix;
    protected String managerSuffix;
    protected String managerPageContent;

    @BeforeClass
    public static void initEnv() {
        applicationName = "app" + new Random().nextInt(100000);
    }

    @Before
    public void setup() throws Exception {
        logger.info("setup");

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .addFilters(springSecurityFilterChain)
            .build();

        User user = null;
        try {
            user = userService.findByLogin("johndoe");
        } catch (ServiceException e) {
            logger.error(e.getLocalizedMessage());
        }

        assert user != null;
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword());
        Authentication result = authenticationManager.authenticate(authentication);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(result);
        session = new MockHttpSession();
        String secContextAttr = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
        session.setAttribute(secContextAttr,
            securityContext);

        // create an application server
        String jsonString = "{\"applicationName\":\"" + applicationName + "\", \"serverName\":\"" + server + "\"}";
        ResultActions resultats = mockMvc.perform(post("/application").session(session).contentType(MediaType.APPLICATION_JSON).content(jsonString));
        resultats.andExpect(status().isOk());
    }

    @After
    public void teardown() throws Exception {
        logger.info("teardown");

        logger.info("Delete application : " + applicationName);
        ResultActions resultats = mockMvc.perform(delete("/application/" + applicationName).session(session).contentType(MediaType.APPLICATION_JSON));
        resultats.andExpect(status().isOk());

        SecurityContextHolder.clearContext();
        session.invalidate();
    }

    @Test
    public void test00_FailToAddModuleBecauseBadAppName() throws Exception {
        logger.info("Cannot add a module because application name missing");
        String jsonString = "{\"applicationName\":\"" + "" + "\", \"imageName\":\"" + module + "\"}";
        ResultActions resultats = mockMvc.perform(post("/module")
            .session(session)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonString));
        resultats.andExpect(status().is5xxServerError());
    }

    @Test
    public void test01_FailToAddModuleBecauseAppNonExist() throws Exception {
        logger.info("Cannot add a module because application name missing");
        String jsonString = "{\"applicationName\":\"" + "UFO" + "\", \"imageName\":\"" + module + "\"}";
        ResultActions resultats = mockMvc.perform(post("/module")
            .session(session)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonString));
        resultats.andExpect(status().is5xxServerError());
    }

    @Test
    public void test02_FailToAddModuleBecauseModuleEmpty() throws Exception {
        logger.info("Cannot add a module because module name empty");
        String jsonString = "{\"applicationName\":\"" + "REALAPP" + "\", \"imageName\":\"" + "" + "\"}";
        ResultActions resultats = mockMvc.perform(post("/module")
            .session(session)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonString));
        resultats.andExpect(status().is5xxServerError());
    }

    @Test
    public void test03_FailToAddModuleBecauseModuleNonExisting() throws Exception {
        logger.info("Cannot add a module because module name empty");
        String jsonString = "{\"applicationName\":\"" + "REALAPP" + "\", \"imageName\":\"" + "UFO" + "\"}";
        ResultActions resultats = mockMvc.perform(post("/module")
            .session(session)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonString));
        resultats.andExpect(status().is5xxServerError());
    }

    @Test
    public void test10_CreateServerThenAddModule() throws Exception {
        logger.info("Create an application, add a " + module + " module and delete it");

        // verify if app exists
        ResultActions resultats = mockMvc.perform(get("/application/" + applicationName).session(session).contentType(MediaType.APPLICATION_JSON));
        resultats.andExpect(jsonPath("name").value(applicationName.toLowerCase()));

        // add a module
        String jsonString = "{\"applicationName\":\"" + applicationName + "\", \"imageName\":\"" + module + "\"}";
        resultats = mockMvc.perform(post("/module")
            .session(session)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonString));
        resultats.andExpect(status().isOk());

        // Expected values
        String genericModule = cuInstanceName.toLowerCase() + "-johndoe-" + applicationName.toLowerCase() + "-" + module + "-1";
        String managerExpected = "http://" + managerPrefix + "1-" + applicationName.toLowerCase() + "-johndoe-admin"+domain+"/" + managerSuffix;

        // get the detail of the applications to verify modules addition
        resultats = mockMvc.perform(get("/application/" + applicationName)
            .session(session).contentType(MediaType.APPLICATION_JSON)).andDo(print());
        resultats
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.modules[0].status").value("START"))
            .andExpect(jsonPath("$.modules[0].name").value(genericModule))
            .andExpect(jsonPath("$.modules[0].managerLocation").value(managerExpected));

        String contentPage = getUrlContentPage(managerExpected);
        int counter = 0;
        while (!contentPage.contains(managerPageContent) || counter++ < 10) {
            contentPage = getUrlContentPage(managerExpected);
            Thread.sleep(1000);
        }

        Assert.assertTrue(contentPage.contains(managerPageContent));

        // remove a module
        resultats = mockMvc.perform(delete("/module/" + applicationName + "/" + genericModule)
            .session(session)
            .contentType(MediaType.APPLICATION_JSON));
        resultats.andExpect(status().isOk());

        // get the detail of the applications to verify modules addition
        resultats = mockMvc.perform(get("/application/" + applicationName)
            .session(session).contentType(MediaType.APPLICATION_JSON)).andDo(print());
        resultats
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.modules[0]").doesNotExist());
    }

    @Test
    public void test20_CreateServerThenAddTwoModule() throws Exception {
        logger.info("Create an application, add two " + module + " modules, stop them then delete all");

        // verify if app exists
        ResultActions resultats = mockMvc.perform(get("/application/" + applicationName).session(session).contentType(MediaType.APPLICATION_JSON));
        resultats.andExpect(jsonPath("name").value(applicationName.toLowerCase()));

        // add a first module
        String jsonString = "{\"applicationName\":\"" + applicationName + "\", \"imageName\":\"" + module + "\"}";
        resultats = mockMvc.perform(post("/module")
            .session(session)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonString));
        resultats.andExpect(status().isOk());

        // Expected values
        String module1 = cuInstanceName.toLowerCase() + "-johndoe-" + applicationName.toLowerCase() + "-" + module + "-1";
        String managerExpected1 = "http://" + managerPrefix + "1-" + applicationName.toLowerCase() + "-johndoe-admin"+domain+"/" + managerSuffix;

        // get the detail of the applications to verify modules addition
        resultats = mockMvc.perform(get("/application/" + applicationName)
            .session(session).contentType(MediaType.APPLICATION_JSON)).andDo(print());
        resultats
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.modules[0].status").value("START"))
            .andExpect(jsonPath("$.modules[0].name").value(module1))
            .andExpect(jsonPath("$.modules[0].managerLocation").value(managerExpected1));

        String contentPage = getUrlContentPage(managerExpected1);

        int counter = 0;
        while (!contentPage.contains(managerPageContent) || counter++ < 20) {
            contentPage = getUrlContentPage(managerExpected1);
            Thread.sleep(1000);
        }

        System.out.println(contentPage);
        System.out.println(managerPageContent);
        Assert.assertTrue(contentPage.contains(managerPageContent));

        // add a second module
        jsonString = "{\"applicationName\":\"" + applicationName + "\", \"imageName\":\"" + module + "\"}";
        resultats = mockMvc.perform(post("/module")
            .session(session)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonString));
        resultats.andExpect(status().isOk());

        // Expected values
        String module2 = cuInstanceName.toLowerCase() + "-johndoe-" + applicationName.toLowerCase() + "-" + module + "-2";
        String managerExpected2 = "http://" + managerPrefix + "2-" + applicationName.toLowerCase() + "-johndoe-admin"+domain+"/" + managerSuffix;

        // get the detail of the applications to verify modules addition
        resultats = mockMvc.perform(get("/application/" + applicationName)
            .session(session).contentType(MediaType.APPLICATION_JSON)).andDo(print());
        resultats
            .andExpect(status().isOk())

            .andExpect(jsonPath("$.modules[0].status").value("START"))
            .andExpect(jsonPath("$.modules[0].name").value(module1))
            .andExpect(jsonPath("$.modules[0].managerLocation").value(managerExpected1))

            .andExpect(jsonPath("$.modules[1].status").value("START"))
            .andExpect(jsonPath("$.modules[1].name").value(module2))
            .andExpect(jsonPath("$.modules[1].managerLocation").value(managerExpected2));

        // remove the first module 
        resultats = mockMvc.perform(delete("/module/" + applicationName + "/" + module1)
            .session(session)
            .contentType(MediaType.APPLICATION_JSON));
        resultats.andExpect(status().isOk());

        // we must verify the first module disappared and second one replaced it
        resultats = mockMvc.perform(get("/application/" + applicationName)
            .session(session).contentType(MediaType.APPLICATION_JSON)).andDo(print());
        resultats
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.modules[0].status").value("START"))
            .andExpect(jsonPath("$.modules[0].name").value(module2))
            .andExpect(jsonPath("$.modules[0].managerLocation").value(managerExpected2))
            .andExpect(jsonPath("$.modules[1]").doesNotExist());
    }

    @Test
    public void test30_AddModuleThenRestart() throws Exception {
        logger.info("Create an application, add a " + module + " modules, restart");

        // verify if app exists
        ResultActions resultats = mockMvc.perform(get("/application/" + applicationName).session(session).contentType(MediaType.APPLICATION_JSON));
        resultats.andExpect(jsonPath("name").value(applicationName.toLowerCase()));

        // add a first module
        String jsonString = "{\"applicationName\":\"" + applicationName + "\", \"imageName\":\"" + module + "\"}";
        resultats = mockMvc.perform(post("/module")
            .session(session)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonString));
        resultats.andExpect(status().isOk());

        // Expected values
        String module1 = cuInstanceName.toLowerCase() + "-johndoe-" + applicationName.toLowerCase() + "-" + module + "-1";
        String managerExpected1 = "http://" + managerPrefix + "1-" + applicationName.toLowerCase() + "-johndoe-admin"+domain+"/" + managerSuffix;

        // Stop the application
        jsonString = "{\"applicationName\":\"" + applicationName + "\"}";
        resultats = mockMvc.perform(post("/application/stop").session(session).contentType(MediaType.APPLICATION_JSON).content(jsonString));
        resultats.andExpect(status().isOk());

        // Start the application
        jsonString = "{\"applicationName\":\"" + applicationName + "\"}";
        resultats = mockMvc.perform(post("/application/start").session(session).contentType(MediaType.APPLICATION_JSON).content(jsonString));
        resultats.andExpect(status().isOk());

        // get the detail of the applications to verify modules addition
        resultats = mockMvc.perform(get("/application/" + applicationName)
                .session(session).contentType(MediaType.APPLICATION_JSON)).andDo(print());
        resultats
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modules[0].status").value("START"))
                .andExpect(jsonPath("$.modules[0].name").value(module1))
                .andExpect(jsonPath("$.modules[0].managerLocation").value(managerExpected1));
    }


}