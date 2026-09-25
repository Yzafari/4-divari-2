const fs = require('fs');
const path = require('path');
const ROOT = path.join(__dirname, '..');
const DIST = path.join(ROOT, 'dist');
const WEB_ITEMS = ['index.html','manifest.json','service-worker.js','version.json','assets'];
function copyRecursive(src,dst){const st=fs.statSync(src); if(st.isDirectory()){fs.mkdirSync(dst,{recursive:true}); for(const n of fs.readdirSync(src)) copyRecursive(path.join(src,n),path.join(dst,n));} else {fs.mkdirSync(path.dirname(dst),{recursive:true}); fs.copyFileSync(src,dst);}}
if(fs.existsSync(DIST)) fs.rmSync(DIST,{recursive:true,force:true});
for(const item of WEB_ITEMS) copyRecursive(path.join(ROOT,item),path.join(DIST,item));
const androidAssets=path.join(ROOT,'android/app/src/main/assets');
if(fs.existsSync(androidAssets)) fs.rmSync(androidAssets,{recursive:true,force:true});
const androidWWW=path.join(androidAssets,'www');
fs.mkdirSync(androidWWW,{recursive:true});
for(const item of WEB_ITEMS) copyRecursive(path.join(ROOT,item),path.join(androidWWW,item));
const iosWWW=path.join(ROOT,'ios/KhanehBeKhaneh/www');
if(fs.existsSync(iosWWW)) fs.rmSync(iosWWW,{recursive:true,force:true});
fs.mkdirSync(iosWWW,{recursive:true});
for(const item of WEB_ITEMS) copyRecursive(path.join(ROOT,item),path.join(iosWWW,item));
console.log('BUILD_SYNC_OK 0.7.1');
