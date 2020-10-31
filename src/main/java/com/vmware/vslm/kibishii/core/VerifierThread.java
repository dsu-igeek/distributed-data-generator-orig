package com.vmware.vslm.kibishii.core;

import java.io.File;
import java.io.IOException;

import com.igeekinc.testutils.TestFilesTool;

public class VerifierThread extends ExecutionThread {
	public VerifierThread(File rootDir, int levels, int dirsPerLevel, int filesPerLevel,
			long fileLength, int blockSize, int passNum) {
		super(rootDir, levels, dirsPerLevel, filesPerLevel, fileLength, blockSize, passNum);
		results = new VerifierResults();
	}
	
	public void run() {
		try {
			verifyTestHierarchy(rootDir, levels);
			results.setCompleted();
			future.complete(results);
		} catch (Throwable t) {
			t.printStackTrace();
			results.setError(t);
			future.completeExceptionally(t);
		}
	}
	
	public VerifierResults getResults() {
		return (VerifierResults)results;
	}
	
	
	int verifyTestHierarchy(File root, int curLevel)
			throws IOException
			{
				int filesCreated = 0;
				if (curLevel > 1)
				{	
					for (int curDirNum = 0; curDirNum < dirsPerLevel; curDirNum++)
					{	
						File curDir = new File(root, "dir"+curDirNum);
						if (!curDir.exists())  {
							((VerifierResults)results).addMissingDir(curDir);
						} else {
							((VerifierResults)results).addVerifiedDir(curDir);
						}
						verifyTestHierarchy(curDir, curLevel - 1);
					}
				}
				for (int curFileNum = 0; curFileNum <filesPerLevel; curFileNum++)
				{
					verifyFile(root, curFileNum);
				}
				return(filesCreated);
			}

	void verifyFile(File root, int curFileNum)
	throws IOException
	{
		File blockFile = new File(root, "file"+curFileNum);
		if (blockFile.exists()) {
			if (TestFilesTool.verifyBlockFileSequential(blockFile, fileLength, blockSize, passNum)) {
				((VerifierResults)results).addVerifiedFile(blockFile);
			} else {
				((VerifierResults)results).addCorruptedFile(blockFile);
			}
		} else {
			((VerifierResults)results).addMissingFile(blockFile);
		}
	}
}
