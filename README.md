# ۴ دیواری — نسخه بازسازی‌شده 0.6.2

این بسته شامل Web/PWA، پوسته Android، پروژه پایه iOS و Backend پایتون/FastAPI است.

## وضعیت صداقت فنی

این نسخه از آخرین سورس کامل قابل‌بازیابی در محیط فعلی (`v0.6.2`) بازسازی شده و به `0.6.2` ارتقا داده شده است. فایل فیزیکی نسخه‌ای که در مرحله قبلی 0.6.2 نامیده شده بود در فضای فایل فعلی موجود نبود؛ بنابراین ادعای «همان فایل قبلی» نمی‌کنیم.

تست‌شده در این محیط:
- JavaScript syntax check
- Python compile
- FastAPI smoke test
- OTP/JWT authentication
- object-level authorization / IDOR guard
- public listing access
- operator approve/reject
- frontend → Android/iOS asset synchronization

تست‌نشده در این محیط:
- build واقعی APK (Android SDK/Gradle محلی نصب نیست)
- build/sign واقعی iOS (macOS/Xcode در دسترس نیست)
- ارسال SMS واقعی
- Push واقعی FCM/APNs
- PostgreSQL production
- پرداخت، KATEB و identity API رسمی
- تست نفوذ مستقل و load test

بنابراین این بسته «قابل توسعه و قابل Build» است، نه یک محصول تولیدی که همه سرویس‌های بیرونی آن فعال شده‌اند.

## ساختار

- `index.html`, `assets/`, `manifest.json`, `service-worker.js`: Web/PWA
- `android/`: Android WebView wrapper + BuildConfig API base
- `ios/`: Xcode project پایه + WKWebView
- `backend/`: Python/FastAPI + SQLite development backend
- `scripts/build.js`: build و همگام‌سازی Web Core با Android/iOS
- `scripts/verify.js`: بررسی ساختار و syntax
- `.github/workflows/android-apk.yml`: ساخت APK با GitHub Actions

## Web

```bash
node scripts/build.js
node scripts/verify.js
```

برای اجرای محلی:

```bash
npx http-server . -p 8080 -c-1
```

## Backend

```bash
python3 -m venv .venv
source .venv/bin/activate
pip install -r backend/requirements.txt
PYTHONPATH=backend python backend/run.py
```

Health: `http://127.0.0.1:8000/api/health`

برای production حتماً `KHB_JWT_SECRET` را با یک secret حداقل 32 بایتی واقعی تنظیم کنید.

## Android

ابتدا:

```bash
node scripts/build.js
node scripts/verify.js
```

سپس در محیط دارای Android SDK/Gradle:

```bash
cd android
gradle --no-daemon assembleDebug
```

یا GitHub Actions را اجرا کنید. برای Release، مقدار واقعی HTTPS را به شکل زیر بدهید:

```bash
gradle -PKHB_API_BASE=https://api.example.ir assembleRelease
```

**APK واقعی داخل این بسته ادعا نشده است** چون در این محیط Android SDK/Gradle قابل اجرای واقعی نبود.

## iOS

`ios/KhanehBeKhaneh.xcodeproj` را روی macOS با Xcode باز کنید، Team/Signing را انتخاب کنید و Web Core داخل Target قرار دارد. build واقعی و signing باید روی macOS انجام شود.

## Python package

کد Python به‌صورت بسته جداگانه نیز تحویل داده شده است.
