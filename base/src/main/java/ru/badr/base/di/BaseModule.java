package ru.badr.base.di;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.badr.base.SchedulersProvider;
import ru.badr.base.ui.dialog.DialogManager;
import ru.badr.base.ui.dialog.IDialogManager;
import ru.badr.base.util.json.SimpleDateSerializer;

/**
 * Created by abadretdinov
 * on 20.06.2018
 */
@Module
public class BaseModule {
    public static final String BASE_URL = "base_url";
    //todo add analytics

    @Singleton
    @Provides
    CallAdapter.Factory provideCallAdapterFactory(SchedulersProvider schedulersProvider) {
        return RxJava2CallAdapterFactory.createWithScheduler(schedulersProvider.io());
    }

    @Singleton
    @Provides
    Converter.Factory provideConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Singleton
    @Provides
    Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new SimpleDateSerializer(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())))
                .create();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(Converter.Factory converterFactory,
                             CallAdapter.Factory callFactory,
                             @Named(BASE_URL) String baseUrl) {
        return new Retrofit.Builder()
                .client(
                        new OkHttpClient.Builder().build()
                )
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(callFactory)
                .build();
    }

    @Provides
    IDialogManager provideDialogManager(@NonNull Context context) {
        return new DialogManager(context);
    }
}
