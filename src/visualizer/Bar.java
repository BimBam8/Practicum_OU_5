package visualizer;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Klasse die een Bar (staafje) representeert. Een bar krijgr een label (label name - value).
 * 
 * @author Medewerker OU
 *
 */

public class Bar extends JPanel {

	private static final long serialVersionUID = 1L;
	private String labelName;
	private int labelValue = 0;

	public Bar(String labelName, int labelValue, int x, int y, int width, int height, Color color) {
		super();
		this.labelName = labelName;
		this.labelValue = labelValue;
		JLabel label = new JLabel(labelName + " - " + labelValue);
		label.setFont(new Font("", Font.ITALIC , 9));
		label.setForeground(Color.white);
		this.add(label);
		this.setBounds(x, y, width, height);
		this.setBackground(color);
	}	
	
	public String getName() {
		return labelName;
	}

	public int getLabelValue() {
		return labelValue;
	}

}
