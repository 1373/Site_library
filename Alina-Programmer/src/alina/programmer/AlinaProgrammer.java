package alina.programmer;

public class AlinaProgrammer
{
    public static void main(String[] args) {
        //1
//        ex_1(4,3);
        //2
//        ex_2(543,0);
        //3
//        ex_3("Алина", 1);
        //4
//        ex_4();
        //5
//        ex_5();
        //6
//        ex_6();
        //7
//        ex_7(10);
        
    }
    
    //Возвращает факториал числа n
    private static long getFact(int n) {
        if(n>1)
            return n*getFact(n-1);
        else
            return 1;
    }
       
    //Возвращает разность квадратов      
    private static int getResult(int a, int b) {
        int c = a*a - b*b;
        return c;
    }
    
    //Выводит большее из чисел x и y
    private static void ex_1(int x, int y) {
        if(x>y)
        {
            System.out.println(x);
        }
        else
        {
            System.out.println(y);
        }
    }
    
    //Работа с циклами
    private static void ex_2(int x, int y) {
        for (int i = 0; i < 100000; i++)
        {
            System.out.println(x - i);
        }
        
        while(x > 0)
        {
            System.out.println(x);
            x--;
        }
        
        for (int i = 0; i < 543; i++) {
            if (x > y) System.out.println("x = " + x);
            else System.out.println("y = " + y);
            x--;
            y++;
        }
    }
    
    //Одиночное приветствие
    private static void ex_3(String name, int num) {
        String str = "Привет, " + name + " №" + num;
        System.out.println(str);
    }
    
    //Приветствие сотни обезьянок
    private static void ex_4() {
        String s= "Привет, Обезьянка ";
        for (int i = 1; i < 101; i++) System.out.println (s + i);
    }
    
    //Работа с массивами
    private static void ex_5() {
        String[] a = new String[10];
        int[] b = new int[10];
        
        a[0]="Alina";
        a[1]="Artem";
        
        System.out.println(a[0]);
        System.out.println(a[1]);
        
        b[0]=1;
        b[1]=2;
        
        System.out.println("b[0] = " + b[0]);
        System.out.println("b[1] = " + b[1]);
        
        System.out.println("a: " + a.length);
        System.out.println("b: " + b.length);
        
        for (int i = 0; i < b.length; i++) {
            b[i] = i + 1;
            a[i] = "Element_" + (i + 1);
            System.out.println(a[i]);
        }
    }
    
    //Вызов простой функции
    private static void ex_6() {
        int x1 = 10;
        int y1 = 20;
        int z1 = getResult(x1,y1);
        
        int x2 = 10;
        int y2 = 20;
        int z2 = getResult(y2,x2);
        
        int x3 = 10;
        int y3 = 20;
        int z3 = getResult(x3,y3);
        
        int x4 = 10;
        int y4 = 20;
        int z4 = getResult(x4,y4);
        
        System.out.println("z1 = " + z1);
        System.out.println("z2 = " + z2);
        System.out.println("z3 = " + z3);
        System.out.println("z4 = " + z4);
    }
    
    //Вывод факториалов от 1 до n
    private static void ex_7(int n) {
        for (int i = 0; i < n; i++) {
           System.out.println(i+1 + "! = " + getFact(i+1)); 
        }
    }
}