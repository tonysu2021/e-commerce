package com.commerce.txn.helper;

/**
 * SnowFlake雪花算法
 * 
 */
public class SnowflakeIdWorker {
	
	/** 開始時間截 (2015-01-01) */
	private static final long TWEPOCH = 1420041600000L;

	/** 機器id所佔的位數 */
	private static final long WORKER_ID_BITS = 5L;

	/** 數據標識id所佔的位數 */
	private static final long DATACENTER_ID_BITS = 5L;

	/** 支持的最大機器id，結果是31 (這個移位算法可以很快的計算出幾位二進制數所能表示的最大十進制數) */
	private static final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);

	/** 支持的最大數據標識id，結果是31 */
	private static final long MAX_DATACENTER_ID = -1L ^ (-1L << DATACENTER_ID_BITS);

	/** 序列在id中佔的位數 */
	private static final long SEQUENCE_BITS = 12L;

	/** 機器ID向左移12位 */
	private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

	/** 數據標識id向左移17位(12+5) */
	private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

	/** 時間截向左移22位(5+5+12) */
	private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

	/** 生成序列的掩碼，這裏爲4095 (0b111111111111=0xfff=4095) */
	private static final long SEQUENCE_MASK = -1L ^ (-1L << SEQUENCE_BITS);

	/** 工作機器ID(0~31) */
	private long workerId;

	/** 數據中心ID(0~31) */
	private long datacenterId;

	/** 毫秒內序列(0~4095) */
	private long sequence = 0L;

	/** 上次生成ID的時間截 */
	private long lastTimestamp = -1L;

	/**
	 * 構造函數
	 * 
	 * @param datacenterId 數據中心ID (0~31)
	 * @param workerId     工作ID (0~31)
	 */
	public SnowflakeIdWorker(long datacenterId,long workerId) {

		if (workerId > MAX_WORKER_ID || workerId < 0) {

			throw new IllegalArgumentException(
					String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
		}
		if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {

			throw new IllegalArgumentException(
					String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATACENTER_ID));
		}
		this.datacenterId = datacenterId;
		this.workerId = workerId;
	}

	/**
	 * 獲得下一個ID (該方法是線程安全的)
	 * 
	 * @return SnowflakeId
	 */
	public synchronized long nextId() {

		long timestamp = timeGen();

		// 如果當前時間小於上一次ID生成的時間戳，說明系統時鐘回退過這個時候應當拋出異常
		if (timestamp < lastTimestamp) {

			throw new RuntimeException(String.format(
					"Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}

		// 如果是同一時間生成的，則進行毫秒內序列
		if (lastTimestamp == timestamp) {

			sequence = (sequence + 1) & SEQUENCE_MASK;
			// 毫秒內序列溢出
			if (sequence == 0) {

				// 阻塞到下一個毫秒,獲得新的時間戳
				timestamp = tilNextMillis(lastTimestamp);
			}
		}
		// 時間戳改變，毫秒內序列重置
		else {

			sequence = 0L;
		}

		// 上次生成ID的時間截
		lastTimestamp = timestamp;

		// 移位並通過或運算拼到一起組成64位的ID
		return ((timestamp - TWEPOCH) << TIMESTAMP_LEFT_SHIFT) //
				| (datacenterId << DATACENTER_ID_SHIFT) //
				| (workerId << WORKER_ID_SHIFT) //
				| sequence;
	}

	/**
	 * 阻塞到下一個毫秒，直到獲得新的時間戳
	 * 
	 * @param lastTimestamp 上次生成ID的時間截
	 * @return 當前時間戳
	 */
	protected long tilNextMillis(long lastTimestamp) {

		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {

			timestamp = timeGen();
		}
		return timestamp;
	}

	/**
	 * 返回以毫秒爲單位的當前時間
	 * 
	 * @return 當前時間(毫秒)
	 */
	protected long timeGen() {
		return System.currentTimeMillis();
	}
}
