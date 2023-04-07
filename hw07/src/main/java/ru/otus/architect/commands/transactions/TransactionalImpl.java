package ru.otus.architect.commands.transactions;

import ru.otus.architect.commands.Command;

public class TransactionalImpl implements Transactional {

    private final Command backupStrategy;
    private final Command rollbackStrategy;
    private final Command command;

    public TransactionalImpl(Command command, Command backupStrategy, Command rollbackStrategy) {
        this.backupStrategy = backupStrategy;
        this.rollbackStrategy = rollbackStrategy;
        this.command = command;
    }

    @Override
    public void backup() {
        backupStrategy.execute();
    }

    @Override
    public void rollback() {
        rollbackStrategy.execute();
    }

    @Override
    public void execute() {
        command.execute();
    }
}
