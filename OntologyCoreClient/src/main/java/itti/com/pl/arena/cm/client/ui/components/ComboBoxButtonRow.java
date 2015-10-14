package itti.com.pl.arena.cm.client.ui.components;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;

public class ComboBoxButtonRow extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JComboBox<String> comboBox = null;
    private JButton button = null;

    public ComboBoxButtonRow(String label, List<String> content) {
        super(new GridLayout(1, 2));
        button = new JButton(label);
        comboBox = new JComboBox<String>();
        if (content != null) {
            for (String item : content) {
                comboBox.addItem(item);
            }
        }
        add(comboBox);
        add(button);
    }

    public void setItems(List<String> content) {
        comboBox.removeAllItems();
        if (content != null) {
            for (String item : content) {
                comboBox.addItem(item);
            }
        }
    }

    public void addItem(String item) {
        if (StringUtils.isNotEmpty(item)) {
            comboBox.addItem(item);
        }
    }

    public void setOnChangeListener(ActionListener actionListener) {
        if (actionListener != null) {
            comboBox.addActionListener(actionListener);
        }
    }

    public String getSelectedItem() {
        return comboBox.getSelectedItem() != null ? String.valueOf(comboBox.getSelectedItem()) : null;
    }

    public void setSelectedItem(String item) {
        if (StringUtils.isNotEmpty(item)) {
            if (getItems().contains(item)) {
                comboBox.setSelectedItem(item);
            }
        }
    }

    public List<String> getItems() {
        List<String> content = new ArrayList<>();
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            content.add(comboBox.getItemAt(i));
        }
        return content;
    }

    public void setOnClickListener(ActionListener actionListener) {
        if (actionListener != null) {
            button.addActionListener(actionListener);
        }
    }

    public void removeSelectedItem() {
        if (comboBox.getSelectedItem() != null) {
            comboBox.removeItem(comboBox.getSelectedItem());
        }
    }

    public boolean containsItem(String item) {
        return (StringUtils.isNotEmpty(item) && getItems() != null) ? getItems().contains(item) : null;
    }
}
