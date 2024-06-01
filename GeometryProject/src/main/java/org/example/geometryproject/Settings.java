package org.example.geometryproject;

public class Settings {
    private String controlMode = "Classic";
    private String moveUpKey = "W";
    private String moveDownKey = "S";
    private String moveLeftKey = "A";
    private String moveRightKey = "D";

    public String getControlMode() {
        return controlMode;
    }

    public void setControlMode(String controlMode) {
        this.controlMode = controlMode;
    }

    public String getMoveUpKey() {
        return moveUpKey;
    }

    public void setMoveUpKey(String moveUpKey) {
        this.moveUpKey = moveUpKey;
    }

    public String getMoveDownKey() {
        return moveDownKey;
    }

    public void setMoveDownKey(String moveDownKey) {
        this.moveDownKey = moveDownKey;
    }

    public String getMoveLeftKey() {
        return moveLeftKey;
    }

    public void setMoveLeftKey(String moveLeftKey) {
        this.moveLeftKey = moveLeftKey;
    }

    public String getMoveRightKey() {
        return moveRightKey;
    }

    public void setMoveRightKey(String moveRightKey) {
        this.moveRightKey = moveRightKey;
    }
}
