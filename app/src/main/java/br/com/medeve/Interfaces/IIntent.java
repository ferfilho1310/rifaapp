package br.com.medeve.Interfaces;

import android.app.Activity;

public interface IIntent {

    void intentWithFinish(Activity activity,Class clazz);

    void intentWithOutFinish(Activity activity,Class clazz);

    void intentWithFlags(Activity activity,Class clazz);

}
