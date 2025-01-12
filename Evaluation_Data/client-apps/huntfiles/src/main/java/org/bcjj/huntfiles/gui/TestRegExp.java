package org.bcjj.huntfiles.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.bcjj.huntfiles.HuntFiles;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.Font;

public class TestRegExp extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textPattern;
	private JTextField text;
	private JLabel labelRespuesta;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws Exception{
		TestRegExp dialog = new TestRegExp();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
	}

	/**
	 * Create the dialog.
	 */
	public TestRegExp() {
		setTitle("Test Regular Expression");
		setBounds(100, 100, 805, 196);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("pattern:");
		lblNewLabel.setToolTipText("patr\u00F3n de b\u00FAsqueda");
		lblNewLabel.setBounds(10, 11, 46, 14);
		contentPanel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("text:");
		lblNewLabel_1.setBounds(10, 36, 46, 14);
		contentPanel.add(lblNewLabel_1);
		
		textPattern = new JTextField();
		textPattern.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				actualizarTooltips();
			}
		});
		textPattern.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				actualizarTooltips();
			}
		});
		textPattern.setText(".*(niña|camion).*");
		textPattern.setBounds(66, 8, 697, 20);
		contentPanel.add(textPattern);
		textPattern.setColumns(10);
		
		text = new JTextField();
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				actualizarTooltips();
			}
		});
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				actualizarTooltips();
			}
		});
		text.setText("   el niño tiene un    camión púrpura  ");
		text.setBounds(66, 33, 697, 20);
		contentPanel.add(text);
		text.setColumns(10);
		
		JButton btnNewButton = new JButton("test");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testExpression();
			}
		});
		btnNewButton.setBounds(66, 80, 89, 23);
		contentPanel.add(btnNewButton);
		
		labelRespuesta = new JLabel("?");
		labelRespuesta.setBounds(66, 114, 46, 14);
		contentPanel.add(labelRespuesta);
		
		JLabel lblNewLabel_2 = new JLabel("TIP: Lines of text are read as lowecase and without accents (áéíóúàèìòùäëïöü), tabs, and double spaces, or any leading and trailing spaces");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblNewLabel_2.setBounds(66, 53, 697, 14);
		contentPanel.add(lblNewLabel_2);
		
		actualizarTooltips();
	}

	protected void actualizarTooltips() {
		//textPattern.setToolTipText("UsedAs: "+HuntFiles.getStandardText(textPattern.getText()));
		text.setToolTipText("UsedAs: "+HuntFiles.getStandardText(text.getText()));
	}

	protected void testExpression() {
		actualizarTooltips();
		//String patron=HuntFiles.getStandardText(textPattern.getText());
		
		
		
		String patron=textPattern.getText();
		String linea=HuntFiles.getStandardText(text.getText());
		
		if (Pattern.compile(patron).matcher(linea).matches()) {
			labelRespuesta.setText("OK");
		} else {
			labelRespuesta.setText("no");
		}
	}
}
