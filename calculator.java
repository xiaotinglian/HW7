import java.util.*;

class Calculator {
  public static void main(String[] args) throws Throwable{
    String expr;

    try {
      if (args.length == 0)
        throw new UserIsADumbassException();
      expr = toPostfix(args);
      double res = evaluateExpression(expr);
      System.out.printf("Result: %.3f\n", res);
    } catch(ArithmeticException ae) {
      System.out.println("Cannot divide by zero");
    } catch(AlgebraFailException afe) {
      System.out.println("not a number");
    } catch(QuitMashingOnYourKeyboardException qe) {
      System.out.println("Please enter valid operator");
    } catch(UserIsADumbassException ue) {
      System.out.println("No arguments entered");
    }
  }

  public static String toPostfix(String[] args) {
    StringBuilder output = new StringBuilder();
    Stack<Character> operator = new Stack<Character>();

    int count = 0; 

    for (String s : args) {
      char op = s.charAt(0);

      if (op == '(') {
        operator.push(op);
      } else if (op == ')') { 
        while (operator.peek() != '(') {
          output.append(operator.pop());
          output.append(' ');
        }
        // pop out the opening (
        operator.pop();
      } else if (count % 2 == 0) { 

        if (isNumeric(s)) {
          output.append(s);
          output.append(' ');
          count++;
        } else {
          throw new AlgebraFailException();
        }
      } else { 
         if (isOperator(op)) { 
          while (!operator.isEmpty()) {
            char topOp = operator.peek();

            if (getPrecedence(topOp) > getPrecedence(op)
              || (getPrecedence(topOp) == getPrecedence(op) && op != '^')) {
              output.append(operator.pop());
              output.append(' ');
            } else
              break;
          }
          operator.push(op);
          count++;
        } else {
          throw new QuitMashingOnYourKeyboardException();
        }
      }
    }
    while (!operator.isEmpty()) {
      output.append(operator.pop());
      output.append(' ');
    }
    return output.toString();
  }

  public static double evaluateExpression(String s) {
    String[] elements = s.split(" ");
    Stack<Double> operands = new Stack<Double>();

    double operand1;
    double operand2;
    char operator;
    double res;

    for (String e : elements) {
      if (isNumeric(e)) {
        operands.push(Double.valueOf(e));
      } else {
        operand2 = operands.pop();
        operand1 = operands.pop();
        operator = e.charAt(0);
        res = operation(operand1, operand2, operator);
        operands.push(res);
      }
    }
    return operands.pop();
  }

  public static double operation(double op1, double op2, char operator) {
    switch(operator) {
      case '+':
        return op1 + op2;
      case '-':
        return op1 - op2;
      case '*':
        return op1 * op2;
      case '/':
        if (op2 == 0)
          throw new ArithmeticException();
        else
          return op1 / op2;
      case '^':
        return Math.pow(op1, op2);
      default:
        return 0.0;
    }
  }

  public static boolean isOperator(char c) {
    if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^')
      return true;
    else
      return false;
  }

  public static boolean isNumeric(String s) {
    if (s.isEmpty())
      return false;
    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) < 48 || s.charAt(i) > 57)
        return false;
    }
    return true;
  }

  public static int getPrecedence(char op) {
    switch (op) {
    case '+':
    case '-':
      return 2;
    case '*':
    case '/':
      return 3;
    case '^':
      return 4;
    default:
      return 0;
    }
  }
}
/*
https://stackoverflow.com/questions/6772222/simple-command-line-calculator
*/