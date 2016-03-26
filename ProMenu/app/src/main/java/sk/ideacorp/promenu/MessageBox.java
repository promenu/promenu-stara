package sk.ideacorp.promenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.DialogPreference;

/**
 * Created by Lukas Knotek on 26. 3. 2016.
 */
public class MessageBox {

    private Activity activity;

    private String title = "";

    private String message = "";

    public MessageBox(Activity activity)
    {
        this.activity = activity;
    }

    public MessageBox(Activity activity, String title, String message)
    {
        this.activity = activity;

        this.title = title;

        this.message = message;
    }

    public void SetTitle(String title)
    {
        this.title = title;
    }

    public void SetMessage(String message)
    {
        this.message = message;
    }

    public void Show()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.activity);

        dialog.setTitle(this.title);
        dialog.setMessage(this.message);
        dialog.setCancelable(false);
        dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Whatever...
            }
        }).create().show();
    }

    public void ShowSettings()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.activity);

        dialog.setTitle(this.title);
        dialog.setMessage(this.message);
        dialog.setCancelable(false);
        dialog.setNeutralButton("Zapnúť GPS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MessageBox.this.activity.startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
            }
        });

        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Whatever...
            }
        }).create().show();
    }
}
