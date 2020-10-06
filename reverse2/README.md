# Reverse I (2)
Points: 200
Description (*Forgot to copy, all based on my memory*):
```
The file is corrupted, try to fix it. The decryptor will decrypt the file to JPEG image file. The image is not just a normal image, look carefully.
```


We're given two file: 
1. decryptor (ELF 64-bit executable)
2. file (data)

Try run it:
```bash
./decryptor 
File is not valid
```
Try running ltrace:
```
ltrace ./decryptor 
fopen("file", "rb")                              = 0x5654b2cd32a0
fseek(0x5654b2cd32a0, 0, 0, 13)                  = 0
fread(0x7fff782f4340, 19000, 1, 0x5654b2cd32a0)  = 0
ftello(0x5654b2cd32a0, 0x5654b2cd3490, 0x4777, 0xfbad24a8) = 0x4777
memcmp(0x5654b0fec0f0, 0x7fff782f4290, 16, 0x5654b0fec0f0) = 140
puts("File is not valid"File is not valid
)                        = 18
strlen("12345678")                               = 8
MD5_Init(0x7fff782f4140, 0x7fff782f4280, 0x5654b0deb860, 32) = 1
MD5_Update(0x7fff782f4140, 0x5654b0deb860, 8, 0x5654b0deb860) = 1
MD5_Final(0x7fff782f41a0, 0x7fff782f4140, 0x7fff782f4140, 0x3837363534333231) = 1
...
strcmp("25d55ad283aa400af464c76d713c07ad"..., "\302<\032\367\255>812`\371&\344\004\310\341\350\325A;.\\7w\210\206>\376\tA\342:"...) = -144
strlen("password")                               = 8
MD5_Init(0x7fff782f4140, 0x7fff782f4280, 0x5654b0deb869, 9) = 1
MD5_Update(0x7fff782f4140, 0x5654b0deb869, 8, 0x5654b0deb869) = 1
MD5_Final(0x7fff782f41a0, 0x7fff782f4140, 0x7fff782f4140, 0x64726f7773736170) = 1
...
strcmp("5f4dcc3b5aa765d61d8327deb882cf99"..., "\302<\032\367\255>812`\371&\344\004\310\341\350\325A;.\\7w\210\206>\376\tA\342:"...) = -141
strlen("qwertyuiop")                             = 10
MD5_Init(0x7fff782f4140, 0x7fff782f4280, 0x5654b0deb872, 18) = 1
MD5_Update(0x7fff782f4140, 0x5654b0deb872, 10, 0x5654b0deb872) = 1
MD5_Final(0x7fff782f41a0, 0x7fff782f4140, 0x7fff782f4140, 0x706f697579747265) = 1
...
strcmp("6eea9b7ef19179a06954edd0f6c05ceb"..., "\302<\032\367\255>812`\371&\344\004\310\341\350\325A;.\\7w\210\206>\376\tA\342:"...) = -140
strlen("passwrd123")                             = 10
MD5_Init(0x7fff782f4140, 0x7fff782f4280, 0x5654b0deb87d, 29) = 1
MD5_Update(0x7fff782f4140, 0x5654b0deb87d, 10, 0x5654b0deb87d) = 1
MD5_Final(0x7fff782f41a0, 0x7fff782f4140, 0x7fff782f4140, 0x3332316472777373) = 1
...
strcmp("bd3304f768220a27d1ae92d9c332dbf8"..., "\302<\032\367\255>812`\371&\344\004\310\341\350\325A;.\\7w\210\206>\376\tA\342:"...) = -96
strlen("123456789")                              = 9
MD5_Init(0x7fff782f4140, 0x7fff782f4280, 0x5654b0deb888, 8) = 1
MD5_Update(0x7fff782f4140, 0x5654b0deb888, 9, 0x5654b0deb888) = 1
MD5_Final(0x7fff782f41a0, 0x7fff782f4140, 0x7fff782f4140, 0x3938373635343332) = 1
...
strcmp("25f9e794323b453885f5181f1b624d0b"..., "\302<\032\367\255>812`\371&\344\004\310\341\350\325A;.\\7w\210\206>\376\tA\342:"...) = -144
puts("\n"

)                                       = 2
fclose(0x5654b2cd32a0)                           = 0
+++ exited (status 1) +++
```
Notice it calculate some MD5 hashes and strcmp it with something...

It's decompile it with Ghidra to understand more!

## Decompiling
Because it is stripped, so we look for the `entry` function:
```c
void entry(undefined8 uParm1,undefined8 uParm2,undefined8 uParm3)

{
  undefined8 in_stack_00000000;
  undefined auStack8 [8];
  
  __libc_start_main(FUN_00100f80,in_stack_00000000,&stack0x00000008,FUN_001017b0,FUN_00101820,uParm3
                    ,auStack8);
  do {
                    /* WARNING: Do nothing block with infinite loop */
  } while( true );
}
```
And the `main` function should be at `FUN_00100f80`

The main function quite long, I'll explain the main part only

*Note: I modified some code to make it more readable*
### Analyze main function
```c
undefined8 FUN_00100f80(void)
{
  int iVar1;
  int iVar2;
  FILE *__stream;
  ulong current_position;
  void *__s1;
  long lVar5;
  long in_FS_OFFSET;
  int local_dffc;
  int local_dff8;
  int local_dff4;
  int local_dff0;
  uint local_dfec;
  int local_dfe8;
  int local_dfe4;
  int local_dfe0;
  char local_df98 [16];
  ...
  char local_df08 [32];
  char buffer [19008];
  char input [19008];
  ...
  long local_20;
  // Open the file given put into stream
  __stream = fopen("file","rb");
  if (__stream != (FILE *)0x0) {
    fseek(__stream,0,0);
    // It reads 19K bytes but file contain only 18295 bytes
    fread(buffer,19000,1,__stream);
    // so the position should at the last character of file
    current_position = ftello(__stream);
    iVar1 = (int)current_position;

    // XOR the data with 1 and store in local_df88
    xor("7dd`8c6dg08068`1",0x10,1,&local_df88);
    // Copy first 16 bytes in buffer and store in s1
    __s1 = copyFirst16Bytes(buffer);
    // If s1 and local_df88 are the same 
    if (memcmp(__s1,&local_df88,0x10) == 0) {
      // XOR the data with 1 and store in output
      xor("7845dee1g7b14bdc12345678",0x10,1,&output);
      // Copy previous 16 bytes from current position
      __s1 = copyPrevious16Bytes(buffer,current_position);
      // If s1 and output are the same 
      if (memcmp(__s1,&output,0x10) == 0) {
      	// So we must get here for valid file
        puts("* File is valid");
      }
      else {
        puts("* File is not valid");
      }
    }
    else {
      puts("File is not valid");
    }
   
    char* last = copyPrevious16Bytes(buffer,current_position);
  	char* first = copyFirst16Bytes(buffer);
  	// Concatenate first and last string
    char* key = concatenate(first,last);
    char* data[5] = {"12345678", "password","qwertyuiop","password123","123456789"}

    while (local_dff8 < 5) {
   	  // Calculate the hash at data then store at hash
      md5sum(data[local_dff8],hash);
      // If key and hash are equal
      if (strcmp(key,hash) == 0) {
        local_dfe8 = 0;
        local_dfe4 = 0x10;
        // Copy the content without the key we append
        while (local_dfe4 < current_position -0x10) {
          input[local_dfe8] = fileContent[local_dfe4];
          local_dfe8++;
          local_dfe4++;
        }
        printf("[+] Decrypted File has been generated. (FinalDecryption.jpeg)");
        memset(&output,0,19000);
        // Decrypt the input using the data as the key
        RC4_decrypt(data[local_dff8],current_position -0x20,
                     input,&output);
        // Write the output to FinalDecryption.jpeg
        writeToFile(&output,current_position -0x20);
      }
      local_dff8++;
    }
    puts("\n");
    fclose(__stream);
}
```
From the code above, the condition of the valid file are:
- First 16 bytes must equal to ```7dd`8c6dg08068`1``` XOR 1
- Last 16 bytes must equal to `7845dee1g7b14bdc12345678` XOR 1

The challenge said that the file is corrupted, so we must correct the file for first 16 bytes and last 16 bytes

But we not sure we need to append those bytes or replace those bytes..

Lets test it out with python!

### Replace to file

```py
from Crypto.Util.strxor import strxor
# Cut first 16 bytes and last 16 bytes
ori  = open("original",'r').read()[16:-16]
first = strxor("7dd`8c6dg08068`1","\x01"*16)
last = strxor("7845dee1g7b14bdc","\x01"*16)
# Write to file
open("file",'w').write(first+ori+last)
```
Run it:
```bash
python solve.py 

./decryptor 
* File is valid
[+] Decrypted File has been generated. (FinalDecryption.jpeg)

file Final_decryption.jpeg 
Final_decryption.jpeg: data

xxd Final_decryption.jpeg | head
00000000: d4a0 b6c8 696c 9335 e8fd c21e 04ab ba08  ....il.5........
00000010: 3fc2 1dee 5b20 9ab4 a3d9 4110 245c 7111  ?...[ ....A.$\q.
00000020: 1906 bcb6 b81f 1530 7237 430a e924 7cd6  .......0r7C..$|.
00000030: e2ff 3419 6732 a877 c68c af0d f8e9 7c96  ..4.g2.w......|.
00000040: 9472 a275 2d2a cacb 4a96 9cbe 0e50 c34a  .r.u-*..J....P.J
00000050: 8c7d 174f dd84 aa4a 0d17 b623 7edf 62b8  .}.O...J...#~.b.
00000060: 0178 098f f205 2f2d df0c 25fb c7c7 0baa  .x..../-..%.....
00000070: 5d1d 8248 0de8 be94 ac6c 7dbe 39ae e574  ]..H.....l}.9..t
00000080: 7c32 d5df 2a03 1c93 8f86 49cf 9ff6 f299  |2..*.....I.....
00000090: 4eda f6e9 99ff 5bb4 5d15 06ad f73c 9d4c  N.....[.]....<.L
```
Not a JPEG file, next try append:

## Append to file
```py
from Crypto.Util.strxor import strxor
ori  = open("original",'r').read()
first = strxor("7dd`8c6dg08068`1","\x01"*16)
last = strxor("7845dee1g7b14bdc","\x01"*16)

open("file",'w').write(first+ori+last)
```
Run it:
```bash
python solve.py 

./decryptor 
* File is valid
[+] Decrypted File has been generated. (FinalDecryption.jpeg)

file Final_decryption.jpeg 
Final_decryption.jpeg: JPEG image data, Exif standard: [TIFF image data, big-endian, direntries=6, xresolution=86, yresolution=94, resolutionunit=2, datetime=2020:07:12 01:05:18], comment: "shellcode: \x6a\x3b\x58\x99\x48\xbb\x2f\x62\x69\x6e\x2f\x73\x68\x00\x53\x48\x89\xe7\x68\x2d\x6", baseline, precision 8, 700x366, components 3

```
Can see it decrypted a JPEG file!!

The image just a door with some binary code:

![image1](test/Final_decryption.jpeg)

Notice there is a comment looks interesting:
```
comment: "shellcode: \x6a\x3b\x58\x99\x48\xbb\x2f\x62\x69\x6e\x2f\x73\x68\x00\x53\x48\x89\xe7\x68\x2d\x6"
```
Run `strings` on it to get the full code:
```
\x6a\x3b\x58\x99\x48\xbb\x2f\x62\x69\x6e\x2f\x73\x68\x00\x53\x48\x89\xe7\x68\x2d\x63\x00\x00\x48\x89\xe6\x52\xe8\x19\x00\x00\x00\x65\x63\x68\x6f\x20\x2d\x6e\x65\x20\x54\x4d\x43\x54\x46\x7b\x67\x30\x30\x64\x77\x30\x72\x6b\x7d\x00\x56\x57\x48\x89\xe6\x0f\x05
```
Shellcode is also [machine code](https://en.wikipedia.org/wiki/Machine_code) only computer can understand (because 0 and 1)

Use Pwntools can convert to assembly code:
```py
context.arch = "amd64"
print disasm("\x6a\x3b\x58\x99\x48\xbb\x2f\x62\x69\x6e\x2f\x73\x68\x00\x53\x48\x89\xe7\x68\x2d\x63\x00\x00\x48\x89\xe6\x52\xe8\x19\x00\x00\x00\x65\x63\x68\x6f\x20\x2d\x6e\x65\x20\x54\x4d\x43\x54\x46\x7b\x67\x30\x30\x64\x77\x30\x72\x6b\x7d\x00\x56\x57\x48\x89\xe6\x0f\x05")

```
Result:
```asm
   0:   6a 3b                   push   0x3b
   2:   58                      pop    rax
   3:   99                      cdq    
   4:   48 bb 2f 62 69 6e 2f    movabs rbx,0x68732f6e69622f
   b:   73 68 00 
   e:   53                      push   rbx
   f:   48 89 e7                mov    rdi,rsp
  12:   68 2d 63 00 00          push   0x632d
  17:   48 89 e6                mov    rsi,rsp
  1a:   52                      push   rdx
  1b:   e8 19 00 00 00          call   0x39
  20:   65 63 68 6f             movsxd ebp,DWORD PTR gs:[rax+0x6f]
  24:   20 2d 6e 65 20 54       and    BYTE PTR [rip+0x5420656e],ch        # 0x54206598
  2a:   4d                      rex.WRB
  2b:   43 54                   rex.XB push r12
  2d:   46 7b 67                rex.RX jnp 0x97
  30:   30 30                   xor    BYTE PTR [rax],dh
  32:   64 77 30                fs ja  0x65
  35:   72 6b                   jb     0xa2
  37:   7d 00                   jge    0x39
  39:   56                      push   rsi
  3a:   57                      push   rdi
  3b:   48 89 e6                mov    rsi,rsp
  3e:   0f 05                   syscall
```
Also can run it:
```py
from pwn import *
context.arch = "amd64"
p = run_shellcode("\x6a\x3b\x58\x99\x48\xbb\x2f\x62\x69\x6e\x2f\x73\x68\x00\x53\x48\x89\xe7\x68\x2d\x63\x00\x00\x48\x89\xe6\x52\xe8\x19\x00\x00\x00\x65\x63\x68\x6f\x20\x2d\x6e\x65\x20\x54\x4d\x43\x54\x46\x7b\x67\x30\x30\x64\x77\x30\x72\x6b\x7d\x00\x56\x57\x48\x89\xe6\x0f\x05")
print p.recv()	
```
And we get the flag!!!
```
[x] Starting local process '/tmp/pwn-asm-Eb7ELF/step3-elf'
[+] Starting local process '/tmp/pwn-asm-Eb7ELF/step3-elf': pid 239648
[*] Process '/tmp/pwn-asm-Eb7ELF/step3-elf' stopped with exit code 0 (pid 239648)
-ne TMCTF{g00dw0rk}
```

Just printing the shellcode also reveal the flag:
```
'j;X\x99H\xbb/bin/sh\x00SH\x89\xe7h-c\x00\x00H\x89\xe6R\xe8\x19\x00\x00\x00echo -ne TMCTF{g00dw0rk}\x00VWH\x89\xe6\x0f\x05'
```
Believe it is running syscall `execve` (rax=0x3b can checking in the [syscall table](https://filippo.io/linux-syscall-table/))

The command should be `/bin/sh -c "echo -ne TMCTF{g00dw0rk}"`

## Alternative solution
We also can solve it without using the `decryptor` program

We know the key is the hash we appended to the file

The hash is `6eea9b7ef19179a06954edd0f6c05ceb`

That is MD5 hash of `qwertyuiop`! (One of the string in data)

Therefore, we can just decrypt the file with this key using python:
```py
from arc4 import ARC4
file = open("original",'rb').read()
arc4 = ARC4('qwertyuiop')
open("flag.jpeg",'wb').write(arc4.decrypt(file))
```
Flag.jpeg:

![image2](test/flag.jpeg)

[Python script](test/solve.py)

[Python script 2](test/solve2.py)

## Flag
```
TMCTF{g00dw0rk}
```