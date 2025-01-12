package org.automyjsa.automyjsa.external.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.stardust.app.GlobalAppContext;
import com.stardust.automyjsa.execution.ExecutionConfig;

import org.automyjsa.automyjsa.automyjsa.Automyjsa;
import org.automyjsa.automyjsa.model.script.ScriptFile;
import org.automyjsa.automyjsa.timing.IntentTask;
import org.automyjsa.automyjsa.timing.TimedTaskManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BaseBroadcastReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = "BaseBroadcastReceiver";

    @SuppressLint("CheckResult")
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive: intent = " + intent + ", this = " + this);
        try {
            TimedTaskManager.INSTANCE.getIntentTaskOfAction(intent.getAction())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(intentTask -> runTask(context, intent, intentTask), Throwable::printStackTrace);
        } catch (Exception e) {
            GlobalAppContext.toast(e.getMessage());
        }
    }

    static void runTask(Context context, Intent intent, IntentTask task) {
        Log.d(LOG_TAG, "runTask: action = " + intent.getAction() + ", script = " + task.getScriptPath());
        ScriptFile file = new ScriptFile(task.getScriptPath());
        ExecutionConfig config = new ExecutionConfig();
        config.setArgument("intent", intent.clone());
        config.setWorkingDirectory(file.getParent());
        try {
            Automyjsa.getInstance().getScriptEngineService().execute(file.toSource(), config);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
