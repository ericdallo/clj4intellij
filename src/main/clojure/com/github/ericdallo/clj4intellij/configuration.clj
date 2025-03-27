(ns com.github.ericdallo.clj4intellij.configuration
  (:import
   [com.intellij.execution ProgramRunnerUtil]
   [com.intellij.execution.executors DefaultRunExecutor]))

(defn execute-configuration
  "API for ProgramRunnerUtil/executeConfiguration
   
   ref: https://github.com/JetBrains/intellij-community/blob/2766d0bf1cec76c0478244f6ad5309af527c245e/platform/execution-impl/src/com/intellij/execution/ProgramRunnerUtil.java#L46"
  [configuration-instance]
  (ProgramRunnerUtil/executeConfiguration
   configuration-instance
   (DefaultRunExecutor/getRunExecutorInstance)))
