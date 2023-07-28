package ru.otus.architect.commands.macrocommands;

import ru.otus.architect.commands.Command;
import ru.otus.architect.commands.transactions.Transactional;

import java.util.List;
import java.util.Stack;

/**
 * Макрокомманда, реализующая транзакционность
 * При создании можно в списке передать команды поддерживающие транзакции
 * В случае возникновения исключения будут выполнены rollback для команд которые были выполднены и поддерживают
 * транзакции в обратном порядке.
 */
public class TransactionalMacroCommandChange implements Command {

    private final List<? extends Command> commands;
    private final Stack<Transactional> history;

    public TransactionalMacroCommandChange(List<Command> commands) {
        this.commands = commands;
        this.history = new Stack<>();

    }

    @Override
    public void execute() {
        try {
            commands.forEach(command -> {
                if (command instanceof Transactional) {
                    addHistory((Transactional) command);
                }
                command.execute();
            });
        } catch (Exception e) {
            while (!history.isEmpty()) {
                history.pop().rollback();
            }
            throw e;
        }
    }

    private void addHistory(Transactional transactional) {
        transactional.backup();
        history.push(transactional);
    }
}
