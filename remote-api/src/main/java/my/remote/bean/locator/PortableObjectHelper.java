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