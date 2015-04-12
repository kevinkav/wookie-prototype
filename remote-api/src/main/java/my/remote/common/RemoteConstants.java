package my.remote.common;


public class RemoteConstants {

	/**
	 * Used in logging to indicate application server A
	 */
	public static final String SERVER_A = "Server-A";
	
	/**
	 * Used in logging to indicate application server B
	 */
	public static final String SERVER_B = "Server-B";
	
	/**
	 * The base corba IIOP port value
	 */
    public static final int IIOP_PORT = 3528;
    
    /**
     * The Corba endpoint address prefix
     */
    public static final String CORBA_ADDR_PREFIX = "corbaname:iiop:localhost:";
	
    
    
	public static String createCorbaEndpointAddress(int portOffset, String binding) {
		int port = IIOP_PORT + portOffset;
		return CORBA_ADDR_PREFIX + port + "#" + binding;
	}
	
}
