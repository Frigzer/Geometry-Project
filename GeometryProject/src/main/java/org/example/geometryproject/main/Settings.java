package org.example.geometryproject.main;

public class Settings {
    private String controlMode = "Classic";
    private String moveUpKey = "W";
    private String moveDownKey = "S";
    private String moveLeftKey = "A";
    private String moveRightKey = "D";
    private int screenWidth = 800;
    private int screenHeight = 600;

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

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }
}
