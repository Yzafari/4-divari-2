package ir.khanehbekhaneh.app;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.ValueCallback;
import android.content.ActivityNotFoundException;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class MainActivity extends Activity {
    private static final int LOCATION_REQ = 1001;
    private static final int FILE_CHOOSER_REQ = 1002;
    private ValueCallback<Uri[]> filePathCallback;
    private WebView webView;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.rgb(5,5,5));
        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        controller.setAppearanceLightStatusBars(false);
        controller.setAppearanceLightNavigationBars(false);
        createNotificationChannel();
        webView = new WebView(this);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setGeolocationEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri u = request.getUrl();
                if ("http".equalsIgnoreCase(u.getScheme()) || "https".equalsIgnoreCase(u.getScheme())) {
                    if (u.getHost() != null && (u.getHost().equals("localhost") || u.getHost().endsWith("khanehbekhaneh.ir"))) return false;
                    startActivity(new Intent(Intent.ACTION_VIEW, u)); return true;
                }
                return false;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> callback, FileChooserParams params) {
                if (filePathCallback != null) filePathCallback.onReceiveValue(null);
                filePathCallback = callback;
                Intent intent = params.createIntent();
                try { startActivityForResult(intent, FILE_CHOOSER_REQ); }
                catch (ActivityNotFoundException ex) { filePathCallback = null; callback.onReceiveValue(null); return false; }
                return true;
            }
            @Override public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) callback.invoke(origin, true, false);
                else { pendingOrigin = origin; pendingCallback = callback; requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQ); }
            }
        });
        // Android 15+ may render the activity edge-to-edge. Apply the real system-bar
        // insets to the WebView so the HTML header/menu never hides behind the status bar.
        ViewCompat.setOnApplyWindowInsetsListener(webView, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, bars.top, 0, bars.bottom);
            return insets;
        });
        ViewCompat.requestApplyInsets(webView);
        setContentView(webView);
        webView.loadUrl("file:///android_asset/www/index.html");
        webView.postDelayed(() -> webView.evaluateJavascript("window.KHB_API_BASE=" + quote(BuildConfig.KHB_API_BASE) + ";", null), 150);
    }

    private String pendingOrigin; private GeolocationPermissions.Callback pendingCallback;
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_REQ && filePathCallback != null) {
            Uri[] results = WebChromeClient.FileChooserParams.parseResult(resultCode, data);
            filePathCallback.onReceiveValue(results);
            filePathCallback = null;
        }
    }
    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);
        if (requestCode == LOCATION_REQ && pendingCallback != null) {
            boolean ok = false; for (int r : results) if (r == PackageManager.PERMISSION_GRANTED) ok = true;
            pendingCallback.invoke(pendingOrigin, ok, false); pendingOrigin = null; pendingCallback = null;
        }
    }
    private String quote(String v) { if (v == null) v = ""; return "'" + v.replace("\\", "\\\\").replace("'", "\\'") + "'"; }
    private void createNotificationChannel() { if (Build.VERSION.SDK_INT >= 26) { NotificationManager nm = getSystemService(NotificationManager.class); nm.createNotificationChannel(new NotificationChannel("khb_general", "۴ دیواری", NotificationManager.IMPORTANCE_DEFAULT)); } }
    @Override public void onBackPressed() { if (webView != null && webView.canGoBack()) webView.goBack(); else super.onBackPressed(); }
}
