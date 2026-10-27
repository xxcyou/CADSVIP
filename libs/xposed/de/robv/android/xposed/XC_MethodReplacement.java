package de.robv.android.xposed;
public abstract class XC_MethodReplacement extends XC_MethodHook {
    protected abstract Object replaceHookedMethod(MethodHookParam param) throws Throwable;
    @Override protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        Object result = replaceHookedMethod(param);
        param.setResult(result);
    }
    @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        Object result = replaceHookedMethod(param);
        param.setResult(result);
    }
}
