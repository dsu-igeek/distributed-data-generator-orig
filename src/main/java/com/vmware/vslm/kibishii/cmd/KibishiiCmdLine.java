package com.vmware.vslm.kibishii.cmd;

import java.io.File;
import java.util.concurrent.ExecutionException;

import com.vmware.vslm.kibishii.core.ExecutionThread;
import com.vmware.vslm.kibishii.core.GeneratorThread;
import com.vmware.vslm.kibishii.core.VerifierThread;

public class KibishiiCmdLine {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		File rootDir = new File("/tmp/kibishii-test");
		generate(rootDir);
		verify(rootDir);
	}

	private static void generate(File rootDir) throws InterruptedException, ExecutionException {
		rootDir.mkdir();
		long startTime = System.currentTimeMillis();
		ExecutionThread generator = new GeneratorThread(rootDir, 1, 1, 1, 128*1024 * 1024, 16*1024*1024, 0);
		generator.start();
		generator.getFuture().get();
		long endTime = System.currentTimeMillis();
		System.out.println("Generated 128M in "+(endTime - startTime) + " ms");
	}

	private static void verify(File rootDir) {
		rootDir.mkdir();
		VerifierThread verifier = new VerifierThread(rootDir, 1, 1, 1, 128 * 1024 * 1024, 16*1024*1024, 0);
		verifier.start();
		try {
			System.out.println(verifier.getFuture().get());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
