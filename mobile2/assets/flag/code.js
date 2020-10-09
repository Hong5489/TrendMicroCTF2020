function entrypoint() {
    if(! test_encryption()) {
        throw "Encryption engine is broken";
    }
    log_ciphertext_js();

    throw "Decryption Failure decrypting " + path_ciphertext;
}

function process(key, arr, bool) {

    if(!key) {
        throw "No password supplied";
    }

	var s = [], j = 0, x, res = [];
	for (var i = 0; i < 256; i++) {
		s[i] = i;
	}
	for (i = 0; i < 256; i++) {
		j = (j + s[i] + key.charCodeAt(i % key.length)) % 256;
		x = s[i];
		s[i] = s[j];
		s[j] = x;
	}
	i = 0;
	j = 0;
	for (var y = 0; y < arr.length; y++) {
		i = (i + 1) % 256;
		j = (j + s[i]) % 256;
		x = s[i];
		s[i] = s[j];
		s[j] = x;
		res.push(String.fromCharCode(arr[y] ^ s[(s[i] + s[j]) % 256]));
	}

	if(bool) {
	    return(bin2String(res));
	} else {
    	return(res);
    }
}

function bin2String(array) {
  var result = "";
  for (var i = 0; i < array.length; i++) {
        var byte = array[i] & 0xff;
        result += String.fromCharCode(byte);
  }
  return result;
}

function hexdump(buffer, blockSize) {
    blockSize = blockSize || 16;
    var lines = [" \n"];
    var hex = "0123456789ABCDEF";
    for (var b = 0; b < buffer.length; b += blockSize) {
        var block = buffer.slice(b, Math.min(b + blockSize, buffer.length));
        var addr = ("0000" + b.toString(16)).slice(-4);
        var codes = block.split('').map(function (ch) {
            var code = ch.charCodeAt(0);
            return " " + hex[(0xF0 & code) >> 4] + hex[0x0F & code];
        }).join("");
        codes += "   ".repeat(blockSize - block.length);
        var chars = block.replace(/[\x00-\x1F\x20]/g, '.');
        chars +=  " ".repeat(blockSize - block.length);
        lines.push(addr + " " + codes + "  " + chars);
    }
    return lines.join("\n") + "\n";
}

function Log(str) {
    if(android && android.util && android.util.log) {
        android.util.Log.d("HIPPO", str);
    }
}

function log_ciphertext_js() {
    Log("Ciphertext file " + path_ciphertext);
    Log("key=" + key);

    var hexdata = hexdump(bin2String(ciphertext_bytes));
    var index = 0;
    var end_index = 0;
    var log_length = 2000;
    var logstr=hexdata;

     while(logstr.length > 0) {
        last_newline = logstr.substring(0,log_length).lastIndexOf("\n");
        next_end = last_newline != -1 ? last_newline : Math.min(log_length, logstr.substring(0,log_length).length)
        next = logstr.substring(0, next_end);
        Log(next);

        if(last_newline != -1) {
            logstr = logstr.substring(next_end+1)
        } else {
            logstr = logstr.substring(next_end)
        }
    }

    if(textView) {
        textView.setTextSize(8);
        textView.setTypeface(android.graphics.Typeface.MONOSPACE, 1);
        textView.setMovementMethod(new android.text.method.ScrollingMovementMethod());
        textView.setText(hexdata);
        titleView.setText(path_ciphertext);
    }
}

function test_encryption() {
    var string = "PineappleHead";
    var bytes = [0x56, 0x8b, 0xc5, 0x65, 0x4d, 0x44, 0x21, 0x0b, 0x76, 0x4c, 0x0d, 0xb7, 0x1e];
    var result = process(string, bytes);
    return(string == result);
}
