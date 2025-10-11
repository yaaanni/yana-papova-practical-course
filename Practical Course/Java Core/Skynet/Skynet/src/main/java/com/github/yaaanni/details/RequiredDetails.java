package com.github.yaaanni.details;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequiredDetails {
    private static ConcurrentHashMap<TypeDetail, Integer> requiredDetails = new ConcurrentHashMap<>();

    static {
        requiredDetails.put(TypeDetail.HEAD, 1);
        requiredDetails.put(TypeDetail.BODY, 1);
        requiredDetails.put(TypeDetail.ARM, 2);
        requiredDetails.put(TypeDetail.LEG, 2);
    }

    public static ConcurrentHashMap<TypeDetail, Integer> getRequiredDetails() {
        return requiredDetails;
    }

    public static boolean ifEnough(ConcurrentHashMap<TypeDetail, Integer> details) {
        for (Map.Entry<TypeDetail, Integer> entry : requiredDetails.entrySet()) {
            TypeDetail detail = entry.getKey();
            int requiredCount = entry.getValue();
            int availableCount = details.getOrDefault(detail, 0);
            if (availableCount < requiredCount) {
                return false;
            }
        }
        return true;
    }

}
