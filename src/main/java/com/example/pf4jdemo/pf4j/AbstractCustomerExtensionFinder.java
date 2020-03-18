package com.example.pf4jdemo.pf4j;

import org.pf4j.*;
import org.pf4j.asm.ExtensionInfo;
import org.pf4j.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @Author sharplee
 * @Date 2020/3/5 17:13
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.pf4j
 * @ClassName AbstractCustomerExtensionFinder
 * @JavaFile com.example.pf4jdemo.pf4j.AbstractCustomerExtensionFinder.java
 */
public abstract class AbstractCustomerExtensionFinder  implements ExtensionFinder, PluginStateListener {

    private static final Logger log = LoggerFactory.getLogger(AbstractCustomerExtensionFinder.class);
    protected PluginManager pluginManager;
    protected volatile Map<String, Set<String>> entries;
    protected volatile Map<String, ExtensionInfo> extensionInfos;
    protected Boolean checkForExtensionDependencies = null;

    public AbstractCustomerExtensionFinder(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public abstract Map<String, Set<String>> readPluginsStorages();

    public abstract Map<String, Set<String>> readClasspathStorages();

    @Override
    public <T> List<ExtensionWrapper<T>> find(Class<T> type) {
        log.debug("Finding extensions of extension point '{}'", type.getName());
        Map<String, Set<String>> entries = this.getEntries();
        List<ExtensionWrapper<T>> result = new ArrayList();
        Iterator var4 = entries.keySet().iterator();

        while(var4.hasNext()) {
            String pluginId = (String)var4.next();
            List<ExtensionWrapper<T>> pluginExtensions = this.find(type, pluginId);
            result.addAll(pluginExtensions);
        }

        if (entries.isEmpty()) {
            log.debug("No extensions found for extension point '{}'", type.getName());
        } else {
            log.debug("Found {} extensions for extension point '{}'", result.size(), type.getName());
        }

        Collections.sort(result);
        return result;
    }

    @Override
    public <T> List<ExtensionWrapper<T>> find(Class<T> type, String pluginId) {
        log.debug("Finding extensions of extension point '{}' for plugin '{}'", type.getName(), pluginId);
        List<ExtensionWrapper<T>> result = new ArrayList();
        Set<String> classNames = this.findClassNames(pluginId);
        if (classNames != null && !classNames.isEmpty()) {
            if (pluginId != null) {
                PluginWrapper pluginWrapper = this.pluginManager.getPlugin(pluginId);
                if (PluginState.STARTED != pluginWrapper.getPluginState()) {
                    return result;
                }

                log.trace("Checking extensions from plugin '{}'", pluginId);
            } else {
                log.trace("Checking extensions from classpath");
            }

            ClassLoader classLoader = pluginId != null ? this.pluginManager.getPluginClassLoader(pluginId) : this.getClass().getClassLoader();
            Iterator var6 = classNames.iterator();

            label91:
            while(var6.hasNext()) {
                String className = (String)var6.next();

                try {
                    if (this.isCheckForExtensionDependencies()) {
                        ExtensionInfo extensionInfo = this.getExtensionInfo(className, classLoader);
                        if (extensionInfo == null) {
                            log.error("No extension annotation was found for '{}'", className);
                            continue;
                        }

                        List<String> missingPluginIds = new ArrayList();
                        Iterator var10 = extensionInfo.getPlugins().iterator();

                        label84:
                        while(true) {
                            String requiredPluginId;
                            PluginWrapper requiredPlugin;
                            do {
                                if (!var10.hasNext()) {
                                    if (!missingPluginIds.isEmpty()) {
                                        StringBuilder missing = new StringBuilder();

                                        String missingPluginId;
                                        for(Iterator var18 = missingPluginIds.iterator(); var18.hasNext(); missing.append(missingPluginId)) {
                                            missingPluginId = (String)var18.next();
                                            if (missing.length() > 0) {
                                                missing.append(", ");
                                            }
                                        }

                                        log.trace("Extension '{}' is ignored due to missing plugins: {}", className, missing);
                                        continue label91;
                                    }
                                    break label84;
                                }

                                requiredPluginId = (String)var10.next();
                                requiredPlugin = this.pluginManager.getPlugin(requiredPluginId);
                            } while(requiredPlugin != null && PluginState.STARTED.equals(requiredPlugin.getPluginState()));

                            missingPluginIds.add(requiredPluginId);
                        }
                    }

                    log.debug("Loading class '{}' using class loader '{}'", className, classLoader);
                    Class<?> extensionClass = classLoader.loadClass(className);
                    log.debug("extensionClass is {}",extensionClass);
                    log.debug("Checking extension type '{}'", className);
                    log.debug("{} is Agginablefrom {} {}",type.isAssignableFrom(extensionClass),type,extensionClass);
                    if (type.isAssignableFrom(extensionClass)) {
                        ExtensionWrapper extensionWrapper = this.createExtensionWrapper(extensionClass);
                        result.add(extensionWrapper);
                        log.debug("Added extension '{}' with ordinal {}", className, extensionWrapper.getOrdinal());
                    } else {
                        log.trace("'{}' is not an extension for extension point '{}'", className, type.getName());
                        if (RuntimeMode.DEVELOPMENT.equals(this.pluginManager.getRuntimeMode())) {
                            this.checkDifferentClassLoaders(type, extensionClass);
                        }
                    }
                } catch (ClassNotFoundException var13) {
                    log.error(var13.getMessage(), var13);
                }
            }

            if (result.isEmpty()) {
                log.debug("No extensions found for extension point '{}'", type.getName());
            } else {
                log.debug("Found {} extensions for extension point '{}'", result.size(), type.getName());
            }

            Collections.sort(result);
            return result;
        } else {
            return result;
        }
    }

    @Override
    public List<ExtensionWrapper> find(String pluginId) {
        log.debug("Finding extensions from plugin '{}'", pluginId);
        List<ExtensionWrapper> result = new ArrayList();
        Set<String> classNames = this.findClassNames(pluginId);
        if (classNames.isEmpty()) {
            return result;
        } else {
            if (pluginId != null) {
                PluginWrapper pluginWrapper = this.pluginManager.getPlugin(pluginId);
                if (PluginState.STARTED != pluginWrapper.getPluginState()) {
                    return result;
                }

                log.trace("Checking extensions from plugin '{}'", pluginId);
            } else {
                log.trace("Checking extensions from classpath");
            }

            ClassLoader classLoader = pluginId != null ? this.pluginManager.getPluginClassLoader(pluginId) : this.getClass().getClassLoader();
            Iterator var5 = classNames.iterator();

            while(var5.hasNext()) {
                String className = (String)var5.next();

                try {
                    log.debug("Loading class '{}' using class loader '{}'", className, classLoader);
                    Class<?> extensionClass = classLoader.loadClass(className);
                    ExtensionWrapper extensionWrapper = this.createExtensionWrapper(extensionClass);
                    result.add(extensionWrapper);
                    log.debug("Added extension '{}' with ordinal {}", className, extensionWrapper.getOrdinal());
                } catch (NoClassDefFoundError | ClassNotFoundException var9) {
                    log.error(var9.getMessage(), var9);
                }
            }

            if (result.isEmpty()) {
                log.debug("No extensions found for plugin '{}'", pluginId);
            } else {
                log.debug("Found {} extensions for plugin '{}'", result.size(), pluginId);
            }

            Collections.sort(result);
            return result;
        }
    }

    @Override
    public Set<String> findClassNames(String pluginId) {
        return (Set)this.getEntries().get(pluginId);
    }

    @Override
    public void pluginStateChanged(PluginStateEvent event) {
        this.entries = null;
        if (this.checkForExtensionDependencies == null && PluginState.STARTED.equals(event.getPluginState())) {
            Iterator var2 = event.getPlugin().getDescriptor().getDependencies().iterator();

            while(var2.hasNext()) {
                PluginDependency dependency = (PluginDependency)var2.next();
                if (dependency.isOptional()) {
                    log.debug("Enable check for extension dependencies via ASM.");
                    this.checkForExtensionDependencies = true;
                    break;
                }
            }
        }

    }

    public final boolean isCheckForExtensionDependencies() {
        return Boolean.TRUE.equals(this.checkForExtensionDependencies);
    }

    public void setCheckForExtensionDependencies(boolean checkForExtensionDependencies) {
        this.checkForExtensionDependencies = checkForExtensionDependencies;
    }

    protected void debugExtensions(Set<String> extensions) {
        if (log.isDebugEnabled()) {
            if (extensions.isEmpty()) {
                log.debug("No extensions found");
            } else {
                log.debug("Found possible {} extensions:", extensions.size());
                Iterator var2 = extensions.iterator();

                while(var2.hasNext()) {
                    String extension = (String)var2.next();
                    log.debug("   " + extension);
                }
            }
        }

    }

    private Map<String, Set<String>> readStorages() {
        Map<String, Set<String>> result = new LinkedHashMap();
        result.putAll(this.readClasspathStorages());
        result.putAll(this.readPluginsStorages());
        return result;
    }

    private Map<String, Set<String>> getEntries() {
        if (this.entries == null) {
            this.entries = this.readStorages();
        }

        return this.entries;
    }

    private ExtensionInfo getExtensionInfo(String className, ClassLoader classLoader) {
        if (this.extensionInfos == null) {
            this.extensionInfos = new HashMap();
        }

        if (!this.extensionInfos.containsKey(className)) {
            log.trace("Load annotation for '{}' using asm", className);
            ExtensionInfo info = ExtensionInfo.load(className, classLoader);
            if (info == null) {
                log.warn("No extension annotation was found for '{}'", className);
                this.extensionInfos.put(className, null);
            } else {
                this.extensionInfos.put(className, info);
            }
        }

        return (ExtensionInfo)this.extensionInfos.get(className);
    }

    private ExtensionWrapper createExtensionWrapper(Class<?> extensionClass) {
        int ordinal = 0;
        if (extensionClass.isAnnotationPresent(Extension.class)) {
            ordinal = ((Extension)extensionClass.getAnnotation(Extension.class)).ordinal();
        }

        ExtensionDescriptor descriptor = new ExtensionDescriptor(ordinal, extensionClass);
        return new ExtensionWrapper(descriptor, this.pluginManager.getExtensionFactory());
    }

    private void checkDifferentClassLoaders(Class<?> type, Class<?> extensionClass) {
        ClassLoader typeClassLoader = type.getClassLoader();
        ClassLoader extensionClassLoader = extensionClass.getClassLoader();
        boolean match = ClassUtils.getAllInterfacesNames(extensionClass).contains(type.getSimpleName());
        if (match && !extensionClassLoader.equals(typeClassLoader)) {
            log.error("Different class loaders: '{}' (E) and '{}' (EP)", extensionClassLoader, typeClassLoader);
        }

    }
}
