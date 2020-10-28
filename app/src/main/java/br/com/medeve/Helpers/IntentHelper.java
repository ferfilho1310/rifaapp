package br.com.medeve.Helpers;

import android.app.Activity;
import android.content.Intent;

import br.com.medeve.Interfaces.IIntent;

public class IntentHelper implements IIntent {

    private static IntentHelper intentHelper;

    public static synchronized IntentHelper getInstance() {
        if (intentHelper == null) {
            intentHelper = new IntentHelper();
        }
        return intentHelper;
    }

    @Override
    public void intentWithFinish(Activity activity,Class clazz) {
        Intent intent = new Intent(activity,clazz);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public void intentWithOutFinish(Activity activity, Class clazz) {
        Intent intent = new Intent(activity, clazz);
        activity.startActivity(intent);
    }

    @Override
    public void intentWithFlags(Activity activity, Class clazz) {
        Intent intent = new Intent(activity, clazz);
        activity.startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}
