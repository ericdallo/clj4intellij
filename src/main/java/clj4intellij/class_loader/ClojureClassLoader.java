package clj4intellij.class_loader;

public class ClojureClassLoader {
    static {
        bind();
    }
    public static void bind() {
        Thread.currentThread().setContextClassLoader(ClojureClassLoader.class.getClassLoader());
    }
}
