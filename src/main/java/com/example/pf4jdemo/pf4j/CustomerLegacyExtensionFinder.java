package com.example.pf4jdemo.pf4j;

import org.pf4j.LegacyExtensionFinder;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.pf4j.processor.LegacyExtensionStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Author sharplee
 * @Date 2020/3/5 17:20
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.pf4j
 * @ClassName CustomerLegacyExtensionFinder
 * @JavaFile com.example.pf4jdemo.pf4j.CustomerLegacyExtensionFinder.java
 */
public class CustomerLegacyExtensionFinder extends AbstractCustomerExtensionFinder {

    private static final Logger log = LoggerFactory.getLogger(CustomerLegacyExtensionFinder.class);

    public CustomerLegacyExtensionFinder(PluginManager pluginManager) {
        super(pluginManager);
    }

    @Override
    public Map<String, Set<String>> readClasspathStorages() {
        log.debug("Reading extensions storages from classpath");
        Map<String, Set<String>> result = new LinkedHashMap();
        HashSet bucket = new HashSet();

        try {
            Enumeration<URL> urls = this.getClass().getClassLoader().getResources(getExtensionsResource());
            if (urls.hasMoreElements()) {
                this.collectExtensions((Enumeration)urls, bucket);
            } else {
                log.debug("Cannot find '{}'", getExtensionsResource());
            }

            this.debugExtensions(bucket);
            result.put(null, bucket);
        } catch (IOException var4) {
            log.error(var4.getMessage(), var4);
        }

        return result;
    }

    @Override
    public Map<String, Set<String>> readPluginsStorages() {
        log.debug("Reading extensions storages from plugins");
        Map<String, Set<String>> result = new LinkedHashMap();
        List<PluginWrapper> plugins = this.pluginManager.getPlugins();
        Iterator var3 = plugins.iterator();

        while(var3.hasNext()) {
            PluginWrapper plugin = (PluginWrapper)var3.next();
            String pluginId = plugin.getDescriptor().getPluginId();
            log.debug("Reading extensions storage from plugin '{}'", pluginId);
            HashSet bucket = new HashSet();

            try {
                log.debug("Read '{}'", getExtensionsResource());
                ClassLoader pluginClassLoader = plugin.getPluginClassLoader();
                InputStream resourceStream = pluginClassLoader.getResourceAsStream(getExtensionsResource());

                try {
                    if (resourceStream == null) {
                        log.debug("Cannot find '{}'", getExtensionsResource());
                    } else {
                        this.collectExtensions((InputStream)resourceStream, bucket);
                    }
                } catch (Throwable var12) {
                    if (resourceStream != null) {
                        try {
                            resourceStream.close();
                        } catch (Throwable var11) {
                            var12.addSuppressed(var11);
                        }
                    }

                    throw var12;
                }

                if (resourceStream != null) {
                    resourceStream.close();
                }

                this.debugExtensions(bucket);
                result.put(pluginId, bucket);
            } catch (IOException var13) {
                log.error(var13.getMessage(), var13);
            }
        }

        return result;
    }

    private void collectExtensions(Enumeration<URL> urls, Set<String> bucket) throws IOException {
        while(urls.hasMoreElements()) {
            URL url = (URL)urls.nextElement();
            log.debug("Read '{}'", url.getFile());
            this.collectExtensions(url.openStream(), bucket);
        }

    }

    private void collectExtensions(InputStream inputStream, Set<String> bucket) throws IOException {
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        try {
            LegacyExtensionStorage.read(reader, bucket);
        } catch (Throwable var7) {
            try {
                reader.close();
            } catch (Throwable var6) {
                var7.addSuppressed(var6);
            }

            throw var7;
        }

        reader.close();
    }

    private static String getExtensionsResource() {
        return "META-INF/extensions.idx";
    }



}
