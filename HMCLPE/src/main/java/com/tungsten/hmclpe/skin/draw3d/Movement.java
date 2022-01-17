package com.tungsten.hmclpe.skin.draw3d;

public class Movement {
    public float cameraMovementX;
    public float cameraMovementY;
    public float cameraMovementZ;
    public float cameraRotationY;
    public float worldRotationX;

    public Movement() {
    }

    public boolean hasMovement() {
        boolean var1;
        if (this.worldRotationX == 0.0F && this.cameraRotationY == 0.0F && this.cameraMovementX == 0.0F && this.cameraMovementY == 0.0F && this.cameraMovementZ == 0.0F) {
            var1 = false;
        } else {
            var1 = true;
        }

        return var1;
    }

    public String toString() {
        StringBuilder var1 = new StringBuilder();
        var1.append("Movement (hasMovement=<");
        var1.append(this.hasMovement());
        var1.append(">, worldRotationX=");
        var1.append(this.worldRotationX);
        var1.append(", cameraRotationY=");
        var1.append(this.cameraRotationY);
        var1.append(", cameraMovementX=");
        var1.append(this.cameraMovementX);
        var1.append(", cameraMovementY=");
        var1.append(this.cameraMovementY);
        var1.append(", cameraMovementZ=");
        var1.append(this.cameraMovementZ);
        var1.append(")");
        return var1.toString();
    }
}