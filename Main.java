package dbs_app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;

public class Main {

	public JFrame frame;
	public JTextField pacientIdField;
	public static JTextField pMenoField;
	public static JTextField pAdresaField;
	public static JTextField pohlavieField;
	public static JTextField telefonField;
	public JTextField zDatumField;
	public JTextField zCasField;
	public JTextField zIdField;
	static JTextArea vypisArea;
	public static JCheckBox pacientHospitChbx;
	public static JCheckBox pacientVyliecenyChbx;
	public pripojenieDatabazy databaza;
	public static String [] diagnozaComboboxHodnoty = new String[25];
	public static String [] lekarComboboxHodnoty = new String[20];
	public static String [] vykonComboboxHodnoty = new String[4];
	private JTextField pacientMenoField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws SQLException 
	 */
	public Main() throws SQLException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws SQLException 
	 */
	private void initialize() throws SQLException {
		
		databaza = new pripojenieDatabazy();
		databaza.naplnenieComboboxov();
		
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 699);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		pacientIdField = new JTextField();
		pacientIdField.setBounds(89, 458, 196, 26);
		frame.getContentPane().add(pacientIdField);
		pacientIdField.setColumns(10);
		
		JLabel lblPacientId = new JLabel("Pacient ID:");
		lblPacientId.setBounds(16, 463, 79, 16);
		frame.getContentPane().add(lblPacientId);
		
		JButton zobrazPacientaButton = new JButton("Zobraz pacienta");
		zobrazPacientaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pacientId = pacientIdField.getText();
				if (pacientId.length() == 0)
					JOptionPane.showMessageDialog(null, "pole ID musi byt vyplnene!", "Pozor", JOptionPane.INFORMATION_MESSAGE);
				else
				{
				try {
					pripojenieDatabazy.vypisPacientovPodlaId(pacientId);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			}
		});
		zobrazPacientaButton.setBounds(6, 496, 133, 29);
		frame.getContentPane().add(zobrazPacientaButton);
		
		JButton upravPacientaButton = new JButton("Uprav pacienta");
		upravPacientaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pacientId = pacientIdField.getText();
				if (pacientId.length() == 0)
					JOptionPane.showMessageDialog(null, "pole ID musi byt vyplnene!", "Pozor", JOptionPane.INFORMATION_MESSAGE);
				else
				{
				try {
					pripojenieDatabazy.aktualizujPacienta(pacientId);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
			}
		});
		upravPacientaButton.setBounds(151, 496, 117, 29);
		frame.getContentPane().add(upravPacientaButton);
		
		JButton vymazPacientaButton = new JButton("Vymaž pacienta");
		vymazPacientaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pacientId = pacientIdField.getText();
				if (pacientId.length() == 0)
					JOptionPane.showMessageDialog(null, "pole ID musi byt vyplnene!", "Pozor", JOptionPane.INFORMATION_MESSAGE);
				else{
				pacientIdField.setText("");
				try {
					pripojenieDatabazy.vymazPacienta(pacientId);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
			}
		});
		vymazPacientaButton.setBounds(280, 496, 126, 29);
		frame.getContentPane().add(vymazPacientaButton);
		
		JButton statistikaButton = new JButton("Zobraz štatistiku");
		statistikaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					pripojenieDatabazy.zobrazStatistiku();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		statistikaButton.setBounds(6, 537, 133, 29);
		frame.getContentPane().add(statistikaButton);
		
		JButton zZobrazButton = new JButton("Zobraz");
		zZobrazButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = zIdField.getText();
				zIdField.setText("");
				if (id.length() == 0)
					JOptionPane.showMessageDialog(null, "Pole ID musi byt vyplnene", "Pozor", JOptionPane.INFORMATION_MESSAGE);
				else
				{
				try {
					pripojenieDatabazy.zobrazZaznam(id);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
			}
		});
		zZobrazButton.setBounds(786, 548, 104, 29);
		frame.getContentPane().add(zZobrazButton);
		
		JLabel lblMeno = new JLabel("Meno:");
		lblMeno.setBounds(516, 11, 38, 16);
		frame.getContentPane().add(lblMeno);
		
		pMenoField = new JTextField();
		pMenoField.setBounds(566, 6, 427, 26);
		frame.getContentPane().add(pMenoField);
		pMenoField.setColumns(10);
		
		JLabel lblAdresa = new JLabel("Adresa:");
		lblAdresa.setBounds(516, 44, 47, 16);
		frame.getContentPane().add(lblAdresa);
		
		pAdresaField = new JTextField();
		pAdresaField.setBounds(576, 39, 417, 26);
		frame.getContentPane().add(pAdresaField);
		pAdresaField.setColumns(10);
		
		JLabel lblPohlavie = new JLabel("Pohlavie:");
		lblPohlavie.setBounds(516, 72, 61, 16);
		frame.getContentPane().add(lblPohlavie);
		
		pohlavieField = new JTextField();
		pohlavieField.setBounds(586, 67, 38, 26);
		frame.getContentPane().add(pohlavieField);
		pohlavieField.setColumns(10);
		
		JLabel lblTelefn = new JLabel("Telefón:");
		lblTelefn.setBounds(636, 72, 51, 16);
		frame.getContentPane().add(lblTelefn);
		
		telefonField = new JTextField();
		telefonField.setBounds(699, 67, 294, 26);
		frame.getContentPane().add(telefonField);
		telefonField.setColumns(10);
		
		JComboBox pLekarBox = new JComboBox(lekarComboboxHodnoty);
		//pLekarBox.setModel(new DefaultComboBoxModel(new String[] {"Lekár"}));
		pLekarBox.setBounds(729, 162, 161, 27);
		frame.getContentPane().add(pLekarBox);
		
		JButton pPridajButton = new JButton("Pridaj");
		pPridajButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pacientMeno = pMenoField.getText();
				String pacientAdresa = pAdresaField.getText();
				String pacientPohlavie = pohlavieField.getText();
				String pacientTelefon = telefonField.getText();
				boolean pacientHospit = pacientHospitChbx.isSelected();
				boolean pacientVylieceny = pacientVyliecenyChbx.isSelected();
				
				String lekar = (String) pLekarBox.getSelectedItem();
				
				
				if(pacientMeno.length()==0 || pacientAdresa.length()==0)
					JOptionPane.showMessageDialog(null, "Nevyplnili ste vsetky potrebne polia!", "Pozor", JOptionPane.INFORMATION_MESSAGE);
				else
				{
					pMenoField.setText("");
					pAdresaField.setText("");
					pohlavieField.setText("");
					telefonField.setText("");
					pacientHospitChbx.setSelected(false);
					pacientVyliecenyChbx.setSelected(false);
				try {
					pripojenieDatabazy.pridajPacienta(pacientMeno, pacientAdresa, pacientPohlavie, pacientTelefon, pacientHospit, pacientVylieceny, lekar);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
			}
		});
		pPridajButton.setBounds(656, 197, 79, 29);
		frame.getContentPane().add(pPridajButton);
		
		JButton pAktualizujButton = new JButton("Aktualizuj");
		pAktualizujButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pacientId = pacientIdField.getText();
				String pacientMeno = pMenoField.getText();
				String pacientAdresa = pAdresaField.getText();
				String pacientPohlavie = pohlavieField.getText();
				String pacientTelefon = telefonField.getText();
				boolean pacientHospit = pacientHospitChbx.isSelected();
				boolean pacientVylieceny = pacientVyliecenyChbx.isSelected();
				if (pacientId.length() == 0 || pacientMeno.length() == 0 || pacientAdresa.length() == 0 || pacientTelefon.length() == 0)
					JOptionPane.showMessageDialog(null, "Všetky polia musia byť vyplnené!", "Pozor", JOptionPane.INFORMATION_MESSAGE);
				else
				{
					pMenoField.setText("");
					pAdresaField.setText("");
					pohlavieField.setText("");
					telefonField.setText("");
					pacientHospitChbx.setSelected(false);
					pacientVyliecenyChbx.setSelected(false);
				try {
					pripojenieDatabazy.updatePacienta(pacientId, pacientMeno, pacientAdresa, pacientPohlavie, pacientTelefon, pacientHospit, pacientVylieceny);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
			}
		});
		pAktualizujButton.setBounds(747, 197, 117, 29);
		frame.getContentPane().add(pAktualizujButton);
		
		JLabel lblDtum = new JLabel("Dátum:");
		lblDtum.setBounds(516, 253, 47, 16);
		frame.getContentPane().add(lblDtum);
		
		zDatumField = new JTextField();
		zDatumField.setBounds(566, 248, 130, 26);
		frame.getContentPane().add(zDatumField);
		zDatumField.setColumns(10);
		
		JLabel lblas = new JLabel("Čas:");
		lblas.setBounds(708, 253, 27, 16);
		frame.getContentPane().add(lblas);
		
		zCasField = new JTextField();
		zCasField.setBounds(747, 248, 71, 26);
		frame.getContentPane().add(zCasField);
		zCasField.setColumns(10);
		
		JComboBox vykonBox = new JComboBox(vykonComboboxHodnoty);
		//vykonBox.setModel(new DefaultComboBoxModel(new String[] {"Výkon"}));
		vykonBox.setBounds(516, 281, 106, 27);
		frame.getContentPane().add(vykonBox);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(516, 320, 466, 164);
		frame.getContentPane().add(scrollPane_1);
		
		JTextArea zTextArea = new JTextArea();
		scrollPane_1.setViewportView(zTextArea);
		
		JComboBox diagnoza2Box = new JComboBox(diagnozaComboboxHodnoty);
		diagnoza2Box.setBounds(645, 281, 173, 27);
		frame.getContentPane().add(diagnoza2Box);
		
		JComboBox lekarBox = new JComboBox(lekarComboboxHodnoty);
		lekarBox.setBounds(832, 281, 161, 27);
		frame.getContentPane().add(lekarBox);
		
		JButton zPridajZaznamButton = new JButton("Pridaj záznam");
		zPridajZaznamButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String datum = zDatumField.getText();
				String cas = zCasField.getText();
				String pacientId = pacientIdField.getText();
				String vykon = (String) vykonBox.getSelectedItem();
				String diagnoza = (String) diagnoza2Box.getSelectedItem();
				String lekar = (String) lekarBox.getSelectedItem();
				String text = zTextArea.getText();
				if (datum.length() == 0 || cas.length() == 0 || text.length() == 0)
					JOptionPane.showMessageDialog(null, "Nevyplnili ste vsetky potrebne polia!", "Pozor", JOptionPane.INFORMATION_MESSAGE);
				else
				{
				zDatumField.setText("");
				zCasField.setText("");
				zTextArea.setText("");
				try {
					pripojenieDatabazy.pridajZaznam(datum, cas, pacientId, vykon, diagnoza, lekar, text);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
				
			}
		});
		zPridajZaznamButton.setBounds(670, 496, 117, 29);
		frame.getContentPane().add(zPridajZaznamButton);
		
		JLabel lblZznamId_1 = new JLabel("Záznam ID:");
		lblZznamId_1.setBounds(516, 553, 79, 16);
		frame.getContentPane().add(lblZznamId_1);
		
		zIdField = new JTextField();
		zIdField.setBounds(594, 549, 174, 26);
		frame.getContentPane().add(zIdField);
		zIdField.setColumns(10);
		
		JButton zVymazButton = new JButton("Vymaž");
		zVymazButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = zIdField.getText();
				zIdField.setText("");
				if (id.length() == 0)
					JOptionPane.showMessageDialog(null, "Pole ID musi byt vyplene!", "Pozor", JOptionPane.INFORMATION_MESSAGE);
				else
				{
				try {
					pripojenieDatabazy.vymazZaznam(id);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
			}
		});
		zVymazButton.setBounds(900, 548, 93, 29);
		frame.getContentPane().add(zVymazButton);
		
		JLabel lblPriezvyskoDoktora = new JLabel("Meno lekara:");
		lblPriezvyskoDoktora.setBounds(516, 595, 85, 16);
		frame.getContentPane().add(lblPriezvyskoDoktora);

		JComboBox menoLekaraBox = new JComboBox(lekarComboboxHodnoty);
		//comboBox_4.setModel(new DefaultComboBoxModel(new String[] {"Choroba"}));
		menoLekaraBox.setBounds(616, 591, 190, 27);
		frame.getContentPane().add(menoLekaraBox);
		
		JButton btnZobraz_1 = new JButton("Registrovaní pacienti");
		btnZobraz_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String lekar = (String) menoLekaraBox.getSelectedItem();
				try {
					pripojenieDatabazy.zobrazPacientovLekara(lekar);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnZobraz_1.setBounds(818, 590, 164, 29);
		frame.getContentPane().add(btnZobraz_1);
		
		JButton btnZobrazPacientov = new JButton("Zobraz všetkých pacientov");
		btnZobrazPacientov.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					pripojenieDatabazy.zobrazVsetkychPacientov();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnZobrazPacientov.setBounds(161, 537, 212, 29);
		frame.getContentPane().add(btnZobrazPacientov);
		
		pacientHospitChbx = new JCheckBox("Pacient hospitalizovany");
		pacientHospitChbx.setBounds(546, 127, 196, 23);
		frame.getContentPane().add(pacientHospitChbx);
		
		pacientVyliecenyChbx = new JCheckBox("Pacient vyliečený");
		pacientVyliecenyChbx.setBounds(841, 127, 153, 23);
		frame.getContentPane().add(pacientVyliecenyChbx);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(16, 11, 488, 444);
		frame.getContentPane().add(scrollPane);
		
		vypisArea = new JTextArea();
		scrollPane.setViewportView(vypisArea);
		
		JLabel lblPrijmajciLekr = new JLabel("Registrujúci lekár:");
		lblPrijmajciLekr.setBounds(612, 166, 121, 16);
		frame.getContentPane().add(lblPrijmajciLekr);
		
		JButton btnAktulny = new JButton("Aktuálny");
		btnAktulny.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
		    	String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
		    	zDatumField.setText(date);
		    	zCasField.setText(time);
			}
		});
		btnAktulny.setBounds(865, 248, 117, 29);
		frame.getContentPane().add(btnAktulny);
		
		JLabel label = new JLabel("Meno lekara:");
		label.setBounds(516, 642, 85, 16);
		frame.getContentPane().add(label);
		
		JComboBox menoLekara2Box = new JComboBox(lekarComboboxHodnoty);
		menoLekara2Box.setBounds(616, 638, 190, 27);
		frame.getContentPane().add(menoLekara2Box);
		
		JButton vysetreniPacientiButton = new JButton("Vyšetrení pacienti");
		vysetreniPacientiButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					String lekar = (String) menoLekara2Box.getSelectedItem();
					try {
						pripojenieDatabazy.zobrazVysetrenychPacientovLekara(lekar);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			
		});
		vysetreniPacientiButton.setBounds(818, 637, 164, 29);
		frame.getContentPane().add(vysetreniPacientiButton);
		
		pacientMenoField = new JTextField();
		pacientMenoField.setBounds(16, 612, 219, 26);
		frame.getContentPane().add(pacientMenoField);
		pacientMenoField.setColumns(10);
		
		JButton pacientMenoButton = new JButton("Zobraz pacientov");
		pacientMenoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String meno = pacientMenoField.getText();
				if (meno.length() == 0)
					JOptionPane.showMessageDialog(null, "Pole meno musi byt vyplene!", "Pozor", JOptionPane.INFORMATION_MESSAGE);
				else
				{
					pacientMenoField.setText("");
				try {
					vypisArea.setText("");
					pripojenieDatabazy.zobrazPacientovPodlaMena(meno);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			}
		});
		pacientMenoButton.setBounds(256, 612, 133, 29);
		frame.getContentPane().add(pacientMenoButton);
		
		
		
		
		
		//vypisArea.setText("ahoj");
	}
}
