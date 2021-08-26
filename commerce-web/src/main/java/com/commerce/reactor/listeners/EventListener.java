package com.commerce.reactor.listeners;

import java.util.List;
import java.util.UUID;

/**
 * 控制消費者(Sink)行為
 * 
 * @author Tony.su
 * 
 */
public interface EventListener<T> {

	UUID getUuid();

	/**
	 * 確認消費者(Sink)是否離線
	 * 
	 */
	boolean isCancelled();

	/**
	 * 處理收到數據事件(多筆)
	 * 
	 */
	void onDataChunk(List<T> chunks);

	/**
	 * 處理收到數據事件(單筆)
	 * 
	 */
	void onDataChunk(T chunk);

	/**
	 * 處理完成事件
	 * 
	 */
	void processComplete();

	/**
	 * 處理異常事件
	 * 
	 */
	void processError(Throwable e);
}
