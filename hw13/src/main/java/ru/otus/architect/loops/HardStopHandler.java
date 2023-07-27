package ru.otus.architect.loops;

public class HardStopHandler implements StopHandler {
    @Override
    public Boolean get() {
        return false;
    }
}
