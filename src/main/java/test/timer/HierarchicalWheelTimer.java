package test.timer;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.common.collect.Maps;

/**
 * @author wheat
 * 时间轮，适用于精度要求不高，又有大量定时请求的场合
 */
@SuppressWarnings("unchecked")
public class HierarchicalWheelTimer implements Timer {

	//最长超时时间，大致相当于20年
	private final static long MAX_DELAYED_TIME = 60 * 60 * 60 * 60 * 60 * 1000;

	//滴答时间，指Timer的精度，为1000毫秒
	private final static int TICK_TIME = 1000;

	//每个时间轮的格子数
	private final static int[] WHEEL_SLOTS_NUM = {10, 10, 10, 10, 10};

	//多级时间轮一共几个
	private final static int WHEELS_NUM = WHEEL_SLOTS_NUM.length;
	
	//高级轮子需要补上的滴答数。 -_- 怎么把这个补丁变成更优雅的方式，我还没想明白
	private final static int[] WHEEL_TICKS_PADDING = {0, 0, 10, 10+10*10, 10+10*10+10*10*10};

	//一组时间轮，每个轮子的每格是一个taskId到时间的映射
	private final Map<Long, Integer>[][] tasksWheels = new Map[WHEELS_NUM][];

	//当前走到哪一格了
	private final int[] currIndex = new int[WHEELS_NUM];

	private final ReadWriteLock indexLock = new ReentrantReadWriteLock();
	private final Lock indexReadLock = indexLock.readLock();
	private final Lock indexWriteLock = indexLock.writeLock();

	//当前一步的开始时间
	private long currentStartTime;

	//任务的执行器
	private final static ExecutorService executor = Executors.newCachedThreadPool();

	private ConcurrentMap<Long, Runnable> taskMap = Maps.newConcurrentMap();
	
	private volatile boolean running;
	
	public HierarchicalWheelTimer() {
		initWheels();
		start();
	}

	private void initWheels() {
		for (int i=0; i<WHEELS_NUM; i++) {
			tasksWheels[i] = new Map[WHEEL_SLOTS_NUM[i]];
			for (int j=0; j<WHEEL_SLOTS_NUM[i]; j++) {
				tasksWheels[i][j] = Maps.newConcurrentMap();
			}
		}
	}

	private void start() {
		executor.execute(new Runnable() {
			public void run() {
				running = true;
				currentStartTime = System.currentTimeMillis();
				while (running) {
					tick();
					executeTimeoutTask();
					currentStartTime += TICK_TIME;
				}
			}
		});
	}

	private void tick() {
		sleepTickTime();
		moveWheelIndex();
	}

	private void sleepTickTime() {
		long remainedSleepTime = currentStartTime + TICK_TIME - System.currentTimeMillis();
		while (remainedSleepTime > 0) {
			try {
				Thread.sleep(remainedSleepTime);
				break;
			} catch (InterruptedException e) {
				remainedSleepTime = currentStartTime + TICK_TIME - System.currentTimeMillis();
			}
		}
	}

	private void moveWheelIndex() {
		int i = 0;

		indexWriteLock.lock();
		try {
			currIndex[i]++;
			while (i < WHEELS_NUM && currIndex[i] >= WHEEL_SLOTS_NUM[i]) {
				currIndex[i] = 0;
				i++;
				if (i < WHEELS_NUM) {
					moveTasksToSubWheel(i);
					currIndex[i]++;
				}
			}
		} finally {
			indexWriteLock.unlock();
		}
	}

	private void moveTasksToSubWheel(int i) {
		for (Entry<Long, Integer> entry : tasksWheels[i][currIndex[i]].entrySet()) {
			Long taskId = entry.getKey();
			int ticks = entry.getValue();
			putTaskToWheel(taskId, ticks);
			tasksWheels[i][currIndex[i]].remove(taskId);
		}
	}

	private void executeTimeoutTask() {
		Map<Long, Integer> tasks = null;

		indexReadLock.lock();
		try {
			tasks = tasksWheels[0][currIndex[0]];
		} finally {
			indexReadLock.unlock();
		}

		for (Long taskId : tasks.keySet()) {
			tasks.remove(taskId);
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
		indexReadLock.lock();
		try {
			putTaskToWheel(taskId, delayedTicks);
		} finally {
			indexReadLock.unlock();
		}

		return taskMap.put(taskId, task);
	}

	private void putTaskToWheel(long taskId, int delayedTicks) {
		int i = 0;
		int slotTicks = 1;
		while (i < WHEELS_NUM && delayedTicks + currIndex[i] * slotTicks >= WHEEL_SLOTS_NUM[i] * slotTicks) {
			delayedTicks -= (WHEEL_SLOTS_NUM[i] - currIndex[i]) * slotTicks;
			slotTicks *= WHEEL_SLOTS_NUM[i];
			i++;
		}
		if (i == WHEELS_NUM) {
			i--;
			slotTicks /= WHEEL_SLOTS_NUM[i];
		}
		int offset = delayedTicks / slotTicks;
		delayedTicks -= offset * slotTicks;
		delayedTicks += WHEEL_TICKS_PADDING[i];
		tasksWheels[i][currIndex[i] + offset].put(taskId, delayedTicks);
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
