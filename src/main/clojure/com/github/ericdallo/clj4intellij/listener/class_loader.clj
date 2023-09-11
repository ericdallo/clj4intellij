(ns com.github.ericdallo.clj4intellij.listener.class-loader
  (:gen-class
   :name com.github.ericdallo.clj4intellij.listener.ClojureClassLoaderListener
   :extends com.github.ericdallo.clj4intellij.ClojureClassLoader
   :implements [com.intellij.ide.AppLifecycleListener])
  (:import
   [com.github.ericdallo.clj4intellij ClojureClassLoader]))

(set! *warn-on-reflection* true)

(defn -appFrameCreated [_ _] (ClojureClassLoader/bind))

(defn -welcomeScreenDisplayed [_])
(defn -appStarted [_])
(defn -projectFrameClosed [_])
(defn -projectOpenFailed [_])
(defn -appStarting [_ _])
(defn -appClosing [_])
(defn -appWillBeClosed [_ _])
