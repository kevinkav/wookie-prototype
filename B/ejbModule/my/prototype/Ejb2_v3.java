/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package my.prototype;

import java.util.logging.Logger;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import my.remote.v3.api.Ejb1Remote;
import my.remote.v3.api.Ejb2Remote;
import my.remote.v3.bean.locator.BeanLocator;


@Remote(Ejb2Remote.class)
@Stateless
public class Ejb2_v3 implements Ejb2Remote{

    @Inject
    BeanLocator beanLocator;
    Ejb1Remote ejb1;    
    private static final Logger LOGGER = Logger.getLogger(Ejb2_v3.class.getCanonicalName());
    private static final String BEAN1_LOOKUP_NAME = "ejb:Ear1/A/Ejb1_v3!my.remote.v3.api.Ejb1Remote";
    

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String getCountryOfOriginAndCreateCast(long id) throws Exception {
        LOGGER.info("Called with id: " + id);
        String countryOfOriginEjb1 = "";
        try {
            ejb1 = (Ejb1Remote) beanLocator.locateBean(BEAN1_LOOKUP_NAME);
            countryOfOriginEjb1 = ejb1.getCountryOfOrigin(id);
            LOGGER.info("received CoutryOfOrigin value from ejb1: " + countryOfOriginEjb1);
            ejb1.addCastToFilm();
        } catch (Exception e) {
            LOGGER.severe("Exception occurred rolling back transaction...");
            throw e;
        }
        return countryOfOriginEjb1;
    }

}
