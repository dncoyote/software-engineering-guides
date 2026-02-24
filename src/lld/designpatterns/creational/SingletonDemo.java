package lld.designpatterns.creational;


public class SingletonDemo{
    public static void main(String[] args){

        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();

        System.out.println("Are s1 and s2 references same - "+ (s1 == s2));

        System.out.println("s1 - "+s1.hashCode());
        System.out.println("s2 - "+s2.hashCode());
 
        Runnable task = () ->{
            Singleton s3 = Singleton.getInstance();
            System.out.println(Thread.currentThread().getName() +" - "+ s3.hashCode());
        };
    
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        Thread t3 = new Thread(task);

        t1.start();
        t2.start();
        t3.start();
    }
}
