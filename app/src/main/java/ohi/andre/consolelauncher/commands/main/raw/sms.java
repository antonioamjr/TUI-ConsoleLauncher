package ohi.andre.consolelauncher.commands.main.raw;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;

import java.util.List;

import ohi.andre.consolelauncher.LauncherActivity;
import ohi.andre.consolelauncher.R;
import ohi.andre.consolelauncher.commands.CommandAbstraction;
import ohi.andre.consolelauncher.commands.ExecutePack;
import ohi.andre.consolelauncher.commands.main.MainPack;
import ohi.andre.consolelauncher.commands.specific.RedirectCommand;
import ohi.andre.consolelauncher.tuils.Tuils;

/**
 * Created by francescoandreuzzi on 02/03/2017.
 */

public class sms extends RedirectCommand {

    @Override
    public String exec(ExecutePack pack) throws Exception {
        MainPack info = (MainPack) pack;
        if (ContextCompat.checkSelfPermission(info.context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) info.context, new String[]{Manifest.permission.READ_CONTACTS}, LauncherActivity.COMMAND_REQUEST_PERMISSION);
            return info.context.getString(R.string.output_waitingpermission);
        }

        beforeObjects.add(pack.get(String.class, 0));

        if(afterObjects.size() == 0) {
            info.redirectator.prepareRedirection(this);
        } else {
            return onRedirect(info);
        }

        return null;
    }

    @Override
    public int minArgs() {
        return 1;
    }

    @Override
    public int maxArgs() {
        return 1;
    }

    @Override
    public int[] argType() {
        return new int[] {CommandAbstraction.CONTACTNUMBER};
    }

    @Override
    public int priority() {
        return 3;
    }

    @Override
    public int helpRes() {
        return R.string.help_sms;
    }

    @Override
    public String onNotArgEnough(ExecutePack info, int nArgs) {
        MainPack pack = (MainPack) info;
        if (ContextCompat.checkSelfPermission(pack.context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) pack.context, new String[]{Manifest.permission.READ_CONTACTS}, LauncherActivity.COMMAND_REQUEST_PERMISSION);
            return pack.context.getString(R.string.output_waitingpermission);
        }

        List<String> contacts = pack.contacts.listNamesAndNumbers();
        Tuils.addPrefix(contacts, Tuils.DOUBLE_SPACE);
        Tuils.insertHeaders(contacts, false);
        return Tuils.toPlanString(contacts);
    }

    @Override
    public String onArgNotFound(ExecutePack pack) {
        MainPack info = (MainPack) pack;
        return info.res.getString(R.string.output_numbernotfound);
    }

    @Override
    public String[] parameters() {
        return null;
    }

    @Override
    public String onRedirect(ExecutePack pack) {
        MainPack info = (MainPack) pack;

        String number = (String) beforeObjects.get(0);
        String message = (String) afterObjects.get(0);
        if(message.length() == 0) {
            info.redirectator.cleanup();
            return info.res.getString(R.string.output_smsnotsent);
        }

        if (ContextCompat.checkSelfPermission(info.context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) info.context, new String[]{Manifest.permission.SEND_SMS}, LauncherActivity.COMMAND_REQUEST_PERMISSION);
            return info.context.getString(R.string.output_waitingpermission);
        }

        info.redirectator.cleanup();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, message, null, null);

            return info.res.getString(R.string.output_smssent);
        } catch (Exception ex) {
            return info.res.getString(R.string.output_error);
        }
    }

    @Override
    public int getHint() {
        return R.string.sms_hint;
    }

    @Override
    public boolean isWaitingPermission() {
        return beforeObjects.size() == 1 && afterObjects.size() == 1;
    }
}
