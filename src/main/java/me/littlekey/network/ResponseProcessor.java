package me.littlekey.network;

/**
 * Created by nengxiangzhou on 15/7/20.
 */
public interface ResponseProcessor<T> {
  T generate(T response) throws Exception;
}
