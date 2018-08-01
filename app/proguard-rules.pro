# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Daniel\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

  -keep class com.google.android.gms.ads.** { *; }
  -keep class com.google.android.gms.common.GooglePlayServicesUtil { *; }

  -keepattributes Signature

  -keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
  }
  -keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
  }
  -keepnames @com.google.android.gms.common.annotation.KeepName class *
  -keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
  }
  -keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
  }

  -dontwarn com.facebook.**
  -dontwarn com.jirbo.adcolony.**
  -dontwarn com.vungle.**
  -dontwarn com.startapp.**
  -dontwarn com.yandex.**
  -dontwarn com.inmobi.**
  -dontwarn com.chartboost.**
  -dontwarn com.avocarrot.**
  -keep class com.flurry.** { *; }
  -dontwarn com.flurry.**
  -keepattributes *Annotation*,EnclosingMethod,Signature
  -keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
  }
  -dontwarn net.pubnative.**
  -dontwarn org.droidparts.**
  -dontwarn java.lang.management.**
  -dontwarn android.test.**
  -keep class android.support.v4.app.Fragment { *; }
  -keep class android.support.v4.app.FragmentActivity { *; }
  -keep class android.support.v4.app.FragmentManager { *; }
  -keep class android.support.v4.app.FragmentTransaction { *; }
  -keep class android.support.v4.content.LocalBroadcastManager { *; }
  -keep class android.support.v4.util.LruCache { *; }
  -keep class android.support.v4.view.PagerAdapter { *; }
  -keep class android.support.v4.view.ViewPager { *; }

  -keep class android.support.v7.widget.RecyclerView { *; }
  -keep class android.support.v7.widget.LinearLayoutManager { *; }
  -keep class com.google.android.gms.common.** { *; }
