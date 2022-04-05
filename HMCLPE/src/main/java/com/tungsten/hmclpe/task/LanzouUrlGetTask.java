package com.tungsten.hmclpe.task;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.tungsten.hmclpe.launcher.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.ref.WeakReference;

public class LanzouUrlGetTask extends AsyncTask<String, Integer, String> {

    public interface Callback{
        void onStart();
        void onFinish(String url);
    }

    private WeakReference<Context> ctx;
    private MainActivity activity;
    private Callback callback;

    public LanzouUrlGetTask(Context ctx, MainActivity activity, Callback callback) {
        this.ctx = new WeakReference<>(ctx);
        this.activity = activity;
        this.callback = callback;
    }

    @Override
    public void onPreExecute() {
        activity.runOnUiThread(new Runnable() {
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
                WebClient webClient=new WebClient();
                HtmlPage page=webClient.getPage(url);
                webClient.getOptions().setJavaScriptEnabled(true);
                webClient.getOptions().setCssEnabled(false);
                webClient.getOptions().setRedirectEnabled(true);
                webClient.getOptions().setThrowExceptionOnScriptError(false);
                webClient.getOptions().setTimeout(50000);
                webClient.waitForBackgroundJavaScript(5*1000);
                doc=Jsoup.parse(page.asXml());
                Elements elements1 = doc.getElementsByTag("a");
                return elements1.get(0).attr("href");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... p1) {

    }

    @Override
    public void onPostExecute(String result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.onFinish(result);
            }
        });
    }

}
