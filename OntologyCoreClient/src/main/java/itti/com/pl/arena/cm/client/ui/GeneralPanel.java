package itti.com.pl.arena.cm.client.ui;

import itti.com.pl.arena.cm.client.ui.components.ButtonRow;
import itti.com.pl.arena.cm.client.ui.components.ComboBoxButtonRow;
import itti.com.pl.arena.cm.client.ui.components.LabelTextBoxRow;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;

public class GeneralPanel extends ContextModulePanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private LabelTextBoxRow brokerUrlRow = null;
    private ButtonRow connectRow = null;
    private JTextArea logComponent = null;
    private ComboBoxButtonRow restoreOntologyRow = null;

    /**
     * Create the dialog.
     */
    public GeneralPanel() {

        super();
        add(createPanelGeneral(), BorderLayout.CENTER);
    }

    private Component createPanelGeneral() {

        JPanel panelGeneral = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        panelGeneral.setLayout(gbl);
        panelGeneral.setBorder(new EmptyBorder(10, 10, 10, 10));

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(3, 3, 3, 3);

        brokerUrlRow = createLabelTextBoxRow(Messages.getString("GeneralPanel.0"), null); //$NON-NLS-1$
        brokerUrlRow.setText(Messages.getString("GeneralPanel.1")); //$NON-NLS-1$
        gbl.setConstraints(brokerUrlRow, gbc);
        panelGeneral.add(brokerUrlRow);

        connectRow = createButtonRow(Messages.getString("GeneralPanel.2")); //$NON-NLS-1$
        gbl.setConstraints(connectRow, gbc);
        panelGeneral.add(connectRow);
        connectRow.setOnClickListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                connectToCM();
            }
        });

        Component emptyComponent = createEmptyRow();
        gbl.setConstraints(emptyComponent, gbc);
        panelGeneral.add(emptyComponent);

        Component logDescComponent = createLabelRow(Messages.getString("GeneralPanel.3")); //$NON-NLS-1$
        gbl.setConstraints(logDescComponent, gbc);
        panelGeneral.add(logDescComponent);

        gbc.weighty = 10.0;
        logComponent = new JTextArea(30, 100);
        gbl.setConstraints(logComponent, gbc);
        panelGeneral.add(logComponent);

        gbc.weighty = 0.0;
        ButtonRow saveOntologyRow = createButtonRow("Save ontology"); //$NON-NLS-1$
        saveOntologyRow.setOnClickListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(getContextModuleAdapter().saveOntology()){
                    showMessage("Ontology successfully saved");
                }else{
                    showMessage("Could not save ontology");                    
                }
                onRefreshClick();
            }
        });
        gbl.setConstraints(saveOntologyRow, gbc);
        panelGeneral.add(saveOntologyRow);
        restoreOntologyRow = createComboBoxButtonRow("Restore ontology", null); //$NON-NLS-1$
        restoreOntologyRow.setOnClickListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                String ontologyName = restoreOntologyRow.getSelectedItem();
                if(StringUtils.isEmpty(ontologyName)){
                    showMessage("Select ontology to load first");
                }else{
                    if(getContextModuleAdapter().loadOntology(ontologyName)){
                        showMessage(String.format("Ontology '%s' successfully loaded", ontologyName));
                    }else{
                        showMessage("Could not load selected ontology");
                    }
                }
            }
        });
        gbl.setConstraints(restoreOntologyRow, gbc);
        panelGeneral.add(restoreOntologyRow);

        return panelGeneral;
    }

    private void connectToCM() {
        if (!getContextModuleAdapter().isConnected()) {
            connect();
        } else {
            disconnect();
        }
    }

    private void connect() {

        String brokerUrl = brokerUrlRow.getText();
        try {
            getContextModuleAdapter().connect(brokerUrl);
            addLogMessage(Messages.getString("GeneralPanel.4")); //$NON-NLS-1$
            connectRow.setButtonText(Messages.getString("GeneralPanel.5")); //$NON-NLS-1$
        } catch (RuntimeException exc) {
            addLogMessage(Messages.getString("GeneralPanel.6") + exc.getLocalizedMessage()); //$NON-NLS-1$
        }
    }

    private void disconnect() {
        try {
            getContextModuleAdapter().disconnect();
            addLogMessage(Messages.getString("GeneralPanel.7")); //$NON-NLS-1$
            connectRow.setButtonText(Messages.getString("GeneralPanel.8")); //$NON-NLS-1$

        } catch (Exception e) {
            addLogMessage(Messages.getString("GeneralPanel.9") + e.getLocalizedMessage()); //$NON-NLS-1$
        }
    }

    private void addLogMessage(String message) {
        logComponent.append(message + Messages.getString("GeneralPanel.10")); //$NON-NLS-1$
    }

    @Override
    protected void onCancelClick() {
        JOptionPane.showMessageDialog(null, Messages.getString("GeneralPanel.11")); //$NON-NLS-1$
    }

    @Override
    protected void onSaveClick() {
        JOptionPane.showMessageDialog(null, Messages.getString("GeneralPanel.12")); //$NON-NLS-1$
    }

    @Override
    protected void onRefreshClick() {
        List<String> ontologies = getContextModuleAdapter().getListOfOntologies();
        restoreOntologyRow.setItems(ontologies);
        String currentOntology = getContextModuleAdapter().getCurrentOntology();
        restoreOntologyRow.setSelectedItem(currentOntology);
    }
}
