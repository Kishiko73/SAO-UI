package kishiko73.sao.ui;

public enum KeyBinds {

    RESPAWN(340); // L Shift

    private int keyCode;

    KeyBinds(int keyCode) {
        this.keyCode = keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }
    public int getKeyCode() {
        return keyCode;
    }

}
