package ru.otus.commands.transactions;

import ru.otus.commands.Command;

public interface Transactional extends Command {

    void backup();

    void rollback();

}
