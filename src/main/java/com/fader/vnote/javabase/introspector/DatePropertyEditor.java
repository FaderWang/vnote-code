package com.fader.vnote.javabase.introspector;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DatePropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {

        try {
            setValue(text == null ? null : new SimpleDateFormat("yyyy-MM-dd").parse(text));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
