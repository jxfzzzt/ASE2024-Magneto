package org.bcjj.huntfiles;

import java.util.ArrayList;
import java.util.List;

public class HuntFilesListener4Test implements HuntFilesListener {
	
	
	List<String> errores=new ArrayList<>();
	List<FileInfo> files=new ArrayList<>();
	
	@Override
	public void addFile(FileInfo fileInfo) {
		files.add(fileInfo);
	}

	@Override
	public void addError(String err) {
		errores.add(err);
	}

	@Override
	public void updateProgress(int foundFiles, int searchedFiles, int totalFiles) {
		// TODO Auto-generated method stub
	}

	@Override
	public void workingInArchive(String archive) {
		//System.out.println("workingInArchive "+archive);
	}

	public List<String> getErrores() {
		return errores;
	}

	public List<FileInfo> getFiles() {
		return files;
	}
	

}
