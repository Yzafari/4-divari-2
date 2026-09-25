# =============================================================================
# Dockerfile — «۴ دیواری» (نسخه فعلی: صرفاً Frontend استاتیک)
#
# چون این پروژه هنوز Backend واقعی ندارد، این Dockerfile فقط فایل‌های
# استاتیک (index.html + assets + manifest + service-worker) را با Nginx
# سرو می‌کند. وقتی Backend اضافه شد، یک Dockerfile/سرویس جدا برای آن
# در docker-compose.yml اضافه کنید (بخش commented‌شده را ببینید).
# =============================================================================

FROM nginx:1.27-alpine

# حذف کانفیگ پیش‌فرض و جایگزینی با کانفیگ ساده SPA-friendly
COPY nginx.conf /etc/nginx/conf.d/default.conf

# کپی فایل‌های استاتیک پروژه
COPY index.html /usr/share/nginx/html/index.html
COPY manifest.json /usr/share/nginx/html/manifest.json
COPY service-worker.js /usr/share/nginx/html/service-worker.js
COPY assets /usr/share/nginx/html/assets

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
