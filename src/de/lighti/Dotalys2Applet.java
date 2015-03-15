package de.lighti;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class Dotalys2Applet extends Applet {

    /**
     * 
     */
    private static final long serialVersionUID = -4145372213031471202L;

    public static void displayException( Exception e ) {

        final StringWriter errors = new StringWriter();
        e.printStackTrace( new PrintWriter( errors ) );

        Logger.getLogger( Main.class.getName() ).severe( e.getLocalizedMessage() );
        e.printStackTrace( System.err );
        JOptionPane.showMessageDialog( null, errors.toString(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE );
    }

    public Dotalys2Applet() throws HeadlessException {
        super();
        setLayout( new BorderLayout() );
        final Dotalys2App app = new Dotalys2App();
        add( app.getJMenuBar(), BorderLayout.NORTH );
        final JComponent com = app.getMainView();
        com.setPreferredSize( app.getContentPane().getPreferredSize() );
        add( com, BorderLayout.CENTER );
    }
}
