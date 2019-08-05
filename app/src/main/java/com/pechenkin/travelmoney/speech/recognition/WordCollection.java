package com.pechenkin.travelmoney.speech.recognition;

import com.pechenkin.travelmoney.Help;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pechenkin on 22.05.2018.
 */

public class WordCollection {

    static final String ME = "{me}";
    static final String ALL = "{all}";
    static final String MASTER = "{master}";
    static final String OWNER = "{owner}";


    private static final String HALF = "<half>";

    private String[] words;
    private int length;
    private int position = -1;

    private static final StringNumeric[] NUMERIC_NAMES = {
            new StringNumeric("(од)([а-я]{2})", "1"),   //одИН, одНА, одНУ
            new StringNumeric("(дв)([а-я]{1})", "2"), // двА, двЕ
            new StringNumeric("три", "3"),
            new StringNumeric("четыре", "4"),
            new StringNumeric("пять", "5"),
            new StringNumeric("шесть", "6"),
            new StringNumeric("семь", "7"),
            new StringNumeric("восемь", "8"),
            new StringNumeric("девять", "9"),

            new StringNumeric("пят(е|ё)р([а-я]{1,2})", "5000"), //пятерА, пятерУ, пятерЫ, пятерКУ
            new StringNumeric("пятихат([а-я]{2})", "500"), //пятихатКУ, две пятихатКИ, семь пятихатОК
            new StringNumeric("полтинник([а-я]{0,2})", "50"), //полтинник, полтинникА, полтинникОВ

            new StringNumeric("косар([а-я]{1,2})", "1000"), //косарЬ, косарЯ, косарЕЙ
            new StringNumeric("тысяч([а-я]{0,1})", "1000"), //тысяч, тысячУ, тысячИ
            new StringNumeric("штук([а-я]{0,1})", "1000"), //штукА штукУ штук штукИ
            new StringNumeric("кус(ок|ка|ков)", "1000"), //кусОК, кусКА, кусКОВ

            new StringNumeric("сот([а-я]{2})", "100"), //сотКУ, две сотКИ, семь сотОК

            new StringNumeric("(с ?половиной)|(1/2)", HALF), // с половиной, споловиной, 1/2
            new StringNumeric("полтор(ы|а)", "1.5") //полторЫ, полторА

    };


    public WordCollection(String text) {
        words = new WrapperString(text.toLowerCase())
                .replaceAll("[,\\.]", "") // убираем все запятые и точки для обработки 3.000
                .replaceAll("(^|[^\\d])(\\d{1,2})\\s(\\d{3})($|[^\\d])", " $2$3") //убираем пробел между числами если певое 1 или 2 значное а второе 3 значное. Обработка 3 500 или 11 700
                .replaceNumeric(NUMERIC_NAMES)
                .replaceAll("(\\d+(\\.\\d+)?)", " $1 ") //все цифры отделяем пробелами для обработки таких строк как 350руб
                .replaceAll(" и | за ", " ")  //убираем лишние слова в центре
                .replaceAllWords(new String[]{"всех", "завсех", "всем"}, ALL) //ключ для обработки "за всех"
                .replaceAllWords(new String[]{"меня", "мне"}, ME) //ключ для обработки пользователя устройства
                .replaceAllWords(new String[]{"себе", "себя"}, MASTER) //ключ для обработки мастера траты
                .replaceAllWords(new String[]{"я"}, OWNER) //ключ для обработки владельца телефона
                .replaceAll("\\s{2}", " ").trim() //Убираем лишние пробелы
                .splitSpace();

        length = words.length;
    }


    public boolean hasNext() {
        return position + 1 < length && length > 0;
    }

    public String getNext() {
        if (hasNext()) {
            position++;
            return words[position];
        } else
            return "";
    }

    public String viewNext(int addPosition) {
        return getPosition(position + addPosition);
    }

    public String viewBefore(int downPosition) {
        return getPosition(position - downPosition);
    }

    public void movePosition(int moveTo) {
        position += moveTo;
        if (position < -1)
            position = -1;
    }

    private String getPosition(int returnPosition) {
        if (returnPosition >= 0 && returnPosition < length) {
            return words[returnPosition];
        } else
            return "";
    }


    private static class WrapperString {
        final String text;

        WrapperString(String text) {
            this.text = text;
        }

        WrapperString replaceAll(String regex, String replacement) {
            return new WrapperString(text.replaceAll(regex, replacement));
        }

        WrapperString replaceAllWords(String[] words, String replacement) {
            String returnText = text;
            for (String word : words) {
                returnText = returnText.replaceAll(String.format("( |^)%s( |$)", word), String.format(" %s ", replacement));
            }

            return new WrapperString(returnText);
        }

        WrapperString trim() {
            return new WrapperString(text.trim());
        }

        WrapperString replaceNumeric(StringNumeric[] numeric) {
            String returnText = text;

            //заменяем слова на экранированные цифры
            //две с пловиной тысячи -> {n}2{/n} {n}1.5{/n} {n}1000{/n}
            for (StringNumeric sNumeric : numeric) {
                returnText = returnText.replaceAll(sNumeric.regex, sNumeric.value);
            }

            //составляем из цифр выражения и экранируем их
            //{n}2{/n} {n}1.5{/n} {n}1000{/n} -> {n}2*1.5*1000{/n}
            returnText = textReplaceAll(returnText, "(\\{n\\})([^\\{\\/n\\}]+)(\\{\\/n\\}) +(\\{n\\})([^\\{\\/n\\}]+)(\\{\\/n\\})", " {n}$2*$5{/n} ");

            //Вносим в экран рядом стоящие неэкранированные цифры
            // 200 {n}2*1.5*1000{/n} -> {n}200*2*1.5*1000{/n}
            returnText = textReplaceAll(returnText, "(\\{n\\})([^\\{\\/n\\}]+)(\\{\\/n\\}) +(\\d+)", " {n}$2*$4{/n} ");
            returnText = textReplaceAll(returnText, "(\\d+) +(\\{n\\})([^\\{\\/n\\}]+)(\\{\\/n\\})", " {n}$1*$3{/n} ");


            //Находим экрнированные числа со знаком умножения и производим вычисления оставляя экраны
            Pattern regexMultiplication = Pattern.compile("(\\{n\\})([^\\{\\/n\\}]+\\*[^\\{\\/n\\}]+)(\\{\\/n\\})");
            Matcher matcher = regexMultiplication.matcher(returnText);
            while (matcher.find()) {
                returnText = matcher.replaceFirst(" {n}" + multiplied(matcher.group(2)) + "{/n} ");
                matcher = regexMultiplication.matcher(returnText);
            }

            //убираем экраны
            returnText = textReplaceAll(returnText, "(\\{n\\})([^\\{\\/n\\}]+)(\\{\\/n\\})", " $2 ");

            return new WrapperString(returnText);
        }

        private static String textReplaceAll(String text, String regex, String replacement) {
            Pattern regexPattern = Pattern.compile(regex);
            while (regexPattern.matcher(text).find()) {
                text = regexPattern.matcher(text).replaceAll(replacement);
            }
            return text;
        }

        static String multiplied(String text) {
            double result = 0;
            String[] numeric = text.split("\\*");
            for (String s : numeric) {

                if (s.equals(HALF)) {

                    String resultStr = Help.doubleToString(result)
                            .replaceAll(" ", "")
                            .replaceAll("(.+)?([1-9]\\d{0,2})((000)+)?$", "$1$2")
                            .replaceAll("null", "");

                    double divider = Help.StringToDouble(resultStr);
                    if (divider > 0) {
                        result += result * 0.5 / divider;
                    }

                    continue;
                }

                try {
                    double m = Double.parseDouble(s);
                    if (result == 0)
                        result = m;
                    else
                        result = result * m;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

            return Help.doubleToString(result).replaceAll(" ", "");
        }

        String[] splitSpace() {
            return text.split(" ");
        }
    }

    private static class StringNumeric {
        final String regex;
        final String value;

        StringNumeric(String regex, String value) {
            this.regex = regex + "(\\s|$)";
            this.value = String.format(" {n}%s{/n} ", value);
        }
    }


}
