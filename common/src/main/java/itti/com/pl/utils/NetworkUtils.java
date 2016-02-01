package itti.com.pl.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import itti.com.pl.utils.exception.MetadataHandlerException;

public final class NetworkUtils {

	private NetworkUtils(){
	}
	
	public static final String getIpAddress(){
		try {
			String hostIP = InetAddress.getLocalHost().getHostAddress();
	        if (!isExcludedAddress(hostIP)) {
	            return hostIP;
	        }

			Enumeration<NetworkInterface> nInterfaces = NetworkInterface
	                .getNetworkInterfaces();
	        while (nInterfaces.hasMoreElements()) {
	            Enumeration<InetAddress> inetAddresses = nInterfaces
	                    .nextElement().getInetAddresses();
	            while (inetAddresses.hasMoreElements()) {
	                String address = inetAddresses.nextElement()
	                        .getHostAddress();
	                if (!isExcludedAddress(address)) {
	                    return address;
	                }
	            }
	        }
		} catch (UnknownHostException | SocketException e) {
			throw new MetadataHandlerException("Could not determine IP address", e);
		}
		throw new MetadataHandlerException("Could not determine IP address");
	}

	private static final boolean isExcludedAddress(String address){
        return address.equals("127.0.0.1") || address.equals("127.0.1.1") || address.contains(":");
	}

}
