(ns nubank.proxy-plus
    (:require [clj-kondo.hooks-api :as hooks]))

(defn proxy+
      [{{:keys [children]} :node :as _context}]
      (binding [*out* *err*] (println 'proxy+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++))
      (if (hooks/vector-node? (second children))
          (let [[_proxy+ super-args-node & impl-body-nodes] children
                new-node (hooks/list-node (concat [(hooks/token-node 'clojure.core/proxy)
                                                   (hooks/vector-node [(hooks/token-node '_ClassNameSymbol)])
                                                   super-args-node]
                                                  impl-body-nodes))]
               {:node new-node})
          (let [[_proxy+ class-name-symbol super-args & impl-body] children
                new-node (hooks/list-node (concat [(hooks/token-node 'clojure.core/proxy)
                                                   (hooks/vector-node [class-name-symbol])
                                                   super-args]
                                                  impl-body))]
               {:node new-node})))

(comment
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
 )
