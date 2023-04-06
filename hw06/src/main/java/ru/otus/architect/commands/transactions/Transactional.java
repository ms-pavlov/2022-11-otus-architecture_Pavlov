package ru.otus.architect.commands.transactions;

import ru.otus.architect.commands.Command;

import java.util.function.Supplier;

public interface Transactional extends Command {

    void backup();

    void rollback();

}
