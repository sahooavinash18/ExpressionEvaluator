/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.csc413.calculator.operators;

import edu.csc413.calculator.evaluator.Operand;

/**
 *
 * @author avinashsahoo
 */
public class PowerOperator extends Operator {

  @Override
  public int priority() 
  {
    return 4;
  }

  @Override
  public Operand execute(Operand operand1, Operand operand2) 
  {
    return new Operand((int) Math.pow(operand1.getValue(), operand2.getValue()));
  }
}
