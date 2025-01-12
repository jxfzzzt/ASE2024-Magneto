package org.bcjj.huntfiles;

public interface HuntFilesListener {

	public void addFile(FileInfo fileInfo);
	
	public void addError(String err);
	
	public void updateProgress(int foundFiles, int searchedFiles, int totalFiles);
	
	public void workingInArchive(String archive);
	
}
