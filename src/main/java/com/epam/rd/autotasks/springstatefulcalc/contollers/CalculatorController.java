package com.epam.rd.autotasks.springstatefulcalc.contollers;

import com.epam.rd.autotasks.springstatefulcalc.util.Calculator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@SessionAttributes("calculator")
public class CalculatorController {

    @ModelAttribute("calculator")
    public Calculator getCalculator(){
        return new Calculator();
    }

    @PutMapping("/calc/expression")
    @ResponseBody
    public ResponseEntity postExpression(@ModelAttribute("calculator") Calculator calculator,
                                         @RequestBody String expression){
        try {
            if (calculator.setExpression(expression)){
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.CREATED);
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/calc/{variableName:[a-z]}")
    @ResponseBody
    public ResponseEntity postVariable(@ModelAttribute("calculator") Calculator calculator,
                               @RequestBody String value,
                                @PathVariable String variableName){
        try {
            if (calculator.addValues(variableName, value)){
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.CREATED);
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/calc/result")
    @ResponseBody
    public String getResult(@ModelAttribute("calculator") Calculator calculator) {
        int result;
        try {
            result = calculator.evaluate();
        } catch (Exception e) {
            throw new IllegalStateException();
        }
        return String.valueOf(result);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity handleException(IllegalStateException e) {
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @DeleteMapping("/calc/{variableName:[a-z]}")
    @ResponseBody
    public ResponseEntity deleteVariable(@ModelAttribute("calculator") Calculator calculator,
                                       @PathVariable String variableName){
        calculator.deleteValue(variableName);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
