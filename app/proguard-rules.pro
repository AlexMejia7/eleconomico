# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# -----------------------------------------
# Reglas mínimas para librerías usadas
# -----------------------------------------

# Gson
-keep class com.google.gson.** { *; }

# Retrofit
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }

# OkHttp
-keep class okhttp3.** { *; }

# Firebase Messaging
-keep class com.google.firebase.messaging.** { *; }

# Glide
-keep class com.bumptech.glide.** { *; }

# Volley
-keep class com.android.volley.** { *; }

# CircleImageView
-keep class de.hdodenhof.circleimageview.** { *; }

# Mantener anotaciones importantes
-keepattributes *Annotation*

# Mantener miembros de clases serializables
-keepclassmembers class * implements java.io.Serializable { *; }
