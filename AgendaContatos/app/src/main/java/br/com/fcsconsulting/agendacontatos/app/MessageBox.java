package br.com.fcsconsulting.agendacontatos.app;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by FCSantos on 25/01/2016.
 */
public class MessageBox {

    public static void ShowInfo(Context ctx, String title, String msg)
    {
        Show(ctx, title, msg, android.R.drawable.ic_dialog_info);
    }

    public static void ShowAlert(Context ctx, String title, String msg)
    {
        Show(ctx, title, msg, android.R.drawable.ic_dialog_alert);
    }

    public static void Show(Context ctx, String title, String msg)
    {
        Show(ctx, title, msg, 0);
    }

    public static void Show(Context ctx, String title, String msg, int iconId)
    {
        AlertDialog.Builder dlg = new AlertDialog.Builder(ctx);
        dlg.setIcon(iconId);
        dlg.setTitle(title);
        dlg.setMessage(msg);
        dlg.setNeutralButton("OK", null);
        dlg.show();
    }
}
