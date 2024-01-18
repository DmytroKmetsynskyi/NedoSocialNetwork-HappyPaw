package com.fernfog.happypaw;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;

public class DateAxisValueFormatter extends ValueFormatter {
    private final List<String> labels;

    public DateAxisValueFormatter(List<String> labels) {
        this.labels = labels;
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        int index = (int) value;

        if (index >= 0 && index < labels.size()) {
            return labels.get(index);
        } else {
            return "";
        }
    }
}


