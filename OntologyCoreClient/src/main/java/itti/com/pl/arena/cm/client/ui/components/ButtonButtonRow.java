package itti.com.pl.arena.cm.client.ui.components;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonButtonRow extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JButton buttonOne = null;
    private JButton buttonTwo = null;

    public ButtonButtonRow(String buttonOneText, String buttonTwoText) {
        super(new GridLayout(1, 2));
        buttonOne = new JButton(buttonOneText);
        add(buttonOne);
        buttonTwo = new JButton(buttonTwoText);
        add(buttonTwo);
    }

    public void setOnFirstButtonClickListener(ActionListener actionListener) {
        if (actionListener != null) {
            buttonOne.addActionListener(actionListener);
        }
    }

    public void setOnSecondButtonClickListener(ActionListener actionListener) {
        if (actionListener != null) {
            buttonTwo.addActionListener(actionListener);
        }
    }
}
