package itti.com.pl.arena.cm.client.ui;

import itti.com.pl.arena.cm.client.ContextModuleAdapter;
import itti.com.pl.arena.cm.client.ui.components.ButtonButtonRow;
import itti.com.pl.arena.cm.client.ui.components.ButtonRow;
import itti.com.pl.arena.cm.client.ui.components.ComboBoxButtonRow;
import itti.com.pl.arena.cm.client.ui.components.ComboBoxRow;
import itti.com.pl.arena.cm.client.ui.components.LabelComboBoxRow;
import itti.com.pl.arena.cm.client.ui.components.ImagePanel;
import itti.com.pl.arena.cm.client.ui.components.TextBoxButtonRow;
import itti.com.pl.arena.cm.client.ui.components.LabelTextBoxRow;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

public abstract class ContextModulePanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private ContextModuleAdapter contextModuleAdapter = null;

    /**
     * Create the dialog.
     */
    public ContextModulePanel() {
        setLayout(new BorderLayout());
        add(createButtonsMenu(), BorderLayout.SOUTH);
    }

    private Component createButtonsMenu() {
        JPanel buttonsPanel = createJPanel();
        buttonsPanel.setLayout(new GridLayout(1, 3, 5, 5));
        JButton refreshButton = new JButton(Messages.getString("ContextModulePanel.0")); //$NON-NLS-1$
        refreshButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (getContextModuleAdapter().isConnected()) {
                    onRefreshClick();
                } else {
                    showMessage(Messages.getString("ContextModulePanel.1")); //$NON-NLS-1$
                }

            }
        });
        JButton saveButton = new JButton(Messages.getString("ContextModulePanel.2")); //$NON-NLS-1$
        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (getContextModuleAdapter().isConnected()) {
                    onSaveClick();
                } else {
                    showMessage(Messages.getString("ContextModulePanel.3")); //$NON-NLS-1$
                }

            }
        });
        JButton cancelButton = new JButton(Messages.getString("ContextModulePanel.4")); //$NON-NLS-1$
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (getContextModuleAdapter().isConnected()) {
                    onCancelClick();
                } else {
                    showMessage(Messages.getString("ContextModulePanel.5")); //$NON-NLS-1$
                }

            }
        });

        buttonsPanel.add(refreshButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);
        return buttonsPanel;
    }

    protected abstract void onCancelClick();

    protected abstract void onSaveClick();

    protected abstract void onRefreshClick();

    protected LabelTextBoxRow createLabelTextBoxRow(String labelText, String text) {

        return new LabelTextBoxRow(labelText, text);
    }

    protected ComboBoxButtonRow createComboBoxButtonRow(String buttonText, List<String> content) {

        return new ComboBoxButtonRow(buttonText, content);
    }

    protected LabelComboBoxRow createLabelComboBoxRow(String label, List<String> items) {
        return new LabelComboBoxRow(label, items);
    }

    protected ComboBoxRow createComboBoxRow(List<String> items) {
        return new ComboBoxRow(items);
    }

    protected TextBoxButtonRow createTextBoxButtonRow(String textBoxText, String buttonText) {

        return new TextBoxButtonRow(textBoxText, buttonText);
    }

    protected ButtonRow createButtonRow(String buttonText) {

        return new ButtonRow(buttonText);
    }

    protected ButtonButtonRow createButtonButtonRow(String buttonOneText, String buttonTwoText) {
        return new ButtonButtonRow(buttonOneText, buttonTwoText);
    }

    protected JPanel createJPanel() {
        JPanel panelPlatform = new JPanel();
        panelPlatform.setLayout(new GridLayout(14, 1, 10, 10));
        panelPlatform.setBorder(new EmptyBorder(10, 10, 10, 10));
        return panelPlatform;
    }

    protected Component createEmptyRow() {
        return new JLabel();
    }

    protected Component createLabelRow(String message) {
        return new JLabel(message);
    }

    public void setContextModule(ContextModuleAdapter cmAdapter) {
        this.contextModuleAdapter = cmAdapter;
    }

    protected ContextModuleAdapter getContextModuleAdapter() {
        return contextModuleAdapter;
    }

    protected Component createImagePanel(String imagePath) {
        ImagePanel panel = new ImagePanel(imagePath);
        JPanel parentPanel = new JPanel();
        parentPanel.add(panel);
        parentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        return parentPanel;
    }

    protected void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}
