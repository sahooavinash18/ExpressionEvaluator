package edu.csc413.calculator.evaluator;



import edu.csc413.calculator.operators.*;

import java.util.HashMap;

import java.util.Objects;
import java.util.Stack;
import java.util.StringTokenizer;

public class Evaluator {
  private Stack<Operand> operandStack;
  private Stack<Operator> operatorStack;
  private StringTokenizer tokenizer;
  private static final String DELIMITERS = "+-*^/() ";

  public Evaluator() {
    operandStack = new Stack<>();
    operatorStack = new Stack<>();
    
    // initialize operator stack
    initializeOperators();
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Evaluator evaluator = (Evaluator) o;
    return Objects.equals(operandStack, evaluator.operandStack) &&
            Objects.equals(operatorStack, evaluator.operatorStack) &&
            Objects.equals(tokenizer, evaluator.tokenizer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operandStack, operatorStack, tokenizer);
  }

  public int eval(String expression ) {
    String token;

    // The 3rd argument is true to indicate that the delimiters should be used
    // as tokens, too. But, we'll need to remember to filter out spaces.
    this.tokenizer = new StringTokenizer( expression, DELIMITERS, true );
    
    if(operatorStack.isEmpty()) {
      operatorStack.push(new InitOperator());
    }


    while ( this.tokenizer.hasMoreTokens() ) {
      // filter out spaces
      if ( !( token = this.tokenizer.nextToken() ).equals( " " )) {
        // check if token is an operand
        if ( Operand.check( token )) {
          operandStack.push( new Operand( token ));
        } else {
          if ( ! Operator.check( token )) {
            System.out.println( "*****invalid token******" );
            throw new RuntimeException("*****invalid token******");
          }


          // TODO Operator is abstract - these two lines will need to be fixed:
          // The Operator class should contain an instance of a HashMap,
          // and values will be instances of the Operators.  See Operator class
          // skeleton for an example.
          //Operator newOperator = new Operator();
          Operator newOperator = Operator.operators.get(token);
          
          if(newOperator.equals(Operator.operators.get("("))) {
            operatorStack.push(newOperator);
          } else if(newOperator.equals(Operator.operators.get(")"))) {
            while(!operatorStack.peek().equals(Operator.operators.get("("))) {
            Operator oldOpr = operatorStack.pop();
            Operand op2 = operandStack.pop();
            Operand op1 = operandStack.pop();
            operandStack.push( oldOpr.execute( op1, op2 ));
            }
            operatorStack.pop();
          } else {
            while (operatorStack.peek().priority() >= newOperator.priority() ) {
            // note that when we eval the expression 1 - 2 we will
            // push the 1 then the 2 and then do the subtraction operation
            // This means that the first number to be popped is the
            // second operand, not the first operand - see the following code
            Operator oldOpr = operatorStack.pop();
            Operand op2 = operandStack.pop();
            Operand op1 = operandStack.pop();
            operandStack.push( oldOpr.execute( op1, op2 ));
            }
          operatorStack.push( newOperator );
        }
      }
    }
    }

    
    // Control gets here when we've picked up all of the tokens; you must add
    // code to complete the evaluation - consider how the code given here
    // will evaluate the expression 1+2*3
    // When we have no more tokens to scan, the operand stack will contain 1 2
    // and the operator stack will have + * with 2 and * on the top;
    // In order to complete the evaluation we must empty the stacks, 
    // that is, we should keep evaluating the operator stack until it is empty;
    // Suggestion: create a method that takes an operator as argument and
    // then executes the while loop.
    
    while(operatorStack.size() > 1) {
      Operator oldOpr = operatorStack.pop();
      Operand op2 = operandStack.pop();
      Operand op1 = operandStack.pop();
      operandStack.push( oldOpr.execute( op1, op2 ));
    }

    int value = operandStack.pop().getValue();
    return value;
    
    //return 0;
  }
  
  
  private void initializeOperators() {
    Operator.operators = new HashMap<String, Operator>();
    Operator.operators.put("+", new AddOperator());
    Operator.operators.put("-", new SubtractOperator());
    Operator.operators.put("*", new MultiplyOperator());
    Operator.operators.put("/", new DivideOperator());
    Operator.operators.put("^", new PowerOperator());
    Operator.operators.put("(", new RightParenthesesOperator());
    Operator.operators.put(")", new LeftParenthesesOperator());
  }
}
