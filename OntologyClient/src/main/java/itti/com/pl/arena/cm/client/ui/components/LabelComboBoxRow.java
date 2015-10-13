package itti.com.pl.arena.cm.client.ui.components;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;

public class LabelComboBoxRow extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JLabel textLabel = null;
    private JComboBox<String> comboBox = null;

    public LabelComboBoxRow(String label, List<String> content) {
        super(new GridLayout(1, 2));
        textLabel = new JLabel();
        comboBox = new JComboBox<String>();
        setLabelText(label);
        setComboBoxContent(content);
        add(textLabel);
        add(comboBox);
    }

    public void setLabelText(String label) {
        if (label != null) {
            textLabel.setText(label);
        }
    }

    public void setComboBoxContent(List<String> content) {

        comboBox.removeAllItems();
        if (content != null) {
            for (String item : content) {
                comboBox.addItem(item.length() > 20 ? item.substring(0, 20) : item);
            }
        }
    }

    public void setOnChangeListener(ActionListener actionListener) {
        if (actionListener != null) {
            comboBox.addActionListener(actionListener);
        }
    }

    public String getSelectedItem() {
        return String.valueOf(comboBox.getSelectedItem());
    }

    public List<String> getItems() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            items.add(comboBox.getItemAt(i));
        }
        return items;
    }

    public void setSelectedItem(String item) {
        if (StringUtils.isNotEmpty(item) && getItems().contains(item)) {
            comboBox.setSelectedItem(item);
        }
    }

}
