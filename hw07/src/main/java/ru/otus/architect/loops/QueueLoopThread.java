package ru.otus.architect.loops;

public interface QueueLoopThread extends Runnable {
    void setCommandHandler(CommandHandler var1);

    void setStopHandler(StopHandler var1);
}
