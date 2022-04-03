package imgui.example.android;

import android.app.NativeActivity;
import android.os.Bundle;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.view.KeyEvent;
import java.util.concurrent.LinkedBlockingQueue;

public class MainActivity extends NativeActivity{

	private InputMethodManager inputMethodManager;
	// Queue for the Unicode characters to be polled from native code (via pollUnicodeChar())
	private LinkedBlockingQueue<Integer> unicodeCharacterQueue = new LinkedBlockingQueue<>();

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	public void showSoftInput(){
		inputMethodManager.showSoftInput(getWindow().getDecorView(), 0);
	}

	public void hideSoftInput(){
		inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
	}

	// We assume dispatchKeyEvent() of the NativeActivity is actually called for every
	// KeyEvent and not consumed by any View before it reaches here
	@Override
	public boolean dispatchKeyEvent(KeyEvent event){
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			unicodeCharacterQueue.offer(Integer.valueOf(event.getUnicodeChar(event.getMetaState())));
		}
		return super.dispatchKeyEvent(event);
	}

	public int pollUnicodeChar(){
		Integer x = (Integer) unicodeCharacterQueue.poll();
		return x != null ? x.intValue() : 0;
	}

}
