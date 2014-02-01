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
import java.util.Hashtable;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import my.prototype.api.Ejb1Remote;
import my.prototype.api.Ejb3Remote;

@Remote(Ejb3Remote.class)
@EJB(name = Ejb3Remote.EJB3_REMOTE_JNDI, beanInterface = Ejb3Remote.class)
@Stateless
public class B implements Ejb3Remote{

  
    @TransactionAttribute(TransactionAttributeType.MANDATORY) 
    @Override
    public String runTest(long id) throws RemoteException {
        Ejb1Remote ejb1 = resolveBean(Ejb1Remote.EJB1_REMOTE_JNDI);
        return ejb1.getAttributeCountryOfOrigin(id);
    }

    private static <T> T resolveBean(final String jndiName) {
        final Hashtable<String, String> jndiProperties = new Hashtable<String, String>();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        try {
            final InitialContext ctx = new InitialContext(jndiProperties);
            @SuppressWarnings("unchecked")
            final T bean = (T) ctx.lookup(jndiName);
            return bean;
        } catch (NamingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
   

}
