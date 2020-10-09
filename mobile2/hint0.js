function entrypoint(key, ciphertext_bytes) {
   Log(path_ciphertext + ":entrypoint()");

    titleView.setText("Key Zero Hints");
    textView.setText(
    " First off -- good luck! As a reward for playing, Key Zero has been decrypted for you.\n\nNow for the hints:\n\n" +
    "1. The 'Flag' is stored in this APK file. The flag is encrypted. When decrypted, it contains a string matching the regular expression '^TMCTF\{[A-Za-z0-9]*\}$'" + "\n\n" +
    "2. There are five 'Flag Keys.' All five Flag Keys are used to decrypt the Flag. Keys match /^KEY[0-4]-.[0-9]+/" + "\n\n" +
    "3. To decrypt the five Flag Keys you must solve five levels." + "\n\n" +
    "4. Each level has its own hint, located in the following files. The hints are encrypted. The hints for Key Zero were decrypted for you. You must figure out the other four:" + "\n" +
    "      key_0/ciphertext.js (already decrypted)" + "\n" +
    "      key_1/ciphertext.js" + "\n" +
    "      key_2/ciphertext.js" + "\n" +
    "      key_3/ciphertext.js" + "\n" +
    "      key_4/ciphertext.js" + "\n" +
    "\n" +
    /* the above ciphertext.js files are ALL encrypted using the same password */
    "5. The file assets/_preload/code.js is included before every executed javascript file." + "\n\n" +
    "6. There are a number of ways to decompile the APK file. You should do that." + "\n\n" +
    "7. Android intents can unlock many of the hints and keys.\n\n" +
    "8. Use the Android Studio Android Emulator to run the challenge. It's the simplest way." + "\n\n\n\n\n\n\n" +
    "https://www.trendmicro.com/en_us/contact.html"
    );

    return(true);
}

function verify_unlocked() {
    return(true);
}
