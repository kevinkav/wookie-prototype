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

import java.rmi.RemoteException;
import java.util.logging.Logger;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import my.prototype.api.Ejb1Remote;
import my.prototype.api.Ejb3Remote;


@Remote(Ejb3Remote.class)
@Stateless
public class Ejb3 implements Ejb3Remote{

    @Inject
    BeanLocator beanLocator;
    
    Ejb1Remote ejb1;    
    
    private final String LEAD_ACTOR = "Harrison Ford";
    
    private static final Logger LOGGER = Logger.getLogger(Ejb3.class.getCanonicalName());
    
    private static final String BEAN1_LOOKUP_NAME = "ejb:Ear1/A/Ejb1NonJTS!my.prototype.api.Ejb1Remote";
    

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String runTest(long id) throws RemoteException {
        LOGGER.info("Ejb3: runTest called with id: " + id);
        ejb1 = (Ejb1Remote) beanLocator.locateBean(BEAN1_LOOKUP_NAME);
        String attr = ejb1.getAttributeCountryOfOrigin_RemoteCall(id);
        LOGGER.info("Ejb3: received coutry of origin value from ejb1: " + attr);
        ejb1.createCast_RemoteCall(LEAD_ACTOR);
        return attr;
    }

}
