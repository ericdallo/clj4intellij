(ns nubank.proxy-plus
  (:require [clj-kondo.hooks-api :as hooks]))

(defn proxy+
  "Expands a proxy+ form into a reify form with a declared proxy name.

   Input:
     (proxy+
       [^String title ^String description ^Icon icon]
       DumbAwareAction
        (actionPerformed [_ event] (on-performed event)))
   Output:
     (do
       (clojure.core/declare proxy_plus14738)
       (clojure.core/let [_ [title description icon]]
         (clojure.core/reify
           DumbAwareAction
           (actionPerformed [_ event] (on-performed event)))))
   "
  [{{:keys [children]} :node :as _context}]
  (let [[_proxy+ & args] children
        [proxy-name-sym super-args impls]
        (if (symbol? (hooks/sexpr (first args)))
          [(first args)
           (first (rest args))
           (rest (rest args))]
          [(gensym "proxy_plus")
           (first args)
           (rest args)])
        new-node (hooks/list-node
                  [(hooks/token-node 'do)
                   (hooks/list-node
                    [(hooks/token-node 'clojure.core/declare)
                     proxy-name-sym])
                   (hooks/list-node
                    [(hooks/token-node 'clojure.core/let)
                     (hooks/vector-node [(hooks/token-node '_)
                                         super-args])
                     (hooks/list-node
                      (concat [(hooks/token-node 'clojure.core/reify)]
                              impls))])])]
    {:node new-node
     :defined-by 'com.rpl.proxy-plus/proxy+}))

(comment
  ;; (require '[clojure.repl.deps :as repl])
  ;; (repl/add-lib 'clj-kondo/clj-kondo {:mvn/version "2025.01.16"})
  ;; I'd love if this worked, but it's returning:
  ;; ; Syntax error (IllegalArgumentException) compiling fn* at (clj_kondo/impl/analysis/java.clj:156:1).
  ;; ; Error - no matches found for static method ASM9 in class org.objectweb.asm.Opcodes
  ;; So, you need to add clj-kondo to the deps.edn file in order to evaluate
  ;; the following code:
  (require '[clj-kondo.hooks-api :as hooks])

  (->> "(proxy+
         [^String title ^String description ^Icon icon]
         DumbAwareAction
          (actionPerformed [_ event] (on-performed event)))"
       hooks/parse-string
       (assoc {} :node)
       proxy+
       :node
       str)
  ;; => "(do (clojure.core/declare proxy_plus14738) (clojure.core/let [_ [title description icon]] (clojure.core/reify DumbAwareAction (actionPerformed [_ event] (on-performed event)))))"
  )
