function entrypoint(key, ciphertext_bytes) {
   Log(path_ciphertext + ":entrypoint()");

    // This is key_2/ciphertext.js

    titleView.setText("Key Two Hints");
    textView.setText("To unlock KEY2, send the secret code.");

    enable_secret_code();

    return(true)
}

function verify_unlocked() {
    return(true);
}

function enable_secret_code() {

}