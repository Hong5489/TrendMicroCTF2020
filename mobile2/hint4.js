// These coordinates are considered the ''canonical'' GPS coordinates of the
// Trend Micro offices for purposes of this competition
// Please forgive me for converting some names (mostly Brazil) - it made things simpler
// for your humble CTF creator :)

//
var Locations = [
	{ location: "Amsterdam", latitude:52.3084375, longitude:4.9426875, hash: "2dce283e0e268a42d62e34467db274c9c38c358f" },
	{ location: "Aukland", latitude:-36.842294, longitude:174.756654, hash: "794a4f25478d31bf87ece956fc7c95466a27c06a" },
	{ location: "Austin", latitude:30.3981626, longitude:-97.7203696, hash: "5b06f1f08503b4e6346926667d318f0f9d7e9fd1" },
	{ location: "Bangalore", latitude:12.9735089, longitude:77.6164228, hash: "91f6fcb18482cc6fe303108f4283180b4fa20599" },
	{ location: "Bangkok", latitude:13.7458125, longitude:100.5381875, hash: "5b7046f25511b56046bee552337dcc9e581928ee" },
	{ location: "Beijing", latitude:39.907319, longitude:116.456865, hash: "7141d151711ec9f5c1a412229f6213dfa16e0c25" },
	{ location: "Brasillia", latitude:-15.79474195, longitude:-47.89338375, hash: "fa3f29a2614c4735b575567c246d48b1d8e99c53" },
	{ location: "Brisbane", latitude:-27.4657999, longitude:153.0309008, hash: "c090e35e99e2efbb480893607dd581a3fca8c91a" },
	{ location: "Bryanston", latitude:-26.0454822, longitude:28.0243021, hash: "8580fb692d33e38bbc54bba580db3d5dad3e4f69" },
	{ location: "Cairo", latitude:30.04399829999999, longitude:31.2388414, hash: "2022c1fddd7df3a85fc01618a0a9dbd1e35d8e97" },
	{ location: "Canberra", latitude:-35.31200499999999, longitude:149.133181, hash: "bd2af89bd428f2a8817a5df5f7a977a3e054016c" },
	{ location: "Chicago", latitude:41.8862429, longitude:-87.6395364, hash: "34971b8fb11caeb1c1dca94916912471fc143971" },
	{ location: "Copenhagen", latitude:55.6831875, longitude:12.4580625, hash: "3f7c839db34cfeebcefe96d90a0e37e02c8e52c6" },
	{ location: "Cork", latitude:51.88962230000001, longitude:-8.523946900000002, hash: "c3bbe02341f2c9e0b6b5d1ab9277bd670068f0ca" },
	{ location: "Doha", latitude:25.31334649999999, longitude:51.51805619999999, hash: "a3f3b9397b6273607de35379930960e995e3e7bc" },
	{ location: "Dubai", latitude:25.0779375, longitude:55.1531875, hash: "91310be76f85f63de666ca9a85c009b4c0e9cabd" },
	{ location: "Espoo", latitude:60.2095625, longitude:24.8274375, hash: "ab3c3286e066c957d8311487ac22320294a86c8d" },
	{ location: "Fukuoka", latitude:33.58725905, longitude:130.41618945, hash: "9c4fea060c6a71539468310fa9162be8ef3229ff" },
	{ location: "Guangzhou", latitude:23.1414375, longitude:113.3221875, hash: "95abe3ce47df71507238014dcc84c5de3ca2d34e" },
	{ location: "Hanoi", latitude:21.0147541, longitude:105.8131993, hash: "3ab7bfccf211b8616b3605b73f2729f1d9330677" },
	{ location: "Hsinchu", latitude:24.7729375, longitude:121.0146875, hash: "2217d677de13e00a4d5999cd7eb85e0426f96006" },
	{ location: "Irving", latitude:32.8655625, longitude:-96.9399375, hash: "aee82ccc0C1998582109e72fa65e61f99d1884db" },
	{ location: "Istanbul", latitude:41.1099209, longitude:29.0185938, hash: "f576d38716b752f5457ecf2833a626d9523e975f" },
	{ location: "Jakarta", latitude:-6.1928338, longitude:106.821679, hash: "1d70ec60cc57dfc3ec4a1fe8fc862ad27ecb6723" },
	{ location: "Jersey City", latitude:40.71607349999999, longitude:-74.0350651, hash: "27c0289bde2602ca1f9a93a1e0cf8a5c1985a8ab" },
	{ location: "Kuala Lumpur", latitude:3.160937499999999, longitude:101.7203125, hash: "616f7c5636114a755a70f107f3536c5a483ea75f" },
	{ location: "Lausanne", latitude:46.53680209999999, longitude:6.6179273, hash: "fb15d1e5d748ce024a944f59f9bf651919032bd1" },
	{ location: "London", latitude:51.5190543, longitude:-0.1815522, hash: "4c57f0c88d9844630327623633ce269cf826ab99" },
	{ location: "Luxembourg", latitude:49.6417279, longitude:6.0068514, hash: "5076721c4060feeb69bd2c3dd9bdce115d5c62f3" },
	{ location: "Madrid", latitude:40.4790625, longitude:-3.6865625, hash: "7a2191964a3d7521269f0068afae6b6116bac15d" },
	{ location: "Manila", latitude:14.5900625, longitude:121.0679375, hash: "9dbdcd77fca85fd2bbf6fa7177ac5db045e4cfa2" },
	{ location: "Mechelen", latitude:51.0551875, longitude:4.459437500000001, hash: "101e10d81a789a15a561898b313c465955543984" },
	{ location: "Melbourne", latitude:-37.85145850000001, longitude:144.9803303, hash: "5fe4b6c33657d1f551a33c09dff1bb59fba99577" },
	{ location: "Mexico City", latitude:19.3924348, longitude:-99.172387, hash: "21334479ce4b504ded1d5a731540587a8022e13b" },
	{ location: "Milan", latitude:45.52633519999999, longitude:9.235562999999999, hash: "5380bcebfb8a22dd30d3999731c61981051f0632" },
	{ location: "Minneapolis", latitude:44.86381249999999, longitude:-93.2968125, hash: "4125a0dc289d6596438e89e73adabd1809afee2b" },
	{ location: "Moscow", latitude:55.77236199999999, longitude:37.648364, hash: "d18a788a440ad02e3f8bb9bece0ff541ee05f885" },
	{ location: "Mumbai", latitude:19.0685625, longitude:72.86818749999999, hash: "5a13ea4a93200ae4f2a818d2a6c31cc4792bb081" },
	{ location: "Munich", latitude:48.25025749999999, longitude:11.6362268, hash: "4bb73f4817cac7549995b34436d424f8d8b0f1b0" },
	{ location: "Nagoya", latitude:35.1738012, longitude:136.9030867, hash: "4c540bf720d77541456894424d17cdc6f629b9a4" },
	{ location: "Nanjing", latitude:31.976268, longitude:118.788529, hash: "16c8ceee4c64780f7b9f6c745e0bb25a5ba5a8e5" },
	{ location: "New Delhi", latitude:28.5501875, longitude:77.2518125, hash: "65af62e74232ff0ede583dc706132430380f3d19" },
	{ location: "Osaka", latitude:34.7338125, longitude:135.4968125, hash: "bd88cc6c2baf90656affa4162b5346eee01cc4e7" },
	{ location: "Oslo", latitude:59.91848529999999, longitude:10.685877, hash: "bcc48172e05cafa61c2a2e8f12de220fdb1385b3" },
	{ location: "Ottawa", latitude:45.3440625, longitude:-75.9194375, hash: "4df0179dda01538c9eedcafB8789c0805e4fd0c4" },
	{ location: "Paris", latitude:48.88393749999999, longitude:2.1743125, hash: "22390ad11c32faec43fc61555b53607660b3c185" },
	{ location: "Pasadena", latitude:34.1433399, longitude:-118.1409318, hash: "de89ddfc83cde87b0adcc50a57e0d29547ad92fd" },
	{ location: "Perth", latitude:-31.9482542, longitude:115.8384514, hash: "7bc1657d0a20068df4808f04aa6b9f02704e4eab" },
	{ location: "Porto Alegre", latitude:-30.023786, longitude:-51.1828457, hash: "063df601a80d4bbb5897b1a4c6506d0ba4841616" },
	{ location: "Prague", latitude:50.0479375, longitude:14.4575625, hash: "f1ef175756e0f637f1fb8ae47f65517d0601549a" },
	{ location: "Reston", latitude:38.9589612, longitude:-77.3578354, hash: "374c3fcb2b533e03e2eeca6b30241d6db162cb39" },
	{ location: "Rio de Janeiro", latitude:-22.8973551, longitude:-43.1802782, hash: "03808d90fc91d72309b6dffaddfc90dcd0e568ec" },
	{ location: "Riyadh", latitude:24.8076875, longitude:46.7159375, hash: "a66046bc43e3bd818876b962b50f7692805d72e1" },
	{ location: "Rome", latitude:41.9037865, longitude:12.4766878, hash: "da7705caa15608fea217326726ec6ed97a9c6c9d" },
	{ location: "Roseville", latitude:38.7446886, longitude:-121.2323206, hash: "1782b18dfd42762833c90d67d89d567697f43d1b" },
	{ location: "Sao Paulo", latitude:-23.5888146, longitude:-46.6805769, hash: "56ee1a445f4c36d9596e2c7e99153287b4ca4d87" },
	{ location: "San Jose", latitude:37.31755810000001, longitude:-121.9485109, hash: "96b3008ff48379b1309bec3920b1b4e06adac397" },
	{ location: "Seattle", latitude:47.607148, longitude:-122.3359398, hash: "26bcaeae2550ec052e88d7aba77190b376580954" },
	{ location: "Seoul", latitude:37.5065625, longitude:127.0653125, hash: "59f054fa5a2164fb3ae235dd40b71eea61fc005f" },
	{ location: "Shanghai", latitude:31.2225625, longitude:121.4720625, hash: "b99463d58a5c8372e6adbdca867428961641cb51" },
	{ location: "Singapore", latitude:1.2946875, longitude:103.8596875, hash: "20c0b7bdab70ca2cc9c844a0d74a3af0bbf41c3e" },
	{ location: "Stockholm", latitude:59.40345269999999, longitude:17.9465391, hash: "c62ff645db3e4e4627445eafdadf5e5683415895" },
	{ location: "Sydney", latitude:-33.8409424, longitude:151.2095047, hash: "ef4355f04bdf02ca9876dd2f8dc07cd7d1dd0fcc" },
	{ location: "Taipei", latitude:25.0230625, longitude:121.5483125, hash: "5ffacf6c2da447f60a21ec54dd9162d1ad396aa2" },
	{ location: "Tel Aviv", latitude:32.0719375, longitude:34.7890625, hash: "c642bc63208bd5c4c7a798727f70e9332c0b8e7a" },
	{ location: "Tokyo", latitude:35.6866, longitude:139.6989, hash: "963dd210cc93A4597038ceabe0fe93b258a362b9" },
	{ location: "Toronto", latitude:43.6391257, longitude:-79.4200135, hash: "b7e31fe1791fdf0862019d14b0c6a15854ddb477" },
	{ location: "Wallisellen", latitude:47.4083321, longitude:8.602781999999998, hash: "35276e593bbc1992f37ac4c16d93daeed82dc79d" },
	{ location: "Wanchai", latitude:22.2801875, longitude:114.1716875, hash: "e56bcc6a067d31c2f5252eb5517f6bf9d5561300" },
	{ location: "Warsaw", latitude:52.2354354, longitude:20.9823552, hash: "706f6339e835d0512d5d873d62a3e08aba143f49" },
	{ location: "Wien", latitude:48.1684375, longitude:16.3463125, hash: "cffcdfd2a17a3af6a70b81e71c040c551ad41b39" },
];


function entrypoint(key, ciphertext_bytes) {
    Log(path_ciphertext + ":entrypoint()");

    var location_match = '';

    for(var index=0 ; index < Locations.length; index++) {
        location = Locations[index];
        var location_latitude = parseFloat(location.latitude);
        var location_longitude = parseFloat(location.longitude);

        var singleton_latitude = parseFloat(singleton.latitude);
        var singleton_longitude = parseFloat(singleton.longitude);

        // why actually calculate distance when you can just fake it ;)
        if(Math.abs(location_latitude - singleton_latitude) < 0.001 && Math.abs(location_longitude - singleton_longitude) < 0.001 ) {
            Log("Matched Location " + location.location);
            var SHA1 = new Hashes.SHA1;
            hash = SHA1.hex(location.location)
            if( hash == location.hash) {
                location_match = "Welcome to " + location.location;
            } else {
                location_match = "Welcome to " + location.location;
                singleton.push(location.hash);
            }
            break;
        } else {
            location_match = ""
        }
    }


    titleView.setText("Key Four Hints");
    textView.setText(
    "Visit " + /* all three of */ "the headquarters to unlock Key 4" + "\n\n" +
    location_match
    );

    return(true)
}

function verify_unlocked() {
    return(true);
}
