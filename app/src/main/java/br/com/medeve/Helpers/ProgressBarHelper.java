package br.com.medeve.Helpers;

import android.app.ProgressDialog;
import android.content.Context;

import br.com.medeve.Util.MensagemSistema;

public class ProgressBarHelper extends ProgressDialog {

    public ProgressBarHelper(Context context) {
        super(context);
        setMessage(MensagemSistema.carregando);
        show();
    }

    public ProgressBarHelper(Context context, int theme) {
        super(context, theme);
    }
}
