package com.fbsearch.utils;

import com.ibm.icu.text.BreakIterator;
import java.util.Locale;

/**
 *
 * @author Songkran Totiya
 */
public class ThaiWordbreak {

    public static String printEachForward(BreakIterator boundary, String source) {
        StringBuilder strout = new StringBuilder();
        int start = boundary.first();
        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
            strout.append(source.substring(start, end)).append("-");
        }
        return strout.toString();
    }

    public static void main(String[] args) {
        Locale thaiLocale = new Locale("th");
        String input = "����ͧ�к��ػ�����  ����ѡ�Թ�  ������ͧ�����´��͹�ҡ��Ѻ����Ѻ������ǹ�˭�  �ѹ�繨ش���������������ѭ�ҵ��ºҧ��������\n"
                + "�����डѺ�кͺʶҺѹ(anti-royalist)\n"
                + "�������Шٹ����ͧ�кͺ�ѡ�Թ�+���������ԭ�ѡ�բͧ����(�����ҤԴ����ͧ)\n"
                + "��ҡѺ�кͺʶҺѹ�Ч��  ��觼��ͧ���������͹��èѺ�Ъ����ҡ����  ���л���繡ѹ\n"
                + "(��ԧ��������ͧ�������������  ����  �һ���� ����ӹ֡  ����ѡ�ҵ�������͡�����͡��Ѻ  ������Է�Ԩ���繵�ҧ��  �����ʴ��͡�ͧ�ǡ����������駹��....������Ѻ��Ѻ)";
        BreakIterator boundary = BreakIterator.getWordInstance(thaiLocale);
        boundary.setText(input);
        printEachForward(boundary, input);
    }
}
