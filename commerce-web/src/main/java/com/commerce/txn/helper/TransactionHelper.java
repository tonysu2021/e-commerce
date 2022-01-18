package com.commerce.txn.helper;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 交易輔助類，主要把鎖粒度ByKey。
 * 
 * @author Tony.su
 * 
 */
public class TransactionHelper {

	private static Logger logger = LoggerFactory.getLogger(TransactionHelper.class);

	private final ConcurrentHashMap<Integer, LockState> lockMap = new ConcurrentHashMap<>();

	private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	public TransactionHelper() {
		// 定時檢查 Time to Live 並且移除超時的 Lock 。
		this.executor.scheduleAtFixedRate(this::clearStateByTTL, 1, 30, TimeUnit.SECONDS);
	}

	public <T> T doInTransaction(Integer key, Supplier<T> supplier) {
		LockState state = lockMap.computeIfAbsent(key, k -> new TransactionHelper.LockState());
		Lock lock = state.getLock();
		lock.lock();
		try {
			return supplier.get();
		} finally {
			lock.unlock();
		}
	}

	public <T> T doInTransaction(Integer key, Supplier<T> supplier, long ttl, TimeUnit unit) {
		LockState state = lockMap.computeIfAbsent(key, k -> new TransactionHelper.LockState());
		Lock lock = state.getLock();
		try {
			if (lock.tryLock(ttl, unit)) {
				return supplier.get();
			}
		} catch (InterruptedException ex) {
			logger.error(ex.getMessage());
			Thread.currentThread().interrupt();
		} finally {
			lock.unlock();
		}
		return null;
	}

	private void clearStateByTTL() {
		long current = Instant.now().toEpochMilli();
		lockMap.entrySet().parallelStream().filter(element -> current > element.getValue().getTtl())
				.forEach(element -> {
					logger.info("生效時間已過，清除Lock key : {}", element.getKey());
					lockMap.remove(element.getKey());
				});
	}

	public static class LockState {
		private final Lock lock;
		private long ttl;

		public LockState() {
			super();
			this.lock = new ReentrantLock();
			// 高併發情況下，減少相同 key 、 Lock 資源使用。
			this.ttl = Instant.now().plus(Duration.ofSeconds(300)).toEpochMilli();
		}
		
		public Lock getLock() {
			return lock;
		}

		public long getTtl() {
			return ttl;
		}

		public void setTtl(long ttl) {
			this.ttl = ttl;
		}
	}
}

