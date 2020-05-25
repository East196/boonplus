package cn.md.jbehave;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

public class MathSteps extends Steps
{
    private int input;
    private int result;
    
    @Given("a variable input with value $value")
    public void givenInputValue(@Named("value") int value)
    {
        input = value;
    }
    
    @When("I multiply input by $value")
    public void whenImultiplyInputBy(@Named("value") int value)
    {
        result = input * value;
    }
    
    @Then("result should equal $value")
    public void thenInputshouldBe(@Named("value") int value)
    {
        if (value != result)
            throw new RuntimeException("result is " + result + ", but should be " + value);
    }
}