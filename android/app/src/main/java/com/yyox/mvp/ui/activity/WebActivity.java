package com.yyox.mvp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.umeng.message.PushAgent;
import com.yyox.R;

public class WebActivity extends AppCompatActivity {

    private WebView webView;
    private TextView tvTitle;

    private String js;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        String strShowUrl = getIntent().getStringExtra("showurl");
        if (strShowUrl == null || strShowUrl.isEmpty()) {
            strShowUrl = "http://www.amazon.com";
        }

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(strShowUrl);
        //友盟设置
        PushAgent.getInstance(this).onAppStart();

        js = "function setValue(){\n" +
                "\t\tcountry=document.getElementById(\"address-ui-widgets-countryCode-dropdown-nativeId\");  \n" +
                "\t\tfor(var i=0; i<country.options.length; i++){\n" +
                "\t\t\tif(country.options[i].innerHTML == 'China'){\n" +
                "\t\t\t\tcountry.options[i].selected = true;\n" +
                "\t\t\t\tbreak;\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t\tcountry.addEventListener('onchange',function(){\n" +
                "\t\t\talert(\"123\");\n" +
                "\t\t});\n" +
                "\t\tdocument.getElementById(\"address-ui-widgets-enterAddressFullName\").value = \"name\";\n" +
                "\t\tdocument.getElementById(\"address-ui-widgets-enterAddressLine1\").value=\"street\";\n" +
                "\t\tdocument.getElementById(\"address-ui-widgets-enterAddressLine2\").value=\"apartment\";\n" +
                "\t\tdocument.getElementById(\"address-ui-widgets-enterAddressCity\").value=\"city\";\n" +
                "\t\tdocument.getElementById(\"address-ui-widgets-enterAddressStateOrRegion\").value=\"state\";\n" +
                "\t\tdocument.getElementById(\"address-ui-widgets-enterAddressPostalCode\").value=\"100000\";\n" +
                "\t\tdocument.getElementById(\"address-ui-widgets-enterAddressPhoneNumber\").value=\"13455556666\";\n" +
                "\t}";


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.indexOf("intent") != -1) {
                    return true;
                } else {
                    view.loadUrl(url);
                    return true;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                view.loadUrl("javascript:" + js);
//                view.loadUrl("javascript:" + "setValue()");
                super.onPageFinished(view, url);
            }
        });

        tvTitle = (TextView) findViewById(R.id.buy_web_activity_title);
        String strTitle = getIntent().getStringExtra("title");
        if (strShowUrl != null && !strShowUrl.isEmpty()) {
            tvTitle.setText(strTitle);
        }

    }

    public void btn_back_click(View v) {
        this.finish();
    }
}
