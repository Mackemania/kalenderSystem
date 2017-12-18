

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class kalenderSystem_spinner extends JSpinner implements FocusListener{
	
	private Font newFont;
	private int min;
	private int max;
	private int value;
	private int stepSize;
	
	
	public kalenderSystem_spinner(int min, int max, int value, int stepSize, Font newFont, int alignment) {
		//super();
		this.min = min;
		this.max = max;
		this.value = value;
		this.stepSize = stepSize;
		this.newFont = newFont;
		
		SpinnerNumberModel model = new SpinnerNumberModel();
		model.setMinimum(min);
		model.setMaximum(max);
		model.setValue(value);
		model.setStepSize(stepSize);
		
		
		super.setModel(model);
		super.setFont(newFont);
		super.setBackground(new Color(255, 255, 255));
		
		for(int i = 0; i<super.getComponents().length; i++) {
			Component[] comp = super.getComponents();
			
			if(comp[i].getClass().getSimpleName().equals("BasicArrowButton")) {
				
				comp[i].setVisible(false);
				
				
			}
		}
		
		JComponent editor = super.getEditor();
		JSpinner.NumberEditor e = (JSpinner.NumberEditor) editor;
		
		e.getTextField().setHorizontalAlignment(alignment);
		if(value<10) {
			e.getTextField().setText("0"+value);
		}
		DecimalFormat format = e.getFormat();
		DecimalFormatSymbols s = new DecimalFormatSymbols();
		s.setZeroDigit('0');
		format.setDecimalFormatSymbols(s);
		format.applyLocalizedPattern("00");
		
		super.setEditor(e);
		
	}
	
	public kalenderSystem_spinner() {
		
		
	}
	
	public String getText() {
		
		JComponent editor = super.getEditor();
		JSpinner.NumberEditor e = (JSpinner.NumberEditor) editor;
		String text = e.getTextField().getText();
		
		return text;
	}

	
	public void focusGained(FocusEvent arg) {
		
		JComponent editor = super.getEditor();
		JSpinner.DefaultEditor e = (JSpinner.DefaultEditor) editor;
		e.getTextField().setHorizontalAlignment(JTextField.LEFT);
		e.getTextField().setCaretPosition(e.getTextField().getText().length());
		System.out.println("HEJ");
		
	}

	
	public void focusLost(FocusEvent arg) {
		
	}

}
