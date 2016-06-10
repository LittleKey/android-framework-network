package com.yuanqi.network;

/**
 * Created by nengxiangzhou on 15/7/1.
 */
public enum RequestStatus {
  NOT_READY,
  ONGOING,
  HIT_CACHE_AND_LOAD_SUCCESS,
  HIT_CACHE_AND_LOAD_FAIL,
  HIT_CACHE_AND_NEED_REFRESH,
  MISS_CACHE_AND_LOAD_SUCCESS,
  MISS_CACHE_AND_LOAD_FAIL
}
