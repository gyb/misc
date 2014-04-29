package test.timer;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class TimerTest {
	private Timer timer = new ZilongWheelTimer();
	private long startTime;

	@Test
	public void test() {
	    CountDownLatch latch=new CountDownLatch(3600);
		final Println[] tasks = new Println[3600];
		for (int i=0; i<3600; i++) {
			tasks[i] = new Println(i + 1, latch);
		}
		final AtomicInteger atom = new AtomicInteger(0);
		
		Executor exe = Executors.newFixedThreadPool(10);
		startTime = System.currentTimeMillis();
		for (int i=0; i<10; i++) {
			exe.execute(new Runnable() {
				@Override
				public void run() {
					int start = atom.getAndIncrement();
					for (int i=0; i<360; i++) {
						timer.addTask(i*10 + start, tasks[i*10 + start], (i*10 + start + 1) * 1000);
					}
				}
			});
		}
		
		try {
			assertTrue(latch.await(3600 + 2, TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
		timer.shutdown();
	}

	private class Println implements Runnable {
		private int number;
		private CountDownLatch latch;
	
		public Println(int number, CountDownLatch latch) {
			this.number = number;
			this.latch = latch;
		}

		@Override
		public void run() {
			int seconds = (int)((System.currentTimeMillis() - startTime) / 1000);
			System.out.print(number + "s");
			System.out.println(" ------ current time: " + System.currentTimeMillis());
			latch.countDown();
			assertTrue(Math.abs(seconds - number) <= 1);
		}
	}
}
