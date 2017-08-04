package com.github.east196.boonplus.http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job> {

	private static final int MAX_WORKER_NUMBERS = 10;
	private static final int DEFAULT_WORKER_NUMBERS = 5;
	private static final int MIN_WORKER_NUMBERS = 1;

	private final LinkedList<Job> jobs = new LinkedList<>();
	private final List<Worker> workers = Collections.synchronizedList(new ArrayList<>());

	class Worker implements Runnable {
		private volatile boolean running = true;

		@Override
		public void run() {

		}

		public void shutdown() {
			running = false;
		}
	}

	@Override
	public void execute(Job job) {
		// TODO Auto-generated method stub

	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addWorkers(int num) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeWorkers(int num) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getJobSize() {
		// TODO Auto-generated method stub
		return 0;
	}

}
