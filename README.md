# DDoS Protection

DDoS Protection is a Java-based library designed to enhance protection against Distributed Denial-of-Service (DDoS) attacks on root and VPS servers. This library allows you to detect suspicious IP addresses and block access to your system, preventing overload and impairment of your services.

## Features

- Detects suspicious activities based on user inputs and keywords.
- Blocks IP addresses that exceed the request limit or are classified as private or loopback.
- Tracks and counts requests per IP address to identify unusual request patterns.
- Logs access events, blocked IP addresses, and audit events.
- Supports reset of request counters and failed login attempts.

## Usage

Using the DDoS Protection library is simple and straightforward. You just need to include the library in your project and use the provided classes.

### Example Code

```java
package com.example.ddosprotection;

import com.example.ddosprotection.logging.Logger;
import com.example.ddosprotection.validation.IpAddressValidator;

public class Main {
    public static void main(String[] args) {
        DdosProtection ddosProtection = new DdosProtection();

        // Example IP addresses
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
```

## Contributing
You are welcome to contribute to this project. If you have any improvement suggestions or found any bugs, you can open a pull request or report an issue in our issue tracker. We appreciate your contribution!

## License
This project is licensed under the MIT License. For more information, please refer to the LICENSE file.
