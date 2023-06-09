package de.emir.ddosprotection;

import de.emir.ddosprotection.logging.Logger;
import de.emir.ddosprotection.validation.IpAddressValidator;

public class Main {
    public static void main(String[] args) {
        DdosProtection ddosProtection = new DdosProtection();

        // Beispiel-IP-Adressen
        String ipAddress1 = "192.168.0.1";
        String ipAddress2 = "192.168.0.2";
        String ipAddress3 = "192.168.0.3";
        String ipAddress4 = "123.456.789.0";

        IpAddressValidator ipAddressValidator = new IpAddressValidator();

        if (ipAddressValidator.isValid(ipAddress1)) {
            boolean isAllowed1 = ddosProtection.isAllowed(ipAddress1, "userInput1");
            Logger.logAccess(ipAddress1, isAllowed1);
        }

        if (ipAddressValidator.isValid(ipAddress2)) {
            boolean isAllowed2 = ddosProtection.isAllowed(ipAddress2, "userInput2");
            Logger.logAccess(ipAddress2, isAllowed2);
        }

        if (ipAddressValidator.isValid(ipAddress3)) {
            boolean isAllowed3 = ddosProtection.isAllowed(ipAddress3, "userInput3");
            Logger.logAccess(ipAddress3, isAllowed3);
        }

        if (ipAddressValidator.isValid(ipAddress4)) {
            boolean isAllowed4 = ddosProtection.isAllowed(ipAddress4, "userInput4");
            Logger.logAccess(ipAddress4, isAllowed4);
        }
    }

}