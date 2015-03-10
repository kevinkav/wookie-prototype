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
package my.remote.bean.locator;

import javax.rmi.PortableRemoteObject;

/**
 * PortableObjectHelper class to aid with junit testing.
 */
public class PortableObjectHelper {

	public Object narrow(Object narrowFrom, Class narrowTo){
		return PortableRemoteObject.narrow(narrowFrom, narrowTo);
	}
}