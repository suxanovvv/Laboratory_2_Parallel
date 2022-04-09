public class Main {


    //1-ий клас, що спадковує Thread
    static class NewThread extends Thread{

        int number;
        String name;

        NewThread(int number, String name){
            this.number = number;
            this.name = name;
        }

        public void run(){
            System.out.println("Number: " + this.number+ "; Name: " +this.name);
        }
    }

    //2-ий клас, що реалізовує інтерфейс Runnable
    static class NewRunnable implements Runnable {
        int number;
        String name;

        NewRunnable(int number, String name){
            this.number = number;
            this.name = name;
        }

        //Метод run(), що виводить екземляр класу та його назву.
        public void run(){
            System.out.println("Number: " + this.number + "; Name: " +this.name);
        }
    }


    public static void main(String[] args) {
        NewThread[] ThisMassive = new NewThread[5];
        NewRunnable[] NewMassive = new NewRunnable[5];
        Thread[] tread = new Thread[5];

        for(int i=0; i<5; i++){
            ThisMassive[i] = new NewThread(i, "ThisMassive");
            NewMassive[i] = new NewRunnable(i, "NewMassive");
            tread[i] = new Thread(NewMassive[i]);
        }

        for(int i=0; i<5; i++){
            ThisMassive[i].start();
            tread[i].start();
        }
    }
}