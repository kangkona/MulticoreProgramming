import java.util.ArrayList;

public class RWSync<T> {
    private ArrayList<T> buffer;
    private final int COUNT = 10;
    public RWSync() {
    	buffer =  new ArrayList<T>();
    }
    public void work() {
        new Reader("r1").start();
        new Writer("w1").start();
        new Writer("w2").start();
        new Reader("r2").start();
    }
    
    

    class Writer extends Thread {
    	String wid;
    	Writer(String wid) {
    		this.wid = wid;
    	}
        public void run() {
            while (true) {
                synchronized (buffer) {
                    try {
                        while (buffer.size() == COUNT) {
                            System.out.println("Buffer is full! " + wid +" can't write.");
                            buffer.wait();
                        }
                        T  item = (T)new Object(); 
                        if ( buffer.add(item) ) {
                            System.out.println(wid + " write, buffer size:" + buffer.size());
                            buffer.notify();
                            
                        }
                    } catch (InterruptedException ie) {
                       System.out.println(wid + " is interrupted!");
                    }
                }
            }
        }
    }

    class Reader extends Thread {
        String rid ;
        Reader(String rid){
        	this.rid = rid;
        }
        public void run() {
            while (true) {
                synchronized (buffer) {
                    try {
                        while (buffer.size() == 0) {
                            System.out.println("Buffer is empty , " + rid +"  can't read now.");
                            buffer.wait();
                        }
                        buffer.remove(0);
                        System.out.println(rid + " read, buffer size:" + buffer.size());
                        Thread.sleep(100);
                        buffer.notify();
                    } catch (InterruptedException ie) {
                        System.out.println(rid + " is interrupted");
                    }
                }
            }
        }
    }
    public static void main(String[] args) throws Exception {
        RWSync pc = new RWSync();
        pc.work();
    }
}