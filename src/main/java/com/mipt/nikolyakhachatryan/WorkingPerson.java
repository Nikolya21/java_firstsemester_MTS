package com.mipt.nikolyakhachatryan;

public abstract class WorkingPerson {
    public abstract void work(int hours);
    public boolean goHome(String string1, String string2) {
        return string1.equals(string2);
    }
}