public class Main {

    public static class Message{
        private String msg;

        public void setMessage(String msg){ this.msg = msg; }
        public String getMessage(){ return msg; }
    }

    public static class Waiter implements Runnable{
        private Message msg;

        public Waiter(Message msg){ this.msg = msg; }

        public void run(){
            String name = Thread.currentThread().getName();
            synchronized (msg){
                try{
                    System.out.println(name + " waits for nitification " + System.currentTimeMillis());
                    msg.wait();
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                System.out.println(name + " notified " + System.currentTimeMillis());
                System.out.println(name + " received message " + msg.getMessage());
            }
        }
    }

    public static class Notifier implements Runnable{
        private Message msg;

        public Notifier(Message msg){
            this.msg = msg;
        }

        public void run(){
            String name = Thread.currentThread().getName();
            System.out.println(name + " started");
            try{
                Thread.sleep(1500);
                synchronized (msg){
                    msg.setMessage(name + " did his work");
                    msg.notifyAll();
                }
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Message msg = new Message();
        Waiter waiter1 = new Waiter(msg);
        new Thread(waiter1, "waiter1").start();

        Waiter waiter2 = new Waiter(msg);
        new Thread(waiter2, "waiter2").start();

        Notifier notifier = new Notifier(msg);
        new Thread(notifier, "notifier").start();
        System.out.println("all threads started");
    }
}
