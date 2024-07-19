package com.van.logging.log4j2;

import com.van.logging.IStorageDestinationAdjuster;
import com.van.logging.utils.StringUtils;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A path adjuster that assumes the original path is a template to expand using a limited-scoped implementation of
 * PatternLayout.
 * Currently, only the date (i.e. %d{...}) template is supported.
 * Additional support for pattern ${ENV_VAR_NAME} can be used to inject values from environment variables.
 */
public class PatternedPathAdjuster implements IStorageDestinationAdjuster {

    @Override
    public String adjustPath(String path) {
        String adjusted = path;
        if (StringUtils.isTruthy(adjusted)) {
            adjusted = adjustDate(adjusted);
            adjusted = adjustPathForEnvVars(adjusted);
        }
        return adjusted;
    }

    private String adjustPathForEnvVars(String path) {
        StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile(Constants.ENV_NAME_PATTERN);
        Matcher matcher = p.matcher(path);
        while (matcher.find()) {
            String envVarName = matcher.group(1);
            String value = Utils.parseInputValue(envVarName);
            matcher.appendReplacement(sb, value);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String adjustDate(String path) {
        String adjusted = path;
        if (StringUtils.isTruthy(path)) {
            // e.g. "%d{yyyy-MM-dd}"
            StringBuilder sb = new StringBuilder();
            PatternLayout layout = PatternLayout.newBuilder().withPattern(path).build();
            MutableLogEvent evt = new MutableLogEvent();
            evt.setTimeMillis(System.currentTimeMillis());
            layout.serialize(evt, sb);
            adjusted = sb.toString();
        }
        return adjusted;
    }

    /*public static void main(String[] args) {
        PatternedPathAdjuster adjuster = new PatternedPathAdjuster();
        String testPath = "logs/logs_%d{yyyy_MM_dd_HH_mm_ss}.log";
        //String testPath = "logs/${SERVICE_NAME}/${ENV_VAR1}/logs_%d{yyyy_MM_dd_HH_mm_ss}.log";
        String adjusted = adjuster.adjustPath(testPath);
        System.out.println(adjusted);

        //System.out.println(parseInputValue("${SERVICE_NAME}"));
    }*/
}
