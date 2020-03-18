package com.example.pf4jdemo.pf4j;

import org.pf4j.PluginClassLoader;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.pf4j.ServiceProviderExtensionFinder;
import org.pf4j.processor.ServiceProviderExtensionStorage;
import org.pf4j.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * @Author sharplee
 * @Date 2020/3/5 17:24
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.pf4j
 * @ClassName CustomerServiceProviderExtensionFinder
 * @JavaFile com.example.pf4jdemo.pf4j.CustomerServiceProviderExtensionFinder.java
 */
public class CustomerServiceProviderExtensionFinder  extends AbstractCustomerExtensionFinder{

    private static final Logger log = LoggerFactory.getLogger(ServiceProviderExtensionFinder.class);

    public CustomerServiceProviderExtensionFinder(PluginManager pluginManager) {
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
        } catch (URISyntaxException | IOException var4) {
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
            log.debug("Reading extensions storages for plugin '{}'", pluginId);
            HashSet bucket = new HashSet();

            try {
                Enumeration<URL> urls = ((PluginClassLoader)plugin.getPluginClassLoader()).findResources(getExtensionsResource());
                if (urls.hasMoreElements()) {
                    this.collectExtensions((Enumeration)urls, bucket);
                } else {
                    log.debug("Cannot find '{}'", getExtensionsResource());
                }

                this.debugExtensions(bucket);
                result.put(pluginId, bucket);
            } catch (URISyntaxException | IOException var8) {
                log.error(var8.getMessage(), var8);
            }
        }

        return result;
    }

    private void collectExtensions(Enumeration<URL> urls, Set<String> bucket) throws URISyntaxException, IOException {
        while(urls.hasMoreElements()) {
            URL url = (URL)urls.nextElement();
            log.debug("Read '{}'", url.getFile());
            this.collectExtensions(url, bucket);
        }

    }

    private void collectExtensions(URL url, Set<String> bucket) throws URISyntaxException, IOException {
        Path extensionPath;
        if (url.toURI().getScheme().equals("jar")) {
            extensionPath = FileUtils.getPath(url.toURI(), getExtensionsResource(), new String[0]);
        } else {
            extensionPath = Paths.get(url.toURI());
        }

        bucket.addAll(this.readExtensions(extensionPath));
    }

    private static String getExtensionsResource() {
        return "META-INF/services";
    }

    private Set<String> readExtensions(Path extensionPath) throws IOException {
        final Set<String> result = new HashSet();
        Files.walkFileTree(extensionPath, Collections.emptySet(), 1, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                CustomerServiceProviderExtensionFinder.log.debug("Read '{}'", file);
                BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8);

                try {
                    ServiceProviderExtensionStorage.read(reader, result);
                } catch (Throwable var7) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Throwable var6) {
                            var7.addSuppressed(var6);
                        }
                    }

                    throw var7;
                }

                if (reader != null) {
                    reader.close();
                }

                return FileVisitResult.CONTINUE;
            }
        });
        return result;
    }

}

