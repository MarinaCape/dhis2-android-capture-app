package com.dhis2.data.forms.dataentry;

import android.content.Context;
import android.support.annotation.NonNull;

import com.dhis2.R;
import com.dhis2.data.dagger.PerFragment;
import com.dhis2.data.forms.FormRepository;
import com.dhis2.data.forms.dataentry.fields.FieldViewModelFactory;
import com.dhis2.data.forms.dataentry.fields.FieldViewModelFactoryImpl;
import com.dhis2.data.schedulers.SchedulerProvider;
import com.dhis2.utils.CodeGenerator;
import com.squareup.sqlbrite2.BriteDatabase;


import org.hisp.dhis.android.core.D2;

import dagger.Module;
import dagger.Provides;

import static android.text.TextUtils.isEmpty;


@PerFragment
@Module(includes = DataEntryStoreModule.class)
public class DataEntryModule {

    @NonNull
    private final FieldViewModelFactory modelFactory;

    @NonNull
    private final DataEntryArguments arguments;

    DataEntryModule(@NonNull Context context, @NonNull DataEntryArguments arguments) {
        this.arguments = arguments;
        this.modelFactory = new FieldViewModelFactoryImpl(
                context.getString(R.string.enter_text),
                context.getString(R.string.enter_long_text),
                context.getString(R.string.enter_number),
                context.getString(R.string.enter_integer),
                context.getString(R.string.enter_positive_integer),
                context.getString(R.string.enter_negative_integer),
                context.getString(R.string.enter_positive_integer_or_zero),
                context.getString(R.string.filter_options),
                context.getString(R.string.choose_date));
    }

    @Provides
    @PerFragment
    RuleEngineRepository ruleEngineRepository(@NonNull BriteDatabase briteDatabase,
            @NonNull FormRepository formRepository) {
        if (!isEmpty(arguments.event())) { // NOPMD
            return new EventsRuleEngineRepository(briteDatabase,
                    formRepository, arguments.event());
        } else if (!isEmpty(arguments.enrollment())) { //NOPMD
            return new EnrollmentRuleEngineRepository(briteDatabase,
                    formRepository, arguments.enrollment());
        } else {
            throw new IllegalArgumentException("Unsupported entity type");
        }
    }

    @Provides
    @PerFragment
    DataEntryPresenter dataEntryPresenter(
            @NonNull CodeGenerator codeGenerator,
            @NonNull SchedulerProvider schedulerProvider,
            @NonNull DataEntryStore dataEntryStore,
            @NonNull DataEntryRepository dataEntryRepository,
            @NonNull RuleEngineRepository ruleEngineRepository) {
        return new DataEntryPresenterImpl(codeGenerator, dataEntryStore,
                dataEntryRepository, ruleEngineRepository, schedulerProvider);
    }

    @Provides
    @PerFragment
    DataEntryRepository dataEntryRepository(@NonNull BriteDatabase briteDatabase,@NonNull D2 d2) {
        if (!isEmpty(arguments.event())) { // NOPMD
            return new ProgramStageRepository(briteDatabase, modelFactory,
                    arguments.event(), arguments.section());
        } else if (!isEmpty(arguments.enrollment())) { //NOPMD
            return new EnrollmentRepository(briteDatabase, modelFactory, arguments.enrollment(),d2);
        } else {
            throw new IllegalArgumentException("Unsupported entity type");
        }
    }
}
