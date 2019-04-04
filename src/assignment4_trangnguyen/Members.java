
package assignment4_trangnguyen;

enum Jobs {
    CAPTAIN,
    MECHANIC,
    CHEF,
    OPERATOR;
}
public class Members {
    private String name;
    private Jobs job;
    private String note;
    private static int counter =0;
    public Members(String n, Jobs j,String note ) throws Exception{
        name = n;        
        job = j;
        this.note= note;  
        counter ++;
    }

    public String getName() {
        return name;
    }

    public Jobs getJob() {
        return job;
    }

    public static int getCounter() {
        return counter;
    }

    public String getNote() {
        return note;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJob(Jobs job) {
        this.job = job;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        if (note != null){
        return "Members{"  + "name=" + name + ", job=" + job + ", note=" + note + '}';
        }
        else 
         return "Members{"  + "name=" + name + ", job=" + job  + '}';   
    }
    
    
    
}

