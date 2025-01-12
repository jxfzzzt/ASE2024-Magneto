package org.bcjj.huntfiles;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.bcjj.huntfiles.FileInfo.FileType;
import org.bcjj.huntfiles.gui.HuntFilesMainWindow;
import org.bcjj.huntfiles.util.SevenZInputStream;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.BaseBlock;
import com.github.junrar.rarfile.FileHeader;


public class HuntFiles  {

	public static void main(String [] s) throws Exception {
		SearchOptions searchOptions=null;
		try {
			 searchOptions=new SearchOptions(s);
		} catch (Exception r) {
			return;
		}
		
		HuntFilesListener huntFilesListener = new HuntFilesListener() {
			@Override
			public void addFile(FileInfo fileInfo) {
				System.out.println(fileInfo.toString(false, true,"  > "));
			}

			@Override
			public void addError(String err) {
				System.out.println("ERROR "+err);
			}

			@Override
			public void updateProgress(int foundFiles, int searchedFiles, int totalFiles) {
				//System.out.println("foundFiles:"+foundFiles+" in  searchedFiles:"+searchedFiles+" /  totalFiles:"+totalFiles);
			}
			
			@Override
			public void workingInArchive(String archive) {
				System.out.println("working in "+archive);
			}
			
		};
		
		if (!searchOptions.isConsole()) {
			HuntFilesMainWindow.main(searchOptions);
		} else if (searchOptions.isValid()){
			HuntFiles huntFiles=new HuntFiles(searchOptions,huntFilesListener);
			String stats=huntFiles.search();
			System.out.println("search stats:"+stats);
		}
	}
	
	HuntFilesListener listener=null;
	SearchOptions searchOptions=null;
	boolean stop=false;
	
	private static final List<String> zipDocuments = Arrays.asList(new String[]{"ods", "ots", "odt", "ott", "odp", "otp",
			"docx", "docm","dotx" ,"dotm" ,"xlsx" ,"xlsm", "xlsb", "xltx", "xltm", "pptx", "pptm", "potx", "potm", "ppsx", "ppsm"});
	
	/*
		docx,docm,dotx,dotm,   / doc,dot
		pptx, pptm, potx, potm, ppsx, ppsm / ppt, pot, pps
		xlsx, xlsm, xlsb, xltx, xltm / xls, xlt
		Open:  (excel) ods, ots, (word) odt, ott, (power) odp, otp 
	 */
	
	
	public HuntFiles(SearchOptions searchOptions,HuntFilesListener listener)  {
		this.listener=listener;
		this.searchOptions=searchOptions;
	}
	


	public String search() throws Exception {
		stop=false;
		if (searchOptions==null || searchOptions.getDir()==null) {
			throw new Exception("Initial Directory is needed");
		}
		initSearchInDirOrFile(new File(searchOptions.getDir()));
		return "ok";
	}

	
	
	
	
	private void initSearchInDirOrFile(File directory) {
		if (directory.isFile()) {
			File file=directory;
			searchInFile(file.getParentFile(), file);
		} else if (directory.isDirectory()) {
			searchInDir(directory);
		}
	}
	
	
	private void searchInDir(File directory) {
		if (Files.isSymbolicLink(directory.toPath())) {
			return;
		}
		File [] files=directory.listFiles();
		if (files==null) {
			return;
		}
		listener.workingInArchive(directory.getPath());
		for (File file:files) {
			if (stop) {
				return;
			}
			if (file.isFile()) {
				searchInFile(directory, file);
				//String ext1 = FilenameUtils.getExtension("/path/to/file/foo.txt"); // returns "txt"
			}
		}
		if (searchOptions.isRecursive()) {
			for (File dir:files) {
				if (stop) {
					return;
				}
				if (dir.isDirectory()) {
					searchInDir(dir);
				}
			}
		}
	}



	private void searchInFile(File directory, File file) {
		String ext=FilenameUtils.getExtension(file.getName());
		if (ext!=null) {
			searchInFile(file,ext.toLowerCase());
			if (zipDocuments.contains(ext.toLowerCase())) {
				try {
					listener.workingInArchive(file.getPath());
					searchInZip(file);
				} catch (Exception r) {
					listener.addError("ERROR in zipFile "+file+" :: "+r);
				}
				listener.workingInArchive(directory.getPath());				
			}
			if (ext.equalsIgnoreCase("zip") || ext.equalsIgnoreCase("jar") || ext.equalsIgnoreCase("war")) {
				if (searchOptions.isZipjar()) {
					try {
						listener.workingInArchive(file.getPath());
						searchInZip(file);
					} catch (Exception r) {
						listener.addError("ERROR in zipFile "+file+" :: "+r);
					}
					listener.workingInArchive(directory.getPath());
				}
			} else if (ext.equalsIgnoreCase("7z")) {
				if (searchOptions.isZ7()) {
					try {
						listener.workingInArchive(file.getPath());
						searchIn7Z(file);
					} catch (Exception r) {
						listener.addError("ERROR in zipFile "+file+" :: "+r);
					}
					listener.workingInArchive(directory.getPath());
				}
			} else if (ext.equalsIgnoreCase("rar")) {
				if (searchOptions.isRar()) {
					try {
						listener.workingInArchive(file.getPath());
						searchInRar(file);
					} catch (Exception r) {
						listener.addError("ERROR in zipFile "+file+" :: "+r);
					}
					listener.workingInArchive(directory.getPath());
				}
			}
		} else {
			searchInFile(file,"txt");
		}
	}

	private void searchIn7Z(File file) throws Exception {
		try (SevenZFile sevenZFile = new SevenZFile(file)) {
			SevenZArchiveEntry sevenZArchiveEntry=null;
			while ((sevenZArchiveEntry =sevenZFile.getNextEntry())!=null) {
				if (stop) {
					return;
				}
				if (!sevenZArchiveEntry.isDirectory()) {
			    	searchIn7ZEntry(sevenZArchiveEntry,sevenZFile,file);
			    }
			}
		}
	}
	
	




	private void searchInZip(File file) throws Exception {
		try (ZipFile zipFile=new ZipFile(file)) {
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			//Hashtable<String, Long> h=new Hashtable<String, Long>();
			while (entries.hasMoreElements()) {
				if (stop) {
					return;
				}
			    ZipEntry zipEntry = entries.nextElement();
			    if (!zipEntry.isDirectory()) {
			    	searchInZipEntry(zipEntry,zipFile,file);
			    }
			}
		}
	}
	
	
	private void searchInRar(File file) throws Exception {
		try (Archive rarFile=new Archive(file)) {
			List<FileHeader> fileHeaders = rarFile.getFileHeaders();
			//Hashtable<String, Long> h=new Hashtable<String, Long>();
			for (FileHeader fileHeader:fileHeaders) {
				if (stop) {
					return;
				}
			    
			    if (!fileHeader.isDirectory()) {
			    	searchInRarEntry(fileHeader,rarFile,file);
			    }
			}
		}
	}	
	
	private void searchInRarEntry(FileHeader fileHeader, Archive rarFile, File file) {
		String path=fileHeader.getFileNameString();
		
		if (searchOptions.getFilename()!=null) {
			String zipPath=fileHeader.getFileNameString();
			String name=getNameFromPath(zipPath);
			if (!FilenameUtils.wildcardMatch(name, searchOptions.getFilename(), IOCase.INSENSITIVE)) {
				return;
			}
		}
		
		if (searchOptions.getAfter()!=null) {
			if (fileHeader.getMTime().getTime()<searchOptions.getAfter()) {
				return;
			}
		}
		
		if (searchOptions.getBefore()!=null) {
			if (fileHeader.getMTime().getTime()>searchOptions.getBefore()) {
				return;
			}
		}
		
		if (searchOptions.getGreaterThan()!=null) {
			if (fileHeader.getUnpSize()<searchOptions.getGreaterThan()) {
				return;
			}
		}
		
		if (searchOptions.getLessThan()!=null) {
			if (fileHeader.getUnpSize()>searchOptions.getLessThan()) {
				return;
			}
		}
		
		if (searchOptions.getIgnorePaths()!=null && searchOptions.getIgnorePaths().size()>0) {
			String filePath=path;
			filePath=StringUtils.replace(filePath, "\\", "/");
			filePath=StringUtils.replace(filePath, "//", "/");
			for (String ignorePath:searchOptions.getIgnorePaths()) {
				if (FilenameUtils.wildcardMatch(filePath, ignorePath, IOCase.INSENSITIVE)) {
					return;
				}
			}
		}
		
		SearchResult hits=null;
		if (searchOptions.getStandardText()!=null) {
			try (InputStream is=  rarFile.getInputStream(fileHeader)) {
				hits=searchText(is,searchOptions.getStandardText(),file.getAbsolutePath()+"|"+path,searchOptions);
				if (hits.size()==0) {
					return;
				}
			} catch (Exception r) {
				listener.addError("Error reading "+file+"|"+path+" :: "+r);
			}
		}
		
		FileInfo fileInfo=new FileInfo(file, path, fileHeader.getUnpSize(), fileHeader.getMTime().getTime() ,hits, FileType.Rar,searchOptions);
		listener.addFile(fileInfo);
	}



	private String getNameFromPath(String path) {
		String name=path;
		path=StringUtils.replace(path,"\\","/");
		int ult=path.lastIndexOf("/");
		if (ult>-1) {
			name=path.substring(ult+1);
		} else {
			name=path;
		}
		return name;
	}
	
	private void searchInZipEntry(ZipEntry zipEntry,ZipFile zipFile,File file) {
		String path=zipEntry.getName();
		
		if (searchOptions.getFilename()!=null) {
			String zipPath=zipEntry.getName();
			String name=getNameFromPath(zipPath);
			if (!FilenameUtils.wildcardMatch(name, searchOptions.getFilename(), IOCase.INSENSITIVE)) {
				return;
			}
		}
		
		if (searchOptions.getAfter()!=null) {
			if (zipEntry.getTime()<searchOptions.getAfter()) {
				return;
			}
		}
		
		if (searchOptions.getBefore()!=null) {
			if (zipEntry.getTime()>searchOptions.getBefore()) {
				return;
			}
		}
		
		if (searchOptions.getGreaterThan()!=null) {
			if (zipEntry.getSize()<searchOptions.getGreaterThan()) {
				return;
			}
		}
		
		if (searchOptions.getLessThan()!=null) {
			if (zipEntry.getSize()>searchOptions.getLessThan()) {
				return;
			}
		}
		
		if (searchOptions.getIgnorePaths()!=null && searchOptions.getIgnorePaths().size()>0) {
			String filePath=path;
			filePath=StringUtils.replace(filePath, "\\", "/");
			filePath=StringUtils.replace(filePath, "//", "/");
			for (String ignorePath:searchOptions.getIgnorePaths()) {
				if (FilenameUtils.wildcardMatch(filePath, ignorePath, IOCase.INSENSITIVE)) {
					return;
				}
			}
		}
		
		SearchResult hits=null;
		if (searchOptions.getStandardText()!=null) {
			try (InputStream is=zipFile.getInputStream(zipEntry)) {
				hits=searchText(is,searchOptions.getStandardText(),file.getAbsolutePath()+"|"+path,searchOptions);
				if (hits.size()==0) {
					return;
				}
			} catch (Exception r) {
				listener.addError("Error reading "+file+"|"+path+" :: "+r);
			}
		}
		
		FileInfo fileInfo=new FileInfo(file, path, zipEntry.getSize(), zipEntry.getTime() ,hits, FileType.Zip,searchOptions);
		listener.addFile(fileInfo);
		return;
	}
	
	private void searchIn7ZEntry(SevenZArchiveEntry sevenZArchiveEntry, SevenZFile sevenZFile, File file) {
		String path=sevenZArchiveEntry.getName();
		
		if (searchOptions.getFilename()!=null) {
			String zipPath=sevenZArchiveEntry.getName();
			String name=getNameFromPath(zipPath);
			if (!FilenameUtils.wildcardMatch(name, searchOptions.getFilename(), IOCase.INSENSITIVE)) {
				return;
			}
		}
		
		if (searchOptions.getAfter()!=null) {
			if (sevenZArchiveEntry.getHasLastModifiedDate() && sevenZArchiveEntry.getLastModifiedDate().getTime()<searchOptions.getAfter()) {
				return;
			}
		}
		
		if (searchOptions.getBefore()!=null) {
			if (sevenZArchiveEntry.getHasLastModifiedDate() &&  sevenZArchiveEntry.getLastModifiedDate().getTime()>searchOptions.getBefore()) {
				return;
			}
		}
		
		if (searchOptions.getGreaterThan()!=null) {
			if (sevenZArchiveEntry.getSize()<searchOptions.getGreaterThan()) {
				return;
			}
		}
		
		if (searchOptions.getLessThan()!=null) {
			if (sevenZArchiveEntry.getSize()>searchOptions.getLessThan()) {
				return;
			}
		}
		
		if (searchOptions.getIgnorePaths()!=null && searchOptions.getIgnorePaths().size()>0) {
			String filePath=path;
			filePath=StringUtils.replace(filePath, "\\", "/");
			filePath=StringUtils.replace(filePath, "//", "/");
			for (String ignorePath:searchOptions.getIgnorePaths()) {
				if (FilenameUtils.wildcardMatch(filePath, ignorePath, IOCase.INSENSITIVE)) {
					return;
				}
			}
		}
		
		SearchResult hits=null;
		if (searchOptions.getStandardText()!=null) {
			try (InputStream is=new SevenZInputStream(sevenZArchiveEntry,sevenZFile)) {
				hits=searchText(is,searchOptions.getStandardText(),file.getAbsolutePath()+"|"+path,searchOptions);
				if (hits.size()==0) {
					return;
				}
			} catch (Exception r) {
				listener.addError("Error reading "+file+"|"+path+" :: "+r);
			}
		}
		
		FileInfo fileInfo=new FileInfo(file, path, sevenZArchiveEntry.getSize(), sevenZArchiveEntry.getLastModifiedDate().getTime() ,hits, FileType.Z7,searchOptions);
		listener.addFile(fileInfo);
		return;
	}
	
	
	
	
	private void searchInFile(File file, String ext) {
		
		if (searchOptions.getFilename()!=null) {
			String patron=searchOptions.getFilename();
			if (patron.indexOf("*")>-1 || patron.indexOf("?")>-1) {
				if (!FilenameUtils.wildcardMatch(file.getName(), searchOptions.getFilename(), IOCase.INSENSITIVE)) {
					return;
				}
			} else {
				if (!file.getName().contains(patron)) {
					return;
				}
			}
		}
		
		if (searchOptions.getAfter()!=null) {
			if (file.lastModified()<searchOptions.getAfter()) {
				return;
			}
		}
		
		if (searchOptions.getBefore()!=null) {
			if (file.lastModified()>searchOptions.getBefore()) {
				return;
			}
		}
		
		if (searchOptions.getGreaterThan()!=null) {
			if (file.length()<searchOptions.getGreaterThan()) {
				return;
			}
		}
		
		if (searchOptions.getLessThan()!=null) {
			if (file.length()>searchOptions.getLessThan()) {
				return;
			}
		}
		
		if (searchOptions.getIgnorePaths()!=null && searchOptions.getIgnorePaths().size()>0) {
			String filePath=file.getPath();
			filePath=StringUtils.replace(filePath, "\\", "/");
			filePath=StringUtils.replace(filePath, "//", "/");
			for (String ignorePath:searchOptions.getIgnorePaths()) {
				if (FilenameUtils.wildcardMatch(filePath, ignorePath, IOCase.INSENSITIVE)) {
					return;
				}
			}
		}
		
		SearchResult hits=null;
		if (searchOptions.getStandardText()!=null) {
			if (ext.equalsIgnoreCase("zip") || ext.equalsIgnoreCase("jar") || ext.equalsIgnoreCase("war") || ext.equalsIgnoreCase("7z") || ext.equalsIgnoreCase("rar")) {
				return;
			}
			try (InputStream is=new FileInputStream(file)) {
				hits=searchText(is,searchOptions.getStandardText(),file.getAbsolutePath(),searchOptions);
				if (hits.size()==0) {
					return;
				}
			} catch (Exception r) {
				listener.addError("Error reading "+file+" :: "+r);
			} 
		}
		
		FileInfo fileInfo=new FileInfo(file, null,null,null, hits, FileType.File,searchOptions);
		listener.addFile(fileInfo);
		
	}

	

	
	private List<Hit> searchText0(InputStream is, String text) throws Exception {
		text=text.toLowerCase();
		//text=StringUtils.stripAccents(text);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
		String line="";
		int lineNum=0;
		List<Hit> hits=new ArrayList<Hit>();
		while ((line=bufferedReader.readLine())!=null) {
			lineNum++;
			if (line.toLowerCase().indexOf(text)>-1) {
				Hit hit=new Hit(lineNum, line);
				hits.add(hit);
			}
		}
		return hits;
	}

	
	public class SearchResult {
		List<Hit> hits;
		Charset preferredCharset;
		
		SearchResult(List<Hit> hits,Charset preferredCharset) {
			this.hits=hits;
			this.preferredCharset=preferredCharset;
		}
		
		public List<Hit> getHits() {
			return hits;
		}
		public Charset getPreferredCharset() {
			return preferredCharset;
		}
		public int size() {
			return hits.size();
		}
	}
	
	public static String getStandardText(String text) {
		if (text==null) {
			return null;
		}
		text=text.trim();
		text=text.toLowerCase();
		//text=StringUtils.stripAccents(text); //tambien cambia las �
		text=StringUtils.replaceChars(text, "���������������\t", "aeiouaeiouaeiou ");
		text=text.replaceAll("\\s+"," ");
		return text;
	}
	

	
	


	private SearchResult searchText(InputStream is, String standardText, String filePath,SearchOptions searchOptions) throws Exception {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[16384];
		while ((nRead = is.read(data, 0, data.length)) != -1) {
		  buffer.write(data, 0, nRead);
		}
		byte[] contenido=buffer.toByteArray();
		
		Set<Hit> hitSet=Collections.synchronizedSet(new HashSet<>());
		
		String stdTexto=null;
		Pattern regPattern=null;
		if (searchOptions.getRegex()!=null) {
			regPattern=searchOptions.getRegex();
		} else {
			stdTexto=standardText;
		}
		
		

		List<Buscador> buscadores=new ArrayList<>();
		
		if (searchOptions.isSearchAnsi()) {
			buscadores.add(new Buscador(contenido,stdTexto,regPattern,hitSet,filePath,true, StandardCharsets.ISO_8859_1));
		}
		if (searchOptions.isSearchUtf8()) {
			buscadores.add(new Buscador(contenido,stdTexto,regPattern,hitSet,filePath,true, StandardCharsets.UTF_8));
		}
		if (searchOptions.isSearchUtf16()) {
			buscadores.add(new Buscador(contenido,stdTexto,regPattern,hitSet,filePath,true, StandardCharsets.UTF_16));
			buscadores.add(new Buscador(contenido,stdTexto,regPattern,hitSet,filePath,true, StandardCharsets.UTF_16BE));
			buscadores.add(new Buscador(contenido,stdTexto,regPattern,hitSet,filePath,true, StandardCharsets.UTF_16LE));
		}
		
		for(Buscador buscador:buscadores) {
			buscador.join();
		}
		
		List<Hit> hits=hitSet.stream().sorted(Comparator.comparing(Hit::getLineNumber)).collect(Collectors.toList());		
		
		Charset preferredCharset=null;
		int maxContador=0;
		
		for(Buscador buscador:buscadores) {
			if (buscador.getEncuentros()>maxContador) {
				preferredCharset=buscador.getCharset();
			}
		};
		
		SearchResult searchResult=new SearchResult(hits,preferredCharset);
		return searchResult;
	}

	private static class Buscador extends Thread {
		
		byte[] contenido;
		String standardText;
		Pattern regPattern;
		Set<Hit> hitSet;
		String filePath;
		Charset charset;
		AtomicInteger encuentros=new AtomicInteger();

		public Buscador(byte[] contenido, String standardText, Pattern regPattern, Set<Hit> hitSet, String filePath, boolean autoStart, Charset charset) throws InterruptedException {
			super();
			this.contenido = contenido;
			this.standardText = standardText;
			this.regPattern=regPattern;
			this.hitSet = hitSet;
			this.filePath = filePath;
			this.charset = charset;
			if (autoStart) {
				start();
			}
		}
		

		public Charset getCharset() {
			return charset;
		}


		@Override
		public void run() {
			searchText(contenido, standardText, regPattern, hitSet, filePath, charset, encuentros);
		}
		

		public int getEncuentros() {
			return encuentros.get();
		}
		
		private static void searchText(byte[] contenido, String text, Pattern regPattern, Set<Hit> hitSet, String filePath, Charset charset,AtomicInteger encuentros) {
			String lineOrig="";
			int lineNum=0;
			ByteArrayInputStream bais=new ByteArrayInputStream(contenido);
			BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(bais,charset));
			try {
				while ((lineOrig=bufferedReader.readLine())!=null) {
					lineNum++;
					boolean encontrado=false;
					String line=getStandardText(lineOrig);
					if (text!=null) {
						if (line.indexOf(text)>-1) {
							encontrado=true;
						}
					}
					if (!encontrado && regPattern!=null) {
						if (regPattern.matcher(line).matches()) {
							encontrado=true;
						}						
					}
					if (encontrado) {
						Hit hit=new Hit(lineNum, lineOrig);
						encuentros.incrementAndGet();
						if (!hitSet.contains(hit)) {
							hitSet.add(hit);
						}
					}
				}		
			} catch (Exception r) {
				System.out.println("Error leyendo "+filePath+" :: "+r);
				encuentros.set(-1);
			}
		}		
		
		
	}






	public static byte[] addAll(final byte[] array1, byte[] array2) {
	    byte[] joinedArray = Arrays.copyOf(array1, array1.length + array2.length);
	    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
	    return joinedArray;
	}







	public void endSearch() {
		stop=true;
	}
	
	

	
	
	
	
	
	
	
}
