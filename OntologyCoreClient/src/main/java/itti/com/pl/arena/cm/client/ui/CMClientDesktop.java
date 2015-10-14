package itti.com.pl.arena.cm.client.ui;

import itti.com.pl.arena.cm.client.ContextModuleAdapter;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;

/**
 * Window application serving as a ContextModule basic client
 * 
 * @author cm-admin
 * 
 */
public class CMClientDesktop extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        CMClientDesktop window = new CMClientDesktop();
        window.setVisible(true);
    }

    /**
     * Create the application.
     */
    public CMClientDesktop() {
        super();
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        setBounds(200, 200, 700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(Messages.getString("MainWindowApp.0")); //$NON-NLS-1$

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        ContextModuleAdapter cma = new ContextModuleAdapter(Messages.getString("CMClientDesktop.0") + System.currentTimeMillis()); //$NON-NLS-1$

        GeneralPanel generalPanel = new GeneralPanel();

        generalPanel.setContextModule(cma);
        tabbedPane.addTab(Messages.getString("MainWindowApp.1"), generalPanel); //$NON-NLS-1$
    }
}
