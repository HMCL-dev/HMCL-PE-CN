package com.tungsten.hmclpe.task;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.DownloadListener;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.tungsten.hmclpe.launcher.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.ref.WeakReference;

/**
 * @author ShirosakiMio
 */
public class LanzouUrlGetTask extends AsyncTask<String, Integer, String> {

    public interface Callback{
        void onStart();
        void onFinish(String url);
    }

    private WeakReference<Activity> activity;
    private Callback callback;
    private String fianalUrl=null;
    private WebView web;

    public LanzouUrlGetTask(Activity activity, Callback callback) {
        this.activity = new WeakReference<>(activity);
        this.callback = callback;
    }

    @Override
    public void onPreExecute() {
        activity.get().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.onStart();
            }
        });
    }

    @Override
    public String doInBackground(String... args) {
        try {
            String url = args[0];
            Document doc= null;
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36")
                    .get();
            Elements elements = doc.getElementsByClass("ifr2");
            for (Element element:elements){
                url=url.substring(0,url.indexOf(".com")+4)+element.attr("src");
                WebClient webClient=new WebClient(BrowserVersion.CHROME);
                HtmlPage page;
                page=webClient.getPage(url);
                webClient.getOptions().setJavaScriptEnabled(true);
                webClient.getOptions().setCssEnabled(false);
                webClient.getOptions().setRedirectEnabled(true);
                webClient.getOptions().setThrowExceptionOnScriptError(false);
                webClient.getOptions().setTimeout(50000);
                webClient.waitForBackgroundJavaScript(5*1000);
                for (int i = 0; i < 20; i++) {
                    if (page != null) {
//                        System.out.println("等待ajax执行完毕");
                        break;
                    }
                    synchronized (page) {
                        page.wait(500);
                    }
                }
                doc=Jsoup.parse(page.asXml());
                Elements elements1 = doc.getElementsByTag("a");
                url=elements1.get(0).attr("href");
                String finalUrl = url;
                activity.get().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        web=new WebView(activity.get());
                        web.getSettings().setJavaScriptEnabled(true);
                        web.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0");
                        web.setWebViewClient(new WebViewClient(){
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                                // 限制使用内置浏览器
                                view.loadUrl(request.getUrl().toString());
                                return true;
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);
                            }
                        });
                        web.setDownloadListener(new DownloadListener(){
                            @Override
                            public void onDownloadStart(String url, String userAgent, String contentDisposition,String mimetype, long contentLength) {
                                fianalUrl=url;
                                web.clearCache(true);
                                web.clearCache(true);
                                web=null;
                            }
                        });
                        web.loadUrl(finalUrl);
                    }
                });
                while (fianalUrl==null){

                }
                webClient.closeAllWindows();
                return fianalUrl;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("测试",e.toString());
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... p1) {

    }

    @Override
    public void onPostExecute(String result) {
        activity.get().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.onFinish(result);
            }
        });
    }

}
