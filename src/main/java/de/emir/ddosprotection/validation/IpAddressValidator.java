package de.emir.ddosprotection.validation;

import java.util.regex.Pattern;

public class IpAddressValidator {
    private static final String IP_ADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public boolean isValid(String ipAddress) {
        return Pattern.matches(IP_ADDRESS_PATTERN, ipAddress);
    }

    public boolean isPrivateIpAddress(String ipAddress) {
        String[] octets = ipAddress.split("\\.");
        int firstOctet = Integer.parseInt(octets[0]);

        return (firstOctet == 10) || (firstOctet == 172 && (16 <= Integer.parseInt(octets[1])) && (Integer.parseInt(octets[1]) <= 31)) ||
                (firstOctet == 192 && Integer.parseInt(octets[1]) == 168);
    }

    public boolean isLoopbackIpAddress(String ipAddress) {
        return ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1") || ipAddress.equalsIgnoreCase("localhost");
    }
}
