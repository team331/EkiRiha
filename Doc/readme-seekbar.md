SeekBarの説明

配列を使用しているがリストの場合も同様。
時間の単位はmsec(= 1/1000 sec)。
totalTime	：配列(リスト)内の動画の総再生時間
lapTime[]	：各動画の総再生時間＊

＊lapTime例
動画[0] = 30sec
動画[1] = 40sec
動画[2] = 50sec
lapTime[0] = 0sec (static)
lapTime[1] = 30sec
lapTime[2] = 70sec(30 + 40)
lapTime[3] = 120sec(70 + 50)

つまり、シークバー上では"i"番目の動画は
"i"番目のラップタイムから始まる。