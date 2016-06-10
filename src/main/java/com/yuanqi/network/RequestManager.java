package com.yuanqi.network;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Map;

/**
 * Created by nengxiangzhou on 15/5/8.
 */
public class RequestManager {
  private final RequestQueue mRequestQueue;

  public RequestManager(Context context) {
    mRequestQueue = Volley.newRequestQueue(context);
  }

  public static String parseUrl(String baseUrl, Map<String, String> pairs) {
    String url = baseUrl;
    if (pairs!= null && pairs.size() > 0) {
      Uri.Builder builder = Uri.parse(url).buildUpon();
      for (Map.Entry<String, String> entry : pairs.entrySet()) {
        builder.appendQueryParameter(entry.getKey(), entry.getValue());
      }
      url = builder.build().toString();
    }
    return url;
  }

  public void submit(Request request) {
    mRequestQueue.add(request);
  }

  public void cancel(Object tag) {
    mRequestQueue.cancelAll(tag);
  }

  public Cache getCache() {
    return mRequestQueue.getCache();
  }

  public static class CacheConfig {
    private boolean preferLocalConfig;
    private long ttl;
    private long softTtl;

    public CacheConfig(boolean preferLocalConfig, long ttl, long softTtl) {
      this.preferLocalConfig = preferLocalConfig;
      this.ttl = ttl;
      this.softTtl = softTtl;
    }

    public boolean isPreferLocalConfig() {
      return preferLocalConfig;
    }

    public long getTtl() {
      return ttl;
    }

    public long getSoftTtl() {
      return softTtl;
    }
  }
}
