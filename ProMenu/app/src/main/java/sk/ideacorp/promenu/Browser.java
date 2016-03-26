package sk.ideacorp.promenu;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.MailTo;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by Lukas Knotek on 26. 3. 2016.
 */
public class Browser {

    private WebView browser;

    private Activity activity;

    private ProgressBar progress_bar;

    private String url = "";

    public Browser(WebView browser, Activity activity, ProgressBar progress_bar)
    {
        this.browser = browser;

        this.browser.getSettings().setJavaScriptEnabled(true);
        this.browser.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //this.browser.addJavascriptInterface(new JavaScriptInterface(this.activity), "JSInterface");

        /*this.browser.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN || keyCode == KeyEvent.KEYCODE_ENTER) {
                    View view = Browser.this.activity.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)Browser.this.activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }

                return true;
            }
        });*/

        this.activity = activity;

        this.progress_bar = progress_bar;
    }

    public void set_url(String url)
    {
        this.url = url;
    }

    public String get_url()
    {
        return this.browser.getUrl();
    }

    public void load()
    {
        this.browser.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        if (!this.isNetworkAvailable()) { // loading offline
            this.browser.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        this.browser.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        this.browser.loadUrl(this.url);

        this.browser.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    if (url.indexOf("tel:") > -1) {
                        Browser.this.activity.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url)));

                        return true;
                    } else if(url.indexOf("mailto:") > -1) {
                        MailTo mt= MailTo.parse(url);

                        send_email(mt.getTo());

                        return true;
                    } else if(url.indexOf("web:") > -1) {
                        //Browser.this.activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                        return true;
                    } else {
                        view.loadUrl(url);

                        return false;
                    }
                } catch(Exception e) {
                    //view.loadUrl(url);

                    return false;
                }
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                if(Browser.this.progress_bar != null)
                {
                    Browser.this.progress_bar.setVisibility(View.GONE);
                }
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                try {
                    view.stopLoading();
                } catch (Exception e) {
                }
                try {
                    view.clearView();
                } catch (Exception e) {
                }
                if (view.canGoBack()) {
                    //view.goBack();
                }

                //view.loadUrl(android.resource://" + getPackageName() + "/" + "/res/html/404.html");
                view.loadData("<p>&nbsp;</p><p style='text-align: center; color: #1395a0;'></p><p style='text-align: center; font-size: 24px; color: #1395a0; padding-top: 25px;'>Už ti škŕka v bruchu?<br />Rýchlo zapni internet a my ti naservírujeme reštaurácie v okolí.</p>", "text/html; charset=utf-8", "UTF-8");
                //super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if(Browser.this.progress_bar != null) {
                    Browser.this.progress_bar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void blank()
    {

        this.browser.loadData("", "", "");
    }

    public void setHTML(String html)
    {

        this.browser.loadData(html, "text/html", "utf-8");

        if(Browser.this.progress_bar != null)
        {
            Browser.this.progress_bar.setVisibility(View.GONE);
        }
    }

    public void clear()
    {
        this.browser.clearCache(true);

        this.browser.clearHistory();
    }

    public boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager)this.activity.getSystemService(this.activity.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void send_email(String email_add) {
        System.out.println("Email address::::" + email_add);

        final Intent emailIntent = new Intent(
                android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[] { email_add });
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        Browser.this.activity.startActivity(
                Intent.createChooser(emailIntent, "Send mail..."));

    }
}
