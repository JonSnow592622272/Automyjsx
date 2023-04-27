package org.automyjsa.automyjsa.external.tile;

import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.stardust.app.GlobalAppContext;
import com.stardust.view.accessibility.AccessibilityService;
import com.stardust.view.accessibility.LayoutInspector;
import com.stardust.view.accessibility.NodeInfo;

import org.automyjsa.automyjsx.R;
import org.automyjsa.automyjsa.automyjsa.Automyjsa;
import org.automyjsa.automyjsa.tool.AccessibilityServiceTool;
import org.automyjsa.automyjsa.ui.floating.FloatyWindowManger;
import org.automyjsa.automyjsa.ui.floating.FullScreenFloatyWindow;

@RequiresApi(api = Build.VERSION_CODES.N)
public abstract class LayoutInspectTileService extends TileService implements LayoutInspector.CaptureAvailableListener {

    private boolean mCapturing = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(getClass().getName(), "onCreate");
        Automyjsa.getInstance().getLayoutInspector().addCaptureAvailableListener(this);
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        Log.d(getClass().getName(), "onStartListening");
        inactive();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(getClass().getName(), "onDestroy");
        Automyjsa.getInstance().getLayoutInspector().removeCaptureAvailableListener(this);
    }

    @Override
    public void onClick() {
        super.onClick();
        Log.d(getClass().getName(), "onClick");
        sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        if (AccessibilityService.Companion.getInstance() == null) {
            Toast.makeText(this, R.string.text_no_accessibility_permission_to_capture, Toast.LENGTH_SHORT).show();
            AccessibilityServiceTool.goToAccessibilitySetting();
            inactive();
            return;
        }
        mCapturing = true;
        GlobalAppContext.postDelayed(() ->
                        Automyjsa.getInstance().getLayoutInspector().captureCurrentWindow()
                , 1000);
    }

    protected void inactive() {
        Tile qsTile = getQsTile();
        if (qsTile == null)
            return;
        qsTile.setState(Tile.STATE_INACTIVE);
        qsTile.updateTile();
    }

    @Override
    public void onCaptureAvailable(NodeInfo capture) {
        Log.d(getClass().getName(), "onCaptureAvailable: capturing = " + mCapturing);
        if (!mCapturing) {
            return;
        }
        mCapturing = false;
        GlobalAppContext.post(() -> {
            FullScreenFloatyWindow window = onCreateWindow(capture);
            if (!FloatyWindowManger.addWindow(getApplicationContext(), window)) {
                inactive();
            }
        });

    }

    protected abstract FullScreenFloatyWindow onCreateWindow(NodeInfo capture);
}