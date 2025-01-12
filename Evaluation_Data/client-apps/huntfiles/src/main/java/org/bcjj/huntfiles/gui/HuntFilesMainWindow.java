package org.bcjj.huntfiles.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.JTextComponent;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.bcjj.huntfiles.CloseableInputStream;
import org.bcjj.huntfiles.CloseableInputStream.PreCloseableInputStream;
import org.bcjj.huntfiles.FileInfo;
import org.bcjj.huntfiles.Hit;
import org.bcjj.huntfiles.HuntFiles;
import org.bcjj.huntfiles.HuntFilesListener;
import org.bcjj.huntfiles.SearchOptions;


import net.iharder.dnd.FileDrop;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HuntFilesMainWindow implements HuntFilesListener {


	int MAX_COMBO_PREFERENCES=15;
	
	private enum FieldType {
		ComboDirectory,ComboText,ComboFileName,ComboAfter,ComboFindText,Exclusions,Compare; //no cambiar estos nombres
	}
	
	private enum TypeFind {init, up, down};
	
	private JFrame frmHuntfiles;
	private JTextField textBefore;
	private JTextField textGreaterThan;
	private JTextField textLessThan;
	private JTextField txtDuplicateEnding;
	private JComboBox<String> comboDirectory;
	private JComboBox<String> comboText;
	private JComboBox<String> comboFileName;
	private JCheckBox checkBoxAfter;
	private JCheckBox checkboxBefore;
	private JCheckBox checkboxZipjar;
	private JCheckBox checkboxRar;
	private JCheckBox checkbox7z;
	private JLabel labelLessThanKb;
	private JLabel labelGreaterThan;
	private JButton buttonStart;
	private JButton buttonStop;
	private JButton buttonToZip;
	private JButton buttonOpenInExplorer;
	private JButton btnDuplicate;
	private JLabel labelDirectory;
	private JLabel labelText;
	private JLabel labelFileName;
	private DefaultComboBoxModel<String> textModel;
	private DefaultComboBoxModel<String> fileNameModel;
	private DefaultComboBoxModel<String> afterModel;
	private DefaultComboBoxModel<String> findTextModel;
	private DefaultComboBoxModel<String> directoryModel;
	private static final String NL="\r\n";
	
	private JComboBox<String> comboAfter;
	private JComboBox<String> comboFindTextPreview;
	private JLabel labelTextInFile;
	private JLabel labelStatus;
	private JPanel panelPreview;
	private JButton botonCompare;
	private JButton botonUp;
	private JButton botonDown;
	private JButton botonOpcionCompare;
	private JButton buttonOpen;
	private JScrollPane scrollPaneFileList;
	private JScrollPane scrollPaneErrores;
	private JTable tableFiles;
	private FilesTableModel filesTableModel;
	int filaAnterior=-1;
	private JTextPane textPaneError;
	private JButton buttonCopy;
	private JButton buttonLastModified;
	private JButton buttonEmpty;
	private JTextField txtLastModified;
	private SimpleImagePanel panelImg;
	private JScrollPane scrollPane;
	private JTextArea textAreaFileText;
	private JButton botonExclusions;
	private JCheckBox chechBoxSubdir;
	
	private boolean inProgress=false;
	private JCheckBox chckbxCopyHits;
	private JCheckBox chckbxCopyOnlyName;
	private JScrollPane scrollPane_1;
	private JTextArea textAreaHits;
	private List<Hit> hitsTextArea;

	private JCheckBox chechBoxAnsi;
	private JCheckBox chechBoxUtf8;
	private JCheckBox chechBoxUtf16;	
	private JCheckBox chckbxRegex;
	
	HuntFiles huntFiles=null;
	private JButton buttonCopyTxt;
	
	private List<String> exclusions=new ArrayList<String>();
	private String compareCommand=null;
	
	DefaultHighlighter.DefaultHighlightPainter mainHighlighter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
	DefaultHighlighter.DefaultHighlightPainter customHighlighter = new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
	private String lastLookFor=null;
	
	boolean exclusionsFromSearchOptions=false;
	private JCheckBox checkboxUpDownText;
	private JButton button;
	private JButton buttonCopyName;
	
	/**
	 * Launch the application.
	 */
	@java.lang.SuppressWarnings("java:S4507")  //Delivering code in production with debug features activated is security-sensitive (Loggers should be used (instead of printStackTrace) to print throwables)
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HuntFilesMainWindow window = new HuntFilesMainWindow();
					window.frmHuntfiles.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@java.lang.SuppressWarnings("java:S4507")  //Delivering code in production with debug features activated is security-sensitive (Loggers should be used (instead of printStackTrace) to print throwables)
	public static void main(final SearchOptions searchOptions) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HuntFilesMainWindow window = new HuntFilesMainWindow(searchOptions);
					window.frmHuntfiles.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	
	public HuntFilesMainWindow() {
		initialize();
		postInitialize();
	}
	
	/**
	 * Create the application.
	 */
	public HuntFilesMainWindow(SearchOptions searchOptions) {
		this();
		initValues(searchOptions);
	}

	public HuntFilesMainWindow(String [] s) throws Exception {
		this();
		SearchOptions searchOptions=new SearchOptions(s);
		initValues(searchOptions);
	}
	
	private void postInitialize() {
		boolean x=true;
		if (x) { //trick for WindowBuilder Pro to parse, ignoring this...
			TextLineNumber textLineNumber = new TextLineNumber(textAreaFileText,5);
			scrollPane.setRowHeaderView( textLineNumber );
		} 	
		
		try {
			List<String> compareCommands=loadPreference(FieldType.Compare);
			compareCommand=compareCommands.get(0);
		} catch (Exception e) {
			compareCommand="C:/Program Files (x86)/Beyond Compare 3/BCompare.exe";
		}
		appendErrMsg("COMPARE COMMAND init with:"+compareCommand);
		
		exclusionsFromSearchOptions=false;
		try {
			exclusions=loadPreference(FieldType.Exclusions);
		} catch (Exception e) {
			exclusions=new ArrayList<String>();
			exclusions.add("*/WEB-INF/log/*");
			exclusions.add("*/WEB-INF/classes/*");
		}
		appendErrMsg("EXCLUSIONES init with:"+exclusions);
	}
	
	private void initValues(SearchOptions searchOptions) {
		
		if (searchOptions.getIgnorePaths()!=null) {
			exclusions=searchOptions.getIgnorePaths();
			exclusionsFromSearchOptions=true;
		} else {
			exclusionsFromSearchOptions=false;
			try {
				exclusions=loadPreference(FieldType.Exclusions);
			} catch (Exception e) {
				exclusions=new ArrayList<String>();
				exclusions.add("*/WEB-INF/log/*");
				exclusions.add("*/WEB-INF/classes/*");
			}
		}
		appendErrMsg("EXCLUSIONES init with:"+exclusions);
		
		if (StringUtils.isNotBlank(searchOptions.getDir())) { 
			comboDirectory.setSelectedItem(searchOptions.getDir());
		}
		if (StringUtils.isNotBlank(searchOptions.getFilename())) {
			comboFileName.setSelectedItem(searchOptions.getFilename());
		}
		if (StringUtils.isNotBlank(searchOptions.getOriginalText())) {
			comboText.setSelectedItem(searchOptions.getOriginalText());
		}
		if (searchOptions.getAfter()!=null) {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date d=new Date(searchOptions.getAfter());
			comboAfter.setSelectedItem(sdf.format(d));
			checkBoxAfter.setSelected(true);
		}
		if (searchOptions.getBefore()!=null) {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date d=new Date(searchOptions.getBefore());
			textBefore.setText(sdf.format(d));
			checkboxBefore.setSelected(true);
		}
		if (searchOptions.getGreaterThan()!=null) {
			textGreaterThan.setText(""+searchOptions.getGreaterThan());
		}
		if (searchOptions.getLessThan()!=null) {
			textLessThan.setText(""+searchOptions.getLessThan());
		}
		if (searchOptions.isZipjar()) {
			checkboxZipjar.setSelected(true);
		}
		if (searchOptions.isZ7()) {
			checkbox7z.setSelected(true);
		}
		if (searchOptions.isRar()) {
			checkboxRar.setSelected(true);
		}
		if (searchOptions.isRecursive()) {
			chechBoxSubdir.setSelected(true);
		} else {
			chechBoxSubdir.setSelected(false);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		textPaneError = new JTextPane();
	
		frmHuntfiles = new JFrame();
		frmHuntfiles.setIconImage(Toolkit.getDefaultToolkit().getImage(HuntFilesMainWindow.class.getResource("/org/bcjj/huntfiles/gui/search.png")));
		frmHuntfiles.setTitle("HuntFiles 1.0");
		frmHuntfiles.setBounds(100, 100, 875, 624);
		frmHuntfiles.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initFileDrop();
		
		JPanel panelCriteria = new JPanel();
		panelCriteria.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelCriteria.setPreferredSize(new Dimension(60, 60));
		frmHuntfiles.getContentPane().add(panelCriteria, BorderLayout.NORTH);
		panelCriteria.setSize(new Dimension(60, 60));
		panelCriteria.setLayout(new BorderLayout(0, 0));
		
		JPanel panelCriteriaLabel = new JPanel();
		panelCriteriaLabel.setPreferredSize(new Dimension(90, 20));
		panelCriteria.add(panelCriteriaLabel, BorderLayout.WEST);
		panelCriteriaLabel.setLayout(null);
		
		labelFileName = new JLabel("File name:");
		labelFileName.setFont(new Font("Tahoma", Font.PLAIN, 10));
		labelFileName.setToolTipText("nombre fichero");
		labelFileName.setBounds(5, 0, 64, 14);
		panelCriteriaLabel.add(labelFileName);
		
		labelText = new JLabel("Text:");
		labelText.setFont(new Font("Tahoma", Font.PLAIN, 10));
		labelText.setToolTipText("texto a buscar");
		labelText.setBounds(5, 22, 46, 14);
		panelCriteriaLabel.add(labelText);
		
		labelDirectory = new JLabel("Directory:");
		labelDirectory.setFont(new Font("Tahoma", Font.PLAIN, 10));
		labelDirectory.setToolTipText("directorio");
		labelDirectory.setBounds(5, 40, 50, 14);
		panelCriteriaLabel.add(labelDirectory);
		
		botonExclusions = new JButton("!");
		botonExclusions.setFont(new Font("Tahoma", Font.PLAIN, 11));
		botonExclusions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirPanelExclusiones();
			}
		});
		botonExclusions.setMargin(new Insets(2, 2, 2, 2));
		botonExclusions.setToolTipText("exclusiones");
		botonExclusions.setBounds(70, 40, 15, 15);
		panelCriteriaLabel.add(botonExclusions);
		
		chechBoxSubdir = new JCheckBox("");
		chechBoxSubdir.setSelected(true);
		chechBoxSubdir.setToolTipText("subdir");
		chechBoxSubdir.setBounds(50, 40, 20, 15);
		panelCriteriaLabel.add(chechBoxSubdir);
		
		chckbxRegex = new JCheckBox("");
		chckbxRegex.setToolTipText("regular expresion");
		chckbxRegex.setBounds(50, 21, 20, 15);
		panelCriteriaLabel.add(chckbxRegex);
		
		JButton botonTextRegexp = new JButton("T");
		botonTextRegexp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirTestRegularExpresion();
			}
		});
		botonTextRegexp.setToolTipText("test Regular Expresion");
		botonTextRegexp.setMargin(new Insets(2, 2, 2, 2));
		botonTextRegexp.setFont(new Font("Tahoma", Font.PLAIN, 11));
		botonTextRegexp.setBounds(70, 21, 15, 15);
		panelCriteriaLabel.add(botonTextRegexp);
		
		JPanel panelCriteriaParameters = new JPanel();
		panelCriteria.add(panelCriteriaParameters, BorderLayout.CENTER);
		panelCriteriaParameters.setLayout(new BorderLayout(0, 0));
		
		comboDirectory = new JComboBox<String>();
		directoryModel = createDirectoryModel();
		comboDirectory.setModel(directoryModel);
		comboDirectory.setEditable(true);
		comboDirectory.setPreferredSize(new Dimension(29, 20));
		comboDirectory.setMinimumSize(new Dimension(29, 20));
		panelCriteriaParameters.add(comboDirectory, BorderLayout.SOUTH);
		
		comboText = new JComboBox<String>();
		comboText.setToolTipText("plain text");
		textModel = createTextModel();
		comboText.setModel(textModel);
		comboText.setEditable(true);
		panelCriteriaParameters.add(comboText, BorderLayout.CENTER);
		
		comboFileName = new JComboBox<String>();
		comboFileName.setToolTipText("dos expression  *.*");
		fileNameModel = createFileNameModel();
		comboFileName.setModel(fileNameModel);
		comboFileName.setEditable(true);
		comboFileName.setMinimumSize(new Dimension(29, 20));
		comboFileName.setPreferredSize(new Dimension(29, 20));
		panelCriteriaParameters.add(comboFileName, BorderLayout.NORTH);
		
		JPanel panelCriteriaParams2 = new JPanel();
		panelCriteriaParams2.setPreferredSize(new Dimension(380, 10));
		panelCriteria.add(panelCriteriaParams2, BorderLayout.EAST);
		panelCriteriaParams2.setLayout(null);
		
		checkBoxAfter = new JCheckBox("after");
		checkBoxAfter.setFont(new Font("Tahoma", Font.PLAIN, 9));
		checkBoxAfter.setToolTipText("modificado antes de");
		checkBoxAfter.setBounds(0, 0, 49, 14);
		panelCriteriaParams2.add(checkBoxAfter);
		
		checkboxBefore = new JCheckBox("before"); 
		checkboxBefore.setFont(new Font("Tahoma", Font.PLAIN, 9));
		checkboxBefore.setToolTipText("modificado despues de");
		checkboxBefore.setBounds(0, 17, 58, 23);
		checkboxBefore.setMargin(new Insets(2, 2, 2, 2));
		panelCriteriaParams2.add(checkboxBefore);
		
		textBefore = new JTextField();
		textBefore.setFont(new Font("Tahoma", Font.PLAIN, 10));
		textBefore.setToolTipText("formato:  yyyy/MM/dd HH:mm:ss");
		textBefore.setBounds(60, 18, 119, 20);
		panelCriteriaParams2.add(textBefore);
		textBefore.setColumns(10);
		
		checkboxZipjar = new JCheckBox("zip,jar");
		checkboxZipjar.setToolTipText("zip,jar,war");
		checkboxZipjar.setFont(new Font("Tahoma", Font.PLAIN, 9));
		checkboxZipjar.setBounds(0, 37, 52, 23);
		panelCriteriaParams2.add(checkboxZipjar);
		
		checkboxRar = new JCheckBox("rar"); 
		//checkboxRar.setVisible(false); //TODO: check rar
		checkboxRar.setFont(new Font("Tahoma", Font.PLAIN, 9));
		checkboxRar.setBounds(52, 37, 39, 23);
		panelCriteriaParams2.add(checkboxRar);
		
		checkbox7z = new JCheckBox("7z"); 
		checkbox7z.setFont(new Font("Tahoma", Font.PLAIN, 9));
		checkbox7z.setBounds(89, 37, 37, 23);
		checkbox7z.setToolTipText("7z very slow");
		panelCriteriaParams2.add(checkbox7z);
		
		labelLessThanKb = new JLabel("< (k)");
		labelLessThanKb.setFont(new Font("Tahoma", Font.PLAIN, 9));
		labelLessThanKb.setToolTipText("tama\u00F1o menor de ([B Bytes,K kiloBytes,M megaBytes ,G gigaBytes] defecto K)");
		labelLessThanKb.setBounds(210, 0, 25, 14);
		panelCriteriaParams2.add(labelLessThanKb);
		
		labelGreaterThan = new JLabel("> (k)");
		labelGreaterThan.setFont(new Font("Tahoma", Font.PLAIN, 9));
		labelGreaterThan.setToolTipText("tama\u00F1o mayor de  ([B Bytes,K kiloBytes,M megaBytes ,G gigaBytes] defecto K)");
		labelGreaterThan.setBounds(210, 21, 25, 14);
		panelCriteriaParams2.add(labelGreaterThan);
		
		textGreaterThan = new JTextField();
		textGreaterThan.setToolTipText("size greatter than ([B Bytes,K kiloBytes,M megaBytes ,G gigaBytes] default K)");
		textGreaterThan.setBounds(235, 18, 65, 18);
		panelCriteriaParams2.add(textGreaterThan);
		textGreaterThan.setColumns(10);
		
		textLessThan = new JTextField();
		textLessThan.setToolTipText("size less than ([B Bytes,K kiloBytes,M megaBytes ,G gigaBytes] default K)");
		textLessThan.setBounds(235, 0, 65, 18);
		panelCriteriaParams2.add(textLessThan);
		textLessThan.setColumns(10);
		
		buttonStart = new JButton("START");
		buttonStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				starSearch();
			}
		});
		buttonStart.setFont(new Font("Tahoma", Font.BOLD, 13));
		buttonStart.setToolTipText("INICIAR BUSQUEDA");
		buttonStart.setBounds(132, 39, 100, 19);
		buttonStart.setMargin(new Insets(2, 2, 2, 2));
		panelCriteriaParams2.add(buttonStart);
		frmHuntfiles.getRootPane().setDefaultButton(buttonStart);
		
		buttonStop = new JButton("Stop");
		buttonStop.setFont(new Font("Tahoma", Font.BOLD, 12));
		buttonStop.setBounds(235, 39, 65, 18);
		buttonStop.setMargin(new Insets(2, 2, 2, 2));
		buttonStop.setToolTipText("PARAR BUSQUEDA");
		panelCriteriaParams2.add(buttonStop);
		buttonStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopSearch();
			}
		});
		
		comboAfter = new JComboBox<String>();
		afterModel = createAfterModel();
		comboAfter.setModel(afterModel);
		comboAfter.setFont(new Font("Tahoma", Font.PLAIN, 10));
		comboAfter.setEditable(true);
		comboAfter.setToolTipText("format:  yyyy/MM/dd HH:mm:ss #coment");
		comboAfter.setBounds(60, 0, 135, 20);
		panelCriteriaParams2.add(comboAfter);
		

		
		chechBoxAnsi = new JCheckBox("ANSI");
		chechBoxAnsi.setSelected(true);
		chechBoxAnsi.setFont(new Font("Tahoma", Font.PLAIN, 9));
		chechBoxAnsi.setToolTipText("buscar texto usando codificacion ANSI (ISO_8859_1) ");
		chechBoxAnsi.setBounds(306, -4, 49, 23);
		panelCriteriaParams2.add(chechBoxAnsi);
		
		chechBoxUtf8 = new JCheckBox("UTF-8");
		chechBoxUtf8.setSelected(true);
		chechBoxUtf8.setFont(new Font("Tahoma", Font.PLAIN, 9));
		chechBoxUtf8.setToolTipText("buscar texto usando codificacion UTF-8 (UTF_8)");
		chechBoxUtf8.setBounds(306, 17, 58, 23);
		panelCriteriaParams2.add(chechBoxUtf8);
		
		chechBoxUtf16 = new JCheckBox("UTF-16");
		chechBoxUtf16.setFont(new Font("Tahoma", Font.PLAIN, 9));
		chechBoxUtf16.setToolTipText("buscar texto usando codificacion UTF-16 (UTF_16, UTF_16BE, UTF_16LE)");
		chechBoxUtf16.setBounds(306, 37, 58, 23);
		panelCriteriaParams2.add(chechBoxUtf16);

		
		JPanel panelResultContainer = new JPanel();
		frmHuntfiles.getContentPane().add(panelResultContainer, BorderLayout.CENTER);
		panelResultContainer.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPanelResultContainer = new JSplitPane();
		splitPanelResultContainer.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panelResultContainer.add(splitPanelResultContainer);
		splitPanelResultContainer.setDividerLocation(240);
		
		JPanel panelResults = new JPanel();
		panelResults.setPreferredSize(new Dimension(100, 140));
		splitPanelResultContainer.setLeftComponent(panelResults);
		panelResults.setLayout(new BorderLayout(0, 0));
		
		JPanel panelFileList = new JPanel();
		panelFileList.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelResults.add(panelFileList, BorderLayout.CENTER);
		panelFileList.setLayout(new BorderLayout(0, 0));
		

		
		scrollPaneFileList = new JScrollPane();
		panelFileList.add(scrollPaneFileList);
		
		tableFiles = new JTable() {
		    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		        Component c = super.prepareRenderer(renderer, row, column);
		        if (c instanceof JComponent) {
		           //if(column == X){
		            JComponent jc = (JComponent) c;
		            jc.setToolTipText(getValueAt(row, column).toString());
		           //}
		        }
		        return c;
		    }
		};
		
		
		tableFiles.setFont(new Font("Tahoma", Font.PLAIN, 10));
		scrollPaneFileList.setViewportView(tableFiles);
		filesTableModel = new FilesTableModel(tableFiles);
		tableFiles.setModel(filesTableModel);
		tableFiles.setAutoCreateRowSorter(true);
		tableFiles.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					//System.out.println("jtable.selecciontionListener "+e);
					registroTablaFileSeleccionado(e);
				}
			}
			
		});
		
		
		JPanel panelOptions = new JPanel();
		panelOptions.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelOptions.setSize(new Dimension(50, 0));
		panelOptions.setPreferredSize(new Dimension(105, 10));
		panelResults.add(panelOptions, BorderLayout.EAST);
		panelOptions.setLayout(null);
		
		buttonToZip = new JButton("to Zip");
		buttonToZip.setMargin(new Insets(2, 2, 2, 2));
		buttonToZip.setFont(new Font("Tahoma", Font.PLAIN, 11));
		buttonToZip.setToolTipText("crear zip con los resultados seleccionados");
		buttonToZip.setBounds(5, 5, 95, 15);
		panelOptions.add(buttonToZip);
		buttonToZip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectToZip();
			}
		});
		
		buttonOpenInExplorer = new JButton("explorer");
		buttonOpenInExplorer.setMargin(new Insets(2, 2, 2, 2));
		buttonOpenInExplorer.setToolTipText("abrir en explorer");
		buttonOpenInExplorer.setFont(new Font("Tahoma", Font.PLAIN, 11));
		buttonOpenInExplorer.setBounds(5, 25, 95, 15);
		panelOptions.add(buttonOpenInExplorer);
		buttonOpenInExplorer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openInExplorer();
			}
		});
		
		btnDuplicate = new JButton("duplicate");
		btnDuplicate.setMargin(new Insets(2, 2, 2, 2));
		btnDuplicate.setToolTipText("crea duplicado");
		btnDuplicate.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnDuplicate.setBounds(5, 45, 95, 15);
		panelOptions.add(btnDuplicate);
		btnDuplicate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				duplicar();
			}
		});
		
		txtDuplicateEnding = new JTextField();
		txtDuplicateEnding.setToolTipText("terminacion de duplicado");
		txtDuplicateEnding.setFont(new Font("Tahoma", Font.PLAIN, 9));
		txtDuplicateEnding.setText("-ORIG");
		txtDuplicateEnding.setBounds(5, 60, 95, 15);
		panelOptions.add(txtDuplicateEnding);
		txtDuplicateEnding.setColumns(10);
		
		botonCompare = new JButton("compare");
		botonCompare.setToolTipText("elige 2 y comparalos");
		botonCompare.setMargin(new Insets(2, 2, 2, 2));
		botonCompare.setFont(new Font("Tahoma", Font.PLAIN, 11));
		botonCompare.setBounds(5, 80, 77, 15);
		panelOptions.add(botonCompare);
		botonCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comparar();
			}
		});
		
		botonOpcionCompare = new JButton("?");
		botonOpcionCompare.setToolTipText("settings / ajustes de comparacion");
		botonOpcionCompare.setFont(new Font("Tahoma", Font.PLAIN, 9));
		botonOpcionCompare.setMargin(new Insets(1, 1, 2, 1));
		botonOpcionCompare.setBounds(85, 80, 15, 15);
		panelOptions.add(botonOpcionCompare);
		botonOpcionCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				compareOptions();
			}
		});
		
		buttonOpen = new JButton("open");
		buttonOpen.setPreferredSize(new Dimension(87, 23));
		buttonOpen.setFont(new Font("Tahoma", Font.PLAIN, 11));
		buttonOpen.setMargin(new Insets(2, 2, 2, 2));
		buttonOpen.setToolTipText("abrir");
		buttonOpen.setBounds(5, 100, 95, 15);
		buttonOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});
		panelOptions.add(buttonOpen);
		
		
		
		chckbxCopyOnlyName = new JCheckBox("");
		chckbxCopyOnlyName.setToolTipText("only name (no path)");
		chckbxCopyOnlyName.setBounds(1, 120, 19, 15);
		chckbxCopyOnlyName.setMargin(new Insets(2, 2, 2, 2));
		panelOptions.add(chckbxCopyOnlyName);
		
		buttonCopyTxt = new JButton("copy path");
		buttonCopyTxt.setFont(new Font("Tahoma", Font.PLAIN, 11));
		buttonCopyTxt.setMargin(new Insets(2, 1, 2, 2));
		buttonCopyTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyFilesTxt();
			}
		});
		buttonCopyTxt.setBounds(20, 120, 61, 15);
		panelOptions.add(buttonCopyTxt);
		

		
		
		chckbxCopyHits = new JCheckBox("");
		chckbxCopyHits.setToolTipText("with hits");
		chckbxCopyHits.setBounds(80, 120, 20, 15);
		panelOptions.add(chckbxCopyHits);

		
		buttonCopy = new JButton("copy");
		buttonCopy.setFont(new Font("Tahoma", Font.PLAIN, 11));
		buttonCopy.setMargin(new Insets(2, 2, 2, 2));
		buttonCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyFiles();
			}
		});
		buttonCopy.setBounds(5, 140, 95, 15);
		panelOptions.add(buttonCopy);
		

		
		
		buttonLastModified = new JButton("setTime");
		buttonLastModified.setFont(new Font("Tahoma", Font.PLAIN, 11));
		buttonLastModified.setMargin(new Insets(2, 2, 2, 2));
		buttonLastModified.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLastModified();
			}
		});
		buttonLastModified.setBounds(5, 160, 95, 15);
		panelOptions.add(buttonLastModified);
		
		txtLastModified = new JTextField();
		txtLastModified.setToolTipText("fecha hora yyyy/MM/dd HH:mm:ss");
		txtLastModified.setFont(new Font("Tahoma", Font.PLAIN, 9));
		txtLastModified.setText("");
		txtLastModified.setBounds(5, 175, 95, 15);
		panelOptions.add(txtLastModified);
		txtLastModified.setColumns(10);	
		
		buttonEmpty = new JButton("empty");
		buttonEmpty.setToolTipText("vaciar fichero");
		buttonEmpty.setFont(new Font("Tahoma", Font.PLAIN, 11));
		buttonEmpty.setMargin(new Insets(2, 2, 2, 2));
		buttonEmpty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				emptyFiles();
			}
		});
		buttonEmpty.setBounds(5, 195, 95, 15);
		panelOptions.add(buttonEmpty);
		
		
		//JPanel panelFilePreview0 = new JPanel();
		//panelFilePreview0.setPreferredSize(new Dimension(100, 100));
		//panelFilePreview0.setLayout(new BorderLayout(0, 0));
		//splitPanelResultContainer.setRightComponent(panelFilePreview0);
		
		JSplitPane splitPanelResultContainer2 = new JSplitPane();
		splitPanelResultContainer2.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPanelResultContainer2.setDividerLocation(50);		
		splitPanelResultContainer.setRightComponent(splitPanelResultContainer2);
		
		JPanel panelErrores = new JPanel();
		panelErrores.setBorder(new LineBorder(new Color(0, 0, 0)));
		//panelFilePreview.add(panelPreview, BorderLayout.CENTER);
		splitPanelResultContainer2.setLeftComponent(panelErrores);
		panelErrores.setLayout(new BorderLayout(0, 0));
		
		scrollPaneErrores = new JScrollPane();
		panelErrores.add(scrollPaneErrores, BorderLayout.CENTER);
		
		
		textPaneError.setBackground(Color.LIGHT_GRAY);
		textPaneError.setFont(new Font("Tahoma", Font.PLAIN, 9));
		scrollPaneErrores.setViewportView(textPaneError);
		
		JSplitPane splitPanelResultContainer3 = new JSplitPane();
		splitPanelResultContainer3.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPanelResultContainer3.setDividerLocation(50);		
		splitPanelResultContainer2.setRightComponent(splitPanelResultContainer3);
		
		//panelImg = new JPanel();
		panelImg=new SimpleImagePanel();
		panelImg.setPreferredSize(new Dimension(100, 10));
		splitPanelResultContainer3.setLeftComponent(panelImg);
		
		JSplitPane splitPanelResultContainer4 = new JSplitPane();
		splitPanelResultContainer4.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPanelResultContainer4.setDividerLocation(50);		
		splitPanelResultContainer3.setRightComponent(splitPanelResultContainer4);
		
		JPanel panelFilePreviewHits = new JPanel();
		panelFilePreviewHits.setPreferredSize(new Dimension(100, 100));
		panelFilePreviewHits.setLayout(new BorderLayout(0, 0));
		splitPanelResultContainer4.setLeftComponent(panelFilePreviewHits);	
		
		scrollPane_1 = new JScrollPane();
		panelFilePreviewHits.add(scrollPane_1, BorderLayout.CENTER);
		
		textAreaHits = new JTextArea();
		textAreaHits.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()==2) {
					dobleClickEnTextAreaHits(e);
				}
			}
		});
		scrollPane_1.setViewportView(textAreaHits);
		
		JPanel panelFilePreview = new JPanel();
		panelFilePreview.setPreferredSize(new Dimension(100, 100));
		panelFilePreview.setLayout(new BorderLayout(0, 0));
		splitPanelResultContainer4.setRightComponent(panelFilePreview);
		
		panelPreview = new JPanel();
		panelPreview.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelPreview.setSize(new Dimension(100, 0));
		panelPreview.setPreferredSize(new Dimension(100, 10));
		panelPreview.setMinimumSize(new Dimension(200, 10));
		panelFilePreview.add(panelPreview, BorderLayout.CENTER);
		panelPreview.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		panelPreview.add(scrollPane, BorderLayout.CENTER);
		
		textAreaFileText = new JTextArea();
		scrollPane.setViewportView(textAreaFileText);

		
		
		JPanel panelPreviewOptions = new JPanel();
		panelPreviewOptions.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelPreviewOptions.setSize(new Dimension(100, 0));
		panelPreviewOptions.setPreferredSize(new Dimension(100, 10));
		panelPreviewOptions.setMinimumSize(new Dimension(200, 10));
		panelFilePreview.add(panelPreviewOptions, BorderLayout.EAST);
		panelPreviewOptions.setLayout(null);
		
		comboFindTextPreview = new JComboBox<String>();
		comboFindTextPreview.setEditable(true);
		findTextModel = createFindTextModel();
		comboFindTextPreview.setModel(findTextModel);
		comboFindTextPreview.setFont(new Font("Tahoma", Font.PLAIN, 9));
		comboFindTextPreview.setBounds(0, 15, 100, 20);
		panelPreviewOptions.add(comboFindTextPreview);
		
		labelTextInFile = new JLabel("find:");
		labelTextInFile.setToolTipText("buscar texto en fichero");
		labelTextInFile.setBounds(2, 1, 33, 14);
		panelPreviewOptions.add(labelTextInFile);
		
		botonUp = new JButton("up");
		botonUp.setFont(new Font("Tahoma", Font.PLAIN, 9));
		botonUp.setMargin(new Insets(2, 2, 2, 2));
		botonUp.setBounds(10, 36, 23, 23);
		panelPreviewOptions.add(botonUp);
		botonUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				findUp();
			}
		});
		
		botonDown = new JButton("down");
		botonDown.setMargin(new Insets(2, 2, 2, 2));
		botonDown.setFont(new Font("Tahoma", Font.PLAIN, 9));
		botonDown.setBounds(43, 36, 47, 23);
		panelPreviewOptions.add(botonDown);
		
		checkboxUpDownText = new JCheckBox("text");
		checkboxUpDownText.setFont(new Font("Tahoma", Font.PLAIN, 9));
		checkboxUpDownText.setToolTipText("selected: up and down for both text (search option) and find. Not selected, only up and down for find.");
		checkboxUpDownText.setSelected(true);
		checkboxUpDownText.setBounds(45, 1, 50, 14);
		panelPreviewOptions.add(checkboxUpDownText);
		botonDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				findDown();
			}
		});
		
		JPanel panelStatus = new JPanel();
		panelStatus.setPreferredSize(new Dimension(20, 20));
		frmHuntfiles.getContentPane().add(panelStatus, BorderLayout.SOUTH);
		panelStatus.setLayout(new BorderLayout(0, 0));
		
		labelStatus = new JLabel("HuntFiles");
		labelStatus.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelStatus.add(labelStatus, BorderLayout.CENTER);
		
		setEnabled(true);
	}

	
	
	protected void abrirTestRegularExpresion() {
		TestRegExp t=new TestRegExp();
		t.setVisible(true);
	}

	protected void setLastModified() {
		String fecha=txtLastModified.getText();
		
		Date date = null;
		SimpleDateFormat sdf=null;
		try {
			sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			date=sdf.parse(fecha);
		} catch (ParseException e) {
			sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm");
			try {
				date=sdf.parse(fecha);
			} catch (ParseException e1) {
				showMessage("ERROR "+fecha+" is not  yyyy/MM/dd HH:mm:ss   or   yyyy/MM/dd HH:mm  "+e);
				return;
			}
		}

		List<FileInfo> files=getSelectedFiles();
        List<File> listOfFiles = new ArrayList();
        Set<String> fichs=new HashSet<String>();
        int errs=0;
        int oks=0;
        for (FileInfo fileInfo:files) {
        	if (!fichs.contains(fileInfo.getFile().getPath())) {
        		fichs.add(fileInfo.getFile().getPath());
        		listOfFiles.add(fileInfo.getFile());
        		boolean hecho=fileInfo.getFile().setLastModified(date.getTime());
        		long verif=fileInfo.getFile().lastModified();
        		if (verif!=date.getTime() || !hecho) {
        			errs++;
        			appendErrMsg("ERROR setting last modified "+fileInfo.getFile()+" modified on "+sdf.format(new Date(date.getTime()))+" instead of "+sdf.format(date));
        		} else {
        			oks++;
        		}
        	}
        }
        if (errs>0) {
        	showMessage("ERRORS. See ErrorPane errors:"+errs+"/"+(oks+errs));
        } else {
        	showMessage("done!, updated "+oks+" files to "+sdf.format(date));
        }
        
	}

	public int getLineStartIndex(JTextComponent textComp, int lineNumber) {
	    if (lineNumber == 0) { return 0; }
	    try {
	        JTextArea jta = (JTextArea) textComp;
	        return jta.getLineStartOffset(lineNumber-1);
	    } catch (BadLocationException ex) { return -1; }
	}
	
	protected void dobleClickEnTextAreaHits(MouseEvent e) {
        
        try {
        	int caretpos = textAreaHits.getCaretPosition();
			int linenum = textAreaHits.getLineOfOffset(caretpos);
			Hit hit=hitsTextArea.get(linenum);
			int lineNumber=hit.getLineNumber();
			int index = getLineStartIndex(textAreaFileText, lineNumber);
			if (index != -1) { 
				textAreaFileText.setCaretPosition(index);
			}
			textAreaFileText.requestFocus();
		} catch (BadLocationException e1) {
			
		}
	}

	protected void abrirPanelExclusiones() {
		StringBuilder sb=new StringBuilder();
		for (String s:exclusions) {
			sb.append(s).append(NL);
		}
		String newExclusions=askForMultiString("directory exclusion list",sb.toString());
		if (newExclusions!=null) {
			StringTokenizer st=new StringTokenizer(newExclusions, "\r\n");
			exclusions.clear();
			while (st.hasMoreTokens()) {
				String tk=st.nextToken().trim();
				if (StringUtils.isNotBlank(tk)) {
					exclusions.add(tk);
				}
			}
			savePreference(FieldType.Exclusions,exclusions);
		}
	}

	private List<String> loadPreference(FieldType fieldType) throws Exception {
		List<String> strings=new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(getPreferenceFile(fieldType)))) {
			String lin=null;
			while ((lin=br.readLine())!=null) {
				if (!lin.trim().equals("")) {
					strings.add(lin);
				}
			}
		} catch (Exception e) {
			appendErrMsg("Error reading preferences "+fieldType.name()+" :: "+e);
			throw e;
		}
		return strings;
	}	
	
	private void savePreference(FieldType fieldType,List<String> pref) {
		try (FileWriter fw = new FileWriter(getPreferenceFile(fieldType))) {
			for (String s:pref) {
				fw.write(s+NL);
			}
		} catch (IOException e) {
			appendErrMsg("Error saving preferences "+fieldType.name()+" :: "+e);
		}
	}
	
	
	protected void copyFilesTxt() {
		boolean withHits=chckbxCopyHits.isSelected();
		boolean onlyNames=chckbxCopyOnlyName.isSelected();
		List<FileInfo> files=getSelectedFiles();
		StringBuilder sb=new StringBuilder();
		for (FileInfo fileInfo:files) {
			sb.append(fileInfo.toString(onlyNames,withHits,"  > "));
		}
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection stringSelection = new StringSelection(sb.toString());
		clipboard.setContents(stringSelection, null);
		
	}

	protected void copyFiles() {
		List<FileInfo> files=getSelectedFiles();
		
        List<File> listOfFiles = new ArrayList();
        Set<String> fichs=new HashSet<String>();
        for (FileInfo fileInfo:files) {
        	if (!fichs.contains(fileInfo.getFile().getPath())) {
        		fichs.add(fileInfo.getFile().getPath());
        		listOfFiles.add(fileInfo.getFile());
        	}
        }

        FileTransferable ft = new FileTransferable(listOfFiles);

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ft, new ClipboardOwner() {
			@Override
			public void lostOwnership(Clipboard clipboard, Transferable contents) {
			}
		});
	}
	
	protected void emptyFiles() {
		List<FileInfo> files=getSelectedFiles();

        List<File> listOfFiles = new ArrayList();
        Set<String> fichs=new HashSet<String>();
        for (FileInfo fileInfo:files) {
        	if (!fichs.contains(fileInfo.getFile().getPath())) {
        		fichs.add(fileInfo.getFile().getPath());
        		listOfFiles.add(fileInfo.getFile());
        	}
        }
        
		if (askConfirm("empty "+listOfFiles.size()+" files")) {
			int errores=0;
			String x="";
			for (File f:listOfFiles) {
				FileWriter fw;
				try {
					fw = new FileWriter(f);
					fw.close();
				} catch (IOException e) {
					errores++;
					x=x+f.getName()+";";
				}
			}
			if (errores>0) {
				showMessage(""+errores+" errors :: "+x);
			} else {
				showMessage("done");
			}
		}
	}
	
	public static String askForString(String message,String value) {
		String reply = JOptionPane.showInputDialog(message,value);
		return reply;
	}	
	
	public static boolean askConfirm(String message) {
		int dialogResult = JOptionPane.showConfirmDialog (null, message,"HuntFiles",JOptionPane.YES_NO_OPTION);
		if(dialogResult == JOptionPane.YES_OPTION){
		  return true;
		}
		return false;
	}
	
	public static String askForMultiString(String message,String value) {
		JTextArea ta = new JTextArea(20, 100);
		ta.setFont(new Font("Tahoma", Font.PLAIN, 11));
		ta.setText(value);
		int dialogResult=JOptionPane.showConfirmDialog(null, new JScrollPane(ta),message,JOptionPane.YES_NO_OPTION);
		if(dialogResult == JOptionPane.YES_OPTION){
			  return ta.getText();
		}
		return null;
	}
	
	
		
	
	public String askForCombo(String message,FieldType fieldType,String value) {
		JComboBox<String> combo=new JComboBox<>();
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		loadPreference(model, fieldType,false);
		combo.setModel(model);
		combo.setEditable(true);
		combo.setSelectedItem(value);
		combo.setMinimumSize(new Dimension(150, 20));
		combo.setPreferredSize(new Dimension(150, 20));
		int dialogResult=JOptionPane.showConfirmDialog(null, combo,message,JOptionPane.YES_NO_OPTION);
		if(dialogResult == JOptionPane.YES_OPTION){
			  return getComboValue(combo, model, fieldType);
		}
		return null;
	}
	
	
	public static void showMessage(String message) {
		JOptionPane.showMessageDialog(null, message,"HuntFiles",JOptionPane.OK_OPTION);
	}
	
	protected void findDown() {
		findCustom(TypeFind.down);
	}

	private void findCustom(TypeFind typeFind) {
		String textValue=getComboValue(comboFindTextPreview, findTextModel,FieldType.ComboFindText);
		String lookFor=null;
		if (distinct(textValue,lastLookFor)) {
			clearCustomHighlighter(textAreaFileText);
			lookFor=textValue;
			lastLookFor=lookFor;
		}
		findTextInJText(textAreaFileText,null,lookFor, typeFind);
	}
	
	private boolean distinct(String textValue, String lastLookFor2) {
		if (textValue==null) {
			textValue="";
		}
		if (lastLookFor2==null) {
			lastLookFor2="";
		}
		return !textValue.equals(lastLookFor2);
	}

	protected void findUp() {
		findCustom(TypeFind.up);
	}

	protected void openFile() {
		List<FileInfo> selected=getSelectedFiles();
		if (selected.size()>1 && !askConfirm("open "+selected.size()+" files")) {
			return;
		}
		for (FileInfo fileInfo:selected) {
			try {
				Desktop.getDesktop().open(fileInfo.getFile());
			} catch (IOException e) {
				appendErrMsg("error opening:"+fileInfo.getFile()+" :: "+e);
			}
		}
	}
	


	protected void appendErrMsg(String errMsg) {
		StringBuilder err=new StringBuilder();
		err.append(textPaneError.getText());
		err.append(errMsg).append(NL);
		textPaneError.setText(err.toString());
	}
	
	protected void compareOptions() {
		String x=askForCombo("set compare command", FieldType.Compare, compareCommand);
		if (x!=null) {
			compareCommand=x;
		}
	}

	protected void comparar() {
		List<FileInfo> files=getSelectedFiles();
		if (files.size()!=2) {
			showMessage("only 2 files for compare");
			return;
		}
		String [] command={compareCommand,files.get(0).getFile().getPath(),files.get(1).getFile().getPath()};
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			showMessage("ERROR executing compare command "+compareCommand+" :: "+e);
		}
		
	}

	protected void duplicar() {
		List<FileInfo> files=getSelectedFiles();
		String txtDup=txtDuplicateEnding.getText();
		txtDup=StringUtils.replace(txtDup, "\\", "-");
		txtDup=StringUtils.replace(txtDup, "/", "-");
		txtDup=StringUtils.replace(txtDup, "|", "-");
		txtDup=StringUtils.replace(txtDup, "*", "-");
		txtDup=StringUtils.replace(txtDup, ":", "-");
		txtDup=StringUtils.replace(txtDup, "\"", "-");
		txtDup=StringUtils.replace(txtDup, "'", "-");
		txtDup=StringUtils.replace(txtDup, "<", "-");
		txtDup=StringUtils.replace(txtDup, ">", "-");
		txtDup=StringUtils.replace(txtDup, "?", "-");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date now=new Date();
		if (files.size()==1) {
			File f=files.get(0).getFile();
			File dest=new File(f.getPath()+"-"+txtDup+"-"+sdf.format(now));
			try {
				FileUtils.copyFile(f, dest);
			} catch (Exception e) {
				showMessage("ERROR "+e);
			}
		} else {
			showMessage("only 1 file please");
		}
	}

	protected void openInExplorer() {
		List<FileInfo> files=getSelectedFiles();
		
		if (files.size()==1) {
			File f=files.get(0).getFile();
			try {

				if (SystemUtils.IS_OS_WINDOWS) {
					String path = f.getCanonicalPath();

					ProcessBuilder pb = new ProcessBuilder("explorer.exe", "/select," + path);
					pb.redirectError();
					Process proc = pb.start();	
					
				} else if (Desktop.isDesktopSupported()) {
				    Desktop desktop = Desktop.getDesktop();
				    //desktop.browse(f.toURI()); 
				    desktop.open(f.getParentFile());
				} else {
					showMessage("not supported");
				}
				
			} catch (Exception e) {
				showMessage("ERROR "+e);
			}
		} else {
			showMessage("only 1 file please");
		}
	}

	protected void selectToZip() {
			List<FileInfo> files=getSelectedFiles();
			
	        List<File> listOfFiles = new ArrayList();
	        Set<String> fichs=new HashSet<String>();
	        for (FileInfo fileInfo:files) {
	        	if (!fichs.contains(fileInfo.getFile().getPath())) {
	        		fichs.add(fileInfo.getFile().getPath());
	        		listOfFiles.add(fileInfo.getFile());
	        	}
	        }
	        
	        if (listOfFiles.size()==0) {
				showMessage("select 1 or more files please");
			}
	        
	        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd_HHmmss");
			Date now=new Date();
			
	        String zip=askForString("zip file name", "/temp/export-"+sdf.format(now)+".zip");
	        File zipFile=new File(zip);
	        try {
	        	zipFile.getParentFile().mkdirs();
	        } catch (Exception r) {
	        	//ignore
	        }
	        try (ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
	            for (File file:listOfFiles) {
	            	File parent=file;
	            	while (parent.getParentFile()!=null) {
	            		parent=parent.getParentFile();
	            	}
	            	Path sourceDir = Paths.get(parent.getPath());
	            	Path sourceFile=Paths.get(file.getPath());
	                Path targetFile = sourceDir.relativize(sourceFile);
	                ZipEntry zipEntry=new ZipEntry(targetFile.toString());
	                zipEntry.setTime(file.lastModified());
	                outputStream.putNextEntry(zipEntry);
	                byte[] bytes = Files.readAllBytes(sourceFile);
	                outputStream.write(bytes, 0, bytes.length);
	                outputStream.closeEntry();
	            };
	        } catch (Exception e) {
	        	showMessage("ERROR making zip "+zip+" :: "+e);
	        }
	        
	}

	protected void stopSearch() {
		if (huntFiles!=null) {
			huntFiles.endSearch();
		}
	}

	
	protected List<FileInfo> getSelectedFiles() {
		List<FileInfo> selected=new ArrayList<FileInfo>();
		//askForString("sin hacer", "");
		int[] selectedIdx=tableFiles.getSelectedRows();
		FilesTableModel ftm=filesTableModel;
		for (int i=0;i<selectedIdx.length;i++) {
			int idx=selectedIdx[i];
			idx=tableFiles.convertRowIndexToModel(idx);
			FileInfo fileInfo=filesTableModel.getFileInfo(idx);
			//System.out.println(" i:"+i+"  "+fileInfo.getFile());
			selected.add(fileInfo);
		}
		return selected;
	}

	protected void registroTablaFileSeleccionado(ListSelectionEvent evento) {
		//System.out.println("click "+System.currentTimeMillis());
		//panel.setEnabled(true);
		int row=tableFiles.getSelectedRow();
		
		FileInfo fileInfo=null;
		if (row>-1) {
			row=tableFiles.convertRowIndexToModel(row);
			fileInfo=filesTableModel.getFileInfo(row);	
			System.out.println(" SEL "+fileInfo);
			preview(fileInfo);
			tableFiles.requestFocus();
		} else {
			System.out.println(" NO SEL ");
			preview(null);
		}
	}

	
	public void preview(FileInfo fileInfo) {
		if (fileInfo==null) {
			
			textAreaFileText.setText("");
			findTextInJText(textAreaFileText,"no","no",TypeFind.init);
			textAreaHits.setText("");
			hitsTextArea=new ArrayList<>();
			try {
				panelImg.setImageInputStream(null);
			} catch (Exception e) {
				//ignorar
			}
		} else {
			StringBuilder hits=new StringBuilder();
			if (fileInfo.getHits()!=null) {
				for (Hit hit:fileInfo.getHits()) {
					hits.append(hit).append(NL);
				}
			}
			hitsTextArea=fileInfo.getHits();
			textAreaHits.setText(hits.toString());
			
			String text="";

			try (CloseableInputStream closeableInputStream=new CloseableInputStream(fileInfo.getPreCloseableInputStream())) {
				InputStream is1=closeableInputStream.getInputStream();
				Charset charset=fileInfo.getHitsPreferredCharset();
				text=getStringFromInputStream(is1,charset);
				textAreaFileText.setText(text);
				String searchText=fileInfo.getSearchOptions().getOriginalText();
				String textValue=getComboValue(comboFindTextPreview, findTextModel,FieldType.ComboFindText);
				lastLookFor=textValue;
				findTextInJText(textAreaFileText, searchText, textValue,TypeFind.init);
			} catch (Throwable r) {
				textAreaFileText.setText("ERROR opening "+fileInfo+" :: "+r);
			} 
			try (CloseableInputStream closeableInputStream2=new CloseableInputStream(fileInfo.getPreCloseableInputStream())) {
				InputStream is2=closeableInputStream2.getInputStream();
				panelImg.setImageInputStream(is2);
			} catch (Exception e) {
				System.out.println("Error panelImg "+e);
			} 
		}
	}
	
	
	public void clearCustomHighlighter(JTextComponent jText) {
		Highlighter highlighter = jText.getHighlighter();
		List<Highlight> toRemove=new ArrayList<Highlight>();
		for (Highlight highlight:highlighter.getHighlights()) {
			if (highlight.getPainter()==customHighlighter) {
				toRemove.add(highlight);
			}
		}
		for (Highlight highlight:toRemove) {
			highlighter.removeHighlight(highlight);
		}
	}
	
	public void findTextInJText(JTextComponent jText,String initialSearch,String lookFor,TypeFind typeFind) {
		Highlighter highlighter = jText.getHighlighter();
		if (typeFind==typeFind.init) {
			highlighter.removeAllHighlights();
			if (StringUtils.isNotBlank(initialSearch)) {
				findTextInJText(jText,initialSearch,mainHighlighter);
			}
		}
		if (StringUtils.isNotBlank(lookFor)) {
			findTextInJText(jText,lookFor,customHighlighter);
		}
		ArrayList<Integer> starts=new ArrayList<Integer>();
		for (Highlight highlight:highlighter.getHighlights()) {
			if (checkboxUpDownText.isSelected()) { //search in custom text and search global text
				starts.add(highlight.getStartOffset());
			} else {
				if (highlight.getPainter()==customHighlighter) {
					starts.add(highlight.getStartOffset());
				}
			}
		}
		Collections.sort(starts);
		if (typeFind==TypeFind.init) {
			if (starts.size()>0) {
				jText.setCaretPosition(starts.get(0));
			} else {
				jText.setCaretPosition(0);
			}
		} else if (starts.size()>0) {
			int act=jText.getCaretPosition();
			int sel=0;
			if (typeFind==TypeFind.up) {
				for (sel=0;sel<starts.size();sel++) {
					if (starts.get(sel)>=act) {
						break;
					}
				}
				sel=sel-1;
				if (sel<0) {
					sel=starts.size()-1;
				}
			} else if (typeFind==TypeFind.down) {
				for (sel=0;sel<starts.size();sel++) {
					if (starts.get(sel)>act) {
						break;
					}
				}
				if (sel>=starts.size()) {
					sel=0;
				}
			}
			int newPos=starts.get(sel);
			jText.setCaretPosition(newPos);
		}
		
		
		jText.requestFocus();
		
	}

	private void findTextInJText(JTextComponent jText, String lookFor, DefaultHighlightPainter customHighlighter2) {
		if (StringUtils.isBlank(lookFor)) {
			return;
		}
		lookFor=lookFor.toLowerCase();
		String text=jText.getText().toLowerCase();
		int pos=-1;
		while ((pos=text.indexOf(lookFor,pos+1))>-1) {
			try {
				jText.getHighlighter().addHighlight(pos, pos+lookFor.length(), customHighlighter2);
			} catch (BadLocationException e) {
				addError("findTextInJText erro: "+e);
			}
		}
	}

	public String getStringFromInputStream(InputStream inputStream,Charset charset) throws IOException {
		//https://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
		//most efficient way 8: (Ways to convert an InputStream to a String)
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[8092];
		int length;
		while ((length = inputStream.read(buffer)) != -1) {
		    result.write(buffer, 0, length);
		}
		// StandardCharsets.UTF_8.name() > JDK 7
		if (charset==null) {
			return result.toString(); //"UTF-8");
		}
		return result.toString(charset.name()); //"UTF-8");
	}
	
	
	private String getComboValue(JComboBox<String> combo,DefaultComboBoxModel<String> modelo, FieldType fieldType) {
		if (combo.getSelectedItem()==null) {
			return null;
		}
		String value= combo.getSelectedItem().toString();
		if (combo.getSelectedIndex()==-1) {
			modelo.insertElementAt(value,0);
		} else {
			modelo.removeElementAt(combo.getSelectedIndex());
			modelo.insertElementAt(value,0);
			modelo.setSelectedItem(value);
		}
		while (modelo.getSize()>MAX_COMBO_PREFERENCES) {
			modelo.removeElementAt(modelo.getSize()-1);
		}
		savePreference(modelo, fieldType);
		return value;
	}

	private void loadPreference(DefaultComboBoxModel<String> modelo, FieldType fieldType,boolean leaveBlank) {
		if (leaveBlank) {
			modelo.addElement("");
		}
		try (BufferedReader br = new BufferedReader(new FileReader(getPreferenceFile(fieldType)))) {
			String lin=null;
			while ((lin=br.readLine())!=null) {
				if (!lin.trim().equals("")) {
					modelo.addElement(lin);
				}
			}
		} catch (IOException e) {
			appendErrMsg("Error reading preferences "+fieldType.name()+" :: "+e);
		}
	}
	
	
	private void savePreference(DefaultComboBoxModel<String> modelo, FieldType fieldType) {
		try (FileWriter fw = new FileWriter(getPreferenceFile(fieldType))) {
			for (int i=0;i<modelo.getSize();i++) {
				String x=modelo.getElementAt(i);
				if (!StringUtils.isBlank(x)) {
					fw.write(x+NL);
				}
			}
		} catch (IOException e) {
			appendErrMsg("Error saving preferences "+fieldType.name()+" :: "+e);
		}
	}
	
	private File getPreferenceFile(FieldType fieldType) {
		String userHomeDir = System.getProperty("user.home");
		String prefDir=userHomeDir+"/.huntFiles";
		File d=new File(prefDir);
		d.mkdirs();
		String prefFile=prefDir+"/"+fieldType.name();
		File f=new File(prefFile);
		return f;
	}

	protected void starSearch() {
		String textValue=getComboValue(comboText, textModel,FieldType.ComboText);
		String dirValue=getComboValue(comboDirectory, directoryModel,FieldType.ComboDirectory);
		String filenameValue=getComboValue(comboFileName, fileNameModel,FieldType.ComboFileName);
		String afterValue=getComboValue(comboAfter, afterModel,FieldType.ComboAfter);
		if (afterValue!=null && afterValue.indexOf("#")>-1) {
			afterValue=afterValue.substring(0, afterValue.indexOf("#")).trim();
		}
		String beforeValue=textBefore.getText();
		if (beforeValue.indexOf("#")>-1) {
			beforeValue=beforeValue.substring(0, beforeValue.indexOf("#")).trim();
		}
		//String findInTextValue=getComboValue(comboFindTextPreview, findTextModel,FieldType.ComboFindText);
		
		String greaterThan=textGreaterThan.getText();
		String lessThan=textLessThan.getText();
		boolean zipjar=checkboxZipjar.isSelected();
		boolean z7=checkbox7z.isSelected();
		boolean rar=checkboxRar.isSelected();
		boolean recursive=chechBoxSubdir.isSelected();
		
		boolean isAnsi=chechBoxAnsi.isSelected();
		boolean isUtf8=chechBoxUtf8.isSelected();
		boolean isUtf16=chechBoxUtf16.isSelected();
		
		boolean regexp=chckbxRegex.isSelected();
		
		SearchOptions searchOptions=new SearchOptions(dirValue);
		searchOptions.setText(textValue); 
		searchOptions.setIsRegExp(regexp);
		
		searchOptions.setFilename(filenameValue);
		searchOptions.setGreater(greaterThan);
		searchOptions.setLessThan(lessThan);
		searchOptions.setRar(rar);
		searchOptions.setRecursive(recursive);
		searchOptions.setZ7(z7);
		searchOptions.setZipjar(zipjar);
		searchOptions.setIgnorePaths(exclusions);
		searchOptions.setSearchAnsi(isAnsi);
		searchOptions.setSearchUtf8(isUtf8);
		searchOptions.setSearchUtf16(isUtf16);
		
		
		if (checkBoxAfter.isSelected()) {
			try {
				searchOptions.setAfter(afterValue);
			} catch (Exception e) {
				showMessage("not valid: After");
				return;
			}
		}
		if (checkboxBefore.isSelected()) {
			try {
				searchOptions.setBefore(beforeValue);
			} catch (Exception e) {
				showMessage("not valid: Before");
				return;
			}
		}

		
		
		filesTableModel.clearFiles();
		filesTableModel.fireTableDataChanged();
		
		/*
		File dir=new File(dirValue);
		File [] fich=dir.listFiles();
		for (File f:fich) {
			if (f.isFile()) {
				filesTableModel.addFileInfo(new FileInfo(f,null,null));
			}
		}
		filesTableModel.fireTableDataChanged();

		*/
		
		setEnabled(false);
		
		huntFiles=new HuntFiles(searchOptions, this);
		
        Thread worker = new Thread() {
            
            public void run() {

	                try {
	                	inProgress=true;
	                	huntFiles.search();
	                } catch (Throwable ex) {
	                	System.out.println("Error worker.run "+ex);
	                	ex.printStackTrace(System.out);
	                	showMessage("ERROR "+ex);
	                }
	                inProgress=false;
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        setEnabled(true);
                    }
                });
            }
        };
         
        worker.start(); // So we don't hold up the dispatch thread.
		
		
		
	}

	private void setEnabled(boolean enable) {
		buttonStart.setEnabled(enable);
		buttonStop.setEnabled(!enable);
	}

	private DefaultComboBoxModel<String> createTextModel() {
		textModel= new DefaultComboBoxModel<String>();
		loadPreference(textModel, FieldType.ComboText,true);
		return textModel;
	}

	private DefaultComboBoxModel<String> createDirectoryModel() {
		directoryModel= new DefaultComboBoxModel<String>();
		loadPreference(directoryModel, FieldType.ComboDirectory,false);
		return directoryModel;
	}
	
	private DefaultComboBoxModel<String> createFileNameModel() {
		fileNameModel = new DefaultComboBoxModel<String>();
		loadPreference(fileNameModel, FieldType.ComboFileName,false);
		return fileNameModel;
	}
	
	private DefaultComboBoxModel<String> createAfterModel() {
		afterModel = new DefaultComboBoxModel<String>();
		loadPreference(afterModel, FieldType.ComboAfter,false);
		return afterModel;
	}
	
	private DefaultComboBoxModel<String> createFindTextModel() {
		findTextModel = new DefaultComboBoxModel<String>();
		loadPreference(findTextModel, FieldType.ComboFindText,false);
		return findTextModel;
	}
	
	public void initFileDrop() {
		FileDrop fileDrop=new FileDrop( frmHuntfiles, new FileDrop.Listener()
			      {   public void filesDropped( java.io.File[] files )
			          {   
			              if (files.length==1) {
			            	  File f=files[0];
			            	  if (f.isDirectory() || f.isFile()) {
			            		  String dir=f.getAbsolutePath();
			            		  //comboDirectory.getEditor().setItem(dir);
			            		  comboDirectory.setSelectedItem(dir);
			            	  } else {
			            		  showMessage("drop only a directory or a file");
			            	  }
			              } else {
			            	  showMessage("drop only 1 directory or 1 file");
			            	  
			              }
			          }   // end filesDropped
			      });		
	}

	@Override
	public void addFile(final FileInfo fileInfo) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		filesTableModel.addFileInfo(fileInfo);
        		filesTableModel.fireTableDataChanged();
            }
        });
	}

	@Override
	public void addError(final String err) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	appendErrMsg(err);
            }
        });
	}

	@Override
	public void updateProgress(int foundFiles, int searchedFiles, int totalFiles) {
		if (inProgress) {
			
		}
	}

	@Override
	public void workingInArchive(final String archive) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	if (inProgress) {
            		labelStatus.setText(archive);
            	}
            }
        });
	}
}
