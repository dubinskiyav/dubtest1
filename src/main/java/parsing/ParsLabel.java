package parsing;

import java.util.ArrayList;
import java.util.List;

public class ParsLabel {

    public void test1() {
        String s = "потребления холодной воды ${v_05_02} л/с, ${v_05_01} куб.м/час, ${v_05_03} куб. м./сутки";
        System.out.println(s);
        List<String> sl = new  ArrayList<>();
        sl.add("потребления холодной ");
        sl.add("воды ");
        sl.add("${v_05_02}");
        sl.add(" л/с, $");
        sl.add("{v_05_");
        sl.add("01");
        sl.add("} ");
        sl.add("куб.м/час, ");
        sl.add("$");
        sl.add("{v_05_03} куб.");
        sl.add(" м./сутки");
        for (String text : sl) {
            System.out.print(text);
        }
        System.out.println();
        List<String> dl = new  ArrayList<>();
        dl.add("потребления холодной ");
        dl.add("воды ");
        dl.add("${v_05_02}"); // Этот не трогаем
        dl.add(" л/с, ");     // От сюда забрали $
        dl.add("${v_05_01}");
        dl.add(" ");          //  От сюда забрали скобку
        dl.add("куб.м/час, "); // не трогаем
        dl.add("${v_05_03}"); // от сюда забрали после скобки
        dl.add(" куб.");
        dl.add(" м./сутки");
        for (String text : dl) {
            System.out.print(text);
        }
        // Попытка 1
        dl = new  ArrayList<>();
        boolean dollar = false; // доллар еще не нашли
        boolean curlyBraceOpen = false; // открывающую фигурную скобку еще не нашли
        boolean curlyBraceClose = false; // закрывающую фигурную скобку еще не нашли
        System.out.println();
        for (String text : sl) {
            String label = "";
            if (!dollar) { // $ еще не нашли
                // Ищем $
                int dollarPos = text.indexOf("$");
                dollar = dollarPos != -1;
                if (dollar) { // доллар нашли
                    label = "$"; // Начинаем получать метку
                    if (dollarPos > 0) {
                        // добавим то что до $
                        dl.add(text.substring(0,dollarPos));
                        // удалим то что до $
                        text = text.substring(dollarPos);
                    }
                    text = text.substring(1); // Удалим и сам доллар
                } else {
                    // Добавим и идем к следующему
                    dl.add(text);
                }
            }
            if (dollar) {
                // ищем открывающуюся скобку в text
                if (!curlyBraceOpen) { // ее еще не нашли
                    int cboPos = text.indexOf("{");
                    if (cboPos == -1) { // Скобку в остатке не нашли
                        dl.add(label + text); // добавляем и переходим к слеюующему элементу
                    } else { // Скобку нашли, проверяем
                        if (cboPos != 0) { // скобка не следует сразу же за долларом - едем дальше
                            dollar = false; // Значит это не метка
                            // Добавим остаток и идем к следующему (про доллар не забывваем)
                            dl.add(label + text);
                        } else {
                            curlyBraceOpen = true; // скобку нашли в правильном месте
                            label = label + "{"; // Теперь в label у нас ${
                            text = text.substring(1);
                        }
                    }
                }
                if (curlyBraceOpen) {
                    // Ищем закрывающую фигурную скобку
                    int cbcPos = text.indexOf("}");
                    if (cbcPos == -1) { // Скобку в остатке не нашли
                        dl.add(label + text); // добавляем и переходим к слеюующему элементу
                    } else {
                        // Скобку нашли, все запоминаем и обнуляем
                        label = label + text.substring(0, cbcPos + 1); // Метка теперь так ${aaa}
                        dl.add(label);
                        text = text.substring(cbcPos + 1);
                        dl.add(text);
                        curlyBraceOpen = false;
                        dollar = false;
                    }
                }
            }
        }
        for (String text : dl) {
            System.out.print(text);
        }
        System.out.println();
        for (String text : dl) {
            System.out.println(text);
        }
    }

}
