function entrypoint(key, ciphertext_bytes) {
   Log(path_ciphertext + ":entrypoint()");

    if(titleView) {
        titleView.setText("Key One Hints");
    } else {
        Log("TextView class is null")
    }
    if(textView) {
        textView.setText(
        "Good job figuring out the password to the Key One hints!\n\n" +
        "As you are aware, the password you used was '"+singleton.hintkey1+"'.\n\n" +
        "Here is a hint about decrypting flagkey1.enc:\n" +
        "To unlock Key 1, you must call Trend Micro\n"
        );
    } else {
        Log("TextView class is null")
    }

    return(true)
}

function verify_unlocked() {
    return(true);
}
