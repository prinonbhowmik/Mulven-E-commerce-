package com.hydertechno.mulven.Interface;

import android.webkit.JavascriptInterface;

public class WebAppInterface {
    WebMsgInterface msgInterface;

    /** Instantiate the interface and set the context */
    public WebAppInterface(WebMsgInterface listener) {
        msgInterface = listener;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void getMessage(String message) {
        msgInterface.onMessage(message);
    }
}
