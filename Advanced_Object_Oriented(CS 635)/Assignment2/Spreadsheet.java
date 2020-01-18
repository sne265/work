
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane; 
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.JRadioButton;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
 
public class Spreadsheet extends JFrame
{  
    /** 
     * Class for GUI and event handling
     * to design view of the Spreadsheet project
	 */
	
	private static final long serialVersionUID = 1L;

	public Spreadsheet() 
    { 
        setTitle("Spreadsheet");
        createGUI();
    }
	
	public void createGUI()
	{
		
		TableModel model = new SpreadsheetTableModel() ;
        
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        JScrollPane sp = new JScrollPane(table); 
        
        JRadioButton valueViewButton = new JRadioButton("Value View"); 
        JRadioButton equationViewButton = new JRadioButton("Equation View"); 
        
        JButton undo = new JButton("UNDO");
        
        ButtonGroup viewButtonGroup = new ButtonGroup();
        viewButtonGroup.add(valueViewButton);
        viewButtonGroup.add(equationViewButton);
        valueViewButton.setSelected(true);
        
        // define action to perform when value view button is selected
        valueViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            	((SpreadsheetTableModel) model).actionValueSelected();
             	repaint();
             }
        });

        // define action to perform when value view button is selected
        equationViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            	((SpreadsheetTableModel) model).actionEquationSelected();
            	repaint();
            }
            
        });
        
     // define action to perform when undo button is selected
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            	((SpreadsheetTableModel) model).undoSelected();
            	
            	repaint();
            }
        });

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(valueViewButton);
        buttonPanel.add(equationViewButton);
        buttonPanel.add(undo);
        
        mainPanel.add(buttonPanel,BorderLayout.NORTH);
        mainPanel.add(sp,BorderLayout.CENTER);
        
        add(mainPanel);
        setSize(700, 150); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true); 
        
    } 
} 
