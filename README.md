# clj4intellij

Library for create IntelliJ plugins with Clojure.

## How to use it?

Configure [clojurephant](https://clojurephant.dev) first:

`build.gradle.kts`
```kotlin
plugins {
    ...
    id("dev.clojurephant.clojure") version "VERSION"
}
clojure.builds.named("main") {
    classpath.from(sourceSets.main.get().runtimeClasspath.asPath)
    checkAll()
    aotAll()
    reflection.set("fail")
}
```

Add clj4intellij as dependency:

`build.gradle.kts`
```kotlin
repositories {
    ...
    maven {
        name = "Clojars"
        url = uri("https://repo.clojars.org")
    }
}
dependencies {
    ...
    implementation ("com.github.ericdallo:clj4intellij:VERSION")
}
```

Add an application listener that will change the classloader on IntelliJ startup to load your plugin Clojure code:

`src/main/resources/META-INF/plugin.xml`
```xml
<applicationListeners>
        <listener topic="com.intellij.ide.AppLifecycleListener"
                  class="com.github.ericdallo.clj4intellij.listener.ClojureClassLoaderListener"/>
</applicationListeners>
```

Now you can create clojure namespaces in your sourcepath (ex `src/main/clojure`), use clj4intellij helpers to create extensions or implement yourself extensions using Clojure's `gen-class`.

## How it works?

This plugin has classes required to make possible code in Clojure a Intellij plugin changing the classloader at IntelliJ's startup.

Also contains useful functions for a more Clojure idiomatic development avoid the directly use of java or Intellij API.

## Useful namespaces

- `com.github.ericdallo.clj4intellij.app-manager` to handle `Application` calls.

## Plugins using clj4intellij

- [clojure-lsp-intellij](https://github.com/clojure-lsp/clojure-lsp-intellij)
