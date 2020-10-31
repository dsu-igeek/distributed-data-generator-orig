package com.vmware.vslm.kibishii.core;

import java.io.File;
import java.io.IOException;

import com.igeekinc.testutils.TestFilesTool;

public class GeneratorThread extends ExecutionThread {
	public GeneratorThread(File rootDir, int levels, int dirsPerLevel, int filesPerLevel,
			long fileLength, int blockSize, int passNum) {
		super(rootDir, levels, dirsPerLevel, filesPerLevel, fileLength, blockSize, passNum);
		results = new GeneratorResults();
	}

	public void run() {
		try {
			makeTestHierarchy(rootDir, levels);
			results.setCompleted();
			future.complete(results);
		} catch (Throwable e) {
			e.printStackTrace();
			results.setError(e);
			future.completeExceptionally(e);
		}
	}

	int makeTestHierarchy(File root, int curLevel)
			throws IOException
			{
				int filesCreated = 0;
				if (curLevel > 1)
				{
					for (int curDirNum = 0; curDirNum < dirsPerLevel; curDirNum++)
					{
						File curDir = createDir(root, curDirNum);
						filesCreated++;
						filesCreated += makeTestHierarchy(curDir, curLevel - 1);
					}
				}
				for (int curFileNum = 0; curFileNum <filesPerLevel; curFileNum++)
				{
					createFile(root, curFileNum);
					filesCreated++;
				}
				return(filesCreated);
			}

	void createFile(File root, int curFileNum)
	throws IOException
	{
		File blockFile = new File(root, "file"+curFileNum);

		TestFilesTool.createBlockFileSequential(blockFile, fileLength, blockSize, passNum);
		((GeneratorResults)results).addGeneratedFile(blockFile);

	}

	File createDir(File root, int curDirNum)
	{
		File newDir = new File(root, "dir"+curDirNum);
		newDir.mkdir();
		((GeneratorResults)results).addGeneratedDir(newDir);
		return newDir;
	}
}
