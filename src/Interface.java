import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
//import javax.swing.border.EmptyBorder;

public class Interface {
    static JFrame frame = new JFrame( "ThunderChickens Hours Logger" );
    static Container mainPane = frame.getContentPane( );
    static JPanel panel = new JPanel( );

    static ArrayList<JButton> buttons = new ArrayList<JButton>( );
    static ArrayList<JLabel> labels = new ArrayList<JLabel>( );

    public static void guiInit( ) {
        frame.setExtendedState( JFrame.MAXIMIZED_BOTH );
        frame.setUndecorated( true );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        panel.setBackground( Color.BLUE );

        GridLayout layout = new GridLayout( 6, 9 );
        layout.setHgap( 25 );
        layout.setVgap( 40 );
        panel.setLayout( layout );
        for ( int index = 0; index < Database.studentCount( ); index++ ) {
            JButton button = new JButton( );
            buttons.add( button );
            button.setEnabled( false );

            Student stu = Database.getStudent( index );
            JLabel name = new JLabel( stu.getName( ) );
            int status = stu.getStatus( );
            JLabel timeT = new JLabel( stu.getTimeT( ) );
            JLabel timeW = new JLabel( stu.getTimeW( ) );

            labels.add( name );
            labels.add( timeT );
            labels.add( timeW );

            if ( status == 1 ) {
                button.setBackground( Color.GREEN );
            } else {
                button.setBackground( Color.RED );
            }

            name.setHorizontalAlignment( JLabel.CENTER );
            timeT.setHorizontalAlignment( JLabel.CENTER );
            timeW.setHorizontalAlignment( JLabel.CENTER );

            button.setLayout( new GridLayout( 3, 0 ) );
            button.add( name );
            button.add( timeT );
            button.add( timeW );

            panel.add( buttons.get( index ) );
        }
        mainPane.add( panel );

        frame.setVisible( true );
    }

    public static void guiUpdate( ) {
        for ( int index = 0; index < Database.studentCount( ) * 3; index += 3 ) {

            Student stu = Database.getStudent( index / 3 );

            labels.get( index + 1 ).setText( stu.getTimeT( ) );
            labels.get( index + 2 ).setText( stu.getTimeW( ) );

            if ( Database.getStudent( index / 3).getStatus( ) == 1 ) {
                buttons.get( index / 3).setBackground( Color.GREEN );
            } else {
                buttons.get( index / 3).setBackground( Color.RED );
            }

        }
        frame.repaint( );
    }

    public static void print( String str ) {
        System.out.println( str );
    }
}
