package com.google.zxing.client.android.camera.helper;

import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;

import com.google.zxing.client.android.helper.CodeManager;

@SuppressWarnings("deprecation") // camera APIs
public final class PreviewCallback implements Camera.PreviewCallback {
	
	private static final String TAG = PreviewCallback.class.getSimpleName();
	
	private final CameraConfigurationManager configManager;
	
	private Handler previewHandler;
	
	private int previewMessage;
	
	public PreviewCallback(CameraConfigurationManager configManager) {
		this.configManager = configManager;
	}
	
	public void setHandler(Handler previewHandler, int previewMessage) {
		this.previewHandler = previewHandler;
		this.previewMessage = previewMessage;
	}
	
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		Point cameraResolution = configManager.getCameraResolution();
		Handler thePreviewHandler = previewHandler;
		if (cameraResolution != null && thePreviewHandler != null) {
			Message message = thePreviewHandler.obtainMessage(previewMessage, cameraResolution.x,
					cameraResolution.y, data);
			message.sendToTarget();
			previewHandler = null;
		} else {
			CodeManager.v(TAG, "Got preview callback, but no handler or resolution available");
		}
	}
	
}
