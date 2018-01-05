import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

    public static boolean running = true; // Main control variable that
    // determines if the scanner
    // searches for values
    public static boolean masterCard = false;

    static Thread guiThread = new Thread(){
        public void run(){
            while(true){
                Interface.guiUpdate();
            }
        }
    };

    public static void main( String[] args ) throws InterruptedException {

        // // Connect to database
        try {
            Class.forName( "com.mysql.jdbc.Driver" ).newInstance( );
        } catch ( Exception e ) {
            e.printStackTrace( );
            return;
        }

        // Connect to scanner and read
        String command = "python RFIDTest.py";
        String rfid = "";
        ArrayList<String> master = new ArrayList<String>( );

        // List of master card values
        master.add( "88002BDE26" );
        master.add( "88002AC92D" );

        //Initial connect to database
        Database.connect( );

        Interface.guiInit( );
        guiThread.start();

        try {
            Process p;
            BufferedReader output = null;

            while ( running ) {
                p = Runtime.getRuntime( ).exec( command );
                output = new BufferedReader( new InputStreamReader( ( p.getInputStream( ) ) ) );

                while ( rfid == null || rfid.length( ) != 10 ) {
                    rfid = output.readLine( );
                }

                System.out.println( rfid );
                for(int index = 0; index < master.size( ); index++){
                    if(master.get( index ).equals( rfid )){
                        masterCard = true;
                        break;
                    } else {
                        masterCard = false;
                    }
                }

                if ( masterCard ) {
                    // Do master card things
                    System.out.println( "Master" );
                } else {
                    System.out.println( "Card scanned" );
                    database( rfid );
                }

                rfid = "";
                if (output != null){
                    output.close( );
                }
                long t = System.currentTimeMillis( );

                while(true){
                    if(System.currentTimeMillis( ) - 5000 > t){
                        break;
                    }
                }
            }

        } catch ( IOException e ) {
            e.printStackTrace( );
        }
    }

    public static void database( String tag ) {
        if ( Database.getInOut( tag ) ) {
            Database.logOut( tag );
            Interface.print( String.format( "Goodbye, %s", Database.getName( tag ) ) );
        } else {
            Database.logIn( tag );
            Interface.print( String.format( "Welcome, %s", Database.getName( tag ) ) );
        }
//		try {
//			Thread.sleep(5000);
//		} catch ( InterruptedException e ) {
//			e.printStackTrace();
//		}
    }
}
