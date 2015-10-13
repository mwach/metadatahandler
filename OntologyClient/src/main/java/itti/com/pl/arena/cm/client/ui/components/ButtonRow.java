package itti.com.pl.arena.cm.client.ui.components;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ButtonRow extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JButton button = null;

    public ButtonRow(String buttonText) {
        super(new GridLayout(1, 2));
        button = new JButton(buttonText);
        add(new JLabel());
        add(button);
    }

    public void setOnClickListener(ActionListener listener) {
        button.addActionListener(listener);
        ;
    }

    public void setButtonText(String buttonText) {
        button.setText(buttonText);
    }
}
