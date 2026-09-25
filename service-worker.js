const CACHE_NAME='khb-app-shell-v0.6.2';
const APP_SHELL=['./','./index.html','./assets/css/styles.css','./assets/js/app.js','./manifest.json','./version.json','./assets/icons/icon-192.png','./assets/icons/icon-512.png','./assets/icons/logo.svg'];
self.addEventListener('install',event=>{event.waitUntil(caches.open(CACHE_NAME).then(c=>c.addAll(APP_SHELL)));self.skipWaiting();});
self.addEventListener('activate',event=>{event.waitUntil(caches.keys().then(keys=>Promise.all(keys.filter(k=>k!==CACHE_NAME).map(k=>caches.delete(k)))));self.clients.claim();});
self.addEventListener('fetch',event=>{const req=event.request;if(req.method!=='GET')return;const url=new URL(req.url);if(url.origin!==self.location.origin)return;
  const dynamic=req.destination==='document'||url.pathname.endsWith('.js')||url.pathname.endsWith('.css')||url.pathname.endsWith('version.json');
  if(dynamic){event.respondWith(fetch(req,{cache:'no-store'}).then(res=>{const cp=res.clone();caches.open(CACHE_NAME).then(c=>c.put(req,cp));return res;}).catch(()=>caches.match(req).then(r=>r||caches.match('./index.html'))));}
  else{event.respondWith(caches.match(req).then(c=>c||fetch(req).then(res=>{const cp=res.clone();caches.open(CACHE_NAME).then(cache=>cache.put(req,cp));return res;})));}
});
self.addEventListener('push',event=>{let data={title:'۴ دیواری',body:'یک اعلان جدید دارید.',url:'/'};try{data=event.data.json()}catch(e){try{data.body=event.data.text()}catch(_){}}
event.waitUntil(self.registration.showNotification(data.title,{body:data.body,icon:'/assets/icons/icon-192.png',badge:'/assets/icons/icon-192.png',data:{url:data.url||'/'}}));});
self.addEventListener('notificationclick',event=>{event.notification.close();event.waitUntil(clients.matchAll({type:'window',includeUncontrolled:true}).then(cs=>{const u=event.notification.data?.url||'/';for(const c of cs){if('focus' in c){c.focus();c.navigate?.(u);return;}}return clients.openWindow(u);}));});
