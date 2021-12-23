package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;

import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ItemEvent;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Options_GUI extends JFrame {

	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Options_GUI frame = new Options_GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Options_GUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 791, 487);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(139, 69, 19));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Options");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblNewLabel.setBounds(276, 4, 195, 54);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Board Color");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(10, 76, 117, 26);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Play Sound");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_1.setBounds(10, 126, 111, 26);
		contentPane.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Show legal moves");
		lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_1_1.setBounds(10, 181, 170, 26);
		contentPane.add(lblNewLabel_1_1_1);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNewLabel_2.setBounds(389, 69, 350, 350);
		contentPane.add(lblNewLabel_2);
		
		File imgFile = new File("resource\\default.png");
		try {
			Image defaultPreview = ImageIO.read(imgFile);
			Image defaultImage = defaultPreview.getScaledInstance(lblNewLabel_2.getWidth(), lblNewLabel_2.getHeight(), Image.SCALE_SMOOTH);
			lblNewLabel_2.setIcon(new ImageIcon(defaultImage));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		JComboBox comboBoxColor = new JComboBox();
		comboBoxColor.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				File imgFile = null;							
									
				if (comboBoxColor.getSelectedIndex() == 1) {
					imgFile = new File("resource\\green.png");
				} else if (comboBoxColor.getSelectedIndex() == 2) {
					imgFile = new File("resource\\blue.png");
				} else {
					imgFile = new File("resource\\default.png");
				}
				
				Image imgpreview = null;
				try {
					imgpreview = ImageIO.read(imgFile);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				Image preview = imgpreview.getScaledInstance(lblNewLabel_2.getWidth(), lblNewLabel_2.getHeight(), Image.SCALE_SMOOTH);
				lblNewLabel_2.setIcon(new ImageIcon(preview));
			}});
		comboBoxColor.setFont(new Font("Tahoma", Font.PLAIN, 20));
		comboBoxColor.setModel(new DefaultComboBoxModel(new String[] {"Default", "Green", "Blue"}));
		comboBoxColor.setBounds(199, 69, 170, 33);
		contentPane.add(comboBoxColor);
		
		JCheckBox checkSound = new JCheckBox("Turn on");
		checkSound.setSelected(true);
		checkSound.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (!checkSound.isSelected()) {
					checkSound.setText("Turn off");
				} else {
					checkSound.setText("Turn on");
				}
			}
		});
		checkSound.setFont(new Font("Tahoma", Font.PLAIN, 20));
		checkSound.setBounds(199, 121, 170, 36);
		contentPane.add(checkSound);
		
		JCheckBox checkShowMoves = new JCheckBox("Turn on");
		checkShowMoves.setSelected(true);
		checkShowMoves.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (!checkShowMoves.isSelected()) {
					checkShowMoves.setText("Turn off");
				} else {
					checkShowMoves.setText("Turn on");
				}
			}
		});
		checkShowMoves.setFont(new Font("Tahoma", Font.PLAIN, 20));
		checkShowMoves.setBounds(199, 176, 170, 36);
		contentPane.add(checkShowMoves);
		
		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//String color = comboBoxColor.getSelectedItem().toString()	
				try {
					FileWriter color = new FileWriter("resource\\Color.txt");
					
					if (comboBoxColor.getSelectedIndex() == 1) {
						color.write("Green : 23, 255, 27 \n"
							         + "LittleWhite : 189, 255, 190");
					} else if (comboBoxColor.getSelectedIndex() == 2) {
						color.write("Blue : 31, 49, 255 \n"
							         + "LittleWhite : 200, 204, 247");
					} else {
						color.write("Brown : 212, 101, 4 \n"
								     + "LittleWhite : 245, 220, 198");
					}
					
					color.close();
					
					
					FileWriter sound = new FileWriter("resource\\Sound.txt");
					
					if (!checkSound.isSelected() && !checkShowMoves.isSelected()) {
						sound.write("Sound play : false \n"
								  + "Show moves : false");
					} else if (!checkShowMoves.isSelected()) {
						sound.write("Sound play : true \n"
								  + "Show moves : false");
					} else if (!checkSound.isSelected()) {
						sound.write("Sound play : false \n"
								  + "Show moves : true");
					} else {
						sound.write("Sound play : true \n"
								  + "Show moves : true");
					}
					
					sound.close();
				} catch(Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnApply.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnApply.setBounds(16, 379, 111, 40);
		contentPane.add(btnApply);
		
		JButton btnReset = new JButton("Reset to default");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboBoxColor.setSelectedIndex(0);
				checkSound.setSelected(true); checkSound.setText("Turn on");
				checkShowMoves.setSelected(true); checkShowMoves.setText("Turn on");
			}
		});
		btnReset.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnReset.setBounds(184, 379, 195, 40);
		contentPane.add(btnReset);
		
		addWindowListener(new java.awt.event.WindowAdapter() {
    	    @Override
    	    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
    	    	Menu_GUI menu = new Menu_GUI();
    	    	menu.setVisible(true);
    	    }
        });
	}
}
