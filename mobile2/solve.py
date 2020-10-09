from arc4 import ARC4
import hashlib
key = []
enc_key = []
for i in range(5):
	enc = open(f"assets/key_{i}/ciphertext.js",'rb').read()
	arc4 = ARC4('TrendMicro')
	open(f"hint{i}.js","wb").write(arc4.encrypt(enc))
	key.append("")
	enc_key.append(open(f"assets/key_{i}/key_{i}.enc",'rb').read())

arc4 = ARC4('TrendMicro')
open("flag.js","wb").write((arc4.encrypt(open(f"assets/flag/ciphertext.js",'rb').read())))

arc4 = ARC4('TrendMicro')
key[0] = arc4.encrypt(enc_key[0])

arc4 = ARC4('81353343618')
key[1] = arc4.encrypt(enc_key[1])

arc4 = ARC4('8736364275')
key[2] = arc4.encrypt(enc_key[2])

arc4 = ARC4('body')
key[3] = arc4.encrypt(enc_key[3])

city = {"Amsterdam" : "2dce283e0e268a42d62e34467db274c9c38c358f" ,
"Aukland" : "794a4f25478d31bf87ece956fc7c95466a27c06a" ,
"Austin" : "5b06f1f08503b4e6346926667d318f0f9d7e9fd1" ,
"Bangalore" : "91f6fcb18482cc6fe303108f4283180b4fa20599" ,
"Bangkok" : "5b7046f25511b56046bee552337dcc9e581928ee" ,
"Beijing" : "7141d151711ec9f5c1a412229f6213dfa16e0c25" ,
"Brasillia" : "fa3f29a2614c4735b575567c246d48b1d8e99c53" ,
"Brisbane" : "c090e35e99e2efbb480893607dd581a3fca8c91a" ,
"Bryanston" : "8580fb692d33e38bbc54bba580db3d5dad3e4f69" ,
"Cairo" : "2022c1fddd7df3a85fc01618a0a9dbd1e35d8e97" ,
"Canberra" : "bd2af89bd428f2a8817a5df5f7a977a3e054016c" ,
"Chicago" : "34971b8fb11caeb1c1dca94916912471fc143971" ,
"Copenhagen" : "3f7c839db34cfeebcefe96d90a0e37e02c8e52c6" ,
"Cork" : "c3bbe02341f2c9e0b6b5d1ab9277bd670068f0ca" ,
"Doha" : "a3f3b9397b6273607de35379930960e995e3e7bc" ,
"Dubai" : "91310be76f85f63de666ca9a85c009b4c0e9cabd" ,
"Espoo" : "ab3c3286e066c957d8311487ac22320294a86c8d" ,
"Fukuoka" : "9c4fea060c6a71539468310fa9162be8ef3229ff" ,
"Guangzhou" : "95abe3ce47df71507238014dcc84c5de3ca2d34e" ,
"Hanoi" : "3ab7bfccf211b8616b3605b73f2729f1d9330677" ,
"Hsinchu" : "2217d677de13e00a4d5999cd7eb85e0426f96006" ,
"Irving" : "aee82ccc0C1998582109e72fa65e61f99d1884db" ,
"Istanbul" : "f576d38716b752f5457ecf2833a626d9523e975f" ,
"Jakarta" : "1d70ec60cc57dfc3ec4a1fe8fc862ad27ecb6723" ,
"Jersey City" : "27c0289bde2602ca1f9a93a1e0cf8a5c1985a8ab" ,
"Kuala Lumpur" : "616f7c5636114a755a70f107f3536c5a483ea75f" ,
"Lausanne" : "fb15d1e5d748ce024a944f59f9bf651919032bd1" ,
"London" : "4c57f0c88d9844630327623633ce269cf826ab99" ,
"Luxembourg" : "5076721c4060feeb69bd2c3dd9bdce115d5c62f3" ,
"Madrid" : "7a2191964a3d7521269f0068afae6b6116bac15d" ,
"Manila" : "9dbdcd77fca85fd2bbf6fa7177ac5db045e4cfa2" ,
"Mechelen" : "101e10d81a789a15a561898b313c465955543984" ,
"Melbourne" : "5fe4b6c33657d1f551a33c09dff1bb59fba99577" ,
"Mexico City" : "21334479ce4b504ded1d5a731540587a8022e13b" ,
"Milan" : "5380bcebfb8a22dd30d3999731c61981051f0632" ,
"Minneapolis" : "4125a0dc289d6596438e89e73adabd1809afee2b" ,
"Moscow" : "d18a788a440ad02e3f8bb9bece0ff541ee05f885" ,
"Mumbai" : "5a13ea4a93200ae4f2a818d2a6c31cc4792bb081" ,
"Munich" : "4bb73f4817cac7549995b34436d424f8d8b0f1b0" ,
"Nagoya" : "4c540bf720d77541456894424d17cdc6f629b9a4" ,
"Nanjing" : "16c8ceee4c64780f7b9f6c745e0bb25a5ba5a8e5" ,
"New Delhi" : "65af62e74232ff0ede583dc706132430380f3d19" ,
"Osaka" : "bd88cc6c2baf90656affa4162b5346eee01cc4e7" ,
"Oslo" : "bcc48172e05cafa61c2a2e8f12de220fdb1385b3" ,
"Ottawa" : "4df0179dda01538c9eedcafB8789c0805e4fd0c4" ,
"Paris" : "22390ad11c32faec43fc61555b53607660b3c185" ,
"Pasadena" : "de89ddfc83cde87b0adcc50a57e0d29547ad92fd" ,
"Perth" : "7bc1657d0a20068df4808f04aa6b9f02704e4eab" ,
"Porto Alegre" : "063df601a80d4bbb5897b1a4c6506d0ba4841616" ,
"Prague" : "f1ef175756e0f637f1fb8ae47f65517d0601549a" ,
"Reston" : "374c3fcb2b533e03e2eeca6b30241d6db162cb39" ,
"Rio de Janeiro":"03808d90fc91d72309b6dffaddfc90dcd0e568ec" ,
"Riyadh" : "a66046bc43e3bd818876b962b50f7692805d72e1" ,
"Rome" : "da7705caa15608fea217326726ec6ed97a9c6c9d" ,
"Roseville" : "1782b18dfd42762833c90d67d89d567697f43d1b" ,
"Sao Paulo" : "56ee1a445f4c36d9596e2c7e99153287b4ca4d87" ,
"San Jose" : "96b3008ff48379b1309bec3920b1b4e06adac397" ,
"Seattle" : "26bcaeae2550ec052e88d7aba77190b376580954" ,
"Seoul" : "59f054fa5a2164fb3ae235dd40b71eea61fc005f" ,
"Shanghai" : "b99463d58a5c8372e6adbdca867428961641cb51" ,
"Singapore" : "20c0b7bdab70ca2cc9c844a0d74a3af0bbf41c3e" ,
"Stockholm" : "c62ff645db3e4e4627445eafdadf5e5683415895" ,
"Sydney" : "ef4355f04bdf02ca9876dd2f8dc07cd7d1dd0fcc" ,
"Taipei" : "5ffacf6c2da447f60a21ec54dd9162d1ad396aa2" ,
"Tel Aviv" : "c642bc63208bd5c4c7a798727f70e9332c0b8e7a" ,
"Tokyo" : "963dd210cc93A4597038ceabe0fe93b258a362b9" ,
"Toronto" : "b7e31fe1791fdf0862019d14b0c6a15854ddb477" ,
"Wallisellen" : "35276e593bbc1992f37ac4c16d93daeed82dc79d" ,
"Wanchai" : "e56bcc6a067d31c2f5252eb5517f6bf9d5561300" ,
"Warsaw" : "706f6339e835d0512d5d873d62a3e08aba143f49" ,
"Wien" : "cffcdfd2a17a3af6a70b81e71c040c551ad41b39" }

for k,v in city.items():
    if hashlib.sha1(k.encode()).hexdigest() != v:
        key[4] = v + key[4]

arc4 = ARC4(key[4])
key[4] = arc4.encrypt(enc_key[4])

flag = open("assets/flag/flag.enc","rb").read()
for i in range(5):
	arc4 = ARC4(key[i])
	flag = arc4.encrypt(flag)
print(flag)