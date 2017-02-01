import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

@SuppressWarnings("serial")

public class Notepad {

	// DEFINE instance and class variables
	private JFrame frame;
	private JTextArea area;
	private JMenuBar menuBar;
	private JMenu file, edit, help;
	private JMenuItem newFile, openFile, saveFile, cut, copy, paste, helpMenu;
	private JScrollBar fontSize;
	private static String copiedText;
	private static String documentName = "new";
	private static String Font = "Times Roman";

	public static void main(String[] args) {
		// create new instance of notepad
		new Notepad();
	}

	// Constructor
	public Notepad() {

		frame = new JFrame("Notepad -- " + documentName);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create menu bar and menu items add actionlisteners to each component
		menuBar = new JMenuBar();
		file = new JMenu("File");
		edit = new JMenu("Edit");
		help = new JMenu("Help");
		newFile = new JMenuItem("New File");
		newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		newFile.addActionListener(new NewText());
		openFile = new JMenuItem("Open File");
		openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		openFile.addActionListener(new OpenFile());
		saveFile = new JMenuItem("Save File");
		saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveFile.addActionListener(new SaveFile());
		file.add(newFile);file.add(openFile);file.add(saveFile);
		//edit menu items
		copy = new JMenuItem("Copy");
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		copy.addActionListener(new Text());
		cut = new JMenuItem("Cut");
		cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		cut.addActionListener(new Text());
		paste = new JMenuItem("Paste");
		paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		paste.addActionListener(new Text());
		edit.add(copy);edit.add(cut);edit.add(paste);
		//help menu items
		helpMenu = new JMenuItem("Help Menu");
		helpMenu.addActionListener(new Help());
		helpMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		help.add(helpMenu);
		//create font options
		fontSize = new JScrollBar();
		fontSize.setOrientation(0);
		fontSize.setUnitIncrement(1);
		fontSize.setMinimum(6);
		fontSize.addAdjustmentListener(new FontSize());
		JLabel labelFont = new JLabel("Font Size");
		JPanel botPanel = new JPanel();
		botPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		botPanel.add(labelFont);botPanel.add(fontSize);

		// Create text area
		area = new JTextArea(5, 10);
		JScrollPane scroller = new JScrollPane(area);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		area.setFont(new Font("TimesRoman", 12, 12));
		// add all components to the frame
		frame.setJMenuBar(menuBar);
		frame.getJMenuBar().add(file);
		frame.getJMenuBar().add(edit);
		frame.getJMenuBar().add(help);
		frame.getContentPane().add(botPanel, BorderLayout.SOUTH);
		frame.getContentPane().add(scroller, BorderLayout.CENTER);
		frame.setBounds(450, 200, 500, 500);
		frame.setVisible(true);
	}
	
	
	class Text implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			copiedText = "";
			if (event.equals(copy)) {
				if (area.getSelectedText() != null) {
					copiedText = area.getSelectedText();
				}
			}
			if (event.equals(paste)) {
				area.insert(copiedText, area.getCaretPosition());
			}
			if (event.equals(cut)) {
				if (area.getSelectedText() != null) {
					copiedText = area.getSelectedText();
					area.getSelectedText().equals("");
				}
			}
		}
	}
	
	class Help implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			new HelpMenu();
		}
	}

	class FontSize implements AdjustmentListener {
		public void adjustmentValueChanged(AdjustmentEvent event) {
			area.setFont(new Font(Font, 20, event.getValue()));
		}
	}

	class NewText implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			frame.dispose();
			new Notepad();
		}
	}

	class SaveFile implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JFileChooser chooser = new JFileChooser();
			int retrival = chooser.showSaveDialog(null);
			if (retrival == JFileChooser.APPROVE_OPTION) {
				try {
					FileWriter writer = new FileWriter(chooser.getSelectedFile() + ".txt");
					documentName = chooser.getName(chooser.getSelectedFile());
					writer.write(area.getText());
					writer.close();
				} catch (Exception e) {
					e.printStackTrace(); 
					new ErrorMessage();
				}
				frame.setTitle(documentName+".txt");
			}
		}
	}

	class OpenFile implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JFileChooser chooser = new JFileChooser();
			int retrival = chooser.showOpenDialog(null);
			if (retrival == JFileChooser.APPROVE_OPTION) {
				try {
					File selectedFile = chooser.getSelectedFile();
					BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
					documentName = chooser.getName(chooser.getSelectedFile());
					area.read(reader, null);
					reader.close();
				} catch (Exception e) {
				e.printStackTrace(); 
				new ErrorMessage();}
			}
			frame.setTitle(documentName);
		}
	}
	class ErrorMessage extends JFrame implements ActionListener {
		JButton ok;
		ErrorMessage() {
			super("ERROR!");
			setResizable(false);
			JTextField field = new JTextField("Error: Something went wrong. Please try again!");
			ok = new JButton("Okay");
			ok.addActionListener(this);
			getContentPane().add(field, BorderLayout.CENTER);
			getContentPane().add(ok, BorderLayout.SOUTH);
			setBounds(625, 400, 270, 100);
			setVisible(true);
		}
		public void actionPerformed(ActionEvent event) {
			dispose();
		}
	}

	class HelpMenu extends JFrame implements ActionListener {
		
		JButton ok;
		JLabel field1;

		HelpMenu() {
			super("Help Menu");
			setResizable(false);
			field1 = new JLabel();
			field1.setFont(new Font("Times Roman", 12, 16));
			field1.setText("<html>Notepad--<br> <br>File: <br>New File - CTRL + N, "
					+ "Save File - CTRL + S, Open File - CTRL + O<br>. <br>"
					+ "Edit: <br>Copy - CTRL + C, Cut - CTRL + X, Paste - CTRL + V<br>"
					+ "<br>Help Menu: <br>Help - CTRL + I </html>");
			field1.setHorizontalAlignment(SwingConstants.LEFT);
			field1.setVerticalAlignment(SwingConstants.TOP);
			ok = new JButton("Okay");
			ok.addActionListener(this);
			getContentPane().add(field1, BorderLayout.CENTER);
			getContentPane().add(ok, BorderLayout.SOUTH);
			setBounds(625, 200, 500, 350);
			setVisible(true);
		}

		public void actionPerformed(ActionEvent event) {
			dispose();
		}
	}
}
