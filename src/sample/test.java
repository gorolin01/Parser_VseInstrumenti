package sample;

public class test {

    public static void main(String [] args){
        System.out.println(persistence(39));
    }

    public static int persistence(long n) {
        long res = 1;
        int iter = 0;
        String [] numbers;

        numbers = String.valueOf(n).split("");
        for(int i = 0; i < numbers.length; i++){
            res = res * Integer.parseInt(numbers[i]);
        }
        if(numbers.length != 1){
            iter = 1;
            iter += persistence(res);
        }

        return iter;
    }

    public static int squareDigits(int n) {
        String res = "";
        String number = String.valueOf(n);
        String [] numbers;

        numbers = number.split("");
        for(int i = 0; i < numbers.length; i++){
            res = res + Integer.parseInt(numbers[i])*Integer.parseInt(numbers[i]);
        }
        return Integer.parseInt(res);
    }

}