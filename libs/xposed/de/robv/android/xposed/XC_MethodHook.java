package de.robv.android.xposed;
import java.lang.reflect.Member;
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
