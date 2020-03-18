package com.example.pf4jdemo.pf4j;

import lombok.extern.slf4j.Slf4j;
import org.pf4j.*;

import java.nio.file.Path;

/**
 * @Author sharplee
 * @Date 2020/3/4 15:31
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.utils
 * @ClassName CustomerPluginManager
 * @JavaFile com.example.pf4jdemo.pf4j.CustomerPluginManager.java
 */
@Slf4j
public class CustomerPluginManager  extends DefaultPluginManager {

    private PluginRepository repository;

    public  CustomerPluginManager(){
        super();
        repository = createPluginRepository();
    }

    @Override
    protected PluginRepository createPluginRepository() {
        return super.createPluginRepository();
    }

    public  PluginRepository getRepository(){
        return pluginRepository;
    }

    public CustomerPluginManager(Path pluginsRoot) {
        super(pluginsRoot);
    }


    @Override
    protected ExtensionFinder createExtensionFinder() {
        CustomerDefaultExtensionFinder customerDefaultExtensionFinder = new CustomerDefaultExtensionFinder(this);
        return  customerDefaultExtensionFinder;
    }

    @Override
    protected PluginWrapper loadPluginFromPath(Path pluginPath) {
        String pluginId = this.idForPath(pluginPath);
        if (pluginId != null) {
            throw new PluginAlreadyLoadedException(pluginId, pluginPath);
        } else {
            PluginDescriptorFinder pluginDescriptorFinder = this.getPluginDescriptorFinder();
            log.debug("Use '{}' to find plugins descriptors", pluginDescriptorFinder);
            log.debug("Finding plugin descriptor for plugin '{}'", pluginPath);
            PluginDescriptor pluginDescriptor = pluginDescriptorFinder.find(pluginPath);
            this.validatePluginDescriptor(pluginDescriptor);
            pluginId = pluginDescriptor.getPluginId();
            if (this.plugins.containsKey(pluginId)) {
                PluginWrapper loadedPlugin = this.getPlugin(pluginId);
                log.info(loadedPlugin.toString());
                throw new PluginRuntimeException("There is an already loaded plugin ({}) with the same id ({}) as the plugin at path '{}'. Simultaneous loading of plugins with the same PluginId is not currently supported.\nAs a workaround you may include PluginVersion and PluginProvider in PluginId.", new Object[]{loadedPlugin, pluginId, pluginPath});
            } else {
                log.debug("Found descriptor {}", pluginDescriptor);
                String pluginClassName = pluginDescriptor.getPluginClass();
                log.debug("Class '{}' for plugin '{}'", pluginClassName, pluginPath);
                log.debug("Loading plugin '{}'", pluginPath);
                ClassLoader pluginClassLoader = this.getPluginLoader().loadPlugin(pluginPath, pluginDescriptor);
                log.debug("Loaded plugin '{}' with class loader '{}'", pluginPath, pluginClassLoader);
                log.debug("Creating wrapper for plugin '{}'", pluginPath);
                PluginWrapper pluginWrapper = new PluginWrapper(this, pluginDescriptor, pluginPath, pluginClassLoader);
                pluginWrapper.setPluginFactory(this.getPluginFactory());
                if (this.isPluginDisabled(pluginDescriptor.getPluginId())) {
                    log.info("Plugin '{}' is disabled", pluginPath);
                    pluginWrapper.setPluginState(PluginState.DISABLED);
                }

                if (!this.isPluginValid(pluginWrapper)) {
                    log.warn("Plugin '{}' is invalid and it will be disabled", pluginPath);
                    pluginWrapper.setPluginState(PluginState.DISABLED);
                }

                log.debug("Created wrapper '{}' for plugin '{}'", pluginWrapper, pluginPath);
                pluginId = pluginDescriptor.getPluginId();
                this.plugins.put(pluginId, pluginWrapper);
                this.getUnresolvedPlugins().add(pluginWrapper);
                this.getPluginClassLoaders().put(pluginId, pluginClassLoader);
                return pluginWrapper;
            }
        }
    }

    @Override
    public void loadPlugins() {
        super.loadPlugins();
    }

    @Override
    protected PluginLoader createPluginLoader() {
        return super.createPluginLoader();
    }
}
