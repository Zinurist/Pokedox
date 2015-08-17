package prg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Pokedox extends JFrame{

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Pokedox mf = new Pokedox();
					mf.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private JPanel contentPane,spinnerPane,sGrid;
	private AutoPanel autoPanel;
	private JSpinner spin,s1,s2,s3;
	private JLabel result;
	private JTable table;
	private int prevRow,prevColumn;
	
	public Pokedox(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		Font f=new Font("Arial", Font.PLAIN,30);
		
		s1=new JSpinner(new SpinnerNumberModel(0,0,9,1));
		s1.setFont(f); 
		s1.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				//s2.requestFocusInWindow();
				try{
					setPokeLocation(getSpinNumber());
				}catch(Exception ex){
					s1.setValue(1);
				}
			}
		});
		
		s2=new JSpinner(new SpinnerNumberModel(0,0,9,1));
		s2.setFont(f); 
		s2.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				//s3.requestFocus();
				try{
					setPokeLocation(getSpinNumber());
				}catch(Exception ex){
					s2.setValue(1);
				}
			}
		});
		
		s3=new JSpinner(new SpinnerNumberModel(0,0,9,1));
		s3.setValue(1);
		s3.setFont(f); 
		s3.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				//s1.requestFocus();
				try{
					setPokeLocation(getSpinNumber());
				}catch(Exception ex){
					s3.setValue(1);
				}
			}
		});
		
		autoPanel=new AutoPanel(s1,s2,s3);
		autoPanel.setPreferredSize(new Dimension(50,50));
		/*
		autoPanel.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				System.out.println("Got it");
			}

			@Override
			public void focusLost(FocusEvent e) {
				System.out.println("Lost it");
			}
			
		});
		*/
		autoPanel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				autoPanel.setOn(!autoPanel.isOn());
			}
		});
		autoPanel.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent arg0) {}
			@Override
			public void keyReleased(KeyEvent arg0) {}
			@Override
			public void keyTyped(KeyEvent k) {
				char c=k.getKeyChar();
				try{
					int num=Integer.parseInt(c+"");
					autoPanel.setSpinnerValue(num);
				}catch(Exception e){
					//user didnt enter a number
				}
			}
		});
		
		sGrid=new JPanel();
		sGrid.add(autoPanel);
		sGrid.add(s1);
		sGrid.add(s2);
		sGrid.add(s3);
		
		spinnerPane=new JPanel(new BorderLayout());
		spin=new JSpinner(new SpinnerNumberModel(1,1,9999999,1));
		spin.setFont(f);
		spin.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				setPokeLocation((Integer) spin.getValue());
			}	
		});
		spinnerPane.add(spin,BorderLayout.NORTH);
		spinnerPane.add(sGrid,BorderLayout.CENTER);
		
		table=new JTable(5,6);
		
		result=new JLabel("Index -, box -, row -, column -");
		
		contentPane=new JPanel(new BorderLayout());
		contentPane.add(spinnerPane,BorderLayout.NORTH);
		contentPane.add(table,BorderLayout.CENTER);
		contentPane.add(result,BorderLayout.SOUTH);

		setContentPane(contentPane);
		pack();
		setLocation(100,100);
		setPokeLocation(1);
	}
	
	public int getSpinNumber(){
		String num = ""+(Integer)s1.getValue();
		num += ""+(Integer)s2.getValue();
		num += ""+(Integer)s3.getValue();
		return Integer.parseInt(num);
	}
	
	
	public void setPokeLocation(int index){
		try{
			int place=(index-1)%30;//001-030 or 061-090 -> 0-29
			int row=(place/6);//001->0->0,(0) |  010->9->1,(1.8)
			int column=(place%6);//001->0->0 | 010->9->4
			/*
			 *          0        1        2        3        4        5
			 * 0       001      002      003      004      005      006
			 * 1       007      008      009      010      011      012
			 * 2       013      014      015      016      017      018
			 * 3       ...
			 * 4 
			 */
			table.setValueAt("", prevRow, prevColumn);
			table.setValueAt("XXXXXXXXXX", row, column);
	
			result.setText("Index "+index+", box "+((index/30)+1)+", row "+(row+1)+", column "+(column+1));
			
			prevRow=row;
			prevColumn=column;
		}catch(RuntimeException e){
			throw e;
		}
	}
	
	
	
	
	
	
	
	private class AutoPanel extends JButton{
		
		private boolean on;
		private JSpinner[] spinners;
		private int curSpin;
		
		public AutoPanel(JSpinner... spin){
			on=false;
			setFocusable(true);
			spinners=spin;
			curSpin=0;
		}
		
		public void setSpinnerValue(int num){
			spinners[curSpin].setValue(num);
			curSpin++;
			if(curSpin>=spinners.length){
				curSpin=0;
			}
		}
		
		public boolean isOn(){
			return on;
		}
		
		public void setOn(boolean focus){
			on=focus;
			if(!focus){
				setFocusable(false);
				setFocusable(true);
			}
		}
		
		@Override
		public void paintComponent(Graphics g){
			if(on){
				g.setColor(Color.GREEN);
			}else{
				g.setColor(Color.RED);
			}
			
			g.fillRect(0,0,50,50);
		}
	}
	
	
}
