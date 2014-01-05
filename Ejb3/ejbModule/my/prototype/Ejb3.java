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

import java.util.Hashtable;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import my.prototype.api.Ejb1Remote;
import my.prototype.api.Ejb3Remote;
import my.prototype.remote.home.api.Ejb3RemoteHome;

@RemoteHome(Ejb3RemoteHome.class)
@EJB(name = my.prototype.remote.home.api.Ejb3Remote.EJB3_REMOTE_JNDI, beanInterface = my.prototype.remote.home.api.Ejb3Remote.class)
//@Remote(Ejb3Remote.class)
@Stateless
public class Ejb3 {
//    public class Ejb3 implements Ejb3Remote{


    private Ejb1Remote ejb1;
    
    private String EJB1_JNDI = "java:global/Ear1/Ejb1/Ejb1!my.prototype.api.Ejb1Remote";
    private String EJB1_EJB_JNDI = "ejb:Ear1/Ejb1//Ejb1!my.prototype.api.Ejb1Remote";
    
    /* (non-Javadoc)
     * @see my.prototype.api.Ejb3Remote#kickEjb3()
     */
    //@Override
    public void kickEjb3(String kicker) {
        log("EJB3 was kicked by " + kicker);
        ejb1 = resolveBean(EJB1_EJB_JNDI);
        ejb1.kickEjb1("EJB3");
    }
    

    /* (non-Javadoc)
     * @see my.prototype.api.Ejb3Remote#getModifiedAttribute()
     */
    // In mediation-core this doesn't have TX attribute mandatory
    //@Override
    //@TransactionAttribute(TransactionAttributeType.MANDATORY) 
    public void setModifiedAttribute(long id) {
        log("EJB3 calling EJB1 remotely and set Country Of Origin: EJB3");
        ejb1 = resolveBean(EJB1_EJB_JNDI);
        //ejb1.kickEjb1("EJB3");
        ejb1.setAttributeCountryOfOrigin(id, "EJB3");
    }

    
    public static <T> T resolveBean(final String jndiName) {
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
    
    private <T> T ejbLookup(String jndi){
        T bean = null; 
        final Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        props.put("jboss.naming.client.ejb.context", "true");

        try {
            InitialContext ic = new InitialContext(props);
            bean = (T)ic.lookup(jndi);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return bean;
    }
    
    private void log (String str){
        System.out.println(str);
    }

    
}
