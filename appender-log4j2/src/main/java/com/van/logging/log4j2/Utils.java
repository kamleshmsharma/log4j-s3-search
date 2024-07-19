package com.van.logging.log4j2;

import com.van.logging.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String maskSensitiveData(String data) {
        if (data == null) {
            return null;
        } else {
            int lengthOfPassword = data.length();
            if (lengthOfPassword < 3) {
                return "****";
            } else {
                StringBuilder stringBuilder = new StringBuilder("****");
                stringBuilder.append(data.substring(Math.max(0, data.length() - 2)));
                return stringBuilder.toString();
            }
        }
    }

    public static String parseInputValue(String input) {
        if (input != null) {
            input = input.trim();
            Pattern p = Pattern.compile(Constants.ENV_NAME_PATTERN);
            Matcher matcher = p.matcher(input);
            if (matcher.matches()) {
                String envVarName = matcher.group(1);
                if (StringUtils.isTruthy(System.getenv(envVarName))) {
                    return System.getenv(envVarName);
                } else if (StringUtils.isTruthy(System.getProperty(envVarName))) {
                    return System.getProperty(envVarName);
                } else {
                    throw new RuntimeException(String.format("No env-var or system property named '%s' found.", envVarName));
                }
            } else {
                return input;
            }
        } else {
            return input;
        }
    }
}
