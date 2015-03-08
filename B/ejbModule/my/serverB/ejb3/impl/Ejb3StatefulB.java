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
package my.serverB.ejb3.impl;

import javax.ejb.Remote;
import javax.ejb.Stateful;

import my.remote.serverB.ejb3.api.StatefulRemoteB;

@Remote(StatefulRemoteB.class)
@Stateful
public class Ejb3StatefulB implements StatefulRemoteB{


	@Override
	public String getCountryOfOriginAndCreateCast(long id) throws Exception {
		return null;
	}


	@Override
	public String getCountryOfOrigin(long id) throws Exception {
		return null;
	}


	@Override
	public void createCast(long id) throws Exception {
		
	}

}
