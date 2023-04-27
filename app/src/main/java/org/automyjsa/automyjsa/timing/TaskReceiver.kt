package org.automyjsa.automyjsa.timing

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import org.automyjsa.automyjsa.automyjsa.Automyjsa
import org.automyjsa.automyjsa.external.ScriptIntents

/**
 * Created by Stardust on 2017/11/27.
 */
class TaskReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(LOG_TAG, "receive intent:" + intent.action)
        Log.d(
            LOG_TAG, "taskInfo [id=" + intent.getLongExtra(EXTRA_TASK_ID, -1)
                    + ", path=" + intent.getStringExtra(ScriptIntents.EXTRA_KEY_PATH)
                    + "]"
        )
        Automyjsa.getInstance().debugInfo("receive intent:" + intent.action)
        ScriptIntents.handleIntent(context, intent)
        val id = intent.getLongExtra(EXTRA_TASK_ID, -1)
        if (id >= 0) {
            TimedTaskManager.notifyTaskFinished(id)
        }
    }

    companion object {
        const val ACTION_TASK = "org.automyjsa.automyjsa.action.task"
        const val EXTRA_TASK_ID = "task_id"
        private const val LOG_TAG = "TaskReceiver"
    }
}