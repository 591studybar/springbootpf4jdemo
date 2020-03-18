package com.example.pf4jdemo.pf4j;

import org.pf4j.*;

import java.util.*;

/**
 * @Author sharplee
 * @Date 2020/3/5 17:35
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.pf4j
 * @ClassName CustomerDefaultExtensionFinder
 * @JavaFile com.example.pf4jdemo.pf4j.CustomerDefaultExtensionFinder.java
 */
public class CustomerDefaultExtensionFinder  implements ExtensionFinder, PluginStateListener {

    protected PluginManager pluginManager;
    protected List<ExtensionFinder> finders = new ArrayList();

    public CustomerDefaultExtensionFinder(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
        this.add(new CustomerLegacyExtensionFinder(pluginManager));
    }

    @Override
    public <T> List<ExtensionWrapper<T>> find(Class<T> type) {
        List<ExtensionWrapper<T>> extensions = new ArrayList();
        Iterator var3 = this.finders.iterator();

        while(var3.hasNext()) {
            ExtensionFinder finder = (ExtensionFinder)var3.next();
            extensions.addAll(finder.find(type));
        }

        return extensions;
    }

    @Override
    public <T> List<ExtensionWrapper<T>> find(Class<T> type, String pluginId) {
        List<ExtensionWrapper<T>> extensions = new ArrayList();
        Iterator var4 = this.finders.iterator();

        while(var4.hasNext()) {
            ExtensionFinder finder = (ExtensionFinder)var4.next();
            extensions.addAll(finder.find(type, pluginId));
        }

        return extensions;
    }

    @Override
    public List<ExtensionWrapper> find(String pluginId) {
        List<ExtensionWrapper> extensions = new ArrayList();
        Iterator var3 = this.finders.iterator();

        while(var3.hasNext()) {
            ExtensionFinder finder = (ExtensionFinder)var3.next();
            extensions.addAll(finder.find(pluginId));
        }

        return extensions;
    }

    @Override
    public Set<String> findClassNames(String pluginId) {
        Set<String> classNames = new HashSet();
        Iterator var3 = this.finders.iterator();

        while(var3.hasNext()) {
            ExtensionFinder finder = (ExtensionFinder)var3.next();
            classNames.addAll(finder.findClassNames(pluginId));
        }

        return classNames;
    }

    @Override
    public void pluginStateChanged(PluginStateEvent event) {
        Iterator var2 = this.finders.iterator();

        while(var2.hasNext()) {
            ExtensionFinder finder = (ExtensionFinder)var2.next();
            if (finder instanceof PluginStateListener) {
                ((PluginStateListener)finder).pluginStateChanged(event);
            }
        }

    }

    public CustomerDefaultExtensionFinder addServiceProviderExtensionFinder() {
        return this.add(new CustomerServiceProviderExtensionFinder(this.pluginManager));
    }

    public CustomerDefaultExtensionFinder add(ExtensionFinder finder) {
        this.finders.add(finder);
        return this;
    }

}

