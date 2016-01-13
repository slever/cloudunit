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

package fr.treeptik.cloudunit.service.impl.docker;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.treeptik.cloudunit.dao.ModuleConfigurationDAO;
import fr.treeptik.cloudunit.dao.SnapshotDAO;
import fr.treeptik.cloudunit.exception.CheckException;
import fr.treeptik.cloudunit.exception.DockerJSONException;
import fr.treeptik.cloudunit.exception.ServiceException;
import fr.treeptik.cloudunit.model.*;
import fr.treeptik.cloudunit.service.*;
import fr.treeptik.cloudunit.utils.AuthentificationUtils;
import fr.treeptik.cloudunit.utils.ShellUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Profile("docker")
@Service
public class SnapshotServiceImpl
        implements SnapshotService {

    private Logger logger = LoggerFactory.getLogger(SnapshotServiceImpl.class);

    @Inject
    private ApplicationService applicationService;

    @Inject
    private AuthentificationUtils authentificationUtils;

    @Inject
    private SnapshotDAO snapshotDAO;

    @Inject
    private ModuleConfigurationDAO moduleConfigurationDAO;

    @Inject
    private ModuleService moduleService;

    @Inject
    private ImageService imageService;

    @Inject
    private ShellUtils shellUtils;

    @Inject
    private ServerService serverService;

    @Value("${cloudunit.max.apps:100}")
    private String numberMaxApplications;

    @Value("${ip.for.registry}")
    private String ipForRegistry;

    @Value("${docker.manager.ip:192.168.50.4:2376}")
    private String dockerManagerIp;

    @Override
    public List<Snapshot> listAll(String login) throws ServiceException {
        return null;
    }

    @Override
    public Snapshot remove(String tag, String login) throws ServiceException, CheckException {
        return null;
    }

    @Override
    public Snapshot findOne(String tag, String login) {
        return snapshotDAO.findByTagAndUser(login, tag);
    }

    @Override
    public Snapshot create(String applicationName, User user, String tag, String description, Status previousStatus) throws ServiceException, CheckException {
        return null;
    }

    @Override
    public Snapshot cloneFromASnapshot(String applicationName, String tag) throws ServiceException, InterruptedException, CheckException {
        return null;
    }


}
