

#Android

## Functionについて
Functionのなかには、よく使い部分を単独になるもの。

##Toastは
Toastはネットで調べてください。`Android Toast`

##Activity間の通信
Activityの通信はIntentをつかている。

	Intent it = new Intent();

自分と相手の情報を入れて、

	it.setClass(getApplicationContext(), MapActivity.class);

転送したいデータを入れて、（ここは一番簡単な方法で）

	it.putExtra("station", station_pos);

パラメーターの一つ目は認識名前、二つ目はデータ

最後は送信

	getApplicationContext().startActivity(it);

で終わり。

受け側はdefaultの`onCreate()`で

	Intent it = getIntent();
	station_pos = it.getIntExtra("station_pos", -1);

パラメーターを受け取れます。

＊ちなみに、戻す方法もありますが、多分使えないので、説明しない。

