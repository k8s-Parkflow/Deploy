package com.mobs1.autoE.domain.zone.validator;

import com.mobs1.autoE.global.apiResponse.code.ErrorCode;
import com.mobs1.autoE.global.apiResponse.exception.BusinessException;
import java.util.regex.Pattern;

public final class VehicleNumberValidator {

    private static final Pattern VEHICLE_NUM_PATTERN = Pattern.compile("^[0-9]{2,3}[가-힣][0-9]{4}$");
    private static final Pattern NORMALIZATION_PATTERN = Pattern.compile("[\\s-]+");

    private VehicleNumberValidator() {
    }

    public static void validate(String vehicleNum) {
        if (!VEHICLE_NUM_PATTERN.matcher(vehicleNum).matches()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
    }

    private static String normalize(String vehicleNum) {
        return NORMALIZATION_PATTERN.matcher(vehicleNum).replaceAll("");
    }

    public static String normalizeAndValidate(String vehicleNum) {
        String normalizedVehicleNum = normalize(vehicleNum);
        validate(normalizedVehicleNum);
        return normalizedVehicleNum;
    }
}
