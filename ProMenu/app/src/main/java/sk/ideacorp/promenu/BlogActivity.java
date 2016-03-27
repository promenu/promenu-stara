package sk.ideacorp.promenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

public class BlogActivity extends AppCompatActivity {

    private Browser browser;

    private String url = "http://mobile.promenu.sk/blog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        WebView web_view = (WebView) findViewById(R.id.webViewMain);

        this.browser = new Browser(web_view, this, null);
        this.browser.set_url(this.url);
        this.browser.load();

        Toolbar about_toolbar = (Toolbar)findViewById(R.id.toolbar);
        about_toolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    BlogActivity.this.onBackPressed();
                }

                return true;
            }
        });

        ImageView back_button = (ImageView)findViewById(R.id.back_button);
        back_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    BlogActivity.this.onBackPressed();
                }

                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(BlogActivity.this.url.equals(BlogActivity.this.browser.get_url())) {
            super.onBackPressed();
        } else {
            //ProgressBar progress_bar = (ProgressBar)findViewById(R.id.blogProgressBar);
            //progress_bar.setVisibility(View.VISIBLE);

            BlogActivity.this.browser.set_url(BlogActivity.this.url);
            BlogActivity.this.browser.load();
        }

    }
}
