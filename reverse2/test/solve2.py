from arc4 import ARC4
file = open("original",'rb').read()
arc4 = ARC4('qwertyuiop')
open("flag.jpeg",'wb').write(arc4.decrypt(file))