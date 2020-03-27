package indwes.libsys.functionalities;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import indwes.libsys.dao.LibraryDao;
import indwes.libsys.main.SqlConnection;
import net.proteanit.sql.DbUtils;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.awt.ComponentOrientation;

public class LibrarianView {

	public JFrame UIFrame;
	private JTable table;
	private JTextField bookTitleTxtField;
	private JTextField authorNameTxtField;
	private JTextField quantityTxtField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LibrarianView window = new LibrarianView();
					window.UIFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	
	Connection connection = null;
	private JTextField textvalue;
	private JTextField bookIDTxtField;
	
	public LibrarianView() {
		initialize();
		connection = SqlConnection.dbConnect(); // Calling the connection class
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		UIFrame = new JFrame();
		UIFrame.setTitle("Librarian View");
		UIFrame.setBounds(100, 100, 788, 561);
		UIFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UIFrame.getContentPane().setLayout(null);
		

		JLabel bookTitleLabel = new JLabel("Book Title:");
		bookTitleLabel.setBounds(37, 39, 86, 15);
		UIFrame.getContentPane().add(bookTitleLabel);
		
		JLabel authorNameLabel = new JLabel("Author Name:");
		authorNameLabel.setBounds(37, 70, 103, 15);
		UIFrame.getContentPane().add(authorNameLabel);
		
		JLabel quantityLabel = new JLabel("Quantity:");
		quantityLabel.setBounds(37, 95, 86, 15);
		UIFrame.getContentPane().add(quantityLabel);
		
		bookTitleTxtField = new JTextField();
		bookTitleTxtField.setBounds(146, 37, 135, 19);
		UIFrame.getContentPane().add(bookTitleTxtField);
		bookTitleTxtField.setColumns(10);
		
		authorNameTxtField = new JTextField();
		authorNameTxtField.setBounds(146, 68, 135, 19);
		UIFrame.getContentPane().add(authorNameTxtField);
		authorNameTxtField.setColumns(10);
		
		quantityTxtField = new JTextField();
		quantityTxtField.setBounds(146, 93, 135, 19);
		quantityTxtField.setColumns(10);
		UIFrame.getContentPane().add(quantityTxtField);
		

		JLabel bookIDLabel = new JLabel("Book ID:");
		bookIDLabel.setBounds(37, 12, 86, 15);
		UIFrame.getContentPane().add(bookIDLabel);
		
		bookIDTxtField = new JTextField();
		//bookIDTxtField.setText();
		bookIDTxtField.setColumns(10);
		bookIDTxtField.setBounds(146, 6, 135, 19);
		UIFrame.getContentPane().add(bookIDTxtField);
		UIFrame.setVisible(true);
		
		
		JButton searchButton = new JButton("Search");
		searchButton.setBounds(361, 29, 86, 35);
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				search();
			}
		});
		UIFrame.getContentPane().add(searchButton);
		
		// *****************************************************
		// ADD BOOKS BUTTON
		// *****************************************************
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				add();
			}
		});
		addButton.setBounds(491, 29, 86, 35);
		UIFrame.getContentPane().add(addButton);
		
		
		
		// *****************************************************
		// REMOVE BOOKS BUTTON
		// *****************************************************
		
		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				remove();
			}
		});
		removeButton.setBounds(614, 29, 97, 35);
		UIFrame.getContentPane().add(removeButton);
		
		
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

			}
		});
		
		scrollPane.setBounds(37, 137, 691, 365);
		UIFrame.getContentPane().add(scrollPane);
		table = new JTable();
		scrollPane.setViewportView(table);
		
		
	}
	
	// *****************************************************
	// ADD BOOKS METHOD
	// *****************************************************
	
	public void add() {
		
		try {
			 String bookName = bookTitleTxtField.getText();
			 String bookAuthor = authorNameTxtField.getText();
			 String quantity = quantityTxtField.getText();
			 int bookQuantity = Integer.parseInt(quantity);
			 
			 int x = LibraryDao.addBook(bookName, bookAuthor, bookQuantity);
			 if (x > 0) {
				 JOptionPane.showMessageDialog(null, "Book has been successfully added!");
			 }
			 else {
				 JOptionPane.showMessageDialog(null, "An error has occured, book has not been added.");
			}
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex);
		}
	}
	
	
	// *****************************************************
	// REMOVE BOOKS METHOD
	// *****************************************************
	
	public void remove() {
		
		String ID = bookIDTxtField.getText();
		int bookID = Integer.parseInt(ID);
		
		int row = table.getSelectedRow();
		if (row != -1) {
			int modelRow = table.convertRowIndexToModel(row);
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			boolean selected = model.getValueAt(row, 0).toString() != null;
			model.removeRow(modelRow);
			
			int x = LibraryDao.removeBook(bookID);
			
				if (x > 0) {
				JOptionPane.showMessageDialog(null, "Book has been successfully deleted!");
				}
			}
		}
	
	
	
	// *****************************************************
	// SEARCH BOOKS METHOD
	// *****************************************************
	

	public void search() {
		
		try {
			
			table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Book ID", "Book Name", "Book Author", "Quantity"
				}
			));
		
		String[] columnNames = { "Book ID", "Book Name", "Book Author", "Quantity" };
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columnNames);
	
		UIFrame.getContentPane().add(bookTitleTxtField);
		bookTitleTxtField.setColumns(10);
		
		
		UIFrame.getContentPane().add(authorNameTxtField);
		authorNameTxtField.setColumns(10);
		
		String bookName = bookTitleTxtField.getText();
		String bookAuthor = authorNameTxtField.getText();
		
		// Actual SQL SELECT statement
		String sql = "SELECT * FROM Books WHERE (book_name LIKE '%" + bookName + "%') OR (book_author LIKE '%" + bookAuthor + "%');";
		PreparedStatement ps = connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		table.setModel(DbUtils.resultSetToTableModel(rs));
		} 
		catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex);
		}

	

	}
}




































