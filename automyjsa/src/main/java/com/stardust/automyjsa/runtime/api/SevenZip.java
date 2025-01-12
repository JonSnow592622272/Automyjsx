package com.stardust.automyjsa.runtime.api;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.hzy.libp7zip.P7ZipApi;
import com.stardust.automyjsa.runtime.ScriptRuntime;
import com.stardust.automyjsa.runtime.exception.ScriptException;
import com.stardust.pio.PFiles;


public class SevenZip {
    private Context mContext;
    private ScriptRuntime mRuntime;

    public SevenZip() {

    }

    @JavascriptInterface
    public int cmdExec(String cmdStr) {
        try {
            return P7ZipApi.executeCommand(cmdStr);
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    @JavascriptInterface
    public int A(String type, String destFilePath, String srcPath) {
        String typeOption = "";
        if (!type.trim().isEmpty()) {
            typeOption = " -t" + type.trim();
        }
        String cmdStr = "7z";
        if (PFiles.isFile(srcPath)) {
            cmdStr = "7z a -y" + typeOption + " -ms=off -mx=1 -mmt " + destFilePath + " " + srcPath;
        } else if (PFiles.isDir(srcPath)) {
            cmdStr = "7z a -y" + typeOption + " -ms=off -mx=1 -mmt -r " + destFilePath + " " + srcPath;
        }
        try {
            return P7ZipApi.executeCommand(cmdStr);
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    @JavascriptInterface
    public int A(String type, String destFilePath, String srcPath, String password) {
        String typeOption = "";
        if (!type.trim().isEmpty()) {
            typeOption = " -t" + type.trim();
        }
        String cmdStr = "7z";
        if (PFiles.isFile(srcPath)) {
            cmdStr = "7z a -y" + typeOption + " -ms=off -mx=1 -mmt -p" + password + " " + destFilePath + " " + srcPath;
        } else if (PFiles.isDir(srcPath)) {
            cmdStr = "7z a -y" + typeOption + " -ms=off -mx=1 -mmt -r -p" + password + " " + destFilePath + " " + srcPath;
        }
        try {
            return P7ZipApi.executeCommand(cmdStr);
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    @JavascriptInterface
    public int X(String filePath0, String dirPath1) {
        String cmdStr = "7z x -y -aos " + filePath0;
        if (PFiles.isFile(filePath0)) {
            if (PFiles.isDir(dirPath1)) {
                cmdStr = "7z x -y -aos -o" + dirPath1 + " " + filePath0 + "";
            } else {
                cmdStr = "7z x -y -aos " + filePath0 + "";
            }
        }
        try {
            return P7ZipApi.executeCommand(cmdStr);
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    @JavascriptInterface
    public int X(String filePath0, String dirPath1, String password) {
        String cmdStr = "7z x -y -aos " + filePath0 + "";
        if (password == "") {
            X(filePath0, dirPath1);
        } else {
            if (PFiles.isFile(filePath0)) {
                if (PFiles.isDir(dirPath1)) {
                    cmdStr = "7z x -y -aos -p" + password + " -o" + dirPath1 + " " + filePath0;
                } else {
                    cmdStr = "7z x -y -aos -p" + password + " " + filePath0;
                }
            }
        }
        try {
            return P7ZipApi.executeCommand(cmdStr);
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }
}