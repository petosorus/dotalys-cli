package de.lighti.components.batch;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.lighti.components.ProgressDialog;
import de.lighti.components.map.MapComponent;
import de.lighti.io.ChartCreator;
import de.lighti.io.DataExporter;
import de.lighti.io.DataImporter;
import de.lighti.model.AppState;
import de.lighti.model.Statics;
import de.lighti.model.game.Player;

public class BatchDialog extends JDialog {
    private static String escapeUrlAsFilename( String url ) {

        final StringBuffer sb = new StringBuffer();

        // Apply the regex.
        final Matcher m = PATTERN.matcher( url );

        while (m.find()) {
            m.appendReplacement( sb,

                            // Convert matched character to percent-encoded.
                            "%" + Integer.toHexString( m.group().charAt( 0 ) ).toUpperCase() );
        }
        m.appendTail( sb );

        final String encoded = sb.toString();

        // Truncate the string.
        final int end = Math.min( encoded.length(), MAX_LENGTH );
        return encoded.substring( 0, end );
    }

    public static void main( String[] args ) {
        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        }
        catch (final Exception e) {
            // Don't care
        }
        final BatchDialog d = new BatchDialog( null );
        d.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosed( WindowEvent e ) {
                super.windowClosed( e );
                System.exit( 0 );
            }

        } );
        d.setVisible( true );
    }

    private JList<File> fileList;

    private CheckBoxList propertyList;
    private JButton okButton;

    private JTextField savePathField;

    /**
     *
     */
    private static final long serialVersionUID = 7655122816807766787L;

    private static final Pattern PATTERN = Pattern.compile( "[^A-Za-z0-9_]" );

    private static final int MAX_LENGTH = 127;

    public BatchDialog( JFrame parent ) {
        super( parent, Statics.BATCH_EXPORT );
        setPreferredSize( new Dimension( 800, 600 ) );
        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        setModal( true );
        final JSplitPane splitPane = new JSplitPane();
        splitPane.setLeftComponent( createFilePanel() );
        splitPane.setRightComponent( createPropertyOkButtonPanel() );
        setContentPane( splitPane );

        pack();
        splitPane.setDividerLocation( 0.5 );

    }

    private Component createFilePanel() {

        final JButton plusButton = new JButton( "+" );
        plusButton.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                final JFileChooser chooser = new JFileChooser( new File( "." ) );
                chooser.setMultiSelectionEnabled( true );
                chooser.setFileFilter( DataImporter.FILE_FILTER );
                final int returnVal = chooser.showOpenDialog( BatchDialog.this );
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    final DefaultListModel<File> model = (DefaultListModel<File>) getFileList().getModel();
                    for (final File f : chooser.getSelectedFiles()) {

                        model.addElement( f );
                    }
                    validateInput();
                }
            }
        } );
        final JButton minusButton = new JButton( "-" );
        minusButton.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                final DefaultListModel<File> model = (DefaultListModel<File>) getFileList().getModel();

                for (final File f : getFileList().getSelectedValuesList()) {
                    model.removeElement( f );
                }

            }
        } );

        final JPanel filePanel = new JPanel();

        filePanel.setLayout( new GridBagLayout() );
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.gridwidth = 2;
        c.insets = new Insets( 5, 5, 5, 5 );
        filePanel.add( getFileList(), c );

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.5;
        filePanel.add( plusButton, c );
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.5;
        filePanel.add( minusButton, c );

        return filePanel;
    }

    private JPanel createPropertyOkButtonPanel() {
        final JPanel propertyOkButtonPanel = new JPanel();
        propertyOkButtonPanel.setLayout( new GridBagLayout() );
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.gridwidth = 1;
        c.insets = new Insets( 5, 5, 5, 5 );
        propertyOkButtonPanel.add( getPropertyList(), c );

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0.0;
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.insets = new Insets( 10, 10, 10, 10 );
        propertyOkButtonPanel.add( createSavePathPanel(), c );

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 0.0;
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.insets = new Insets( 10, 10, 10, 10 );
        propertyOkButtonPanel.add( getOkButton(), c );
        return propertyOkButtonPanel;
    }

    private JPanel createSavePathPanel() {
        final JPanel savePathPanel = new JPanel();
        final JLabel label = new JLabel( Statics.SAVE_TO );
        final JTextField savePathField = getSavePathField();
        final JButton button = new JButton( Statics.BROWSE );

        button.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent evt ) {
                final JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
                final int retVal = chooser.showSaveDialog( BatchDialog.this );
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    savePathField.setText( chooser.getSelectedFile().getAbsolutePath() );
                }
            }
        } );
        savePathPanel.add( label );
        savePathPanel.add( savePathField );
        savePathPanel.add( button );
        return savePathPanel;
    }

    private void export() {
        final File dir = new File( getSavePathField().getText() );
        if (!dir.exists() || !dir.isDirectory()) {
            handleError( dir.getAbsolutePath() + " is not a writable directory" );
            return;
        }

        final Enumeration<File> fileList = ((DefaultListModel<File>) getFileList().getModel()).elements();
        final List<CheckBoxListEntry> properties = getPropertyList().getSelectedValuesList();

        final ProgressDialog pd = new ProgressDialog( BatchDialog.this );
        pd.setMaximum( 10 * properties.size() * ((DefaultListModel<File>) getFileList().getModel()).getSize() );

        final Thread t = new Thread( new Runnable() {

            private void exportData( String header, String[][] data, String fileOut ) {
                final File file = new File( fileOut );

                try {
                    DataExporter.exportCSV( file, header, data );
                }
                catch (final IOException e) {
                    handleError( e.getLocalizedMessage() );
                }

            }

            @Override
            public void run() {
                int progress = 0;

                while (fileList.hasMoreElements()) {
                    final File f = fileList.nextElement();
                    final AppState state = new AppState();
                    DataImporter.parseReplayFile( state, null, f );
                    for (final CheckBoxListEntry entry : properties) {
                        for (final Player p : state.getPlayers()) {
                            final String fileOut = dir.getAbsolutePath() + "/" + f.getName().replace( ".dem", "" ) + "_" + entry.getValue() + "_"
                                            + escapeUrlAsFilename( p.getName() ) + ".csv";
                            String header;
                            String[][] data;
                            switch (entry.getValue()) {
                                case MapComponent.CAT_MOVEMENT:
                                    header = "#tickms, x , y";
                                    data = ChartCreator.createMoveLog( p.getName(), state );
                                    break;
                                case MapComponent.CAT_ZONES:
                                    header = "#tickms, zone";
                                    data = ChartCreator.createZoneLog( p.getName(), state );
                                    break;
                                case MapComponent.CAT_ABILITIES:
                                    header = "#tickms, x, y, ability";
                                    data = ChartCreator.createAbilityLog( p );
                                case MapComponent.CAT_ITEMS:
                                    header = "#tickms, x, y, item";
                                    data = ChartCreator.createItemLog( p );
                                    break;
                                default:
                                    throw new RuntimeException( "Unknown property " + entry.getValue() );

                            }
                            exportData( header, data, fileOut );
                        }
                        progress++;
                        pd.setValue( progress );

                    }
                }
                pd.setVisible( false );
            }
        } );
        t.start();
        pd.setVisible( true );
    }

    private JList<File> getFileList() {
        if (fileList == null) {
            fileList = new JList<File>( new DefaultListModel<File>() );
            fileList.setBorder( BorderFactory.createLoweredBevelBorder() );
        }

        return fileList;
    }

    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton( Statics.OK );
            okButton.setEnabled( false );
            okButton.addActionListener( new ActionListener() {
                @Override
                public void actionPerformed( ActionEvent e ) {
                    export();
                }
            } );
        }

        return okButton;
    }

    private CheckBoxList getPropertyList() {
        if (propertyList == null) {
            propertyList = new CheckBoxList();
            propertyList.setBorder( BorderFactory.createLoweredBevelBorder() );
            ((DefaultListModel<CheckBoxListEntry>) propertyList.getModel()).addElement( new CheckBoxListEntry( MapComponent.CAT_MOVEMENT, false ) );
            ((DefaultListModel<CheckBoxListEntry>) propertyList.getModel()).addElement( new CheckBoxListEntry( MapComponent.CAT_ZONES, false ) );
            ((DefaultListModel<CheckBoxListEntry>) propertyList.getModel()).addElement( new CheckBoxListEntry( MapComponent.CAT_ABILITIES, false ) );
            ((DefaultListModel<CheckBoxListEntry>) propertyList.getModel()).addElement( new CheckBoxListEntry( MapComponent.CAT_ITEMS, false ) );
            propertyList.addPropertyChangeListener( new PropertyChangeListener() {
                @Override
                public void propertyChange( PropertyChangeEvent arg0 ) {
                    validateInput();
                }
            } );
        }
        return propertyList;
    }

    private JTextField getSavePathField() {
        if (savePathField == null) {
            savePathField = new JTextField( new File( "." ).getAbsolutePath(), 20 );
            savePathField.getDocument().addDocumentListener( new DocumentListener() {

                @Override
                public void changedUpdate( DocumentEvent e ) {
                    validateInput();
                }

                @Override
                public void insertUpdate( DocumentEvent e ) {
                }

                @Override
                public void removeUpdate( DocumentEvent e ) {
                }
            } );
        }
        return savePathField;
    }

    private void handleError( String error ) {
        JOptionPane.showMessageDialog( BatchDialog.this, error, "An error has occured", JOptionPane.ERROR_MESSAGE );
    }

    private void validateInput() {
        boolean ret = true;
        ret &= getFileList().getModel().getSize() > 0;
        ret &= !getPropertyList().getSelectedValuesList().isEmpty();
        ret &= !getSavePathField().getText().isEmpty();
        getOkButton().setEnabled( ret );
    }
}
