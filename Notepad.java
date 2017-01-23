import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")

public class Notepad extends JFrame {

	private JTextArea area;
	private JMenuBar menuBar;
	private JMenu file, edit, help;
	private JMenuItem newFile, openFile, saveFile, cut, copy, paste, helpMenu;
	private JPanel borderWest, borderSouth;
	private JScrollBar fontSize;
	private SaveFilePopUp popupS;
	private LoadFilePopUp popupL;
	private static String documentName = "document1.txt";
	private static String Font = "Times Roman";

	public static void main(String[] args) {
		new Notepad();
	}

	// Constructor
	public Notepad() {
		super("Notepad--" + " " + documentName);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		borderWest = new JPanel();
		borderSouth = new JPanel();
		// Create menu bar and menu items
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
		file.add(newFile);
		file.add(openFile);
		file.add(saveFile);

		copy = new JMenuItem("Copy");
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		copy.addActionListener(new Text());
		cut = new JMenuItem("Cut");
		cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		paste = new JMenuItem("Paste");
		paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		edit.add(copy);
		edit.add(cut);
		edit.add(paste);

		helpMenu = new JMenuItem("Help Menu");
		helpMenu.addActionListener(new Help());
		helpMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,ActionEvent.CTRL_MASK));
		help.add(helpMenu);

		fontSize = new JScrollBar();
		fontSize.setOrientation(0);
		fontSize.setUnitIncrement(1);
		fontSize.addAdjustmentListener(new FontSize());
		borderSouth.setLayout(new FlowLayout(FlowLayout.LEADING));
		JLabel labelFont = new JLabel("Font Size");
		borderSouth.add(labelFont);
		borderSouth.add(fontSize);

		// Create text area
		area = new JTextArea();
		JScrollPane scroller = new JScrollPane(area);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		area.setFont(new Font("TimesRoman", 12, 12));
		// add all components to the frame
		setJMenuBar(menuBar);
		getJMenuBar().add(file);
		getJMenuBar().add(edit);
		getJMenuBar().add(help);

		getContentPane().add(scroller, BorderLayout.CENTER);
		getContentPane().add(borderWest, BorderLayout.WEST);
		getContentPane().add(borderSouth, BorderLayout.SOUTH);
		setBounds(450, 200, 500, 500);
		setVisible(true);
	}

	class Text implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String copiedText = "";
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
					area.getSelectedText().equals(null);
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
			dispose();
			new Notepad();
		}
	}

	class SaveFile implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			popupS = new SaveFilePopUp();
			popupS.SaveToFile();
			System.out.println("File Saved");
		}
	}

	class OpenFile implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			popupL = new LoadFilePopUp();
			popupL.LoadFile();
			System.out.println("File Loaded");

		}
	}

	class SaveFilePopUp extends JFrame {
		JTextField name;

		private JPanel panel;
		private JButton buttonSave, buttonCancel;
		private boolean isSaved = false;

		SaveFilePopUp() {
			super("Save File");

			name = new JTextField("document1.txt");

			panel = new JPanel();
			panel.add(name);
			
			Box buttonBox = new Box(BoxLayout.X_AXIS);
			buttonSave = new JButton("Save");
			buttonCancel = new JButton("Cancel");
			buttonCancel.addActionListener(new Cancel());
			buttonSave.addActionListener(new Save());
			buttonBox.add(buttonSave);
			buttonBox.add(buttonCancel);

			setResizable(false);
			getContentPane().add(panel);
			getContentPane().add(buttonBox, BorderLayout.SOUTH);
			pack();
			setBounds(625, 400, 150, 100);
			setVisible(true);

		}

		public void SaveToFile() {

			if (isSaved) {
				try {
					File file = new File(documentName);
					BufferedWriter writer = new BufferedWriter(new FileWriter(file));
					writer.write(area.getText());
					writer.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		class Cancel implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				dispose();
			}
		}

		class Save implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				documentName = name.getText();
				isSaved = true;
				SaveToFile();
				setTitle(documentName);
				dispose();
			}
		}
	}

	class LoadFilePopUp extends JFrame {
		JTextField name;

		private JPanel panel;
		private JButton buttonLoad, buttonCancel;
		private boolean isLoaded = false;
		String documentName;

		LoadFilePopUp() {
			super("Load File");

			name = new JTextField("Enter name of file with .txt");

			panel = new JPanel();
			panel.add(name);
			

			Box buttonBox = new Box(BoxLayout.X_AXIS);
			buttonLoad = new JButton("Load");
			buttonCancel = new JButton("Cancel");
			buttonCancel.addActionListener(new Cancel());
			buttonLoad.addActionListener(new Load());
			buttonBox.add(buttonLoad);
			buttonBox.add(buttonCancel);

			getContentPane().add(panel);
			getContentPane().add(buttonBox, BorderLayout.SOUTH);
			pack();
			setBounds(625, 400, 150, 100);
			setVisible(true);

		}

		class Cancel implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				dispose();
			}
		}

		class Load implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				documentName = name.getText();
				isLoaded = true;
				LoadFile();
				dispose();
			}
		}

		public void LoadFile() {
			//serialise in future
			if (isLoaded) {
				try {
					FileReader reader = new FileReader(documentName);
					area.read(reader, documentName);
				} catch (Exception e) {e.printStackTrace();}
			}
		}

	}

	class ErrorMessage extends JFrame implements ActionListener {
		JButton ok;

		ErrorMessage() {
			super("ERROR!");

			setResizable(false);
			JTextField field = new JTextField("Error: File not found.");
			ok = new JButton("Okay");
			ok.addActionListener(this);
			getContentPane().add(field, BorderLayout.CENTER);
			getContentPane().add(ok, BorderLayout.SOUTH);
			setBounds(625, 400, 150, 100);
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
			field1.setFont(new Font("Times Roman",12,16));
			field1.setText("<html>Notepad--<br> <br>File: <br>New File - CTRL + N, "
					+ "Save File - CTRL + S, Open File - CTRL + O<br>. <br>"
					+ "Edit: <br>Copy - CTRL + C, Cut - CTRL + X, Paste - CTRL + V<br>"
					+ "<br>Help Menu: <br>Help - CTRL + I </html>");
			
			field1.setHorizontalAlignment(SwingConstants.LEFT);
			field1.setVerticalAlignment(SwingConstants.TOP);
			ok = new JButton("Okay");
			ok.addActionListener(this);
			getContentPane().add(field1, BorderLayout.CENTER);
			
			getContentPane().add(ok,BorderLayout.SOUTH);
			setBounds(625, 200, 500, 350);
			setVisible(true);
		}
		public void actionPerformed(ActionEvent event) {
			dispose();
		}
	}

}
