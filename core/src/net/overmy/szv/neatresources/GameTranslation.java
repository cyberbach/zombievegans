package net.overmy.szv.neatresources;

public enum GameTranslation {
	
	TITLE("Зомби\nВеганы!", "Vegan\nZombies!"),
	OPTIONS("Настройки", "Options"),
	PLAY("Играть", "Play"),
	SND("Звук", "Sound"),
	MUS("Музыка", "Music"),
	FIELD_SIZE("Размер поля", "Field size"),
	
	LEVEL_SIZE("Уровень ", "Level "), //6
	YOUR_TURN("Ваш ход", "Your turn"),
	EN_TURN("Ход противника", "Enemy turn"),
	YOUR_EX_TURN("Ваш дополнительный ход", "Your extra turn"),
	EN_EX_TURN("Дополнительный ход противника", "Enemy extra turn"),
	LEVEL_COML("Уровень\nпройден!", "Level\nComplete!"), // 11
	LEVEL_LOOSE("Сегодня\nне ваш день", "It's not your day"),

	NO_VARS("Нет\nвариантов", "No\nvariants"), // 13
	
	HI_BABY("Привет,\nмалыш!", "Hi,\nlittle one."), // 14
	XZ("...", "..."),
	HEY_BOY("Эй, я с тобой\nразговариваю!", "Hey, I talking\nto you!"), 
	WHAT("...?", "...?"), // 17
	IWL_LEARN("Я научу тебя\nманерам", "I'll teach\nyou manners"),
	SEE("Посмотрим...", "Will see..."),
	HI_THERE("Привет!", "Hi, there!"), // 20 
	FIGHT("Сразимся?!", "Let's figth!"), // 21 
	AKEI("Легко!", "Go!"), // 22 
	
	OK("ОК", "OK"), // 23

	KUNG_FU("Мое кунфу\nлучше твоего!", "My kung fu\nis better\nthan yours!"), // 24
	NOPE("Нет!\nТы ошибаешься.", "No!\nYou wrong."), // 25

	YOU_BABY("Сам ты малыш!", "I'm bigger\nand stronger!"), // 26

	I_AM1("Я самый умный\nи сильный!", "I'm the best\nand strongest!"), // 27
	I_AM2("Все противники\nповержены!", "All enemies\nare defeated!"), // 28
	I_AM3("Никто со мной\nне сравнится!!!", "Nobody\nbeats me!!!"), // 29
	GAME_OVER("--- КОНЕЦ - ИГРЫ ---", "--- GAME - IS - OVER ---"), // 30

	ON("Есть", "ON"), // 31
	OFF("Нет", "OFF"); // 32
	
	private final String rus;
	private final String eng;
	
	private static boolean lang;
	
	GameTranslation( final String rus, final String eng ){ this.rus = rus; this.eng = eng; }
	public String get() { return lang ? rus : eng; }
	public static void detect() { lang = java.util.Locale.getDefault().toString().equals("ru_RU"); }
};
