/*
 * LICENCE : CloudUnit is available under the GNU Affero General Public License : https://gnu.org/licenses/agpl.html
 * but CloudUnit is licensed too under a standard commercial license.
 * Please contact our sales team if you would like to discuss the specifics of our Enterprise license.
 * If you are not sure whether the AGPL is right for you,
 * you can always test our software under the AGPL and inspect the source code before you contact us
 * about purchasing a commercial license.
 *
 * LEGAL TERMS : "CloudUnit" is a registered trademark of Treeptik and can't be used to endorse
 * or promote products derived from this project without prior written permission from Treeptik.
 * Products or services derived from this software may not be called "CloudUnit"
 * nor may "Treeptik" or similar confusing terms appear in their names without prior written permission.
 * For any questions, contact us : contact@treeptik.fr
 */

package fr.treeptik.cloudunit.manager.impl.tutum;

import fr.treeptik.cloudunit.exception.CheckException;
import fr.treeptik.cloudunit.exception.ServiceException;
import fr.treeptik.cloudunit.manager.ApplicationManager;
import fr.treeptik.cloudunit.model.*;
import fr.treeptik.cloudunit.service.ApplicationService;
import fr.treeptik.cloudunit.service.ModuleService;
import fr.treeptik.cloudunit.service.ServerService;
import fr.treeptik.cloudunit.utils.FilesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * LifeCycle for an application
 * Created by nicolas on 21/09/15.
 */
@Profile("tutum")
@Component
public class ApplicationManagerImpl
        implements ApplicationManager {

    private Logger logger = LoggerFactory.getLogger(ApplicationManagerImpl.class);


    @Override
    public void create(String applicationName, String userLogin, String serverName) throws ServiceException, CheckException {

    }

    @Override
    public void start(Application application, User user) throws ServiceException, CheckException {

    }

    @Override
    public void stop(Application application, User user) throws ServiceException, CheckException {

    }

    @Override
    public void deploy(MultipartFile fileUpload, Application application) throws ServiceException, CheckException {

    }
}
