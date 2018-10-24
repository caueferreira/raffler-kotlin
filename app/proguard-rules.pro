# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Debugging
-keepattributes LineNumberTable, SourceFile

# Common attributes to keep
-keepattributes Exceptions, InnerClasses, Signature, Deprecated, *Annotation*, EnclosingMethod

# Remove log calls
-assumenosideeffects class android.util.Log {
    public static *** d(...);
}

# Kotlin
-keep class kotlin.** { *; }
-dontnote kotlin.coroutines.jvm.internal.DebugMetadataKt**
-dontnote kotlin.internal.PlatformImplementationsKt
-dontnote kotlin.jvm.internal.Reflection

# Keep custom views
-keep class com.fibelatti.raffler.core.platform.customview.** { *; }

# Keep model classes (since they are all annotaded with @Entity we can use it to make it simpler)
-keep @androidx.room.Entity class * { *; }

# Keep custom errors
-keep class * extends java.lang.Exception
-keep class * extends java.lang.Throwable

# Support Library
-keep class * extends androidx.fragment.app.Fragment
-keep class * extends androidx.appcompat.app.AppCompatActivity
-keep class * extends android.app.Application
-keep class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep class com.google.android.material.** { *; }

# Gson
-dontnote sun.misc.Unsafe
-dontnote com.google.gson.**
