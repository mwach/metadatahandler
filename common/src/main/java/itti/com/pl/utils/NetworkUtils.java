package itti.com.pl.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import itti.com.pl.utils.exception.MetadataHandlerException;

public final class NetworkUtils {

	private NetworkUtils(){
	}
	
	public static final String getIpAddress(){
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			throw new MetadataHandlerException("Could not determine IP address", e);
		}
	}
}
