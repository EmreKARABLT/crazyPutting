package objects;

import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import physics.PhysicEngine;
import physics.Vector2D;


public class Ball extends PhysicEngine implements GameObject {

    private double RADIUS = 0.05;
    private static final double MASS = 0.0459;

    private Vector2D position ;
    private Vector2D previousPosition;
    private Vector2D velocity ;
    private Vector2D acceleration;
    private Target target ;
    private Sphere sphere;
    private boolean willMove;

    public Ball(Vector2D position) {
        this.position = position;
        this.previousPosition = position ;
        velocity = new Vector2D(0,0);
        willMove = false;
        setAcceleration(velocity);
        createSphere();
    }


    private void createSphere() {
        this.sphere = new Sphere(RADIUS * 2);
        this.sphere.setTranslateX(this.position.getX());
        this.sphere.setTranslateY(this.position.getY());
        this.sphere.setTranslateZ((-TerrainGenerator.getHeight(position) - 2*RADIUS));
        Material material = new PhongMaterial(Color.WHITESMOKE);
        this.sphere.setMaterial(material);
        System.out.println(TerrainGenerator.getHeight(position));
    }
    public boolean isMoving(){ return velocity.getMagnitude() > 0.005; }
    public boolean getWillMove(){
        willMove = TerrainGenerator.getStaticFrictionCoefficient(position) < (Math.sqrt(Math.pow(TerrainGenerator.getSlopeX(position), 2) + Math.pow(TerrainGenerator.getSlopeY(getPosition()), 2) ));
        return willMove;
    }
    public void setWillMove(boolean willMove){
        this.willMove = willMove;
    }
    public boolean isOnSlope() {return TerrainGenerator.getSlopeX(position) != 0  || TerrainGenerator.getSlopeY(position) != 0 ; }
    public boolean isOnTarget(){
        double xdiff = target.getPosition().getX()-position.getX();
        double ydiff = target.getPosition().getY()-position.getY();
        Vector2D diff = new Vector2D(xdiff,ydiff);
        return diff.getMagnitude() < target.getCylinder().getRadius();
    }
    public void setTarget(Target target){this.target = target;}

    public Vector2D getPosition(){
        return position;
    }
    public void setPosition(Vector2D position){this.position = position;    }

    public void setDimension(Vector2D dimension) {this.RADIUS = dimension.getX();}
    public Vector2D getDimension() {return new Vector2D(RADIUS, RADIUS);}

    public Vector2D getAcceleration(){ return acceleration;}
    public void setAcceleration(Vector2D acceleration){ this.acceleration = acceleration;    }

    public Vector2D getVelocity(){return velocity;}

    public void setVelocity(Vector2D velocity){this.velocity = velocity;}

    public double getPositionX(){return position.getX();}
    public double getPositionY(){return position.getY();}

    public void setPreviousPosition(Vector2D previousPosition){this.previousPosition = previousPosition;}
    public Vector2D getPreviousPosition() { return previousPosition; }

    public void setState( Vector2D position , Vector2D velocity){
        setPosition(position);
        setVelocity(velocity);
    }

    public double getRADIUS() {return this.RADIUS;}
    public double getMass(){return MASS;    }
    public Sphere getSphere() {return this.sphere;}

}
