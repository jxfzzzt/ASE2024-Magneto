package org.bcjj.huntfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class HuntFilesTest  extends TestCase
{
	
	private String dirResources="C:\\proyectos\\huntfiles\\src\\test\\resources";
	
    public HuntFilesTest( String testName )  {
        super( testName );
    }

    @Override
	protected void setUp() {
      // nada de momento
    }
    
    
    public static Test suite()  {
        return new TestSuite( HuntFilesTest.class );
    }
    
    
    
    public void testHuntFilesContraste() throws Exception {
    	SearchOptions searchOptions=new SearchOptions(dirResources);
    	searchOptions.setText("contraste");
    	searchOptions.setFilename("*.*");
    	searchOptions.setZipjar(true);
    	searchOptions.setZ7(true);
    	searchOptions.setSearchAnsi(true);
    	searchOptions.setSearchUtf8(true);
    	searchOptions.setSearchUtf16(true);
    	
    	HuntFilesListener4Test listener=new HuntFilesListener4Test();
    	HuntFiles huntFiles=new HuntFiles(searchOptions, listener);
    	huntFiles.search();
    	listener.files.forEach(f -> System.out.println("testHuntFilesContraste:"+f.getName()));
    	assertEquals("Esperaba solo 3 ficheros con 'contraste'",3, listener.files.size());
    	
    	List<String> nombres=listener.files.stream().map(FileInfo::getName).collect(Collectors.toList());
    	
    	assertTrue("Esperaba contraste.txt",nombres.contains("contraste.txt"));
    	assertTrue("Esperaba contraste.txt : (resources.zip)",nombres.contains("contraste.txt : (resources.zip)"));
    	assertTrue("Esperaba contraste.txt : (resources.7z)",nombres.contains("contraste.txt : (resources.7z)"));
    	assertEquals("no esperaba errores",0, listener.errores.size());
    }
    
    public void testHuntFilesContrasteRegexp() throws Exception {
    	
    	Pattern p=Pattern.compile(".*con.*te.*");
    	Matcher m=p.matcher("este es para contraste negativo.");
    	System.out.println("m:"+m.matches());
    	
    	
    	
    	SearchOptions searchOptions=new SearchOptions(dirResources);
    	searchOptions.setText(".*con.*te.*");
    	searchOptions.setIsRegExp(true);
    	searchOptions.setFilename("contraste.txt");
    	searchOptions.setZipjar(true);
    	searchOptions.setZ7(true);
    	searchOptions.setSearchAnsi(true);
    	searchOptions.setSearchUtf8(true);
    	searchOptions.setSearchUtf16(true);
    	
    	HuntFilesListener4Test listener=new HuntFilesListener4Test();
    	HuntFiles huntFiles=new HuntFiles(searchOptions, listener);
    	huntFiles.search();
    	listener.files.forEach(f -> System.out.println("testHuntFilesContraste:"+f.getName()));
    	assertEquals("Esperaba solo 3 ficheros con 'contraste'",3, listener.files.size());
    	
    	List<String> nombres=listener.files.stream().map(FileInfo::getName).collect(Collectors.toList());
    	
    	assertTrue("Esperaba contraste.txt",nombres.contains("contraste.txt"));
    	assertTrue("Esperaba contraste.txt : (resources.zip)",nombres.contains("contraste.txt : (resources.zip)"));
    	assertTrue("Esperaba contraste.txt : (resources.7z)",nombres.contains("contraste.txt : (resources.7z)"));
    	assertEquals("no esperaba errores",0, listener.errores.size());
    }    
    

    public void testHuntFilesNoNo() throws Exception {
    	
    	SearchOptions searchOptions=new SearchOptions(dirResources);
    	char eNNe=209;
    	char enne=241;
    	searchOptions.setText(""+eNNe+"o"+enne+"o"); //Ñoño
    	searchOptions.setFilename("*.*");
    	searchOptions.setZipjar(true);
    	searchOptions.setZ7(true);
    	
    	searchOptions.setSearchAnsi(true);
    	searchOptions.setSearchUtf8(true);
    	searchOptions.setSearchUtf16(true);
    	
    	System.out.println("search form '"+searchOptions.getOriginalText()+"'  in "+dirResources+" ...");
    	
    	HuntFilesListener4Test listener=new HuntFilesListener4Test();
    	HuntFiles huntFiles=new HuntFiles(searchOptions, listener);
    	huntFiles.search();
    	
    	assertEquals("no esperaba errores",0, listener.errores.size());
    	

    	List<String> fichEncontrados=listener.files.stream().map(FileInfo::getName).collect(Collectors.toList());
    	
    	List<String> fichEsperados=Arrays.asList("ansi.txt",
    			"uft8.txt",
    			"content.xml : (calc.ods)",
    			"sharedStrings.xml : (excel.xlsx)",
    			"document.xml : (word.docx)",
    			"word.doc",
    			"excel.xls",
    			"slide1.xml : (powerpoint.pptx)",
    			"powerpoint.ppt",
    			"content.xml : (writer.odt)",
    			"content.xml : (impress.odp)",
    			"ansi.txt : (resources.zip)",
    			"excel.xls : (resources.zip)",
    			"powerpoint.ppt : (resources.zip)",
    			"uft8.txt : (resources.zip)",
    			"word.doc : (resources.zip)",
    			"ansi.txt : (resources.7z)",
    			"excel.xls : (resources.7z)",
    			"powerpoint.ppt : (resources.7z)",
    			"uft8.txt : (resources.7z)",
    			"word.doc : (resources.7z)");
    	
    	
    	List<String> fichEsperados2=new ArrayList<>(fichEsperados);
    	
    	fichEncontrados.forEach(f -> fichEsperados2.remove(f));
    	
    	assertEquals("falta ficheros por encontrar"+fichEsperados2, 0, fichEsperados2.size());
    	
    	fichEsperados.forEach(f -> fichEncontrados.remove(f));
    	
    	assertEquals("sobran ficheros que no deberian haber sido encontrados "+fichEncontrados, 0, fichEncontrados.size());
    	
    	System.out.println("search form 'Ñoño' finalizado ok");
    	
    }
    
    
}
