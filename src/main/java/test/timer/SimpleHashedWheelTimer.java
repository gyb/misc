package test.timer;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.collect.Maps;

/**
 * @author wheat
 * 时间轮，适用于精度要求不高，又有大量定时请求的场合
 * 
 * TODO 每个hashmap没有排序，所以每次都需要取出map里的所有task。如果排序了就只用取超时的就可以了
 */
@SuppressWarnings("unchecked")
public class SimpleHashedWheelTimer implements Timer {

	//滴答时间，指Timer的精度，为1000毫秒
	private final static int TICK_TIME = 1000;

	//每个时间轮的格子数
	private final static int WHEEL_SLOTS_NUM = 60;
	
	private final static long MAX_DELAYED_TIME = Integer.MAX_VALUE;

	//时间轮，轮子的每格是taskId的map
	private final Map<Long, Integer>[] tasksWheel = new Map[WHEEL_SLOTS_NUM];

	//当前走到哪一格了
	private int currIndex;

	//当前一步的开始时间
	private long currentStartTime;

	//任务的执行器
	private final static ExecutorService executor = Executors.newCachedThreadPool();

	private ConcurrentMap<Long, Runnable> taskMap = Maps.newConcurrentMap();
	
	private volatile boolean running;
	
	public SimpleHashedWheelTimer() {
		initWheel();
		start();
	}

	private void initWheel() {
		for (int i=0; i<WHEEL_SLOTS_NUM; i++) {
			tasksWheel[i] = Maps.newConcurrentMap();
		}
	}

	private void start() {
		running = true;
		executor.execute(new Runnable() {
			public void run() {
				currentStartTime = System.currentTimeMillis();
				while (running) {
					tick(currentStartTime);
					currentStartTime += TICK_TIME;
				}
			}
		});
	}

	private void tick(long startTime) {
		long remainedSleepTime = startTime + TICK_TIME - System.currentTimeMillis();
		while (remainedSleepTime > 0) {
			try {
				Thread.sleep(remainedSleepTime);
				break;
			} catch (InterruptedException e) {
				remainedSleepTime = startTime + TICK_TIME - System.currentTimeMillis();
			}
		}
		synchronized(tasksWheel) {
			moveWheelIndex();
			checkTimeoutTasks();
		}
	}

	private void moveWheelIndex() {
		currIndex++;
		if (currIndex >= WHEEL_SLOTS_NUM) {
			currIndex -= WHEEL_SLOTS_NUM;
		}
	}

	private void checkTimeoutTasks() {
		Map<Long, Integer> temp = Maps.newHashMap();
		for (Long taskId : tasksWheel[currIndex].keySet()) {
			Integer round = tasksWheel[currIndex].remove(taskId);
			if (round > 0) {
				temp.put(taskId, round - 1);
			} else {
				Runnable task = taskMap.remove(taskId);
				if (task != null) {
					executor.execute(task);
				}
			}			
		}
		tasksWheel[currIndex].putAll(temp);
	}

	@Override
	public Runnable addTask(long taskId, Runnable task, long delayedTime) {
		if (!running) {
			throw new IllegalStateException("Timer has already been shutdowned!");
		}
		if (delayedTime > MAX_DELAYED_TIME) {
			throw new IllegalArgumentException("Delayed time is too long!");
		}
		if (delayedTime <= 0) {
			throw new IllegalArgumentException("Delayed time should be larger than zero!");
		}

		int delayedTicks = (int)(delayedTime / TICK_TIME);

		synchronized(tasksWheel) {
			int index = (delayedTicks + currIndex) % WHEEL_SLOTS_NUM;
			int round = delayedTicks / WHEEL_SLOTS_NUM;
			tasksWheel[index].put(taskId, round);
		}
		return taskMap.put(taskId, task);
	}

	@Override
	public boolean cancelTask(long taskId) {
		//时间轮里的任务id就不管了，只是把任务给删了
		return taskMap.remove(taskId) != null;
	}

	@Override
	public void shutdown() {
		running = false;
		executor.shutdown();
	}
}
