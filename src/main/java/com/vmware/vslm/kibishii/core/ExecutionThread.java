package com.vmware.vslm.kibishii.core;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public abstract class ExecutionThread implements Runnable {
	protected Results results;
	protected CompletableFuture<Results> future = new CompletableFuture<Results>();
	protected final Thread thread;
	protected final File rootDir;
	protected final int levels;
	protected final int dirsPerLevel;
	protected final int filesPerLevel;
	protected final long fileLength;
	protected final int blockSize;
	protected final int passNum;

	public ExecutionThread(File rootDir, int levels, int dirsPerLevel, int filesPerLevel, long fileLength,
			int blockSize, int passNum) {
		this.rootDir = rootDir;
		thread = new Thread(this);
		this.levels = levels;
		this.dirsPerLevel = dirsPerLevel;
		this.filesPerLevel = filesPerLevel;
		this.fileLength = fileLength;
		this.blockSize = blockSize;
		this.passNum = passNum;
	}

	public CompletableFuture<Results> getFuture() {
		return future;
	}

	public Results getResults() {
		return results;
	}

	public void start() {
		thread.start();
	}
}
