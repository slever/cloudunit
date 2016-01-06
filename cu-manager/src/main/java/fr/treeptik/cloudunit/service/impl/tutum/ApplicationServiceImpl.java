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

package fr.treeptik.cloudunit.service.impl.tutum;

import fr.treeptik.cloudunit.dao.ApplicationDAO;
import fr.treeptik.cloudunit.dao.PortToOpenDAO;
import fr.treeptik.cloudunit.docker.model.DockerContainer;
import fr.treeptik.cloudunit.dto.ContainerUnit;
import fr.treeptik.cloudunit.exception.CheckException;
import fr.treeptik.cloudunit.exception.ServiceException;
import fr.treeptik.cloudunit.model.*;
import fr.treeptik.cloudunit.service.*;
import fr.treeptik.cloudunit.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Profile("tutum")
@Service
public class ApplicationServiceImpl
        implements ApplicationService {

    Locale locale = Locale.ENGLISH;

    private Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);


    @Override
    public Application findByNameAndUser(User user, String applicationName) throws ServiceException, CheckException {
        return null;
    }

    @Override
    public List<Application> findAll() throws ServiceException {
        return null;
    }

    @Override
    public List<Application> findAllByUser(User user) throws ServiceException {
        return null;
    }

    @Override
    public Long countApp(User user) throws ServiceException {
        return null;
    }

    @Override
    public void isValid(String applicationName, String serverName) throws ServiceException, CheckException {

    }

    @Override
    public void checkCreate(Application application, String serverName) throws CheckException, ServiceException {

    }

    @Override
    public Application saveInDB(Application application) throws ServiceException {
        return null;
    }

    @Override
    public boolean checkAppExist(User user, String applicationName) throws ServiceException, CheckException {
        return false;
    }

    @Override
    public void setStatus(Application application, Status status) throws ServiceException {

    }

    @Override
    public Application deploy(File file, Application application) throws ServiceException, CheckException {
        return null;
    }

    @Override
    public Application start(Application application) throws ServiceException {
        return null;
    }

    @Override
    public Application stop(Application application) throws ServiceException {
        return null;
    }

    @Override
    public Application saveGitPush(Application application, String login) throws ServiceException, CheckException {
        return null;
    }

    @Override
    public List<ContainerUnit> listContainers(String applicationName) throws ServiceException {
        return null;
    }

    @Override
    public List<String> listContainersId(String applicationName) throws ServiceException {
        return null;
    }

    @Override
    public List<String> getListAliases(Application application) throws ServiceException {
        return null;
    }

    @Override
    public void addNewAlias(Application application, String alias) throws ServiceException, CheckException {

    }

    @Override
    public void updateAliases(Application application) throws ServiceException {

    }

    @Override
    public void removeAlias(Application application, String alias) throws ServiceException, CheckException {

    }

    @Override
    public Application updateEnv(Application application, User user) throws ServiceException {
        return null;
    }

    @Override
    public Application postStart(Application application, User user) throws ServiceException {
        return null;
    }

    @Override
    public Application remove(Application application, User user) throws ServiceException, CheckException {
        return null;
    }

    @Override
    public Application sshCopyIDToServer(Application application, User user) throws ServiceException {
        return null;
    }

    @Override
    public Application create(String applicationName, String login, String serverName, String tagName) throws ServiceException, CheckException {
        return null;
    }

    @Override
    public void addPort(Application application, String nature, Integer port) throws ServiceException {

    }

    @Override
    public void removePort(Application application, Integer port) throws CheckException, ServiceException {

    }
}
