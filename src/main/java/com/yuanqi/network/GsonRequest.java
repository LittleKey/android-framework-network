package com.yuanqi.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

/**
 *
 * Created by nengxiangzhou on 15/5/8.
 */
public class GsonRequest<T> extends ApiRequest<T> {
  private static final Gson GSON = new Gson();
  private final Class<T> mClass;

  public GsonRequest(ApiContext apiContext, int method, String url, Class<T> clazz,
                     Listener<T> listener, ErrorListener errorListener) {
    super(apiContext, method, url, listener, errorListener);
    this.mClass = clazz;
  }

  @Override
  protected T parseResponse(NetworkResponse response) throws Exception {
    String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
    return GSON.fromJson(json, mClass);
  }
}
