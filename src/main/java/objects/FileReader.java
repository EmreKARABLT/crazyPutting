package objects;

import physics.PhysicEngine;
import physics.Vector2D;
import Main.Universe;

import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.*;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;


/**
 * reads the both the input file and shoot file
 */
public class FileReader {

    private Scanner shotReader;
    private final String fileString = "";
    private double x0, y0;
    private double xt, yt;
    private double r;
    private double muk, mus;
    private String heightProfile;
    private Vector2D sandPitX, sandPitY;
    private double muks, muss;
    private Expression expression;

    public FileReader() {
        try {
//            Replace File file = new File("explicit path"); with Path path1 = (Paths.get("shot.txt")).toAbsolutePath();
//            File shotFile = new File(path1.toString());
//            File shotFile = new File("C:\\Users\\majag\\IdeaProjects\\crazyPutting\\src\\main\\java\\shot.txt");
            File shotFile = new File((Paths.get("shot.txt")).toAbsolutePath().toString());

            try {
                if (shotFile.exists()) {
                    shotReader = new Scanner(shotFile);
                }
            }
            catch (NullPointerException e) {
                System.out.println("error in reading the shot file!");
            }

            File inputFile = new File("C:\\Users\\majag\\IdeaProjects\\crazyPutting\\src\\main\\java\\example_inputfile.txt");
            if (inputFile.exists()){
                Scanner inputReader = new Scanner(inputFile);

                while(inputReader.hasNextLine()){
                    String[] next = inputReader.nextLine().split("=");
                    for (int i = 0; i < next.length; i++) {
                        next[i] = next[i].strip();
                    }
                    switch (next[0]) {
                        case "x0" -> x0 = Double.parseDouble(next[1]);
                        case "y0" -> y0 = Double.parseDouble(next[1]);
                        case "xt" -> xt = Double.parseDouble(next[1]);
                        case "yt" -> yt = Double.parseDouble(next[1]);
                        case "r" -> r = Double.parseDouble(next[1]);
                        case "muk" -> muk = Double.parseDouble(next[1]);
                        case "mus" -> mus = Double.parseDouble(next[1]);
                        case "heightProfile" -> {
                            heightProfile = next[1];
                            String equation = next[1].replaceAll("Math.", "");
                            expression = new ExpressionBuilder(equation)
                                    .variables("x", "y")
                                    .build();
                        }
                        case "sandPitX" -> {
                            String[] pitX = next[1].split("<");
                            sandPitX = new Vector2D(Double.parseDouble(pitX[0]), Double.parseDouble(pitX[2]));
                        }
                        case "sandPitY" -> {
                            String[] pitY = next[1].split("<");
                            sandPitY = new Vector2D(Double.parseDouble(pitY[0]), Double.parseDouble(pitY[2]));
                        }
                        case "muks" -> muks = Double.parseDouble(next[1]);
                        case "muss" -> muss = Double.parseDouble(next[1]);
                    }

                }
            }

        }
        catch (Exception ignored) {}
    }

    public Vector2D getNextShotFromFile(){
        try {
            String next = shotReader.nextLine();
            String[] shot = next.split(",");
            return new Vector2D(Double.parseDouble(shot[0]), Double.parseDouble(shot[1]));
        }
        catch (NoSuchElementException e) {
            System.out.println("no more shots to be read!");
            return null;
        }
    }

    public Vector2D getInitialPosition() {
        return new Vector2D(x0,y0);
    }

    public Vector2D getTargetPosition() {
        return new Vector2D(xt,yt);
    }

    public double getTargetRadius() {
        return r;
    }

    public double getKineticFriction(){
        return muk;
    }

    public double getStaticFriction(){
        return mus;
    }

    public double getSandPitKineticFriction(){
        return muks;
    }

    public double getSandPitStaticFriction(){
        return muss;
    }

    public Vector2D getSandPitX(){
        return sandPitX;
    }

    public Vector2D getSandPitY(){
        return sandPitY;
    }

    public String getHeightProfile(){
        return heightProfile;
    }

    public Expression getExpression()
    {
        return expression;
    }

    public float calculator( float x, float y)
    {
        return calculator(expression, x, y);
    }

    public float calculator(Expression expression, float x, float y)
    {
        expression
                .setVariable("x", x)
                .setVariable("y", y);
        System.out.println(expression.evaluate());

        return (float) expression.evaluate();

    }

}