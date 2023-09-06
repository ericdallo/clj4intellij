(ns build
  (:require
   [clojure.tools.build.api :as b]))

(def lib 'dev.ericdallo/clj4intellij)
(def version "0.1.0")
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(def jar-file (format "target/%s.jar" (name lib)))

(defn clean [_]
  (b/delete {:path "target"}))

(defn compile-java [_]
  (b/javac {:src-dirs ["src/main/java"]
            :class-dir class-dir
            :basis basis}))

(defn compile-clj [_]
  (b/compile-clj {:basis basis
                  :src-dirs ["src/main/clojure"]
                  :class-dir class-dir}))

(defn jar [_]
  (clean _)
  (compile-java _)
  (compile-clj _)
  (b/write-pom {:class-dir class-dir
                :lib lib
                :version version
                :basis basis
                :src-dirs ["src/main/clojure"]})
  (b/jar {:class-dir class-dir
          :jar-file jar-file}))

(defn install [_]
  (jar _)
  (println "Installing to local mvn repo...")
  (b/install {:basis basis
              :lib lib
              :version version
              :jar-file jar-file
              :class-dir class-dir}))
