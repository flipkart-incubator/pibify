package com.flipkart.pibify.codegen.log;

import java.lang.reflect.Field;

/**
 * This class is used for holding logs during Code Spec gen for a field
 * Author bageshwar.pn
 * Date 13/09/24
 */
public class FieldSpecGenLog extends SpecGenLog {
    private final String fieldName;

    public FieldSpecGenLog(String fieldName, SpecGenLogLevel logLevel, String logMessage) {
        super(logLevel, logMessage);
        this.fieldName = fieldName;
    }

    public FieldSpecGenLog(Field field, SpecGenLogLevel logLevel, String logMessage) {
        this(field.getName(), logLevel, logMessage);
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String getLogMessage() {
        return super.getLogMessage() + " for " + fieldName;
    }
}
