package com.van.logging.log4j2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {

    public static final String ENV_NAME_PATTERN ="\\$\\{([a-zA-Z0-9_\\-^}]+)\\}";

    /*public static void main(String[] args) {

        Pattern p = Pattern.compile(ENV_NAME_PATTERN);

            String[] inputs = {"${tempVar123}","${temp_Var_123}","${temp-var>-123}"};

        for (String input : inputs) {
            Matcher matcher = p.matcher(input);
            if (matcher.matches()) {
                System.out.println(matcher.group(1));
            }
        }
    }*/
}
