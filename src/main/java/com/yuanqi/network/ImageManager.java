package com.yuanqi.network;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.yuanqi.base.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ImageManager is used to init Volley/Fresco image loader, init it when Application create and get
 * the instance from Application.
 * Created by nengxiangzhou on 15/5/11.
 */
public class ImageManager {
  private static final String FRESCO_MAIN_CACHE_FILE_NAME = "fresco_main";
  private static final String FRESCO_SMALL_CACHE_FILE_NAME = "fresco_small";
  ImageConfig mConfig;
  private HeaderCallback mHeaderCallback;

  public ImageManager(ImageConfig config) {
    mConfig = config;
    initImagePipeline(config);
  }

  private void initImagePipeline(final ImageConfig config) {
    DiskCacheConfig mainCacheConfig = DiskCacheConfig.newBuilder(config.getContext())
        .setBaseDirectoryPath(config.getFileCacheDir())
        .setBaseDirectoryName(FRESCO_MAIN_CACHE_FILE_NAME)
        .setMaxCacheSize(config.getFileCacheSize())
        .setMaxCacheSizeOnLowDiskSpace(config.getFileCacheSize() / 2)
        .setMaxCacheSizeOnVeryLowDiskSpace(config.getFileCacheSize() / 4)
        .build();
    DiskCacheConfig smallCacheConfig = DiskCacheConfig.newBuilder(config.getContext())
        .setBaseDirectoryPath(config.getFileCacheDir())
        .setBaseDirectoryName(FRESCO_SMALL_CACHE_FILE_NAME)
        .setMaxCacheSize(config.getSmallFileCacheSize())
        .setMaxCacheSizeOnLowDiskSpace(config.getSmallFileCacheSize() / 2)
        .setMaxCacheSizeOnVeryLowDiskSpace(config.getSmallFileCacheSize() / 4)
        .build();
    OkHttpClient okhttpClient = new OkHttpClient.Builder()
        .addNetworkInterceptor(new Interceptor() {
          @Override
          public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            if (mHeaderCallback != null) {
              for (NameValuePair header : mHeaderCallback.getHeaders()) {
                builder.addHeader(header.getName(), header.getValue());
              }
            }
            return chain.proceed(builder.build());
          }
        })
        .build();
    ImagePipelineConfig pipelineConfig = OkHttpImagePipelineConfigFactory
        .newBuilder(config.getContext(), okhttpClient)
        .setBitmapMemoryCacheParamsSupplier(new Supplier<MemoryCacheParams>() {
          @Override
          public MemoryCacheParams get() {
            return new MemoryCacheParams(config.getMemoryCacheSize(),
                config.getMemoryCacheEntries(),
                config.getMemoryCacheSize() / 2,
                config.getMemoryCacheEntries() / 2,
                config.getMemoryCacheSize() / 2);
          }
        })
        .setMainDiskCacheConfig(mainCacheConfig)
        .setSmallImageDiskCacheConfig(smallCacheConfig)
        .build();
    Fresco.initialize(config.getContext(), pipelineConfig);
  }

  public ImagePipeline getImagePipeline() {
    return Fresco.getImagePipeline();
  }

  public void cleanCache() {
    FileUtils.delete(new File(mConfig.getFileCacheDir(), FRESCO_MAIN_CACHE_FILE_NAME));
    FileUtils.delete(new File(mConfig.getFileCacheDir(), FRESCO_SMALL_CACHE_FILE_NAME));
  }

  public void setHeaderCallback(HeaderCallback callback) {
    mHeaderCallback = callback;
  }

  public void shutdown() {
    Fresco.shutDown();
  }

  public interface HeaderCallback {
    List<NameValuePair> getHeaders();
  }
}
