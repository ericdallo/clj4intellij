(ns com.github.ericdallo.clj4intellij.action
  (:require
   [com.rpl.proxy-plus :refer [proxy+]])
  (:import
   [com.intellij.openapi.actionSystem
    ActionManager
    Anchor
    Constraints
    DefaultActionGroup
    KeyboardShortcut]
   [com.intellij.openapi.keymap KeymapManager]
   [com.intellij.openapi.project DumbAwareAction]
   [javax.swing Icon]))

(set! *warn-on-reflection* true)

#_{:clj-kondo/ignore [:unused-binding]}
(defn register-action!
  "Dynamically register an action."
  [& {:keys [id title description icon use-shortcut-of keyboard-shortcut on-performed]}]
  (let [manager (ActionManager/getInstance)
        keymap-manager (KeymapManager/getInstance)
        action (proxy+
                [^String title ^String description ^Icon icon]
                DumbAwareAction
                 (actionPerformed [_ event] (on-performed event)))]
    (.registerAction manager id action)
    (when use-shortcut-of
      (.addShortcut (.getActiveKeymap keymap-manager)
                    id
                    (first (.getShortcuts (.getShortcutSet (.getAction manager use-shortcut-of))))))
    (when keyboard-shortcut
      (.addShortcut (.getActiveKeymap keymap-manager)
                    id
                    (KeyboardShortcut/fromString keyboard-shortcut)))
    action))

(defn ^:private ->constraint ^Constraints [anchor relative-to]
  (Constraints. (case anchor
                  :first Anchor/FIRST
                  :last Anchor/LAST
                  :before Anchor/BEFORE
                  :after Anchor/AFTER) relative-to))

(defn register-group!
  "Dynamically register an action group."
  [& {:keys [id popup ^String text icon children]}]
  (let [group (DefaultActionGroup.)
        manager (ActionManager/getInstance)]
    (when popup
      (.setPopup group popup))
    (when text
      (.setText (.getTemplatePresentation group) text))
    (when icon
      (.setIcon (.getTemplatePresentation group) icon))
    (.registerAction manager id group)
    (doseq [{:keys [type group-id anchor relative-to ref]} children]
      (case type
        :add-to-group (.add ^DefaultActionGroup (.getAction manager group-id) group (->constraint anchor relative-to))
        :reference (.add group (.getAction manager ref))
        :separator (.addSeparator group)))
    group))
