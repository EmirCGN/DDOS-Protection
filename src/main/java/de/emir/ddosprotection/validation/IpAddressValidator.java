package de.emir.ddosprotection.validation;

import java.util.regex.Pattern;

public class IpAddressValidator {
    private static final Pattern IP_ADDRESS_PATTERN = Pattern.compile(
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$"
    );

    private static final Pattern PRIVATE_IP_ADDRESS_PATTERN = Pattern.compile(
            "^(10\\.|172\\.(1[6-9]|2\\d|3[01])\\.|192\\.168\\.)"
    );

    private static final Pattern LOOPBACK_IP_ADDRESS_PATTERN = Pattern.compile(
            "^(127\\.)"
    );

    public boolean isValid(String ipAddress) {
        return IP_ADDRESS_PATTERN.matcher(ipAddress).matches();
    }

    public boolean isPrivateIpAddress(String ipAddress) {
        return PRIVATE_IP_ADDRESS_PATTERN.matcher(ipAddress).find();
    }

    public boolean isLoopbackIpAddress(String ipAddress) {
        return LOOPBACK_IP_ADDRESS_PATTERN.matcher(ipAddress).find();
    }
}
