package de.robv.android.xposed;
import de.robv.android.xposed.callbacks.XC_LoadHook;
public interface IXposedHookLoadPackage {
    void handleLoadPackage(XC_LoadHook.LoadPackageParam lpparam);
}
