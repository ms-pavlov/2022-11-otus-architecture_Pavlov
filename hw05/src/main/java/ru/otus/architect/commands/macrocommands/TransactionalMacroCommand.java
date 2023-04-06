package ru.otus.architect.commands.macrocommands;

import java.util.List;
import java.util.Queue;
import ru.otus.architect.commands.Command;

/**
 * Макрокомманда, реализующая транзакционность
 * При создании комманды мы обязаны предоставить "обратное" действие в соответствующий список
 * В случае возникновения исключения будут выполнены команды из reverseCommandList в обратном порядке
 */
public class TransactionalMacroCommand implements Command {

    private final Queue<Command> commandList;
    private final List<Command> reverseCommandList;
    private int counter;

    public TransactionalMacroCommand(Queue<Command> commandList, List<Command> reverseCommandList) {
        this.commandList = commandList;
        this.reverseCommandList = reverseCommandList;
        this.counter = -1;

    }

    @Override
    public void execute() {
        if(commandList.size() != reverseCommandList.size()) {
            throw new IncorrectReverseCommandListException("For every command in queue must be a reverse action!");
        }

        while (!commandList.isEmpty()) {
            try{
                commandList.poll().execute();
                counter++;
            } catch (Exception e) {
                while(counter >= 0) {
                    reverseCommandList.get(counter).execute();
                    counter--;
                }
                throw e;
            }
        }
    }
}
