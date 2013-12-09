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
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import my.prototype.api.Ejb1Remote;
import my.prototype.api.Ejb3Remote;

@EJB(name=Ejb3Remote.EJB3_REMOTE_JNDI, beanInterface=Ejb3Remote.class)
@Stateless
public class Ejb3 implements Ejb3Remote{

    //@EJB(lookup = Ejb1Remote.EJB1_REMOTE_JNDI)
    private Ejb1Remote ejb1;
    
    private String EJB1_JNDI = "java:global/Ear1/Ejb1/Ejb1!my.prototype.api.Ejb1Remote";
    
    /* (non-Javadoc)
     * @see my.prototype.api.Ejb3Remote#kickEjb3()
     */
    @Override
    public void kickEjb3() {
        log("Ejb3 was kicked remotely by Ejb2!");
    }
    

    /* (non-Javadoc)
     * @see my.prototype.api.Ejb3Remote#getModifiedAttribute()
     */
    // In mediation-core this doesn't have TX attribute mandatory
    @Override
    @TransactionAttribute(TransactionAttributeType.MANDATORY) 
    public String getModifiedAttribute(long id) {
        ejb1 = (Ejb1Remote) ejbLookup(EJB1_JNDI);
        //ejb1.kickEjb1();
        //String country = ejb1.getAttributeCountryOfOrigin(id);
        String country = ejb1.setAttributeCountryOfOrigin(id);
        log("EJB3 CountryOfOrigin: " + country);
        return country;
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
