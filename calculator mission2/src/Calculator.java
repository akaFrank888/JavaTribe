import java.util.Scanner;
public class Calculator{

    //设计四个方法，进行加减乘除运算

    public float add(float a,float b){
        return a+b;
    }
    public float minus(float a,float b){
        return a-b;
    }
    public float multiply(float a,float b){
        return a*b;
    }
    public float divide(float a,float b){
        return a/b;
    }

    //设计一个方法，进行控制台操作

    public void calculate() {
        Scanner input = new Scanner(System.in);
        System.out.print("请输入第一个数字:");
        String one = input.nextLine();
        //String-->Float的类型转换
        float a = Float.parseFloat(one);

        //需要一个死循环以持续计算，输入 = 结束计算
        while (true) {
            System.out.print("请输入符号:");
            String symbol = input.nextLine();
            if(symbol.equals("=")){
                System.out.println("计算结束");
                break;
            }
            System.out.print("请输入第二个数字:");
            String two = input.nextLine();
            //为了避免nextInt会保留回车而导致出错所以均换成String然后再转换
            float b = Float.parseFloat(two);
            switch (symbol) {
                case "+":
                    a = this.add(a, b);
                    break;
                case "-":
                    a = this.minus(a, b);
                    break;
                case "*":
                    a = this.multiply(a, b);
                    break;
                case "/":
                    a = this.divide(a, b);
                    break;
                default:;
                    //注意为什么是a =
            }
            System.out.println(a);
        }
    }

    //主方法

    public static void main(String[] args){
        Calculator c = new Calculator();
        c.calculate();
    }
}
