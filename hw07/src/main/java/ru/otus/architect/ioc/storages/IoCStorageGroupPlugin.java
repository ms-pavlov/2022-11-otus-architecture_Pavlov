package ru.otus.architect.ioc.storages;

import java.util.List;

public class IoCStorageGroupPlugin implements IoCStoragePlugin {

    private final List<IoCStoragePlugin> plugins;

    public IoCStorageGroupPlugin(IoCStoragePlugin... plugins) {
        this.plugins = List.of(plugins);
    }

    public IoCStorageGroupPlugin(List<IoCStoragePlugin> plugins) {
        this.plugins = plugins;
    }

    @Override
    public void execute(IoCStorage storage) {
        plugins.forEach(plugin -> plugin.execute(storage));
    }
}
