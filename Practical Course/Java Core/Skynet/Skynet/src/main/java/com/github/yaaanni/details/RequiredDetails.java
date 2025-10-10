package com.github.yaaanni.details;

import java.util.HashMap;
import java.util.Map;

public class RequiredDetails {
    private static Map<TypeDetail, Integer> requiredDetails = new HashMap<>();

    static {
        requiredDetails.put(TypeDetail.HEAD, 1);
        requiredDetails.put(TypeDetail.BODY, 1);
        requiredDetails.put(TypeDetail.ARM, 2);
        requiredDetails.put(TypeDetail.LEG, 2);
    }

    public static Map<TypeDetail, Integer> getRequiredDetails() {
        return requiredDetails;
    }

    public static boolean ifEnough(Map<TypeDetail, Integer> details) {
        for (Map.Entry<TypeDetail, Integer> entry : requiredDetails.entrySet()) {
            TypeDetail detail = entry.getKey();
            int requiredCount = entry.getValue();
            int availableCount = details.getOrDefault(detail, 0);
            if (requiredCount <= availableCount) {
                return true;
            }

        }
        return false;
    }
}
