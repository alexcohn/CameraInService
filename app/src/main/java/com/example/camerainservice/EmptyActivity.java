package com.example.camerainservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class EmptyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startService(new Intent(this, CameraService.class));
	}
}
