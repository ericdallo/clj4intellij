(ns com.github.ericdallo.clj4intellij.test
  "Test utilities for clj4intellij"
  (:import
   [com.intellij.testFramework EdtTestUtil LightProjectDescriptor]
   [com.intellij.testFramework.fixtures CodeInsightTestFixture IdeaTestFixtureFactory]
   [com.intellij.util ThrowableRunnable]
   [com.intellij.util.ui UIUtil]))


(defn setup
  "Setup fixture factory, and return an instance of CodeInsightTestFixture
   
   ref: https://github.com/JetBrains/intellij-community/blob/2766d0bf1cec76c0478244f6ad5309af527c245e/platform/testFramework/src/com/intellij/testFramework/fixtures/CodeInsightTestFixture.java"
  ^CodeInsightTestFixture []
  (let [factory (IdeaTestFixtureFactory/getFixtureFactory)
        raw-fixture (-> factory
                        (.createLightFixtureBuilder LightProjectDescriptor/EMPTY_PROJECT_DESCRIPTOR (str *ns*))
                        (.getFixture))
        fixture (.createCodeInsightFixture factory raw-fixture)]
    (.setUp fixture)
    fixture))

(defn dispatch-all
  "API for `UIUtil/dispatchAllInvocationEvents`.
  
  ref:https://github.com/JetBrains/intellij-community/blob/2766d0bf1cec76c0478244f6ad5309af527c245e/platform/util/ui/src/com/intellij/util/ui/UIUtil.java#L1450"
  []
  (EdtTestUtil/runInEdtAndWait
   (reify ThrowableRunnable
     (run [_]
       (UIUtil/dispatchAllInvocationEvents)))))

(defn dispatch-all-until
  "Dispatch all events in the EDT until condition is met.
   Returns a promise which can be `deref` to await the the condition to be met.
   See `dispatch-all` for more information."
  [condition]
  (let [p (promise)]
    (future
      (loop []
        (if (condition)
          (deliver p true)
          (do
            (dispatch-all)
            (Thread/sleep 100)
            (recur)))))
    p))
