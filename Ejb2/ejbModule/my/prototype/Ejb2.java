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

import my.prototype.api.Ejb3Remote;

@Stateless
public class Ejb2 {

    //@EJB(lookup = Ejb3Remote.EJB3_REMOTE_JNDI)
    private Ejb3Remote ejb3;
    private String EJB3_JNDI = "java:global/Ear2/Ejb3/Ejb3!my.prototype.api.Ejb3Remote";
    // lookup using ejb namespace
    private String EJB3_EJB_LOOKUP = "ejb:Ear2/Ejb3//Ejb3!my.prototype.api.Ejb3Remote";
    
    //@TransactionAttribute(TransactionAttributeType.MANDATORY)
    public void runTest(long id){
        log("Ejb2 calling Ejb3 using resolveBean method");
        //ejb3 = ejbLookup(EJB3_JNDI);
        ejb3 = resolveBean(EJB3_EJB_LOOKUP);
        ejb3.setModifiedAttribute(id);
    }
    
    public void kickEjb3(){
        ejb3 = resolveBean(EJB3_EJB_LOOKUP);
        ejb3.kickEjb3("EJB2");
    }
    
    private void log (String str){
        System.out.println(str);
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
}
