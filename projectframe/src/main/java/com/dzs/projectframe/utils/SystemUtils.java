package com.dzs.projectframe.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Parcelable;
import android.telephony.TelephonyManager;

import java.io.File;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.List;
import java.util.UUID;


/**
 * 系统工具类
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @Date 2015-12-23 上午9:54:18
 */
public class SystemUtils {

    public static final int NETWORK_UNKNOWN = 0;
    public static final int MOBILE_2G = 1;
    public static final int MOBILE_3G = 2;
    public static final int MOBILE_4G = 3;
    public static final int WIFI = 5;
    public static final int NoNetConnection = 6;

    /**
     * 获取手机IEMI码
     *
     * @param cxt Context
     * @return String
     */
    public static String getPhoneIMEI(Context cxt) {
        TelephonyManager tm = (TelephonyManager) cxt.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获取手机系统SDK版本
     *
     * @return int
     */
    public static int getSDKVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机系统版本
     *
     * @return String
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取MAC地址
     *
     * @return String
     */
    public static String getMac(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * 获取设备的GUID
     *
     * @return String
     */
    public static String getGUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取手机设备信息
     *
     * @return StringBuilder
     */
    @SuppressLint("NewApi")
    public static String getPhoneDriverInfo() {
        return "产品名称: " + Build.PRODUCT
                + ("\nCPU型号: ") + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? Build.SUPPORTED_ABIS : Build.CPU_ABI)
                + ("\n标签: ") + (Build.TAGS)
                + ("\n手机型号: ") + (Build.MODEL)
                + ("\nSDK版本: ") + (Build.VERSION.SDK_INT)
                + ("\n系统版本: ") + (Build.VERSION.RELEASE)
                + ("\n设备驱动: ") + (Build.DEVICE)
                + ("\n显示: ") + (Build.DISPLAY)
                + ("\n品牌: ") + (Build.BRAND)
                + ("\n主板: ") + (Build.BOARD)
                + ("\n指纹: ") + (Build.FINGERPRINT)
                + ("\nID: ") + (Build.ID)
                + ("\n制造商: ") + (Build.MANUFACTURER)
                + ("\n用户组: ") + (Build.USER);
    }

    /**
     * 获取手机卡的一些信息
     *
     * @param context context
     * @return String
     */
    @SuppressLint("HardwareIds")
    public static String getPhoneInfo(Context context) {
        TelephonyManager Tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return String.format("设备id: %s", Tm.getDeviceId())
                + ("\n用户id: ") + (Tm.getSubscriberId())// 没插SIM取不到
                + ("\nICCID: ") + (Tm.getSimSerialNumber())// 没插SIM取不到
                + ("\nMSISDN: ") + (Tm.getLine1Number());// 有的SIM卡取不到
    }

    /**
     * 检查网络是否连接
     *
     * @param context 上下文
     * @return boolean
     */
    public static boolean checkNetConttent(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != cm) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 返回当前是否为2/3/4G移动网络
     *
     * @param context context
     * @return String
     */
    public static String getNetWorkTypeName(Context context) {
        String typeName = null;
        switch (getNetworkType(context)) {
            case MOBILE_2G:
                typeName = "2G移动网络";
                break;
            case MOBILE_3G:
                typeName = "3G移动网络";
                break;
            case MOBILE_4G:
                typeName = "4G移动网络";
                break;

            default:
                break;
        }
        return typeName;
    }

    /**
     * 获取当前网络类型
     *
     * @param context Context
     * @return int
     */
    public static int getNetworkType(Context context) {
        int strNetworkType = 0;
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = SystemUtils.WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                int networkType = networkInfo.getSubtype();
                try {
                    Class<?> threadClazz = Class.forName("android.telephony.TelephonyManager");
                    Method method = threadClazz.getMethod("getNetworkClass", int.class);
                    int invoke = (Integer) method.invoke(null, networkType);
                    switch (invoke) {
                        case 1:
                            strNetworkType = SystemUtils.MOBILE_2G;
                            break;
                        case 2:
                            strNetworkType = SystemUtils.MOBILE_3G;
                            break;
                        case 3:
                            strNetworkType = SystemUtils.MOBILE_4G;
                            break;
                        default:
                            strNetworkType = SystemUtils.NETWORK_UNKNOWN;
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            strNetworkType = SystemUtils.NoNetConnection;
        }
        return strNetworkType;
    }

    /**
     * 打开网络设置
     *
     * @param activity activity
     */
    public static void openNetSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 回到主界面，让程序后台运行
     *
     * @param context Context
     */
    public static void goHome(Context context) {
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(mHomeIntent);
    }

    /**
     * 判断当前应用程序是否在后台运行
     *
     * @param context Context
     * @return boolean
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    // 后台运行
                    return true;
                } else {
                    // 前台运行
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 判断手机是否处理睡眠
     *
     * @param context Context
     * @return boolean
     */
    public static boolean isSleeping(Context context) {
        KeyguardManager kgMgr = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();
        return isSleeping;
    }

    /**
     * 安装apk
     *
     * @param context Context
     * @param file    File文件
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("application/vnd.android.package-archive");
        intent.setData(Uri.fromFile(file));
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 获取安装包的信息
     *
     * @param context Context
     * @return PackageInfo
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 获取应用签名
     *
     * @param context Context
     * @param pkgName 包名
     * @return String
     */
    public static String getSign(Context context, String pkgName) {
        try {
            PackageInfo pis = context.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_SIGNATURES);
            return hexdigest(pis.signatures[0].toByteArray());
        } catch (NameNotFoundException e) {
            throw new RuntimeException(SystemUtils.class.getName() + "the " + pkgName + "'s application not found");
        }
    }

    /**
     * 将签名字符串转换成需要的32位签名
     *
     * @return String
     */
    private static String hexdigest(byte[] paramArrayOfByte) {
        final char[] hexDigits = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            for (int i = 0, j = 0; ; i++, j++) {
                if (i >= 16) {
                    return new String(arrayOfChar);
                }
                int k = arrayOfByte[i];
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                arrayOfChar[++j] = hexDigits[(k & 0xF)];
            }
        } catch (Exception e) {
            LogUtils.exception(e);
        }
        return "";
    }

    /**
     * 创建快捷方式
     *
     * @param context 当前Context
     * @param icon    图标
     * @param title   标题
     * @param cls     启动的activity
     */
    public static void createDeskShortCut(Context context, int icon, String title, Class<?> cls) {
        // 创建快捷方式的Intent
        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重复创建
        shortcutIntent.putExtra("duplicate", false);
        // 需要现实的名称
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        // 快捷图片
        Parcelable ico = Intent.ShortcutIconResource.fromContext(context.getApplicationContext(), icon);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, ico);
        Intent intent = new Intent(context, cls);
        // 下面两个属性是为了当应用程序卸载时桌面上的快捷方式会删除
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        // 点击快捷图片，运行的程序主入口
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        // 发送广播。OK
        context.sendBroadcast(shortcutIntent);
    }

    /**
     * 获取设备的可用内存大小
     *
     * @param context Context
     * @return int
     */
    public static int getDeviceUsableMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        // 返回当前系统的可用内存
        return (int) (mi.availMem / (1024 * 1024));
    }

    /**
     * 清理后台进程和服务
     *
     * @param context Context
     * @return int
     */
    public static int gc(Context context) {
        long i = getDeviceUsableMemory(context);
        int count = 0; // 清理掉的进程数
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取正在运行的service列表
        List<RunningServiceInfo> serviceList = am.getRunningServices(100);
        if (serviceList != null)
            for (RunningServiceInfo service : serviceList) {
                if (service.pid == android.os.Process.myPid())
                    continue;
                try {
                    android.os.Process.killProcess(service.pid);
                    count++;
                } catch (Exception e) {
                    e.getStackTrace();
                    continue;
                }
            }

        // 获取正在运行的进程列表
        List<RunningAppProcessInfo> processList = am.getRunningAppProcesses();
        if (processList != null)
            for (RunningAppProcessInfo process : processList) {
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程了
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程都是非可见进程，也就是在后台运行着
                if (process.importance > RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    // pkgList 得到该进程下运行的包名
                    String[] pkgList = process.pkgList;
                    for (String pkgName : pkgList) {
                        try {
                            am.killBackgroundProcesses(pkgName);
                            count++;
                        } catch (Exception e) { // 防止意外发生
                            LogUtils.exception(e);
                        }
                    }
                }
            }
        LogUtils.info("清理了" + (getDeviceUsableMemory(context) - i) + "M内存");
        return count;
    }

}
