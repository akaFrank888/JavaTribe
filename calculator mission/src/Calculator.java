import java.util.Stack;
import java.util.Scanner;


public class Calculator {
    static Stack<Character> op = new Stack<>();
    public static Float getv(char op, Float f1, Float f2){
        if(op == '+') {
            return f2 + f1;
        } else if(op == '-') {
            return f2 - f1;
        } else if(op  == '*') {
            return f2 * f1;
        } else if(op == '/') {
            return f2 / f1;
        } else {
            return Float.valueOf(-0);
        }
    }
    /*
     param rp - reverse Polish expression

     return - result of the expression
     */
    public static float calrp(String rp){
        Stack<Float> v = new Stack<>();
        char[] arr = rp.toCharArray();
        int len = arr.length;
        for(int i = 0; i < len; i++){
            Character ch = arr[i];
            if(ch >= '0' && ch <= '9') {
                v.push(Float.valueOf(ch - '0'));
            }
            else {
                v.push(getv(ch, v.pop(), v.pop()));
            }
        }
        return v.pop();
    }
    /*
    param s - String in the form of infix

    return String in the form of postfix

     */
    public static String getrp(String s){
        char[] arr = s.toCharArray();
        int len = arr.length;
        String out = "";
        for(int i = 0; i < len; i++){
            char ch = arr[i];
            if(ch == ' ') {
                continue;
            }
            if(ch >= '0' && ch <= '9') {
                out+=ch;
                continue;
            }
            if(ch == '(') {
                op.push(ch);
            }
            if(ch == '+' || ch == '-'){
                while(!op.empty() && (op.peek() != '(')) {
                    out+=op.pop();
                }
                op.push(ch);
                continue;
            }
            if(ch == '*' || ch == '/'){
                while(!op.empty() && (op.peek() == '*' || op.peek() == '/')) {
                    out+=op.pop();
                }
                op.push(ch);
                continue;
            }
            if(ch == ')'){
                while(!op.empty() && op.peek() != '(') {
                    out += op.pop();
                }
                op.pop();
                continue;
            }
        }
        while(!op.empty()) {
            out += op.pop();
        }
        return out;
    }

    public static void main(String[] args){
        System.out.print("输入：");
        Scanner input = new Scanner(System.in);
        String exp = input.nextLine();
        System.out.print("输出：");
        System.out.println(calrp(getrp(exp)));
    }
}
