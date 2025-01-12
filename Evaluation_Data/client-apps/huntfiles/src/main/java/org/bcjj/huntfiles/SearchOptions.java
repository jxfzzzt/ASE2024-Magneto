package org.bcjj.huntfiles;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;

public class SearchOptions {

	/*only set through set methods not directly into fields*/
	private String dir=null;
	private String filename=null;
	private String standardText=null;
	private String originalText=null;
	private Pattern regPattern=null;
	private boolean regex=false;
	private boolean recursive=true;
	private Long after=null;
	private Long before=null;
	private Date afterDate=null;
	private Date beforeDate=null;
	private Long greaterThan=null;
	private Long lessThan=null;
	private boolean zipjar=false;
	private boolean z7=false;
	private boolean rar=false;
	
	private List<String> ignorePaths=null;
	private boolean valid=false;
	
	private boolean searchAnsi=false;
	private boolean searchUtf8=false;
	private boolean searchUtf16=false;
	


	public boolean isValid() {
		return valid;
	}

	private boolean console=false;


	public SearchOptions(String dir) {
		setDir(dir);
	}

	public SearchOptions(String [] args) throws Exception {
        Options options = new Options();

        Option consoleOpt = new Option("con", "console", false, "execute in console mode (default graphical interface)");
        consoleOpt.setRequired(false);
        options.addOption(consoleOpt);
        
        Option dirOpt = new Option("d", "dir", true, "search directory");
        dirOpt.setRequired(false);
        options.addOption(dirOpt);

        Option filenameOpt = new Option("f", "file", true, "search file name");
        filenameOpt.setRequired(false);
        options.addOption(filenameOpt);
        
        Option textOpt = new Option("t", "text", true, "search text");
        textOpt.setRequired(false);
        options.addOption(textOpt);

        Option regexpOpt = new Option("reg", "reg", false, "search text is a regular expression");
        regexpOpt.setRequired(false);
        options.addOption(regexpOpt);        
        
        Option recOpt = new Option("r", "recursive", false, "recursive search (default), search in sub-directories");
        recOpt.setRequired(false);
        options.addOption(recOpt);
        
        Option norecOpt = new Option("nr", "nonrecursive", false, "non recursive search");
        norecOpt.setRequired(false);
        options.addOption(norecOpt);
        
        Option afterOpt = new Option("a", "after", true, "after (yyyy/MM/dd HH:mm:ss)");
        afterOpt.setRequired(false);
        options.addOption(afterOpt);
        
        Option beforeOpt = new Option("b", "before", true, "before (yyyy/MM/dd HH:mm:ss)");
        beforeOpt.setRequired(false);
        options.addOption(beforeOpt);
        
        Option greaterThanOpt = new Option("gt", "greaterthan", true, "greater than (kb)");
        greaterThanOpt.setRequired(false);
        options.addOption(greaterThanOpt);
        
        Option lessThanOpt = new Option("ls", "lessthan", true, "less than (kb)");
        lessThanOpt.setRequired(false);
        options.addOption(lessThanOpt);
        
        Option zipjarOpt = new Option("z", "zip", false, "search in zip and jar");
        zipjarOpt.setRequired(false);
        options.addOption(zipjarOpt);
        
        Option z7Opt = new Option("7z", "7z", false, "search in 7z");
        z7Opt.setRequired(false);
        options.addOption(z7Opt);
        
        Option rarOpt = new Option("rar", "rar", false, "search in rar");
        rarOpt.setRequired(false);
        options.addOption(rarOpt);

        Option utf16 = new Option("utf16", "utf16", false, "search using utf16 (default using ansi and utf8)");
        utf16.setRequired(false);
        options.addOption(utf16);        

        Option noAnsi = new Option("na", "no_ansi", false, "search without using ansi");
        noAnsi.setRequired(false);
        options.addOption(noAnsi);   
        
        Option noUtf8 = new Option("nu8", "no_utf8", false, "search without using ansi utf8");
        noUtf8.setRequired(false);
        options.addOption(noUtf8);           
        
        Option help1Opt = new Option("h", "h", false, "help");
        help1Opt.setRequired(false);
        options.addOption(help1Opt);
        
        Option help2Opt = new Option("help", "help", false, "help");
        help2Opt.setRequired(false);
        options.addOption(help2Opt);
        
        Option help3Opt = new Option("ayuda", "ayuda", false, "ayuda");
        help3Opt.setRequired(false);
        options.addOption(help3Opt);
        
        Option ignorePathOpt = new Option("i","ignorepath",true,"ignore paths (list of paths to ignore (wildcard *)");
        ignorePathOpt.setArgs(Option.UNLIMITED_VALUES);
        ignorePathOpt.setRequired(false);
        options.addOption(ignorePathOpt);
        
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd=null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            throw e;
        }

        console=cmd.hasOption(consoleOpt.getLongOpt());
        
        setDir(cmd.getOptionValue(dirOpt.getLongOpt()));
        setFilename(cmd.getOptionValue(filenameOpt.getLongOpt(),""));
        
        setText(cmd.getOptionValue(textOpt.getLongOpt(),""));
        
        if (cmd.hasOption(regexpOpt.getLongOpt())) {
        	setIsRegExp(true);
        }
        
        setRecursive(true);
        if (cmd.hasOption(norecOpt.getLongOpt())) {
        	setRecursive(false);
        }
        if (cmd.hasOption(recOpt.getLongOpt())) {
        	setRecursive(true);
        }
        setAfter(cmd.getOptionValue(afterOpt.getLongOpt(),""));
        setBefore(cmd.getOptionValue(beforeOpt.getLongOpt(),""));
        setGreater(cmd.getOptionValue(greaterThanOpt.getLongOpt(),""));
        setLessThan(cmd.getOptionValue(lessThanOpt.getLongOpt(),""));
        setZipjar(false);
        if (cmd.hasOption(zipjarOpt.getLongOpt())) {
        	setZipjar(true);
        }
        setZ7(false);
        if (cmd.hasOption(z7Opt.getLongOpt())) {
        	setZ7(true);
        }
        setRar(false);
        if (cmd.hasOption(rarOpt.getLongOpt())) {
        	setRar(true);
        }
        
        setSearchAnsi(true);
        if (cmd.hasOption(noAnsi.getLongOpt())) {
        	setSearchAnsi(false);
        }        
        
        setSearchUtf8(true);
        if (cmd.hasOption(noUtf8.getLongOpt())) {
        	setSearchUtf8(false);
        }  
        
        setSearchUtf16(false);
        if (cmd.hasOption(utf16.getLongOpt())) {
        	setSearchUtf16(true);
        }          

        String [] ignorePaths=cmd.getOptionValues(ignorePathOpt.getLongOpt()); 
        if (ignorePaths!=null) {
        	List<String>paths=Arrays.asList(ignorePaths);
        	setIgnorePaths(paths);
        }
        

        valid=true;
        if (console && StringUtils.isBlank(getDir())) {
        	formatter.printHelp("utility-name", options);
        	System.out.println("In console mode must be set: --dir path");
        	valid=false;
        } else {
        	if (cmd.hasOption(help1Opt.getLongOpt()) || cmd.hasOption(help2Opt.getLongOpt()) || cmd.hasOption(help3Opt.getLongOpt())) {
        		formatter.printHelp("utility-name", options);
        	}
        }
        
	}
	
	public boolean isConsole() {
		return console;
	}

	private String getStr(String str) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		return str.trim();
	}
	
	private Date getDate(String dateStr) throws Exception {
		if (dateStr==null || dateStr.trim().equals("")) {
			return null;
		}
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date d=sdf.parse(dateStr.trim());
			return d;
		} catch (Exception r) {
			//ignore
		}
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm");
			Date d=sdf.parse(dateStr.trim());
			return d;
		} catch (Exception r) {
			//ignore
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
		Date d=sdf.parse(dateStr.trim());
		return d;
	}

	private Long getBytes(String numStr) {
		if (numStr==null || numStr.trim().equals("")) {
			return null;
		}
		numStr=numStr.toLowerCase().trim();
		
		String last=numStr.substring(numStr.length()-1,numStr.length());
		String ini=numStr.substring(0,numStr.length()-1).trim();
		//System.out.println("ini:"+ini+"  last:"+last);
		
		long size=0;
		if (last.equals("b")) {
			size=Long.parseLong(ini);
		} else if (last.equals("k")) {
			size=Long.parseLong(ini)*1024;
		} else if (last.equals("m")) {
			size=Long.parseLong(ini)*1024*1024;
		} else if (last.equals("g")) {
			size=Long.parseLong(ini)*1024*1024*1024;
		} else {
			//default: k
			size=Long.parseLong(numStr)*1024;
		} 
		return size;
	}
	
	public String getOriginalText() {
		return originalText;
	}
	
	public String getStandardText() {
		return standardText;
	}

	public void setText(String text) {
		this.standardText = HuntFiles.getStandardText(getStr(text));
		this.originalText=text;
		regPattern=null;
	}

	public void setIsRegExp(boolean regexp) {
		regex=regexp;
		regPattern=null;
	}
	
	
	Pattern getRegex() {
		if (!regex) {
			return null;
		}
		if (regPattern==null) {
			regPattern=Pattern.compile(originalText);
		}
		return regPattern;
	}	
	
	
	
	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = getStr(dir);
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = getStr(filename);
	}



	
	
	public boolean isRecursive() {
		return recursive;
	}

	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}

	public Long getAfter() {
		return after;
	}

	public Date getAfterDate() {
		return afterDate;
	}
	
	public void setAfter(String afterStr) throws Exception {
		this.afterDate = getDate(afterStr);
		if (this.afterDate==null) {
			this.after=null;
		} else {
			this.after=this.afterDate.getTime();
		}
	}

	public Long getBefore() {
		return before;
	}

	public Date getBeforeDate() {
		return beforeDate;
	}
	
	public void setBefore(String beforeStr) throws Exception {
		this.beforeDate = getDate(beforeStr);
		if (this.beforeDate==null) {
			this.before=null;
		} else {
			this.before=this.beforeDate.getTime();
		}
	}

	public Long getGreaterThan() {
		return greaterThan;
	}

	public void setGreater(String greaterThan) {
		this.greaterThan = getBytes(greaterThan);
	}

	public Long getLessThan() {
		return lessThan;
	}

	public void setLessThan(String lessThan) {
		this.lessThan = getBytes(lessThan);
	}

	public boolean isZipjar() {
		return zipjar;
	}

	public void setZipjar(boolean zipjar) {
		this.zipjar = zipjar;
	}

	public boolean isZ7() {
		return z7;
	}

	public void setZ7(boolean z7) {
		this.z7 = z7;
	}

	public boolean isRar() {
		return rar;
	}

	public void setRar(boolean rar) {
		this.rar = rar;
	}
	
	public boolean isSearchAnsi() {
		return searchAnsi;
	}

	public void setSearchAnsi(boolean searchAnsi) {
		this.searchAnsi = searchAnsi;
	}

	public boolean isSearchUtf8() {
		return searchUtf8;
	}

	public void setSearchUtf8(boolean searchUtf8) {
		this.searchUtf8 = searchUtf8;
	}

	public boolean isSearchUtf16() {
		return searchUtf16;
	}

	public void setSearchUtf16(boolean searchUtf16) {
		this.searchUtf16 = searchUtf16;
	}	
	
	public List<String> getIgnorePaths() {
		return ignorePaths;
	}

	public void setIgnorePaths(List<String> ignorePaths) {
		List<String> ignores=new ArrayList<String>();
		if (ignorePaths==null) {
			ignorePaths=null;
			return;
		}
		for (String path:ignorePaths) {
			if (StringUtils.isNotBlank(path)) {
				path=StringUtils.replace(path, "\\", "/");
				path=StringUtils.replace(path, "//", "/");
				ignores.add(path);
			}
		}
		this.ignorePaths = ignores;
	}


	
}
