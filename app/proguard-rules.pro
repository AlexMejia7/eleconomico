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

# ===============================
# Librerías externas
# ===============================

# ===============================
# Librerías externas
# ===============================

# Gson
-keep class com.google.gson.** { *; }

# Retrofit
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }

# OkHttp
-keep class okhttp3.** { *; }

# Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.firebase.messaging.** { *; }

# Glide
-keep class com.bumptech.glide.** { *; }
-keep interface com.bumptech.glide.** { *; }
-keep enum com.bumptech.glide.** { *; }

# Volley
-keep class com.android.volley.** { *; }

# CircleImageView
-keep class de.hdodenhof.circleimageview.** { *; }

# ===============================
# Tu proyecto
# ===============================

# Mantener todos los modelos para Gson y Retrofit (ajusta el paquete si es diferente)
-keep class com.example.eleconomico.models.** { *; }

# Mantener todas las clases usadas por reflexión en tu app
-keep class com.example.eleconomico.** { *; }

# ===============================
# General
# ===============================

# Mantener anotaciones importantes
-keepattributes *Annotation*

# Mantener miembros de clases serializables
-keepclassmembers class * implements java.io.Serializable { *; }

# Evitar advertencias MultiDex
-dontwarn androidx.multidex.**
