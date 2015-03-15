package de.lighti.components;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import de.lighti.DotaPlay.ProgressListener;
import de.lighti.Dotalys2App;
import de.lighti.components.batch.BatchDialog;
import de.lighti.io.DataImporter;
import de.lighti.model.AppState;

public class DotalysMenuBar extends JMenuBar {
    /**
     * 
     */
    private static final long serialVersionUID = -152856153942387447L;

    private final Dotalys2App owner;

    private JMenu fileMenu;
    private JMenuItem fileOpenMenuItem;

    private JMenuItem batchExportMenuItem;

    private JMenuItem aboutMenuItem;

    public DotalysMenuBar( Dotalys2App o ) {
        super();
        setLayout( new FlowLayout( FlowLayout.LEFT ) );
        owner = o;

        add( getFileMenu() );
        add( getBatchExportItem() );
        add( getAboutMenuItem() );
    }

    private JMenuItem getAboutMenuItem() {
        if (aboutMenuItem == null) {
            aboutMenuItem = new JMenuItem();

            aboutMenuItem.setAction( new AbstractAction() {

                /**
                 * 
                 */
                private static final long serialVersionUID = -7625044491823967L;

                @Override
                public void actionPerformed( ActionEvent e ) {
                    final String message = "<html>Dotalys2 is a replay parser and analysis tool for Valve's popular Dota2 MOBA game.<br/>It has been written by me, Tobias Mahlmann, in 2013-2014.<br/>I am in no way affiliated with Valve. The software is provided \"as is\" as a research project and I assume no liability.<br/>If you'd like to contact me, mail me at <a href='mailto:t.mahlmann@gmail.com'>t.mahlmann@gmail.com</a> or surf to <a href='http://www.lighti.de'>www.lighti.de</a><br/>This software use the following open source libraries:<ul><li>JFreeChart <a href='http://www.jfree.org'>http://www.jfree.org</a></li><li>Google's Protocol Buffer <a href='https://code.google.com/p/protobuf/'>https://code.google.com/p/protobuf/</a></li><li>Snappy for Java <a href='https://code.google.com/p/snappy-java/'>https://code.google.com/p/snappy-java/</a></li></ul>Acknowledgements go out to github user dschleck for his white-paper on parsing Dota2 packet entities <a href='https://github.com/dschleck/edith'>https://github.com/dschleck/edith</a></html>";
                    JOptionPane.showMessageDialog( owner, message, "About", JOptionPane.INFORMATION_MESSAGE );

                }
            } );
            aboutMenuItem.setText( "About" );
        }
        return aboutMenuItem;
    }

    private JMenuItem getBatchExportItem() {
        if (batchExportMenuItem == null) {
            batchExportMenuItem = new JMenuItem( "Batch Export" );
            batchExportMenuItem.addActionListener( new ActionListener() {

                @Override
                public void actionPerformed( ActionEvent arg0 ) {
                    new BatchDialog( owner ).setVisible( true );

                }
            } );
        }

        return batchExportMenuItem;
    }

    public JMenu getFileMenu() {
        if (fileMenu == null) {
            fileMenu = new JMenu( "File" );
            fileMenu.add( getFileOpenMenuItem() );
        }
        return fileMenu;
    }

    public JMenuItem getFileOpenMenuItem() {
        if (fileOpenMenuItem == null) {
            fileOpenMenuItem = new JMenuItem();
            fileOpenMenuItem.setAction( new AbstractAction() {

                /**
                 * 
                 */
                private static final long serialVersionUID = 437644308981178302L;

                @Override
                public void actionPerformed( ActionEvent e ) {

                    // Create a file chooser
                    final JFileChooser fc = new JFileChooser( "." );
                    fc.setFileFilter( DataImporter.FILE_FILTER );

                    // In response to a button click:
                    final int returnVal = fc.showOpenDialog( owner );
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        final AppState appState = owner.getAppState();
                        appState.clear();
                        final ProgressDialog pd = new ProgressDialog( owner );
                        final long fs = fc.getSelectedFile().length();
                        pd.setMaximum( fs );
                        final Thread t = new Thread( new Runnable() {

                            @Override
                            public void run() {

                                DataImporter.parseReplayFile( appState, owner, fc.getSelectedFile(), new ProgressListener() {

                                    @Override
                                    public void bytesRemaining( int position ) {
                                        pd.setValue( fs - position );

                                    }
                                } );
                                pd.setVisible( false );
                            }
                        } );
                        t.start();
                        pd.setVisible( true );

                    }
                }
            } );
            fileOpenMenuItem.setText( "Open" );
        }

        return fileOpenMenuItem;
    }
}
