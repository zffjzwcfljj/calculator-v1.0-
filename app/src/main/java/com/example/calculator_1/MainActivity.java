package com.example.calculator_1;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import static java.lang.System.exit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

//    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
//    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

    private TextView resultView;
    private EditText input;
    Button n0,n1,n2,n3,n4,n5,n6,n7,n8,n9,add,sub,mul,div,dot,ac,back,equal,left,right;
    OptrStack optrStack = new OptrStack(); //操作符栈
    OpndStack opndStack = new OpndStack(); //操作数栈

    public char[] ch={'+' , '-' , '*' , '/' ,'(' , ')' , '='}; //把符号转换成字符数组
    public int[] ss1={3,3,5,5,1,6,0};                           //栈内元素优先级
    public int[] ss2={2,2,4,4,6,1,0};                           //栈外元素优先级
    public String str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input=(EditText)findViewById(R.id.input);
        n0=(Button)findViewById(R.id.Num_0);
        n1=(Button)findViewById(R.id.Num_1);
        n2=(Button)findViewById(R.id.Num_2);
        n3=(Button)findViewById(R.id.Num_3);
        n4=(Button)findViewById(R.id.Num_4);
        n5=(Button)findViewById(R.id.Num_5);
        n6=(Button)findViewById(R.id.Num_6);
        n7=(Button)findViewById(R.id.Num_7);
        n8=(Button)findViewById(R.id.Num_8);
        n9=(Button)findViewById(R.id.Num_9);
        add=(Button)findViewById(R.id.Num_add);
        sub=(Button)findViewById(R.id.Num_sub);
        mul=(Button)findViewById(R.id.Num_mul);
        div=(Button)findViewById(R.id.divide);
        dot=(Button)findViewById(R.id.Num_dot);
        ac=(Button)findViewById(R.id.ac);
        back=(Button)findViewById(R.id.Num_back);
        left=(Button)findViewById(R.id.left);
        right=(Button)findViewById(R.id.right);
        equal=(Button)findViewById(R.id.Num_equal);

        n0.setOnClickListener(this);
        n1.setOnClickListener(this);
        n2.setOnClickListener(this);
        n3.setOnClickListener(this);
        n4.setOnClickListener(this);
        n5.setOnClickListener(this);
        n6.setOnClickListener(this);
        n7.setOnClickListener(this);
        n8.setOnClickListener(this);
        n9.setOnClickListener(this);
        add.setOnClickListener(this);
        sub.setOnClickListener(this);
        mul.setOnClickListener(this);
        div.setOnClickListener(this);
        dot.setOnClickListener(this);
        equal.setOnClickListener(this);
        right.setOnClickListener(this);
        left.setOnClickListener(this);
        back.setOnClickListener(this);
        ac.setOnClickListener(this);

        this.resultView = (TextView) this.findViewById(R.id.resultView);


    }
    @Override
    public void onClick(View v){
        str = input.getText().toString();
        switch(v.getId()){
            case R.id.Num_add:
            case R.id.Num_sub:
            case R.id.Num_mul:
            case R.id.divide:
            case R.id.left:
            case R.id.right:
            case R.id.Num_dot:
            case R.id.Num_0:
            case R.id.Num_1:
            case R.id.Num_2:
            case R.id.Num_3:
            case R.id.Num_4:
            case R.id.Num_5:
            case R.id.Num_6:
            case R.id.Num_7:
            case R.id.Num_8:
            case R.id.Num_9:
                str = str+((Button)v).getText();
                input.setText(str);
                break;
            case R.id.ac:
                str="";
                input.setText(str);
                resultView.setText(str);
                break;
            case R.id.Num_back:
                str=str.substring(0,str.length()-1);
                input.setText(str);
                break;
            case R.id.Num_equal:
                str = str+((Button)v).getText();
                input.setText(str);
                String result = String.valueOf(evaluate());
                resultView.setText(result);
                break;

        }
    }

    int converse(char c)                                //把字符数字化
    {
        switch(c){
            case '+':  return 0;
            case '-':  return 1;
            case '*':  return 2;
            case '/':  return 3;
            case '(':  return 4;
            case ')':  return 5;
            default:   return 6;
        }
    }


    char comp(char r1,char r2) {                      //字符优先级比较
        int i1=converse(r1);                          //把字符1变成数字
        int i2=converse(r2);                          //把字符2变成数字
        if(ss1[i1]>ss2[i2]) return '>';                 //通过原来的设定找到优先级
        else if(ss1[i1]<ss2[i2]) return '<';
        else return '=';
    }

    double operate(double a,int t,double b){         //加减乘除的运算操作
        double sum=0;
        switch(t){
            case 0: sum=a+b; break;
            case 1: sum=a-b; break;
            case 2: sum=a*b; break;
            case 3: if(b!=0){sum=a/b;break;}else exit(1);
        }
        return sum;
    }


    double evaluate(){                      //表达式函数
        char c;
        int i=0;
        double sum,q;
        boolean k=true,flag=false;                       //flag标志区分是否带小数点
        double a,b;
        char t;
        optrStack.push('=');            //将‘=’压入栈
        c = str.charAt(i);
        i++;
        while (c != '=' || !(optrStack.peek() == '=')) {    //没有输入结束符'='或者栈顶不是结束符'='则一直循环
            if (isdigit(c) || c == '.') {            //isdigit(c)判断c是否为数字
                sum = 0;                           //sum是储存一个完整的数
                q = 10;                            //用来记录小数点后几位
                while (isdigit(c) || c == '.') {
                    if (isdigit(c) && flag == false)     //是数字且暂无出现小数点
                        sum = sum * 10 + (c - '0');
                    else if (c == '.')
                        flag = true;                  //记录数出现小数点
                    else if (isdigit(c) && flag == true) {      //记录出现小数点后一个完整的数sum
                        sum = sum + (c - '0') / q;
                        q = q * 10;
                    }
                    c = str.charAt(i);
                    i++;
                }
                flag = false;
                opndStack.push(sum);
            }

            else if (k) {
                // comp('=', '+')
                switch (comp(optrStack.peek(), c)) {
                    case '<':
                        optrStack.push(c);  //把字符整型化，然后压入操作符栈
                        c = str.charAt(i);
                        i++;
                        break;
                    case '=':
                        optrStack.pop();                      //操作符栈顶元素出栈
                        c = str.charAt(i);
                        i++;
                        break;
                    case '>':
                        t = optrStack.peek();
                        optrStack.pop();        //操作符栈顶元素出栈
                        b = opndStack.peek();
                        opndStack.pop();              //操作数栈顶元素出栈
                        a = opndStack.peek();
                        opndStack.pop();              //操作数栈顶元素出栈
                        opndStack.push(operate(a, converse(t), b)); //两个运算数及一个运算符的运算结果入栈
                        break;
                }
            }
        }

        return (opndStack.peek());

    }

    private boolean isdigit(char c) {
        return c >='0' && c <= '9';
    }

}


class OptrStack {
    private char[] value=new char[100];
    private int top;

    OptrStack(){
        top=-1;
    }

    public char peek() {
        return value[top];
    }

    public void push(char c){
        value[++top]=c;
    }
    public void pop(){
        top--;
    }

}

class OpndStack {
    private double[] value=new double[100];
    private int top;

    OpndStack(){
        top=-1;
    }

    public double peek() {
        return value[top];
    }

    public void push(double c){
        value[++top]=c;
    }
    public void pop(){
        top--;
    }

}
