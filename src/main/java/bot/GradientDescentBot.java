package bot;


import physics.Vector2D;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * not to be included in this phase
 */
public class GradientDescentBot extends Bot {

    private double learningRate = 0.9;


    public GradientDescentBot() {
        this.name = "Gradient Descent Bot";
    }


    @Override
    public void run() {

        int power = -8;
        double stepSize = 0.01;

        Vector2D velocity =  new Vector2D(
                this.universe.getTarget().getPosition().getX() - this.universe.getBall().getPosition().getX(),
                this.universe.getTarget().getPosition().getY() - this.universe.getBall().getPosition().getY()
        );
        velocity = velocity.getUnitVector();
        this.shotCounter++;
        this.bestVelocity = velocity;
        this.bestResult = new TestShot(velocity, this.targetPosition, Heuristics.finalPosition).getTestResult();
        boolean play = true;
        ArrayList<Vector2D> testVelocities = new ArrayList<>();
        testVelocities.add(0, velocity);

        while (play) {
            if(bestResult == 0 ) {
                stop();
            }
            play = false;
            this.shotCounter++;
            double derivativeX = derivativeX(velocity, stepSize) * learningRate;
            double derivativeY = derivativeY(velocity, stepSize) * learningRate;

            Vector2D testVelocity = new Vector2D(
                    velocity.getX() - derivativeX,
                    velocity.getY() - derivativeY
            );
            testVelocities.add(0, testVelocity);
            double testResult = new TestShot(testVelocity, this.targetPosition).getTestResult();

            // target was hit
            if (testResult == 0) {
                this.bestVelocity = testVelocity;
                this.bestResult = testResult;
                stop();
            }

            else if (testResult < this.bestResult) {
                velocity = testVelocities.get(0);
                this.bestResult = testResult;
                this.bestVelocity = velocity;
                play = true;
            }

            if( bestResult != 0 && testResult > bestResult && learningRate > Math.pow(10, power) ){
                testVelocities.remove(0);
                velocity = testVelocities.get(0);
                stepSize *=0.1;
                learningRate *= 0.1;
                play = true ;
            }
        }
        bestResult = new TestShot(bestVelocity, this.targetPosition).getTestResult();
        stop();
    }


    public double derivativeX(Vector2D velocity , double step){
        Vector2D velocityPlus = velocity.add(step , 0 );
        Vector2D velocityMinus = velocity.add(-1 * step , 0 );
        double testShotPlus = new TestShot(velocityPlus, this.targetPosition).getTestResult();
        double testShotMinus = new TestShot(velocityMinus, this.targetPosition).getTestResult();
        return (testShotPlus - testShotMinus) / ( 2 * step) ;
    }

    public double derivativeY(Vector2D velocity, double step){
        Vector2D velocityPlus = velocity.add(0 , step );
        Vector2D velocityMinus = velocity.add( 0 , -1.d * step );
        double testShotPlus = new TestShot(velocityPlus, this.targetPosition).getTestResult();
        double testShotMinus = new TestShot(velocityMinus, this.targetPosition).getTestResult();
        return (testShotPlus - testShotMinus) / ( 2 * step) ;
    }

}