package ru.otus.architect.commands.transactions;

import ru.otus.architect.commands.Command;

public interface Transactional extends Command {

    void backup();

    void rollback();

}
