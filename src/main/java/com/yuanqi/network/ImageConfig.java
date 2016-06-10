package com.yuanqi.network;

import android.content.Context;

import java.io.File;

/**
 * Created by nengxiangzhou on 15/5/11.
 */
public interface ImageConfig {
  File getFileCacheDir();

  int getFileCacheSize();

  int getThreadPoolSize();

  int getMemoryCacheSize();

  int getMemoryCacheEntries();

  Context getContext();

  int getSmallFileCacheSize();
}
