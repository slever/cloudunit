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

package fr.treeptik.cloudunit.core.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nicolas on 25/08/2014.
 */
final public class LogUnit {

    private final String source;
    private final String message;

    public LogUnit(final String source, final String message) {
        if (source == null) {
            throw new IllegalArgumentException("Source cannot be null");
        }
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        this.message = message;
        if (source != null && source.contains("/")) {
            this.source = source.substring(source.lastIndexOf("/") + 1);
        } else {
            this.source = source;
        }
    }

    public String getMessage() {
        return message;
    }

    public String getSource() {
        return source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogUnit logUnit = (LogUnit) o;

        if (!source.equals(logUnit.source)) return false;
        return message.equals(logUnit.message);

    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }
}
