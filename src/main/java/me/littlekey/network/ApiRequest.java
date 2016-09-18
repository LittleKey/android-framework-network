package me.littlekey.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nengxiangzhou on 15/6/19.
 */
public abstract class ApiRequest<T> extends Request<T> {
  private ApiContext mApiContext;
  private Response.Listener<T> mListener;
  private Response.ErrorListener mErrorListener;
  private boolean mEnableMultiResponse = false;
  private boolean mResponseDelivered = false;
  private Map<String, String> mHeaders = new HashMap<>();
  private Map<String, String> mParams;
  private ResponseProcessor<T> mProcessor;

  public ApiRequest(ApiContext apiContext, int method, String url, Response.Listener<T> listener,
      Response.ErrorListener errorListener) {
    super(method, url, null);
    this.mApiContext = apiContext;
    this.mListener = listener;
    this.mErrorListener = errorListener;
  }

  @Override
  public void markDelivered() {
    super.markDelivered();
    mResponseDelivered = true;
  }

  @Override
  public void deliverError(VolleyError error) {
    if (mErrorListener == null) {
      return;
    }
    mErrorListener.onErrorResponse(error);
  }

  @Override
  public Response.ErrorListener getErrorListener() {
    return mErrorListener;
  }

  @Override
  public void cancel() {
    super.cancel();
    unregister();
  }

  public void unregister() {
    mListener = null;
    mErrorListener = null;
    setTag(null);
  }

  @Override
  public boolean hasHadResponseDelivered() {
    return !mEnableMultiResponse && mResponseDelivered;
  }

  @Override
  protected void deliverResponse(T response) {
    if (mListener == null) {
      return;
    }
    mListener.onResponse(response);
  }

  public void setProcessor(ResponseProcessor<T> processor) {
    if (this.mProcessor == null) {
      this.mProcessor = processor;
    } else {
      throw new IllegalStateException("Already set processor.");
    }
  }

  protected final T processResponse(T response) throws Exception {
    return mProcessor != null ? mProcessor.generate(response) : response;
  }

  @Override
  protected final Response<T> parseNetworkResponse(NetworkResponse response) {
    try {
      T item = processResponse(parseResponse(response));
      return Response.success(item, parseCache(response));
    } catch (VolleyError e) {
      return Response.error(e);
    } catch (Exception e) {
      return Response.error(new ParseError(e));
    }
  }

  protected abstract T parseResponse(NetworkResponse response) throws Exception;

  public void setEnableMultiResponse(boolean enableMultiResponse) {
    this.mEnableMultiResponse = enableMultiResponse;
  }

  protected Cache.Entry parseCache(NetworkResponse response) {
    return HttpHeaderParser.parseCacheHeaders(response);
  }

  @Override
  public Map<String, String> getHeaders() throws AuthFailureError {
    return mHeaders;
  }

  @Override
  protected Map<String, String> getParams() throws AuthFailureError {
    return mParams;
  }

  public void setHeaders(Map<String, String> headers) {
    this.mHeaders = headers;
  }

  public void setParams(Map<String, String> params) {
    this.mParams = params;
  }

  public void submit() {
    mApiContext.getRequestManager().submit(this);
  }

  public Cache getCache() {
    return mApiContext.getRequestManager().getCache();
  }
}
