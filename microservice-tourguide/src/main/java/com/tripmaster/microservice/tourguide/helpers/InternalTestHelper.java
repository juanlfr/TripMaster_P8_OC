package com.tripmaster.microservice.tourguide.helpers;

public class InternalTestHelper {
    // Set this default up to 100,000 for testing
    private static int internalUserNumber = 1;

    public static void setInternalUserNumber(int internalUserNumber) {
        InternalTestHelper.internalUserNumber = internalUserNumber;
    }

    public static int getInternalUserNumber() {
        return internalUserNumber;
    }
}
