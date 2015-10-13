package itti.com.pl.arena.cm.client.ui.components;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LabelTextBoxRow extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JLabel label;
    private JTextField textField;

    public LabelTextBoxRow(String labelText, String text) {
        super(new GridLayout(1, 2));
        label = new JLabel();
        add(label);
        textField = new JTextField();
        add(textField);

        setLabelText(labelText);
        setText(text);
    }

    public String getText() {
        return textField.getText();
    }

    public void setLabelText(String text) {
        label.setText(text != null ? text : "");
    }

    public void setText(String text) {
        textField.setText(text != null ? text : "");
    }
}
