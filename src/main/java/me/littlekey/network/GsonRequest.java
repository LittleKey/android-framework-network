package me.littlekey.network;

import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import com.android.volley.Response;

/**
 *
 * Created by nengxiangzhou on 15/5/8.
 */
public class GsonRequest<T> extends ApiRequest<T> {
  private static final Gson GSON = new Gson();
  private final Class<T> mClass;

  public GsonRequest(ApiContext apiContext, int method, String url, Class<T> clazz,
                     Response.Listener<T> listener, Response.ErrorListener errorListener) {
    super(apiContext, method, url, listener, errorListener);
    this.mClass = clazz;
  }

  @Override
  protected T parseResponse(NetworkResponse response) throws Exception {
    String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
    return GSON.fromJson(json, mClass);
  }
}
