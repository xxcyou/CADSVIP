package de.robv.android.xposed.callbacks;
public class XC_LoadHook {
    public static class LoadPackageParam {
        public String packageName;
        public ClassLoader classLoader;
        public Object appInfo;
        public boolean isFirstApplication;
    }
}
