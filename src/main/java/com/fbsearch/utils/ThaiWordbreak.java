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
        String input = "เรื่องระบบอุปถัมภ์  และศักดินา  เป็นเรื่องละเอียดอ่อนมากครับสำหรับคนไทยส่วนใหญ่  มันเป็นจุดที่ทำให้มนุึษย์สัญชาติไทยบางกลุ่มที่\n"
                + "ไม่โอเคกับระบอบสถาบัน(anti-royalist)\n"
                + "พยายามจะจูนเรื่องระบอบศักดินา+ความไม่เจริญซักทีของคนไทย(ที่เค้าคิดเอาเอง)\n"
                + "เข้ากับระบอบสถาบันซะงั้น  ซึ่งผมมองว่าเป็นเหมือนการจับแพะชนแกะมากกว่า  คนละประเด็นกัน\n"
                + "(จริงๆผมไม่ได้มองคนกลุ่มนี้ว่า  ไม่ดี  บาปกรรม ไม่สำนึก  ไม่รักชาติอะไรเทือกนั้นหรอกครับ  เค้ามีสิทธิจะเห็นต่างนะ  แต่การแสดงออกของพวกเค้าหลายๆครั้งนี่....เหลือรับครับ)";
        BreakIterator boundary = BreakIterator.getWordInstance(thaiLocale);
        boundary.setText(input);
        printEachForward(boundary, input);
    }
}
