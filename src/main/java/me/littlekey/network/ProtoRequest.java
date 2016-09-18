package me.littlekey.network;

import com.android.volley.NetworkResponse;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;

import com.android.volley.Response;

/**
 *
 * Created by nengxiangzhou on 15/5/8.
 */
public class ProtoRequest<T extends Message> extends ApiRequest<T> {
  private final Class<T> mClass;

  public ProtoRequest(ApiContext apiContext, int method, String url, Class<T> clazz,
                      Response.Listener<T> listener, Response.ErrorListener errorListener) {
    super(apiContext, method, url, listener, errorListener);
    this.mClass = clazz;
  }

  @Override
  protected T parseResponse(NetworkResponse response) throws Exception {
    return ProtoAdapter.get(mClass).decode(response.data);
  }
}
