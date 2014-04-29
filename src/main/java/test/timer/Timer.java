package test.timer;

public interface Timer {

	/**
	 * 增加一个定时任务
	 * @param taskId
	 * @param task
	 * @param delayedTime 延迟时间，单位为毫秒
	 * @return 返回已有的相同id的任务，如果没有则返回空
	 */
	Runnable addTask(long taskId, Runnable task, long delayedTime);

	/**
	 * 根据ID取消一个任务
	 * @param taskId
	 * @return 是否取消成功
	 */
	boolean cancelTask(long taskId);
	
	/**
	 * 关掉timer
	 */
	void shutdown();
}
