# Android build

این پوشه یک پوسته Android برای نسخه 0.5.0 پروژه است. محتوای PWA در `app/src/main/assets` قرار گرفته و با WebView اجرا می‌شود.

- `targetSdk = 36`
- `minSdk = 23`
- applicationId: `ir.khanehbekhaneh.app`
- خروجی آزمایشی: `app-debug.apk`

برای ساخت APK در محیط توسعه، Android SDK و Gradle لازم است. همچنین فایل GitHub Actions در `.github/workflows/android-apk.yml` می‌تواند روی GitHub یک APK آزمایشی تولید کند.

این APK هنوز نسخه انتشار نهایی فروشگاه نیست؛ برای انتشار واقعی باید امضای release، سیاست‌های فروشگاه، backend، دامنه HTTPS و در صورت استفاده از TWA، Digital Asset Links تکمیل شوند.
