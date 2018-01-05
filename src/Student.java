
public class Student {
    private String name;
    private int status;
    private String timeT;
    private String timeW;

    public Student( String nm, int stat, String t, String w ) {
        name = nm;
        status = stat;
        timeT = t;
        timeW = w;
    }

    public String getName( ) {
        return name;
    }

    public int getStatus( ) {
        return status;
    }

    public String getTimeT( ) {
        return timeT;
    }

    public String getTimeW(){
        return timeW;
    }
}
