(ns dev.ericdallo.clj4intellij.listener.class-loader
  (:gen-class
   :name dev.ericdallo.clj4intellij.listener.ClojureClassLoaderListener
   :extends dev.ericdallo.clj4intellij.ClojureClassLoader
   :implements [com.intellij.ide.AppLifecycleListener])
  (:import
   [dev.ericdallo.clj4intellij ClojureClassLoader]))

(defn -appFrameCreated [_ _] (ClojureClassLoader/bind))

(defn -welcomeScreenDisplayed [_])
(defn -appStarted [_])
(defn -projectFrameClosed [_])
(defn -projectOpenFailed [_])
(defn -appStarting [_ _])
(defn -appClosing [_])
(defn -appWillBeClosed [_ _])
