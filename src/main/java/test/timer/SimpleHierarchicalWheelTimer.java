package test.timer;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author wheat
 * 时间轮，适用于精度要求不高，又有大量定时请求的场合
 * 
 * TODO 在获取超时的任务那一步，使用了几个集合的交集运算，如果定时任务较多的话可能会比较耗时
 */
@SuppressWarnings("unchecked")
public class SimpleHierarchicalWheelTimer implements Timer {

	//最长超时时间，大致相当于20年
	private final static long MAX_DELAYED_TIME = 60 * 60 * 60 * 60 * 60 * 1000;

	//滴答时间，指Timer的精度，为1000毫秒
	private final static int TICK_TIME = 1000;

	//每个时间轮的格子数
	private final static int[] WHEEL_SLOTS_NUM = {60, 60, 60, 60, 60};

	//多级时间轮一共几个
	private final static int WHEELS_NUM = WHEEL_SLOTS_NUM.length;

	//一组时间轮，每个轮子的每格是一个taskId的集合
	private final Set<Long>[][] tasksWheels = new Set[WHEELS_NUM][];

	//当前走到哪一格了
	private final int[] currIndex = new int[WHEELS_NUM];

	//当前一步的开始时间
	private long currentStartTime;

	//任务的执行器
	private final static ExecutorService executor = Executors.newCachedThreadPool();

	private ConcurrentMap<Long, Runnable> taskMap = Maps.newConcurrentMap();
	
	private volatile boolean running;
	
	public SimpleHierarchicalWheelTimer() {
		initWheels();
		start();
	}

	private void initWheels() {
		for (int i=0; i<WHEELS_NUM; i++) {
			tasksWheels[i] = new Set[WHEEL_SLOTS_NUM[i]];
			for (int j=0; j<WHEEL_SLOTS_NUM[i]; j++) {
				tasksWheels[i][j] = Sets.newConcurrentHashSet();
			}
		}
	}

	private void start() {
		running = true;
		executor.execute(new Runnable() {
			public void run() {
				currentStartTime = System.currentTimeMillis();
				while (running) {
					tick(currentStartTime);
					checkTimeoutTask();
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
		moveWheelIndex();
	}

	private void moveWheelIndex() {
		int i = WHEELS_NUM - 1;

		synchronized(currIndex) {
			currIndex[i]++;
			while (currIndex[i] >= WHEEL_SLOTS_NUM[i]) {
				currIndex[i] = 0;
				if (i > 0) {
					i--;
					currIndex[i]++;
				}
			}
		}
	}

	private void checkTimeoutTask() {
		int i = WHEELS_NUM - 1;
		Set<Long> timeoutTasks = tasksWheels[i][currIndex[i]];
		while (i > 0) {
			i--;
			timeoutTasks = Sets.intersection(timeoutTasks, tasksWheels[i][currIndex[i]]);
		}
		for (Long taskId : timeoutTasks) {
			for (int j=0; j<WHEELS_NUM; j++) {
				tasksWheels[j][currIndex[j]].remove(taskId);
			}
			Runnable task = taskMap.remove(taskId);
			if (task != null) {
				executor.execute(task);
			}
		}
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

		synchronized(currIndex) {
			for (int i = WHEELS_NUM - 1; i >= 0; i--) {
				int index = (delayedTicks + currIndex[i]) % WHEEL_SLOTS_NUM[i];
				tasksWheels[i][index].add(taskId);
				delayedTicks = (delayedTicks + currIndex[i]) / WHEEL_SLOTS_NUM[i];
			}
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
