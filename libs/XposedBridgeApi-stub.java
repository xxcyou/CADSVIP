package de.robv.android.xposed;

import java.lang.reflect.Member;

public interface IXposedHookLoadPackage {
    void handleLoadPackage(de.robv.android.xposed.callbacks.XC_LoadHook.LoadPackageParam lpparam);
}

final public class XposedBridge {
    public static void log(String text) {}
    public static void log(Throwable t) {}
}

final public class XposedHelpers {
    public static Class findClass(String className, ClassLoader classLoader) { return null; }
    public static XC_MethodHook.Unhook findAndHookMethod(Class clazz, String methodName, Object... parameterTypesAndCallback) { return null; }
    public static XC_MethodHook.Unhook findAndHookMethod(String className, ClassLoader classLoader, String methodName, Object... parameterTypesAndCallback) { return null; }
}

public abstract class XC_MethodHook {
    public static class Unhook {
        public void unhook() {}
    }
    public static class MethodHookParam {
        public Member method;
        public Object thisObject;
        public Object[] args;
        public Throwable throwable;
        public Object getResult() { return null; }
        public void setResult(Object result) {}
        public boolean hasThrowable() { return false; }
        public Throwable getThrowable() { return null; }
        public Object getResultOrThrowable() throws Throwable { return null; }
    }
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {}
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {}
}

public abstract class XC_MethodReplacement extends XC_MethodHook {
    protected abstract Object replaceHookedMethod(MethodHookParam param) throws Throwable;
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        Object result = replaceHookedMethod(param);
        param.setResult(result);
    }
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        Object result = replaceHookedMethod(param);
        param.setResult(result);
    }
}
