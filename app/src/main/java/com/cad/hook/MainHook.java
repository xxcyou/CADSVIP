package com.cad.hook;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadHook;

public class MainHook implements IXposedHookLoadPackage {

    public static final String TAG = "CADSVIP";
    private static final String PKG = "com.stone.app";

    @Override
    public void handleLoadPackage(final XC_LoadHook.LoadPackageParam lp) {
        if (!PKG.equals(lp.packageName)) return;
        final ClassLoader cl = lp.classLoader;

        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class,
                new XC_MethodHook() {
                    @Override protected void afterHookedMethod(MethodHookParam p) {
                        final Context ctx = (Context) p.args[0];
                        new Thread(new Runnable() {
                            @Override public void run() {
                                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
                                doHook(cl, ctx);
                            }
                        }).start();
                    }
                });
    }

    private void doHook(ClassLoader cl, Context ctx) {
        List<String> names = enumClassNames(ctx);
        XposedBridge.log(TAG + " 枚举到 " + names.size() + " 个类");
        for (String n : names) {
            if (n.endsWith("AppSharedPreferences")) {
                XposedBridge.log(TAG + " 命中 AppSharedPreferences: " + n);
                hookAppShared(n, cl);
            } else if (n.endsWith("NewUserInfoModel")) {
                XposedBridge.log(TAG + " 命中 NewUserInfoModel: " + n);
                hookVipType(n, cl);
            }
        }
    }

    private void hookAppShared(String clsName, ClassLoader cl) {
        try {
            Class c = XposedHelpers.findClass(clsName, cl);
            XposedHelpers.findAndHookMethod(c, "getUserType",
                    new XC_MethodReplacement() {
                        @Override protected Object replaceHookedMethod(MethodHookParam p) { return 7; }
                    });
            String[] flags = {"isUserVip_Super", "isUserVip_High", "isUserVip_AD", "isUserVip_Company"};
            for (String f : flags) {
                try {
                    XposedHelpers.findAndHookMethod(c, f,
                            new XC_MethodReplacement() {
                                @Override protected Object replaceHookedMethod(MethodHookParam p) { return Boolean.TRUE; }
                            });
                    XposedBridge.log(TAG + " 已 hook " + f);
                } catch (Throwable ignored) {
                    XposedBridge.log(TAG + " " + f + " 无此方法");
                }
            }
            try {
                XposedHelpers.findAndHookMethod(c, "checkUserVIP_Pay",
                        new XC_MethodReplacement() {
                            @Override protected Object replaceHookedMethod(MethodHookParam p) { return Boolean.TRUE; }
                        });
            } catch (Throwable ignored) {}
        } catch (Throwable t) {
            XposedBridge.log(TAG + " hookAppShared 失败: " + t);
        }
    }

    private void hookVipType(String clsName, ClassLoader cl) {
        try {
            Class m = XposedHelpers.findClass(clsName, cl);
            XposedHelpers.findAndHookMethod(m, "getVipType",
                    new XC_MethodReplacement() {
                        @Override protected Object replaceHookedMethod(MethodHookParam p) { return 7; }
                    });
            XposedBridge.log(TAG + " getVipType 已锁定 7");
        } catch (Throwable t) {
            XposedBridge.log(TAG + " hookVipType 失败: " + t);
        }
    }

    private List<String> enumClassNames(Context ctx) {
        List<String> out = new ArrayList<>();
        try {
            String apkPath = ctx.getApplicationInfo().sourceDir;
            DexFile dex = new DexFile(apkPath);
            Enumeration<String> e = dex.entries();
            while (e.hasMoreElements()) out.add(e.nextElement());
        } catch (Throwable t) {
            XposedBridge.log(TAG + " DexFile 枚举失败: " + t);
        }
        return out;
    }
}
