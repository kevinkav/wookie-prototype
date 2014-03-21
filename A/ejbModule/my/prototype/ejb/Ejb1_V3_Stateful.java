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
package my.prototype.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import my.prototype.test.api.TestCase;
import my.remote.v3.api.Ejb1StatefulRemote;

//@Local(TestCase.class)
//@Remote(Ejb1StatefulRemote.class)
//@Stateful
//@EJB(name=Ejb1StatefulRemote.EJB1_STATEFUL_JNDI_LOOKUP, beanInterface=Ejb1StatefulRemote.class)
public class Ejb1_V3_Stateful extends Ejb1Base implements Ejb1StatefulRemote {

   
    @Override
    public void runTest() throws Exception {
        
    }

    
/*    @Override
    public String getCountryOfOrigin(long id) throws RemoteException {
        return null;
    }


    @Override
    public void addCastToFilm() throws RemoteException {
        
    }*/

 
}
