import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

    /**
     * Establishes a connection to the mySQL database
     *
     * @return A Connection object storing the connection to the database
     */
    public static Connection connect( ) {
        Connection connection = null;
        try {
            connection = DriverManager
                    .getConnection( "jdbc:mysql://localhost:3306/HOURS","root", "chickens" );
        } catch ( SQLException ex ) {
            ex.printStackTrace();
            System.out.println( "SQLException: " + ex.getMessage( ) );
            System.out.println( "SQLState: " + ex.getSQLState( ) );
            System.out.println( "VendorError: " + ex.getErrorCode( ) );
        }
        return connection;
    }

    /**
     * Iterates through the database to determine the number of students in the
     * database
     *
     * @return Number of students in the database
     */
    public static int studentCount( ) {
        int count = 0;
        Statement state = null;
        ResultSet rs = null;

        try ( Connection con = connect( ) ) {
            state = con.createStatement( );
            rs = state.executeQuery( "SELECT * FROM hours" );

            while ( rs.next( ) ) {
                count++;
            }

        } catch ( SQLException ex ) {
            System.out.println( "SQLException: " + ex.getMessage( ) );
            System.out.println( "SQLState: " + ex.getSQLState( ) );
            System.out.println( "VendorError: " + ex.getErrorCode( ) );
        } finally {
            if ( rs != null ) {
                try {
                    rs.close( );
                } catch ( SQLException e ) {
                }
            }
            if ( state != null ) {
                try {
                    state.close( );
                } catch ( SQLException e ) {
                }
            }
        }
        return count;

    }

    public static String getName( String tag ) {

        Statement state = null;
        ResultSet rs = null;
        String name = "";

        try ( Connection con = connect( ) ) {
            state = con.createStatement( );
            rs = state.executeQuery( String.format( "SELECT first,last FROM hours WHERE tagId = '%s'", tag ) );

            rs.next( );
            name = rs.getString( 1 ) + " " + rs.getString( 2 );

        } catch ( SQLException ex ) {
            System.out.println( "SQLException: " + ex.getMessage( ) );
            System.out.println( "SQLState: " + ex.getSQLState( ) );
            System.out.println( "VendorError: " + ex.getErrorCode( ) );
        } finally {
            // Closes ResultSet
            if ( rs != null ) {
                try {
                    rs.close( );
                } catch ( SQLException e ) {
                }
            }
            // Closes Statement
            if ( state != null ) {
                try {
                    state.close( );
                } catch ( SQLException e ) {
                }
            }
        }
        return name;
    }

    public static boolean getInOut( String tag ) {
        Statement state = null;
        ResultSet rs = null;
        int inOut = 0;

        try ( Connection con = connect( ) ) {
            state = con.createStatement( );
            rs = state.executeQuery( String.format( "SELECT status FROM hours WHERE tagId = '%s'", tag ) );

            rs.next( );
            inOut = rs.getInt( 1 );

        } catch ( SQLException ex ) {
            System.out.println( "SQLException: " + ex.getMessage( ) );
            System.out.println( "SQLState: " + ex.getSQLState( ) );
            System.out.println( "VendorError: " + ex.getErrorCode( ) );
        } finally {
            // Closes ResultSet
            if ( rs != null ) {
                try {
                    rs.close( );
                } catch ( SQLException e ) {
                }
            }
            // Closes Statement
            if ( state != null ) {
                try {
                    state.close( );
                } catch ( SQLException e ) {
                }
            }
        }

        if ( inOut == 1 ) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean logIn( String tag ) {
        Statement state = null;
        boolean success = false;

        try ( Connection con = connect( ) ) {
            state = con.createStatement( );
            state.executeUpdate( String.format( "UPDATE hours SET status = 1 WHERE tagId = '%s'", tag ) );
            state.executeUpdate( String.format( "UPDATE hours SET timeIn = NOW() WHERE tagId = '%s'", tag ) );

            // log( getName( tag ), 1 );
            success = true;

        } catch ( SQLException ex ) {
            System.out.println( "SQLException: " + ex.getMessage( ) );
            System.out.println( "SQLState: " + ex.getSQLState( ) );
            System.out.println( "VendorError: " + ex.getErrorCode( ) );

        } finally {
            // Closes Statement
            if ( state != null ) {
                try {
                    state.close( );
                } catch ( SQLException e ) {
                }
            }
        }
        return success;
    }

    public static boolean logOut( String tag ) {
        Statement state = null;
        boolean success = false;

        try ( Connection con = connect( ) ) {
            state = con.createStatement( );
            state.executeUpdate( String.format( "UPDATE hours SET status = 0 WHERE tagId = '%s'", tag ) );
            state.executeUpdate( String.format( "UPDATE hours SET timeOut = NOW() WHERE tagId = '%s'", tag ) );
            state.executeUpdate( String.format(
                    "UPDATE hours SET hoursToday = ADDTIME(hoursToday, TIMEDIFF(timeOut, timeIn)) WHERE tagId = '%s'",
                    tag ) );
            state.executeUpdate( String.format(
                    "UPDATE hours SET hoursThisWeek = ADDTIME(hoursThisWeek, TIMEDIFF(timeOut, timeIn)) WHERE tagId = '%s'",
                    tag ) );

            // log( getName( tag ), 0 );
            success = true;

        } catch ( SQLException ex ) {
            System.out.println( "SQLException: " + ex.getMessage( ) );
            System.out.println( "SQLState: " + ex.getSQLState( ) );
            System.out.println( "VendorError: " + ex.getErrorCode( ) );

        } finally {
            // Closes Statement
            if ( state != null ) {
                try {
                    state.close( );
                } catch ( SQLException e ) {
                }
            }
        }
        return success;
    }

    public static boolean logAllOut( ) {
        return true;
    }

    public static boolean endWeek( ) {
        return true;
    }

    public static void log( String name, int inOut ) {
        try ( FileWriter logger = new FileWriter( "hoursLog.txt", true );
              BufferedWriter writer = new BufferedWriter( logger );
              PrintWriter out = new PrintWriter( writer ); ) {

            if ( inOut == 1 ) {
                out.printf( "LOG IN \t %s \t at %s", name, new Date( ).toString( ) );
            } else {
                out.printf( "LOG OUT \t %s \t at %s", name, new Date( ).toString( ) );
            }
        } catch ( IOException e ) {
            Interface.print( "Log error" );
        }
    }

    public static Student getStudent( int index ) {
        Statement state = null;
        ResultSet rs = null;
        Student stu = null;

        try ( Connection con = connect( ) ) {
            state = con.createStatement( );
            rs = state.executeQuery( "SELECT * FROM hours" );

            for(int i = 0; i <= index; i++){
                rs.next( );
            }

            String name = rs.getString( 2 ) + " " + rs.getString( 3 );
            int status = rs.getInt( 4 );
            String timeT = rs.getTime( 7 ).toString( );
            String timeW = rs.getTime( 8 ).toString( );

            stu = new Student(name, status, timeT, timeW);

        } catch ( SQLException ex ) {
            System.out.println( "SQLException: " + ex.getMessage( ) );
            System.out.println( "SQLState: " + ex.getSQLState( ) );
            System.out.println( "VendorError: " + ex.getErrorCode( ) );
        } finally {
            // Closes ResultSet
            if ( rs != null ) {
                try {
                    rs.close( );
                } catch ( SQLException e ) {
                }
            }
            // Closes Statement
            if ( state != null ) {
                try {
                    state.close( );
                } catch ( SQLException e ) {
                }
            }
        }

        return stu;

    }
}
