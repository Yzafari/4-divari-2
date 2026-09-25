const fs=require('fs'),path=require('path'),{execFileSync}=require('child_process');
const root=path.join(__dirname,'..');
const required=['index.html','manifest.json','service-worker.js','version.json','assets/js/app.js','assets/css/styles.css','android/app/build.gradle','android/app/src/main/AndroidManifest.xml','android/app/src/main/java/ir/khanehbekhaneh/app/MainActivity.java','ios/KhanehBeKhaneh/www/index.html','backend/app/main.py'];
for(const f of required) if(!fs.existsSync(path.join(root,f))) throw new Error('Missing '+f);
const v=JSON.parse(fs.readFileSync(path.join(root,'version.json'))); if(v.version!=='0.7.1') throw new Error('Version mismatch');
const androidRoot=fs.readdirSync(path.join(root,'android/app/src/main/assets')); if(androidRoot.some(x=>x!=='www')) throw new Error('Stale Android root assets detected: '+androidRoot.filter(x=>x!=='www').join(','));
const checks=[['assets/js/app.js','node','--check'],['service-worker.js','node','--check']];
for(const [rel,cmd,arg] of checks) execFileSync(cmd,[arg,path.join(root,rel)],{stdio:'ignore'});
console.log('VERIFY_OK 0.7.1');
