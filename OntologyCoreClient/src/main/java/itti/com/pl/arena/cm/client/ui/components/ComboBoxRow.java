package itti.com.pl.arena.cm.client.ui.components;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class ComboBoxRow extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JComboBox<String> comboBox = null;

    public ComboBoxRow(List<String> content) {
        super(new GridLayout(1, 1));
        comboBox = new JComboBox<String>();
        setItems(content);
        add(comboBox);
    }

    public void setItems(List<String> content) {

        comboBox.removeAllItems();
        if (content != null) {
            for (String item : content) {
                comboBox.addItem(item);
            }
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

    public void removeItem(String item) {
        comboBox.removeItem(item);
    }

    public void addItem(String item) {
        comboBox.addItem(item);
    }

    public List<String> getItems() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            items.add(comboBox.getItemAt(i));
        }
        return items;
    }

    public void removeSelectedItem() {
        if (comboBox.getSelectedItem() != null) {
            comboBox.removeItem(comboBox.getSelectedItem());
        }
    }

}
