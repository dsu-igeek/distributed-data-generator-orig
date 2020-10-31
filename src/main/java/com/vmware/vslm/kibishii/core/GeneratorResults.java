package com.vmware.vslm.kibishii.core;

import java.io.File;

import org.json.simple.JSONObject;

public class GeneratorResults extends Results {
	private int filesGenerated;
	private int dirsGenerated;
	public void addGeneratedFile(File generatedFile) {
		filesGenerated++;
	}
	
	public void addGeneratedDir(File generatedDir) {
		dirsGenerated++;
	}
	public int getFilesGenerated() {
		return filesGenerated;
	}
	public int getDirsGenerated() {
		return dirsGenerated;
	}
	
	public int getGeneratedFilesAndDirs() {
		return filesGenerated + dirsGenerated;
	}
	
	public String toString() {
		String resultStr = "Total generated files and dirs = "+getGeneratedFilesAndDirs() + 
				", total generated dirs = " + getDirsGenerated() + 
				", total generated files = " + getFilesGenerated();
		if (completed) {
			resultStr += " completed at " + completedTime;
		} else {
			resultStr += " not completed";
		}
		return resultStr;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject retJSON = new JSONObject();
		retJSON.put("generatedFiles", getFilesGenerated());
		retJSON.put("generatedDirs", getDirsGenerated());
		addCompletionAndErrorInfo(retJSON);
		return retJSON;
	}
}
