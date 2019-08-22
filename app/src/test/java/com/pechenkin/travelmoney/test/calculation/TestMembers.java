package com.pechenkin.travelmoney.test.calculation;

import com.pechenkin.travelmoney.bd.Member;

public enum TestMembers {

    EVGENIY(new MemberForTest("Евгений", 1)),
    MARINA(new MemberForTest("Марина", 1)),

    GREEN(new MemberForTest("Сергей", 2)),
    SVETA(new MemberForTest("Сергей", 2)),

    PETR(new MemberForTest("Петя", 3)),

    SEDOY(new MemberForTest("Седой", 4)),
    TONYA(new MemberForTest("Тоня", 5)),

    VLAD(new MemberForTest("Влад", 6));




    private Member member;

    TestMembers(Member member) {
        this.member = member;
    }

    public Member getMember() {
        return member;
    }
}
