package com.dhis2.data.forms.dataentry.fields.datetime;

import android.support.annotation.NonNull;

import com.dhis2.data.forms.dataentry.fields.FieldViewModel;
import com.google.auto.value.AutoValue;

import org.hisp.dhis.android.core.common.ValueType;

/**
 * Created by frodriguez on 1/24/2018.
 */

@AutoValue
public abstract class DateTimeViewModel extends FieldViewModel {

    @NonNull
    public abstract ValueType valueType();

    public static FieldViewModel create(String id, String label, Boolean mandatory, ValueType type, String value,String section, Boolean allowFutureDates) {
        return new AutoValue_DateTimeViewModel(id, label, mandatory, value,section, allowFutureDates,true, type);
    }
}
